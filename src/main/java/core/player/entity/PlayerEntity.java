package core.player.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import core.player.dto.PlayerDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@Entity
@Table(name="PLAYER")
@NoArgsConstructor
public class PlayerEntity extends BaseEntity{
	
	@NotNull
	@Column(name="PLAYER_NAME",nullable = false,length=20)
	private String playerName;
	
	@NotNull
	@Column(name="RES_REG_NO",nullable = false,length=14)
	private String resRegNo;
	
	@NotNull
	@Column(name="UNIFORM_NO",unique=true)
	private int uniformNo;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="TEAM_ID")
	private TeamEntity team;
	
	public void setTeam(TeamEntity team) {
		this.team = team;
	}
	
	@Builder
	public PlayerEntity(String playerName,String resRegNo,int uniformNo,TeamEntity team) {
		this.playerName = playerName;
		this.resRegNo = resRegNo;
		this.uniformNo = uniformNo;
		this.team = team;
	}
	
	public void setPlayerEntity(PlayerDto dto) {
		this.playerName = dto.getPlayerName();
		this.resRegNo = dto.getResRegNo();
		this.uniformNo = dto.getUniformNo();
		this.team = dto.getTeam().toEntity();
	}
	
	public void updateInfo(String playerName,String resRegno,int uniformNo,TeamEntity team) {
		this.playerName = playerName;
		this.resRegNo = resRegNo;
		this.uniformNo = uniformNo;
		this.team = team;
	}
	
	public PlayerDto toDto() {
		if(this == null) return null;
		return PlayerDto.builder()
				.playerName(playerName)
				.resRegNo(resRegNo)
				.uniformNo(uniformNo)
				.team(team.toDto())
				.build();
	}
	
}
