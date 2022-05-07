package core.team.dto;

import javax.validation.constraints.NotNull;

import core.player.entity.BelongType;
import core.team.entity.TeamEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamDto {
	
	@NotNull
	private String teamName;
	
	private String location;
	
	private BelongType belongType;
	
	private String introduction;
	
	@Builder
	public TeamDto(String teamName,String location,BelongType belongType,String introduction) {
		this.teamName = teamName;
		this.location = location;
		this.belongType = belongType;
		this.introduction = introduction;
	}
	
	public TeamEntity toEntity() {
		if(this==null) return null;
		return TeamEntity.builder()
				.teamName(teamName)
				.location(location)
				.belongType(belongType)
				.introduction(introduction)
				.build();
	}
	
	
}
