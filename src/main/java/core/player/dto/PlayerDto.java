package core.player.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import core.player.entity.PlayerEntity;
import core.player.entity.TeamEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class PlayerDto {
	
	@NotNull
	private String playerName;
	
	@NotNull
	@PositiveOrZero
	private int uniformNo;
	
	@NotNull
	@Size(min=14,max=14)
	private String resRegNo;
	
	private TeamDto team;
	
	@Builder
	public PlayerDto(String playerName,String resRegNo,int uniformNo,TeamDto team) {
		this.playerName = playerName;
		this.resRegNo = resRegNo;
		this.uniformNo = uniformNo;
		this.team = team;
	}
	
	public PlayerEntity toEntity() {
		if(this == null) return null;
		return PlayerEntity.builder()
				.playerName(playerName)
				.resRegNo(resRegNo)
				.uniformNo(uniformNo)
				.team(team.toEntity())
				.build();
				
	}
	
}
