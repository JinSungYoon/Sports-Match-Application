package core.join.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.join.dto.JoinDto;
import core.join.dto.JoinSearchCondition;

public interface JoinService {
	// 등록 신청
	JoinDto requestJoin(JoinDto joinDto);
	// 등록 거절
	JoinDto rejectJoin(JoinDto joinDto);
	// 등록 승인
	JoinDto approveJoin(JoinDto joinDto);
	// 등록 조회
	Page<JoinDto> searchJoin(JoinSearchCondition condition,Pageable pageable);
	// 승인 확정
	JoinDto confirmApprove(JoinDto joinDto);
	// 승인 거절
	JoinDto rejectApprove(JoinDto joinDto);
}
