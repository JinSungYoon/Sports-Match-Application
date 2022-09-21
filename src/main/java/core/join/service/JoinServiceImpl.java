package core.join.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		
		// 기존에 요청한 제안 중 동일한 대상에게 제안한 활성화된 요청이 있는지 확인한다.
		inquiryList = joinRepositoryCustom.findPlayerJoinApplication(StatusType.PROPOSAL, joinDto.getPlayerId(), page);
		
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
		
		// 기존에 요청한 제안 중 동일한 대상에게 제안한 활성화된 요청이 있는지 확인한다.
		inquiryList = joinRepositoryCustom.findTeamJoinApplication(StatusType.PROPOSAL, joinDto.getTeamId(), page);
		
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
	public JoinDto rejectPlayerJoin(JoinDto joinDto) {
		
		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		
		PageRequest page = PageRequest.of(0, 1);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		// 거절할 제안이 있는지 확인.
		inquiryList = joinRepositoryCustom.findPlayerJoinOffer(StatusType.PROPOSAL, joinDto.getPlayerId(), page);
		
		if(inquiryList.getTotalElements()<=0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"거절할 제안이 없습니다.",new Exception());
		}
		
		Long joinId = inquiryList.getContent().get(0).getId();
		
		JoinEntity proposal =  joinRepository.findById(joinId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청한 제안이 없습니다.",new Exception()));
		
		proposal.updateStatus(StatusType.REJECT);
		
		joinRepository.save(proposal);
		
		return proposal.toDto();
	}

	@Override
	public JoinDto rejectTeamJoin(JoinDto joinDto) {

		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		
		PageRequest page = PageRequest.of(0, 1);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		// 거절할 제안이 있는지 확인.
		inquiryList = joinRepositoryCustom.findTeamJoinOffer(StatusType.PROPOSAL, joinDto.getTeamId(), page);
		
		if(inquiryList.getTotalElements()<=0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"거절할 제안이 없습니다.",new Exception());
		}
		
		Long joinId = inquiryList.getContent().get(0).getId();
		
		JoinEntity proposal =  joinRepository.findById(joinId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청한 제안이 없습니다.",new Exception()));
		
		proposal.updateStatus(StatusType.REJECT);
		
		proposal = joinRepository.save(proposal);
		
		return proposal.toDto();
	}

	@Override
	public JoinDto approvePlayerJoin(JoinDto joinDto) {
		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		
		PageRequest page = PageRequest.of(0, 1);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		// Player에게 제안한 요청이 있는지 확인.
		inquiryList = joinRepositoryCustom.findPlayerJoinOffer(StatusType.PROPOSAL, joinDto.getPlayerId(), page);
		
		if(inquiryList.getTotalElements()<=0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"승인할 제안이 없습니다.",new Exception());
		}
		
		Long joinId = inquiryList.getContent().get(0).getId();
		
		JoinEntity proposal =  joinRepository.findById(joinId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청한 제안이 없습니다.",new Exception()));
		
		proposal.updateStatus(StatusType.APPROVAL);
		
		proposal = joinRepository.save(proposal);
		
		return proposal.toDto();
	}
	
	@Override
	public JoinDto approveTeamJoin(JoinDto joinDto) {
		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 team이 존재하지 않습니다.",new Exception()));
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청하신 player가 존재하지 않습니다.",new Exception()));
		
		PageRequest page = PageRequest.of(0, 1);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		// Player에게 제안한 요청이 있는지 확인.
		inquiryList = joinRepositoryCustom.findTeamJoinOffer(StatusType.PROPOSAL, joinDto.getTeamId(), page);
		
		if(inquiryList.getTotalElements()<=0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"승인할 제안이 없습니다.",new Exception());
		}
		
		Long joinId = inquiryList.getContent().get(0).getId();
		
		JoinEntity proposal =  joinRepository.findById(joinId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청한 제안이 없습니다.",new Exception()));
		
		proposal.updateStatus(StatusType.APPROVAL);
		
		proposal = joinRepository.save(proposal);
		
		return proposal.toDto();
	}

	@Override
	public Page<JoinDto> searchPlayerJoinApplication(Long playerId,JoinSearchCondition condition, Pageable pageable) {
		Page<JoinDto> rtnList = new PageImpl<>(new ArrayList<>(),pageable,0);
		rtnList = joinRepositoryCustom.findPlayerJoinApplication(condition.getStatusType(), playerId, pageable);
		return rtnList;
	}
	
	@Override
	public Page<JoinDto> searchPlayerJoinOffer(Long playerId,JoinSearchCondition condition, Pageable pageable) {
		Page<JoinDto> rtnList = new PageImpl<>(new ArrayList<>(),pageable,0);
		rtnList = joinRepositoryCustom.findPlayerJoinOffer(condition.getStatusType(), playerId, pageable);
		return rtnList;
	}

	@Override
	public Page<JoinDto> searchTeamJoinApplication(Long teamId,JoinSearchCondition condition, Pageable pageable) {
		Page<JoinDto> rtnList = new PageImpl<>(new ArrayList<>(),pageable,0);
		rtnList = joinRepositoryCustom.findTeamJoinApplication(condition.getStatusType(), teamId, pageable);
		return rtnList;
	}

	@Override
	public Page<JoinDto> searchTeamJoinOffer(Long teamId,JoinSearchCondition condition, Pageable pageable) {
		Page<JoinDto> rtnList = new PageImpl<>(new ArrayList<>(),pageable,0);
		rtnList = joinRepositoryCustom.findTeamJoinOffer(condition.getStatusType(), teamId, pageable);
		return rtnList;
	}
	
	@Override
	public JoinDto confirmApprove(JoinDto joinDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JoinDto withdrawApprove(JoinDto joinDto) {
		// TODO Auto-generated method stub
		return null;
	}

}
