package core.join.dto;

import javax.validation.constraints.NotNull;

import core.join.entity.JoinEntity;
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import core.player.entity.PlayerEntity;
import core.team.entity.TeamEntity;
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
	public JoinDto(StatusType statusType,RequesterType requesterType,Long playerId,Long teamId,char activeYN) {
		this.statusType = statusType;
		this.requesterType = requesterType;
		this.teamId = teamId;
		this.playerId = playerId;
		this.activeYN = activeYN;
	}
	
	public JoinEntity toEntity(JoinDto join,PlayerEntity player,TeamEntity team) {
		if(this == null) return null;
		return JoinEntity.builder()
				.statusType(join.getStatusType())
				.requesterType(join.getRequesterType())
				.team(team)
				.player(player)
				.activeYN(join.getActiveYN())
				.build();
	}
	
	public Boolean checkStatus(StatusType statusType) {
		if(this.statusType.equals(statusType)) {
			return true;
		}else {
			return false;
		}
		 
	}
	
}
