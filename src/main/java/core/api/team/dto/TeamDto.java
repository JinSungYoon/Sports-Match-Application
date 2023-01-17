package core.api.team.dto;

import javax.validation.constraints.NotNull;

import core.api.player.entity.BelongType;
import core.api.team.entity.TeamEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamDto {
	
	@Schema(description="Team ID", example="1L",required=true)
	private Long id;
	
	@NotNull
	@Schema(description="Team Name", example="purpleTeam",required=true)
	private String teamName;
	
	@Schema(description="Location", example="Seoul",required=false)
	private String location;
	
	@Schema(description="Belong Type", example="CLUB",required=false)
	private BelongType belongType;
	
	@Schema(description="Introduction", example="We are Purple team",required=false)
	private String introduction;
	
	@Builder
	public TeamDto(String teamName,String location,BelongType belongType,String introduction) {
		this.teamName = teamName;
		this.location = location;
		this.belongType = belongType;
		this.introduction = introduction;
	}
	
	public TeamEntity toEntity() {
		return TeamEntity.builder()
				.teamName(teamName)
				.location(location)
				.belongType(belongType)
				.introduction(introduction)
				.build();
	}
	
	
}
