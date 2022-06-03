package core.join.entity;

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
	private Long joinId;
	
	@Enumerated
	private StatusType statusType;
	
	@Enumerated
	private RequesterType requesterType;
	
	private String teamId;
	
	private String playerId;
	
	@Builder
	public JoinEntity(StatusType statusType,RequesterType requesterType,String teamId,String playerId) {
		this.statusType = statusType;
		this.requesterType = requesterType;
		this.teamId = teamId;
		this.playerId = playerId;
	}
	
	public JoinDto toDto() {
		if(this == null) return null;
		return JoinDto.builder()
				.statusType(statusType)
				.requesterType(requesterType)
				.teamId(teamId)
				.playerId(playerId)
				.build();
	}
	
	
}
