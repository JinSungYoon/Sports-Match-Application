package core.join.dto;

import java.time.LocalDateTime;

import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import lombok.Data;

@Data
public class JoinSearchCondition {
	private StatusType statusType;
	private RequesterType requesterType;
	private Long teamId;
	private Long playerId;
	private char activeYN;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
}
