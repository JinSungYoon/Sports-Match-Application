package core.player.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name="team")
@NoArgsConstructor
@RequiredArgsConstructor
public class Team {
	
	@Id
	@NonNull
	@Column(name="TEAM_ID")
	private String teamId;
	
	@NonNull
	@Column(name="NAME")
	private String name;
	
	@OneToMany(mappedBy="team",fetch = FetchType.LAZY)
	private List<Player> player = new ArrayList<Player>();
		
}
