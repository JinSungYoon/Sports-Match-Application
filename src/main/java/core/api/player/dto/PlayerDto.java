package core.api.player.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import core.api.player.entity.PlayerEntity;
import core.api.team.dto.TeamDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class PlayerDto {
	
	private Long id;
	
	@NotNull
	private String playerName;
	
	@NotNull
	private String resRegNo;
	
	@NotNull
	@PositiveOrZero
	private int uniformNo;
	
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
		if(team!=null) {
			return PlayerEntity.builder()
					.playerName(playerName)
					.resRegNo(resRegNo)
					.uniformNo(uniformNo)
					.team(team.toEntity())
					.build();
		}else {
			return PlayerEntity.builder()
					.playerName(playerName)
					.resRegNo(resRegNo)
					.uniformNo(uniformNo)
					.team(null)
					.build();
		}
	}
	
	public boolean isExistTeam() {
		if (this.team == null) {
			return false;
		}else {
			return true;
		}
	}
	
}
