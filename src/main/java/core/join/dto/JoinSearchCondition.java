package core.join.dto;

import java.time.LocalDateTime;

import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinSearchCondition {
	private StatusType statusType;
	private RequesterType requesterType;
	private char activeYN;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	
	public JoinSearchCondition(StatusType statusType, RequesterType requesterType, char activeYN, LocalDateTime fromDate, LocalDateTime toDate) {
		this.statusType = statusType;
		this.requesterType = requesterType;
		this.activeYN = activeYN;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}
}
