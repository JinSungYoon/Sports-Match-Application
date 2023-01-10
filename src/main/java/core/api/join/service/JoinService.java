package core.api.join.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.api.join.dto.JoinDto;
import core.api.join.dto.JoinSearchCondition;

public interface JoinService {

	// Player 가입 신청
	JoinDto requestPlayerJoin(Long id,JoinDto joinDto) throws Exception;
	// Team 가입 신청
	JoinDto requestTeamJoin(Long id,JoinDto joinDto) throws Exception;
	// 가입 신청 거절
	JoinDto rejectPlayerJoin(JoinDto joinDto) throws Exception;
	// 가입 신청 거절
	JoinDto rejectTeamJoin(JoinDto joinDto) throws Exception;
	// 가입 신청 승인
	JoinDto approvePlayerJoin(JoinDto joinDto) throws Exception;
	// 가입 신청 승인
	JoinDto approveTeamJoin(JoinDto joinDto) throws Exception;
	// 승인 확정
	JoinDto confirmPlayerApprove(JoinDto joinDto) throws Exception;
	// 승인 확정
	JoinDto confirmTeamApprove(JoinDto joinDto) throws Exception;
	// 승인 철회
	JoinDto withdrawPlayerApprove(JoinDto joinDto) throws Exception;
	// 승인 철회
	JoinDto withdrawTeamApprove(JoinDto joinDto) throws Exception;
	// 승인 반려
	JoinDto returnPlayerApprove(JoinDto joinDto) throws Exception;
	// 승인 반려
	JoinDto returnTeamApprove(JoinDto joinDto) throws Exception;
	// 선수 가입 신청 조회
	Page<JoinDto> searchPlayerJoinApplication(Long playerId,JoinSearchCondition condition,Pageable pageable);
	// 선수 가입 제안 조회
	Page<JoinDto> searchPlayerJoinOffer(Long playerId,JoinSearchCondition condition,Pageable pageable);
	// 팀 가입 신청 조회
	Page<JoinDto> searchTeamJoinApplication(Long teamId,JoinSearchCondition condition,Pageable pageable);
	// 팀 가입 제안 조회
	Page<JoinDto> searchTeamJoinOffer(Long teamId,JoinSearchCondition condition,Pageable pageable);
}
