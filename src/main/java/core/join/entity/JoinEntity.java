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
import javax.persistence.PrePersist;
import javax.persistence.Table;

import core.common.entity.BaseEntity;
import core.join.dto.JoinDto;
import core.player.entity.PlayerEntity;
import core.team.entity.TeamEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "JOINING")
@NoArgsConstructor
public class JoinEntity extends BaseEntity {
	
	@Id @GeneratedValue
	@Column(name="JOIN_ID")
	private Long id;
	
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

	@PrePersist
	public void prePersist() {
		if(this.activeYN != 0)
			this.activeYN = this.activeYN;
		else
			this.activeYN = 'Y';
	}
	
	public void initId(Long id) {
		if(this.id == null) {
			this.id = id;
		}
	}
	
	@Builder
	public JoinEntity(RequesterType requesterType,StatusType statusType,PlayerEntity player,TeamEntity team) {
		this.statusType = statusType;
		this.requesterType = requesterType;
		this.team = team;
		this.player = player;
	}
	
	public void updateStatus(StatusType status) {
		this.statusType = status;
	}
	
	public JoinDto toDto() {
		if(this == null) return null;
		return JoinDto.builder()
				.id(id)
				.statusType(statusType)
				.requesterType(requesterType)
				.teamId(team.getId())
				.teamName(team.getTeamName())
				.playerId(player.getId())
				.playerName(player.getPlayerName())
				.activeYN(activeYN)
				.createdDate(createdDate)
				.updatedDate(updatedDate)
				.build();
	}
	
}
