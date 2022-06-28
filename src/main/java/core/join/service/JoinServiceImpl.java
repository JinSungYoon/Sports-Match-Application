package core.join.service;

import java.util.ArrayList;
import java.util.List;

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
		
		List<JoinDto> inquiryList = new ArrayList<>();
		
		// 기존에 요청한 제안 중 동일한 대상에게 제안한 활성화된 요청이 있는지 확인한다.
		if(joinDto.getRequesterType().equals(RequesterType.Player)) {
			inquiryList = joinRepositoryCustom.findPlayerJoinRequest(StatusType.PROPOSAL, joinDto.getPlayerId(),joinDto.getTeamId(), PageRequest.of(0,1));
		}else if(joinDto.getRequesterType().equals(RequesterType.Team)) {
			inquiryList = joinRepositoryCustom.findTeamJoinRequest(StatusType.PROPOSAL, joinDto.getPlayerId(),joinDto.getTeamId(), PageRequest.of(0,1));
		}
		
		if(inquiryList.size()>1) {
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
		
		List<JoinDto> inquiryList = new ArrayList<>();
		
		// 기존에 요청한 제안 중 동일한 대상에게 제안한 활성화된 요청이 있는지 확인한다.
		if(joinDto.getRequesterType().equals(RequesterType.Player)) {
			inquiryList = joinRepositoryCustom.findPlayerJoinRequest(StatusType.PROPOSAL,joinDto.getPlayerId(), joinDto.getTeamId(), page);
		}else {
			inquiryList = joinRepositoryCustom.findTeamJoinRequest(StatusType.PROPOSAL,joinDto.getPlayerId(), joinDto.getTeamId(), page);
		}
		
		
		
		return null;
	}

	@Override
	public JoinDto approveJoin(JoinDto joinDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<JoinDto> searchJoin(JoinSearchCondition condition, Pageable pageable) {
		
		List<JoinDto> rtnList = new ArrayList<>();
		
		if(condition.getRequesterType().equals(RequesterType.Player)) {
			rtnList = joinRepositoryCustom.findPlayerJoinRequest(condition.getStatusType(), condition.getPlayerId(), condition.getTeamId(), pageable);
		}else {
			rtnList = joinRepositoryCustom.findTeamJoinRequest(condition.getStatusType(), condition.getPlayerId(), condition.getTeamId(), pageable);
		}
		
		return rtnList;
	}
	
	@Override
	public JoinDto confirmApprove(JoinDto joinDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JoinDto rejectApprove(JoinDto joinDto) {
		// TODO Auto-generated method stub
		return null;
	}

}
