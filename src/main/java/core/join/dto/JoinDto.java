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
	private String teamId;
	@NotNull
	private String playerId;
	
	@Builder
	public JoinDto(StatusType statusType,RequesterType requesterType,String teamId,String playerId) {
		this.statusType = statusType;
		this.requesterType = requesterType;
		this.teamId = teamId;
		this.playerId = playerId;
	}
	
	public JoinEntity toEntity() {
		if(this == null) return null;
		return JoinEntity.builder()
				.statusType(statusType)
				.requesterType(requesterType)
				.teamId(teamId)
				.playerId(playerId)
				.build();
	}
}
