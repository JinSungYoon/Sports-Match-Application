package core.api.join.dto;

import java.time.LocalDateTime;

import core.api.join.entity.RequesterType;
import core.api.join.entity.StatusType;
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
	
	public void setFromToDate(LocalDateTime date,Integer beforeAfter,Integer diff) {
		
		date = date.withNano(0);
		
		LocalDateTime fromDate = date;
		LocalDateTime toDate = date;
		
		// 특정 일자로부터 이전 일자를 구할때
		if(beforeAfter == -1) {
			fromDate = fromDate.minusDays(diff);
		}else if(beforeAfter == 1) {	// 특정 일자로부터 이후 일자를 구할때
			toDate = toDate.plusDays(diff);
			
		}else if(beforeAfter == 0){		// 특정 일자로부터 앞뒤 일자를 구할떄
			fromDate = fromDate.minusDays(diff);
			toDate = toDate.plusDays(diff);
		}
		
		this.fromDate = fromDate;
		this.toDate   = toDate;
	}
}
