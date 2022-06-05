package core.join.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import core.common.entity.BaseEntity;
import core.join.dto.JoinDto;
import lombok.Builder;

@Entity
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
	
	@Column(name="PLAYER_ID")
	private Long playerId;
	
	@Column(name="TEAM_ID")
	private Long teamId;
	
	@Column(name="ACTIVE_YN")
	private char activeYN;
	
	@Builder
	public JoinEntity(StatusType statusType,RequesterType requesterType,Long playerId,Long teamId,char activeYN) {
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
