package core.join.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.join.dto.JoinDto;
import core.join.dto.JoinSearchCondition;

public interface JoinService {
	// 가입 신청
	JoinDto requestJoin(JoinDto joinDto);
	// 가입 신청 거절
	JoinDto rejectJoin(JoinDto joinDto);
	// 가입 신청 승인
	JoinDto approveJoin(JoinDto joinDto);
	// 선수 가입 제안 조회
	Page<JoinDto> searchPlayerJoinApplication(JoinSearchCondition condition,Pageable pageable);
	// 선수 가입 제안 조회
	Page<JoinDto> searchPlayerJoinOffer(JoinSearchCondition condition,Pageable pageable);
	// 팀 가입 신청 조회
	Page<JoinDto> searchTeamJoinApplication(JoinSearchCondition condition,Pageable pageable);
	// 팀 가입 제안 조회
	Page<JoinDto> searchTeamJoinOffer(JoinSearchCondition condition,Pageable pageable);
	// 승인 확정
	JoinDto confirmApprove(JoinDto joinDto);
	// 승인 거절
	JoinDto rejectApprove(JoinDto joinDto);
}
