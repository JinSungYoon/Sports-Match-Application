package core.player.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import core.player.entity.PlayerEntity;
import core.player.entity.TeamEntity;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlayerRepositoryTest {
	
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private TeamRepository teamRepository;
	
	@Test
	@Rollback(false)
	@DisplayName("Player 생성")
	public void savePlayer() {
		
		TeamEntity team1 = new TeamEntity("team1","Team1팀 입니다.");
		teamRepository.save(team1);

		PlayerEntity player1 = new PlayerEntity("player1","220428-1111111",1,team1);
		PlayerEntity player2 = new PlayerEntity("player2","220428-2222222",2,team1);
		PlayerEntity player3 = new PlayerEntity("player2","220428-3333333",3,team1);
		playerRepository.save(player1);
		playerRepository.save(player2);
		playerRepository.save(player3);
		
		TeamEntity rtnTeam = teamRepository.findById(1L).orElse(null);
		Assertions.assertThat(rtnTeam).isEqualTo(team1);
		
		PlayerEntity rtnPlayer = playerRepository.findByName("player1");
		Assertions.assertThat(rtnPlayer).isEqualTo(player1);
	
	}

	@Test
	@DisplayName("Team 이름으로 Player 목록 조회하기")
	public void searchPlayerListByTeamName() {
		List<PlayerEntity> list =  playerRepository.findByTeam_teamId(1L);
		list.forEach(l->System.out.println(l));
	}
	
}
