package core.join.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.join.dto.JoinDto;
import core.join.dto.JoinSearchCondition;

public interface JoinService {

	// Player 가입 신청
	JoinDto requestPlayerJoin(Long id,JoinDto joinDto);
	// Team 가입 신청
	JoinDto requestTeamJoin(Long id,JoinDto joinDto);
	// 가입 신청 거절
	JoinDto rejectPlayerJoin(Long playerId,Long teamId);
	// 가입 신청 거절
	JoinDto rejectTeamJoin(Long teamId,Long playerId);
	// 가입 신청 승인
	JoinDto approveJoin(JoinDto joinDto);
	// 선수 가입 신청 조회
	Page<JoinDto> searchPlayerJoinApplication(Long playerId,JoinSearchCondition condition,Pageable pageable);
	// 선수 가입 제안 조회
	Page<JoinDto> searchPlayerJoinOffer(Long playerId,JoinSearchCondition condition,Pageable pageable);
	// 팀 가입 신청 조회
	Page<JoinDto> searchTeamJoinApplication(Long teamId,JoinSearchCondition condition,Pageable pageable);
	// 팀 가입 제안 조회
	Page<JoinDto> searchTeamJoinOffer(Long teamId,JoinSearchCondition condition,Pageable pageable);
	// 승인 확정
	JoinDto confirmApprove(JoinDto joinDto);
	// 승인 철회
	JoinDto withdrawApprove(JoinDto joinDto);
}
