package core.join.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import core.common.entity.BaseEntity;
import core.join.dto.JoinDto;
import core.player.entity.PlayerEntity;
import core.team.entity.TeamEntity;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "JOINING")
public class JoinEntity extends BaseEntity {
	
	@Id @GeneratedValue
	@Column(name="JOIN_ID")
	private Long joinId;
	
	@Enumerated(EnumType.STRING)
	@Column(name="STATUS_TYPE")
	private StatusType statusType;
	
	@Enumerated(EnumType.STRING)
	@Column(name="REQUESTER_TYPE")
	private RequesterType requesterType;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name="PLAYER_ID")
	private PlayerEntity player;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name="TEAM_ID")
	private TeamEntity team;
	
	@Column(name="ACTIVE_YN")
	private char activeYN;
	
	@Builder
	public JoinEntity(StatusType statusType,RequesterType requesterType,PlayerEntity player,TeamEntity team,char activeYN) {
		this.statusType = statusType;
		this.requesterType = requesterType;
		this.team = team;
		this.player = player;
		this.activeYN = activeYN;
	}
	
	public JoinDto toDto() {
		if(this == null) return null;
		return JoinDto.builder()
				.statusType(statusType)
				.requesterType(requesterType)
				.teamId(team.id)
				.playerId(player.id)
				.activeYN(activeYN)
				.build();
	}
	
	
}
