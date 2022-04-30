package core.player.entity;

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
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="PLAYER")
@NoArgsConstructor
public class PlayerEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="PLAYER_ID")
	private Long playerId;
	
	@NonNull
	@Column(name="NAME",nullable = false,length=20)
	private String name;
	
	@NonNull
	@Column(name="RES_REG_NO",nullable = false,length=14)
	private String resRegNo;
	
	@Column(name="UNIFORM_NO",unique=true)
	private int uniformNo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TEAM_ID")
	private TeamEntity team;
	
	public void setTeam(TeamEntity team) {
		this.team = team;
	}
	
	@Builder
	public PlayerEntity(String name,String resRegNo,int uniformNo,TeamEntity team) {
		this.name = name;
		this.resRegNo = resRegNo;
		this.uniformNo = uniformNo;
		this.team = team;
	}
	
	
}
