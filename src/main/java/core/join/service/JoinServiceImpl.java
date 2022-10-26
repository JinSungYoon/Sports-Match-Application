package core.join.service;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import core.join.dto.JoinDto;
import core.join.dto.JoinSearchCondition;
import core.join.entity.JoinEntity;
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import core.join.repository.JoinRepository;
import core.join.repository.JoinRepositoryCustom;
import core.player.entity.PlayerEntity;
import core.player.repository.PlayerRepository;
import core.team.entity.TeamEntity;
import core.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinServiceImpl implements JoinService {

	private final JoinRepository joinRepository;
	private final JoinRepositoryCustom joinRepositoryCustom; 
	private final PlayerRepository playerRepository;
	private final TeamRepository teamRepository;
	
	@Override
	public JoinDto requestPlayerJoin(Long id, JoinDto joinDto) {

		if(id != joinDto.getPlayerId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"타인의 가입 요청은 할 수 없습니다.",new Exception());
		}
		
		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		
		PageRequest page = PageRequest.of(0, 1);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.PLAYER);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// 기존에 요청한 제안 중 동일한 대상에게 제안한 활성화된 요청이 있는지 확인한다.
		inquiryList = joinRepositoryCustom.findPlayerJoinApplication(condition, joinDto.getPlayerId(), page);
		
		if(inquiryList.getTotalElements()>0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미 요청한 제안입니다.",new Exception());
		}
		
		// 만일 제안 요청인데 StatusType이 PRPOSAL이 아닐경우 PROPOSAL로 변경해 준다.
		if(!joinDto.checkStatus(StatusType.PROPOSAL)) {
			joinDto.setStatusType(StatusType.PROPOSAL);
		}
		
		JoinEntity join = joinRepository.save(joinDto.toEntity(joinDto,player,team));
		
		return join.toDto();
	}

	@Override
	public JoinDto requestTeamJoin(Long id, JoinDto joinDto) {
		
		// 본인의 요청인지 확인
		if(id != joinDto.getTeamId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"타팀의 가입 요청은 할 수 없습니다.",new Exception());
		}
		
		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		
		PageRequest page = PageRequest.of(0, 1);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.TEAM);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// 기존에 요청한 제안 중 동일한 대상에게 제안한 활성화된 요청이 있는지 확인한다.
		inquiryList = joinRepositoryCustom.findTeamJoinApplication(condition, joinDto.getTeamId(), page);
		
		if(inquiryList.getTotalElements()>0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미 요청한 제안입니다.",new Exception());
		}
		
		// 만일 제안 요청인데 StatusType이 PRPOSAL이 아닐경우 PROPOSAL로 변경해 준다.
		if(!joinDto.checkStatus(StatusType.PROPOSAL)) {
			joinDto.setStatusType(StatusType.PROPOSAL);
		}
		
		JoinEntity join = joinRepository.save(joinDto.toEntity(joinDto,player,team));
		
		return join.toDto();
	}
	
	@Override
	public JoinDto rejectPlayerJoin(JoinDto joinDto) throws Exception {
		
		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		
		PageRequest page = PageRequest.of(0, 100);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.PLAYER);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// 거절할 제안이 있는지 확인.
		inquiryList = joinRepositoryCustom.findPlayerJoinOffer(condition, joinDto.getPlayerId(), page);
		
		JoinEntity proposal = inquiryList.getContent().stream()
				.filter(d->d.getTeamId()==joinDto.getTeamId())
				.map(d->d.toEntity(joinDto, player, team))
				.findFirst().orElseThrow(() -> new Exception("해당 팀이 "+StatusType.PROPOSAL+"한 요청이 없습니다."));
		
		proposal.updateStatus(StatusType.REJECT);
		
		joinRepository.save(proposal);
		
		return proposal.toDto();
	}

	@Override
	public JoinDto rejectTeamJoin(JoinDto joinDto) throws Exception {

		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		
		PageRequest page = PageRequest.of(0, 100);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.TEAM);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// 거절할 제안이 있는지 확인.
		inquiryList = joinRepositoryCustom.findTeamJoinOffer(condition, joinDto.getTeamId(), page);
		
		JoinEntity proposal = inquiryList.getContent().stream()
				.filter(d->d.getPlayerId()==joinDto.getPlayerId())
				.map(d->d.toEntity(joinDto, player, team))
				.findFirst().orElseThrow(() -> new Exception("해당 선수가 "+StatusType.PROPOSAL+"한 요청이 없습니다."));
		
		proposal.updateStatus(StatusType.REJECT);
		
		proposal = joinRepository.save(proposal);
		
		return proposal.toDto();
	}

	@Override
	public JoinDto approvePlayerJoin(JoinDto joinDto) throws Exception {
		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		
		PageRequest page = PageRequest.of(0, 100);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.PLAYER);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// Player에게 제안한 요청이 있는지 확인.
		inquiryList = joinRepositoryCustom.findPlayerJoinOffer(condition, joinDto.getPlayerId(), page);
		
		JoinEntity proposal = inquiryList.getContent().stream()
				.filter(d->d.getPlayerId()==joinDto.getPlayerId())
				.map(d->d.toEntity(d, player, team))
				.findFirst().orElseThrow(() -> new Exception("해당 팀이 "+StatusType.PROPOSAL+"한 요청이 없습니다."));
		
		proposal.updateStatus(StatusType.APPROVAL);
		
		proposal = joinRepository.save(proposal);
		
		return proposal.toDto();
	}
	
	@Override
	public JoinDto approveTeamJoin(JoinDto joinDto) throws Exception {
		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		
		PageRequest page = PageRequest.of(0, 100);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.TEAM);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// Team에게 제안한 요청이 있는지 확인.
		inquiryList = joinRepositoryCustom.findTeamJoinOffer(condition, joinDto.getTeamId(), page);
		
		JoinEntity proposal = inquiryList.getContent().stream()
				.filter(d->d.getPlayerId()==joinDto.getPlayerId())
				.map(d->d.toEntity(d, player, team))
				.findFirst().orElseThrow(() -> new Exception("해당 선수가 "+StatusType.PROPOSAL+"한 요청이 없습니다."));
		
		proposal.updateStatus(StatusType.APPROVAL);
		
		proposal = joinRepository.save(proposal);
		
		return proposal.toDto();
	}
	
	@Override
	public JoinDto confirmPlayerApprove(JoinDto joinDto) throws Exception {
		// Player, Team Entity 조회
		// 승인된 요청이 있는지 확인
			// 승인된 요청중 일주일 이내인 경우만 유효한 승인요청이다.
		// 기 승인한 요청이 있을경우 기 승인 요청을 거절 처리한다.
		// 승인 확정 후 해당 선수를 팀으로 영입하여 선수 명단에 선수의 정보를 추가한다.
		// 선수의 소속팀과 유니폼 번호를 변경한다.
			// 유니폼 번호는 소속팀의 가장 큰 유니폼 번호보다 1 큰 숫자를 설정한다.
		return null;
	}

	@Override
	public JoinDto confirmTeamApprove(JoinDto joinDto) throws Exception {
		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		// 승인된 팀 요청이 있는지 확인
	
		//joinRepositoryCustom.
			// 승인된 요청중 일주일 이내인 경우만 유효한 승인요청이다.
		// 기 승인한 요청이 있을경우 기 승인 요청을 거절 처리한다.
		// 승인 확정 후 해당 선수를 팀으로 영입하여 선수 명단에 선수의 정보를 추가한다.
		// 승인 확정 후 해당 선수는 소속팀과 유니폼 번호를 변경한다.
			// 유니폼 번호는 소속팀의 가장 큰 유니폼 번호보다 1 큰 숫자를 설정한다.
		return null;
	}
	
	@Override
	public JoinDto withdrawPlayerApprove(JoinDto joinDto) throws Exception {
		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		
		PageRequest page = PageRequest.of(0,100);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.PLAYER);
		condition.setStatusType(StatusType.APPROVAL);
		
		// Player에게 승인된 요청이 있는지 확인.
		inquiryList = joinRepositoryCustom.findPlayerJoinApplication(condition, joinDto.getPlayerId(), page);
		
		JoinEntity proposal = inquiryList.getContent().stream()
													.filter(d->d.getTeamId()==joinDto.getTeamId())
													.map(d->d.toEntity(d, player, team))
													.findFirst().orElseThrow(() -> new Exception("해당 팀으로부터 "+StatusType.APPROVAL+"한 요청이 없습니다."));
		
		proposal.updateStatus(StatusType.WITHDRAW);
		
		proposal = joinRepository.save(proposal);
		
		return proposal.toDto();
	}

	@Override
	public JoinDto withdrawTeamApprove(JoinDto joinDto) throws Exception {
		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		
		PageRequest page = PageRequest.of(0,100);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		// Player에게 승인된 요청이 있는지 확인.
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.TEAM);
		condition.setStatusType(StatusType.APPROVAL);
		
		inquiryList = joinRepositoryCustom.findTeamJoinApplication(condition, joinDto.getTeamId(), page);
		
		JoinEntity proposal = inquiryList.getContent().stream()
													.filter(d->d.getPlayerId()==joinDto.getPlayerId())
													.map(d->d.toEntity(d, player, team))
													.findFirst().orElseThrow(() -> new Exception("해당 선수로부터 "+StatusType.APPROVAL+"한 요청이 없습니다."));
		
		proposal.updateStatus(StatusType.WITHDRAW);
		
		proposal = joinRepository.save(proposal);
		
		return proposal.toDto();
	}

	@Override
	public Page<JoinDto> searchPlayerJoinApplication(Long playerId,JoinSearchCondition condition, Pageable pageable) {
		Page<JoinDto> rtnList = new PageImpl<>(new ArrayList<>(),pageable,0);
		rtnList = joinRepositoryCustom.findPlayerJoinApplication(condition, playerId, pageable);
		return rtnList;
	}
	
	@Override
	public Page<JoinDto> searchPlayerJoinOffer(Long playerId,JoinSearchCondition condition, Pageable pageable) {
		Page<JoinDto> rtnList = new PageImpl<>(new ArrayList<>(),pageable,0);
		rtnList = joinRepositoryCustom.findPlayerJoinOffer(condition, playerId, pageable);
		return rtnList;
	}

	@Override
	public Page<JoinDto> searchTeamJoinApplication(Long teamId,JoinSearchCondition condition, Pageable pageable) {
		Page<JoinDto> rtnList = new PageImpl<>(new ArrayList<>(),pageable,0);
		rtnList = joinRepositoryCustom.findTeamJoinApplication(condition, teamId, pageable);
		return rtnList;
	}

	@Override
	public Page<JoinDto> searchTeamJoinOffer(Long teamId,JoinSearchCondition condition, Pageable pageable) {
		Page<JoinDto> rtnList = new PageImpl<>(new ArrayList<>(),pageable,0);
		rtnList = joinRepositoryCustom.findTeamJoinOffer(condition, teamId, pageable);
		return rtnList;
	}
		
}