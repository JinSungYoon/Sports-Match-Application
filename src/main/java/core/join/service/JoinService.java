package core.join.service;

import core.join.dto.JoinDto;

public interface JoinService {
	// 등록 신청
	JoinDto requestJoin(JoinDto joinDto);
	// 등록 거절
	JoinDto rejectJoin(JoinDto joinDto);
	// 등록 승인
	JoinDto approveJoin(JoinDto joinDto);
	// 승인 확정
	JoinDto confirmApprove(JoinDto joinDto);
	// 승인 거절
	JoinDto rejectApprove(JoinDto joinDto);
}
