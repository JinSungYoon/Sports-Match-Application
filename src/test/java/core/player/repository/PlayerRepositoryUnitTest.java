package core.player.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import core.player.entity.BelongType;
import core.player.entity.PlayerEntity;
import core.team.entity.TeamEntity;
import core.team.repository.TeamRepository;

/* 단위 테스트(DB 관련된 Bean이 IoC에 등록)
 * @DataJpaTest Repository관련 Bean을 Ioc를 등록
 * */

@DataJpaTest
@Transactional
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)	// 가짜 DB
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)	// 실제 DB
public class PlayerRepositoryUnitTest {
	
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private TeamRepository teamRepository;
	
	@Test
	@DisplayName("Player 생성")
	public void savePlayer() {
		
		TeamEntity team1 = new TeamEntity("team1","서울",BelongType.CLUB,"Team1팀 입니다.");
		teamRepository.save(team1);

		PlayerEntity player1 = new PlayerEntity("player1","220428-1111111",1,team1);
		PlayerEntity player2 = new PlayerEntity("player2","220428-2222222",2,team1);
		PlayerEntity player3 = new PlayerEntity("player2","220428-3333333",3,team1);
		playerRepository.save(player1);
		playerRepository.save(player2);
		playerRepository.save(player3);
		
		TeamEntity rtnTeam = teamRepository.findByTeamName("team1");
		Assertions.assertThat(rtnTeam).isEqualTo(team1);
		
		PlayerEntity rtnPlayer = playerRepository.findByPlayerName("player1");
		Assertions.assertThat(rtnPlayer).isEqualTo(player1);
	
	}

	@Test
	@DisplayName("Team Id로 Player 목록 조회하기")
	public void searchPlayerListByTeamId() {
		List<PlayerEntity> list =  playerRepository.findByTeam_id(1L);
		list.forEach(l->System.out.println(l));
	}
	
	@Test
	@DisplayName("Team 이름에 해당한 Player 목록 조회하기")
	public void searchPlayerListByTeamName() {
		TeamEntity team1 = new TeamEntity("team1","Seoul",BelongType.fromValue("C"),"1팀 입니다.");
		TeamEntity team2 = new TeamEntity("team2","Seoul",BelongType.fromValue("H"),"2팀 입니다.");
		teamRepository.save(team1);
		teamRepository.save(team2);
		PlayerEntity player1 = new PlayerEntity("가선수","220504-1111111",1,team1);
		PlayerEntity player2 = new PlayerEntity("나선수","220504-1111111",2,team2);
		PlayerEntity player3 = new PlayerEntity("다선수","220504-1111111",3,team1);
		PlayerEntity player4 = new PlayerEntity("라선수","220504-1111111",4,team2);
		PlayerEntity player5 = new PlayerEntity("마선수","220504-1111111",5,team2);
		PlayerEntity player6 = new PlayerEntity("바선수","220504-1111111",6,team1);
		PlayerEntity player7 = new PlayerEntity("사선수","220504-1111111",7,team2);
		playerRepository.save(player1);
		playerRepository.save(player2);
		playerRepository.save(player3);
		playerRepository.save(player4);
		playerRepository.save(player5);
		playerRepository.save(player6);
		playerRepository.save(player7);
		List<PlayerEntity> list = playerRepository.findByTeam_teamName("team1");
		list.forEach(d->System.out.println(d.toString()));
	}
	
}
