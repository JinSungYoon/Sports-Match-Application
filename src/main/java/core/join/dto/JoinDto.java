package core.join.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import core.join.entity.JoinEntity;
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import core.player.entity.PlayerEntity;
import core.team.entity.TeamEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinDto {
	
	private Long id;
	@NotNull
	private StatusType statusType;
	@NotNull
	private RequesterType requesterType;
	@NotNull
	private Long teamId;
	private String teamName;
	@NotNull
	private Long playerId;
	private String playerName;
	@NotNull
	private char activeYN;
	
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	
	@Builder
	public JoinDto(Long id,Long playerId,String playerName, Long teamId, String teamName, RequesterType requesterType,StatusType statusType,char activeYN,LocalDateTime createdDate,LocalDateTime updatedDate) {
		this.id = id;
		this.playerId = playerId;
		this.playerName = playerName;
		this.teamId = teamId;
		this.teamName = teamName;
		this.requesterType = requesterType;
		this.statusType = statusType;
		this.activeYN = activeYN;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}
	
	public JoinDto(Long playerId,String playerName, Long teamId, String teamName, RequesterType requesterType,StatusType statusType,char activeYN,LocalDateTime createdDate,LocalDateTime updatedDate) {
		this.playerId = playerId;
		this.playerName = playerName;
		this.teamId = teamId;
		this.teamName = teamName;
		this.requesterType = requesterType;
		this.statusType = statusType;
		this.activeYN = activeYN;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}
	
	public JoinDto(Long playerId,Long teamId,RequesterType requesterType,StatusType statusType,char activeYN,LocalDateTime createdDate,LocalDateTime updatedDate) {
		this.playerId = playerId;
		this.teamId = teamId;
		this.requesterType = requesterType;
		this.statusType = statusType;
		this.activeYN = activeYN;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}
	
	public JoinDto(Long playerId,Long teamId,RequesterType requesterType,StatusType statusType) {
		this.statusType = statusType;
		this.requesterType = requesterType;
		this.teamId = teamId;
		this.playerId = playerId;
	}
	
	public JoinEntity toEntity(JoinDto join,PlayerEntity player,TeamEntity team) {
		if(this == null) return null;
		return JoinEntity.builder()
				.statusType(join.getStatusType())
				.requesterType(join.getRequesterType())
				.team(team)
				.player(player)
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
