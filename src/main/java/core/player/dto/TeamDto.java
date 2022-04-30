package core.player.dto;

import javax.validation.constraints.NotNull;

import core.player.entity.TeamEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamDto {
	
	@NotNull
	private String name;
	
	private String introduction;
	
	@Builder
	public TeamDto(String name,String introduction) {
		this.name = name;
		this.introduction = introduction;
	}
	
	public TeamEntity toEntity() {
		if(this==null) return null;
		return TeamEntity.builder()
				.name(name)
				.introduction(introduction)
				.build();
	}
	
	
}
