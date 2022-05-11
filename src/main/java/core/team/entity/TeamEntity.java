package core.team.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import core.player.dto.PlayerDto;
import core.player.entity.BaseEntity;
import core.player.entity.BelongType;
import core.team.dto.TeamDto;
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
		return TeamDto.builder()
				.teamName(teamName)
				.location(location)
				.belongType(belongType)
				.introduction(introduction)
				.build();
	}
		
}
