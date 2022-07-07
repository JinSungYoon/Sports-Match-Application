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
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import core.join.repository.JoinRepository;
import core.join.repository.JoinRepositoryCustom;
import core.join.repository.JoinRepositoryImpl;
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
	public JoinDto requestJoin(JoinDto joinDto) {
		PageRequest page = PageRequest.of(0, 1);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		// 기존에 요청한 제안 중 동일한 대상에게 제안한 활성화된 요청이 있는지 확인한다.
		if(joinDto.getRequesterType().equals(RequesterType.PLAYER)) {
			inquiryList = joinRepositoryCustom.findPlayerJoinApplication(StatusType.PROPOSAL, joinDto.getPlayerId(),joinDto.getTeamId(), page);
		}else if(joinDto.getRequesterType().equals(RequesterType.TEAM)) {
			inquiryList = joinRepositoryCustom.findTeamJoinApplication(StatusType.PROPOSAL, joinDto.getPlayerId(),joinDto.getTeamId(), page);
		}
		
		if(inquiryList.getTotalElements()>0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미 요청한 제안입니다.",new Exception());
		}
		
		// 만일 제안 요청인데 StatusType이 PRPOSAL이 아닐경우 PROPOSAL로 변경해 준다.
		if(!joinDto.checkStatus(StatusType.PROPOSAL)) {
			joinDto.setStatusType(StatusType.PROPOSAL);
		}
		
		// Player,Team Entity 조회
		TeamEntity team = teamRepository.findById(joinDto.getTeamId()).orElse(null);
		PlayerEntity player = playerRepository.findById(joinDto.getPlayerId()).orElse(null);
		
		JoinEntity join = joinRepository.save(joinDto.toEntity(joinDto,player,team));
		
		return join.toDto();
	}

	@Override
	public JoinDto rejectJoin(JoinDto joinDto) {
		
		PageRequest page = PageRequest.of(0, 1);
		
		Page<JoinDto> inquiryList = new PageImpl<>(new ArrayList<>(),page,0);
		
		// 기존에 요청한 제안 중 동일한 대상에게 제안한 활성화된 요청이 있는지 확인한다.
		if(joinDto.getRequesterType().equals(RequesterType.PLAYER)) {
			inquiryList = joinRepositoryCustom.findPlayerJoinApplication(StatusType.PROPOSAL,joinDto.getPlayerId(), joinDto.getTeamId(), page);
		}else {
			inquiryList = joinRepositoryCustom.findTeamJoinApplication(StatusType.PROPOSAL,joinDto.getPlayerId(), joinDto.getTeamId(), page);
		}
		
		if(inquiryList.getTotalElements()<=0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"거절할 제안이 없습니다.",new Exception());
		}
		
		Long joinId = inquiryList.getContent().get(0).getId();
		
		JoinEntity proposal =  joinRepository.findById(joinId).orElse(null);
		
		proposal.updateStatus(StatusType.REJECT);
		
		return proposal.toDto();
	}

	@Override
	public JoinDto approveJoin(JoinDto joinDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<JoinDto> searchPlayerJoinApplication(JoinSearchCondition condition, Pageable pageable) {
		
		Page<JoinDto> rtnList = new PageImpl<>(new ArrayList<>(),pageable,0);
		rtnList = joinRepositoryCustom.findPlayerJoinApplication(condition.getStatusType(), condition.getPlayerId(), condition.getTeamId(), pageable);
		List<JoinDto> joinList = rtnList.getContent();
		
		return rtnList;
	}
	
	@Override
	public Page<JoinDto> searchPlayerJoinOffer(JoinSearchCondition condition, Pageable pageable) {
		Page<JoinDto> rtnList = new PageImpl<>(new ArrayList<>(),pageable,0);
		rtnList = joinRepositoryCustom.findPlayerJoinOffer(condition.getStatusType(), condition.getPlayerId(), condition.getTeamId(), pageable);
		return rtnList;
	}

	@Override
	public Page<JoinDto> searchTeamJoinApplication(JoinSearchCondition condition, Pageable pageable) {
		Page<JoinDto> rtnList = new PageImpl<>(new ArrayList<>(),pageable,0);
		rtnList = joinRepositoryCustom.findTeamJoinApplication(condition.getStatusType(), condition.getPlayerId(), condition.getTeamId(), pageable);
		return rtnList;
	}

	@Override
	public Page<JoinDto> searchTeamJoinOffer(JoinSearchCondition condition, Pageable pageable) {
		Page<JoinDto> rtnList = new PageImpl<>(new ArrayList<>(),pageable,0);
		rtnList = joinRepositoryCustom.findTeamJoinOffer(condition.getStatusType(), condition.getPlayerId(), condition.getTeamId(), pageable);
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
