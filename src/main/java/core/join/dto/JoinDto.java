package core.join.dto;

import javax.validation.constraints.NotNull;

import core.join.entity.JoinEntity;
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import lombok.Builder;
import lombok.Data;

@Data
public class JoinDto {
	
	private Long id;
	@NotNull
	private StatusType statusType;
	@NotNull
	private RequesterType requesterType;
	@NotNull
	private Long teamId;
	@NotNull
	private Long playerId;
	@NotNull
	private char activeYN;
	
	@Builder
	public JoinDto(StatusType statusType,RequesterType requesterType,Long teamId,Long playerId,char activeYN) {
		this.statusType = statusType;
		this.requesterType = requesterType;
		this.teamId = teamId;
		this.playerId = playerId;
		this.activeYN = activeYN;
	}
	
	public JoinEntity toEntity() {
		if(this == null) return null;
		return JoinEntity.builder()
				.statusType(statusType)
				.requesterType(requesterType)
				.teamId(teamId)
				.playerId(playerId)
				.activeYN(activeYN)
				.build();
	}
}
