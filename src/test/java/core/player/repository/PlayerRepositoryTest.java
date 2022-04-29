package core.player.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import core.player.entity.Player;
import core.player.entity.Team;

@SpringBootTest
//@DataJpaTest
//@EnableAutoConfiguration
//@ContextConfiguration(classes = {Player.class, PlayerRepository.class, Team.class, TeamRepository.class})
public class PlayerRepositoryTest {
	
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private TeamRepository teamRepository;
	
	@Test
	@Transactional
	@DisplayName("Player 생성 확인")
	public void savePlayer() {
		
		Team team1 = new Team();
		team1.setTeamId(1L);
		team1.setName("team1");
		teamRepository.save(team1);

		Player player1 = new Player();
		player1.setName("player1");
		player1.setResRegNo("220428-1111111");
		player1.setUniformNo(1);		
		player1.setTeam(team1);
		team1.getPlayers().add(player1);
		playerRepository.save(player1);
		
		Team findTeam = teamRepository.findByName("team1");
		Assertions.assertThat(findTeam).isEqualTo(team1);
		
//		Optional<Player> rtnPlayer = playerRepository.findById(1L);
//		Assertions.assertThat(rtnPlayer).isEqualTo(player1);
//		Optional<Team> rtnTeam = teamRepository.findById(1L);
//		Assertions.assertThat(rtnTeam).isEqualTo(team1);

		
	}
	
}
