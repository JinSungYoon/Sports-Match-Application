package core.player.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Builder;
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
@Table(name="team")
@NoArgsConstructor
@RequiredArgsConstructor
public class TeamEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="TEAM_ID")
	private Long teamId;
	
	@NonNull
	@Column(name="NAME")
	private String name;
	
	@Lob
	private String introduction;
	
	@Builder
	public TeamEntity(String name,String introduction) {
		this.name   = name;
		this.introduction = introduction;
	}
		
}
