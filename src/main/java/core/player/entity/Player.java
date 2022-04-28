package core.player.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="PLAYER")
@NoArgsConstructor
@RequiredArgsConstructor
public class Player {
	@NonNull
	@Column(name="NAME",nullable = false,length=20)
	private String name;
	
	@Id
	@NonNull
	@Column(name="RES_REG_NO",nullable = false,length=14)
	private String resRegNo;
	
	@Column(name="UNIFORM_NO")
	private int uniformNo;
	
	@ManyToOne
	@JoinColumn(name="TEAM_ID")
	private Team team;
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
}
