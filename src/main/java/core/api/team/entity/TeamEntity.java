package core.api.team.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import core.api.player.entity.BelongType;
import core.api.team.dto.TeamDto;
import core.common.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Entity
@Table(name="TEAM")
@NoArgsConstructor
public class TeamEntity extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TEAM_ID")
	public Long id;
	
	@NotNull
	@Column(name="TEAM_NAME",nullable=false)
	private String teamName;
	
	private String location;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private BelongType belongType;
	
	@Lob
	private String introduction;
	
	public void initId(Long id) {
		if(this.id == null) {
			this.id = id;
		}
	}
	
	@Builder
	public TeamEntity(String teamName,String location,BelongType belongType,String introduction) {
		this.teamName   = teamName;
		this.location = location;
		this.belongType = belongType;
		this.introduction = introduction;
	}
	
	public void setTeamEntity(TeamDto dto) {
		this.teamName   = dto.getTeamName();
		this.location = dto.getLocation();
		this.belongType = dto.getBelongType();
		this.introduction = dto.getIntroduction();
	}
	
	public void updateInfo(String teamName,String location,BelongType belongType,String introduction) {
		this.teamName   = teamName;
		this.location = location;
		this.belongType = belongType;
		this.introduction = introduction;
	}
	
	public TeamDto toDto() {
		if(this==null) return null;
		TeamDto dto = TeamDto.builder()
				.teamName(teamName)
				.location(location)
				.belongType(belongType)
				.introduction(introduction)
				.build();
		dto.setId(id);
		return dto;
	}
		
}
