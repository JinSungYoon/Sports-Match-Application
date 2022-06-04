package core.join.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import core.common.entity.BaseEntity;
import core.join.dto.JoinDto;
import core.join.dto.JoinDto.JoinDtoBuilder;
import core.player.entity.PlayerEntity;
import core.team.entity.TeamEntity;
import lombok.Builder;

@Entity
@Table(name = "JOIN")
public class JoinEntity extends BaseEntity {
	
	@Id @GeneratedValue
	@Column(name="JOIN_ID")
	private Long joinId;
	
	@Enumerated
	@Column(name="STATUS_TYPE")
	private StatusType statusType;
	
	@Enumerated
	@Column(name="REQUESTER_TYPE")
	private RequesterType requesterType;
	
	@Column(name="TEAM_ID")
	private Long teamId;
	
	@Column(name="PLAYER_ID")
	private Long playerId;
	
	@Column(name="ACTIVE_YN")
	private char activeYN;
	
	@Builder
	public JoinEntity(StatusType statusType,RequesterType requesterType,Long teamId,Long playerId,char activeYN) {
		this.statusType = statusType;
		this.requesterType = requesterType;
		this.teamId = teamId;
		this.playerId = playerId;
		this.activeYN = activeYN;
	}
	
	public JoinDto toDto() {
		if(this == null) return null;
		return JoinDto.builder()
				.statusType(statusType)
				.requesterType(requesterType)
				.teamId(teamId)
				.playerId(playerId)
				.activeYN(activeYN)
				.build();
	}
	
	
}
