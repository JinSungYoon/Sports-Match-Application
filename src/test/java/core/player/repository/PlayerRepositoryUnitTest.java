package core.player.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;

import com.querydsl.jpa.impl.JPAQueryFactory;

import core.api.player.entity.BelongType;
import core.api.player.entity.PlayerEntity;
import core.api.player.repository.PlayerRepository;
import core.api.player.repository.PlayerRepositoryCustom;
import core.api.team.entity.TeamEntity;
import core.api.team.repository.TeamRepository;
import core.api.team.repository.TeamRepositoryCustom;
import lombok.extern.slf4j.Slf4j;

/* 단위 테스트(DB 관련된 Bean이 IoC에 등록)
 * @DataJpaTest Repository관련 Bean을 Ioc를 등록
 * */

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PlayerRepositoryUnitTest {
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private PlayerRepositoryCustom playerRepositoryCustom;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	TeamRepositoryCustom teamRepositoryCustom;
	
	
	@Autowired
	EntityManager entityManager;
	
	@TestConfiguration
    static class TestConfig {

        @PersistenceContext
        private EntityManager entityManager;

        @Bean
        public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory(entityManager);
        }
    }

	
	@AfterEach
	public void init() {
		playerRepository.deleteAll();
		entityManager
		.createNativeQuery("ALTER TABLE player AUTO_INCREMENT = 1;")
		.executeUpdate() ;
	}
	
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
		PageRequest pageRequest = PageRequest.of(0, 2);
		TeamEntity rtnTeam = teamRepositoryCustom.findTeam("team1",null,null,null,pageRequest).get(0);
		Assertions.assertThat(rtnTeam).isEqualTo(team1);
		
		PlayerEntity rtnPlayer = playerRepository.findByPlayerName("player1");
		Assertions.assertThat(rtnPlayer).isEqualTo(player1);
		Assertions.assertThat(rtnPlayer.getId()).isEqualTo(1L);
	}
	
	@Test
	@DisplayName("Team Id로 Player 목록 조회하기")
	public void searchPlayerListByTeamId() {
		TeamEntity team1 = new TeamEntity("team1","서울",BelongType.CLUB,"Team1팀 입니다.");
		teamRepository.save(team1);

		PlayerEntity player1 = new PlayerEntity("player1","220428-1111111",1,team1);
		PlayerEntity player2 = new PlayerEntity("player2","220428-2222222",2,team1);
		PlayerEntity player3 = new PlayerEntity("player2","220428-3333333",3,team1);
		playerRepository.save(player1);
		playerRepository.save(player2);
		playerRepository.save(player3);
		
		List<PlayerEntity> list =  playerRepository.findByTeam_id(1L);
		Assertions.assertThat(list.get(0).getPlayerName()).isEqualTo(player1.getPlayerName());
		Assertions.assertThat(list.get(1).getResRegNo()).isEqualTo(player2.getResRegNo());
		Assertions.assertThat(list.get(2).getUniformNo()).isEqualTo(player3.getUniformNo());
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
		List<PlayerEntity> list1 = playerRepository.findByTeam_teamName("team1");
		Assertions.assertThat(list1.get(0).getPlayerName()).isEqualTo(player1.getPlayerName());
		Assertions.assertThat(list1.get(1).getResRegNo()).isEqualTo(player3.getResRegNo());
		Assertions.assertThat(list1.get(2).getUniformNo()).isEqualTo(player6.getUniformNo());
		List<PlayerEntity> list2 = playerRepository.findByTeam_teamName("team2");
		Assertions.assertThat(list2.get(0).getPlayerName()).isEqualTo(player2.getPlayerName());
		Assertions.assertThat(list2.get(1).getResRegNo()).isEqualTo(player4.getResRegNo());
		Assertions.assertThat(list2.get(2).getUniformNo()).isEqualTo(player5.getUniformNo());
		Assertions.assertThat(list2.get(3).getTeam()).isEqualTo(player7.getTeam());
	}
	
	@Test
	@DisplayName("조건에 맞는 player 찾기")
	void findPlayers() {
		// given
		TeamEntity team1 = new TeamEntity("team1","Seoul",BelongType.fromValue("C"),"1팀 입니다.");
		TeamEntity team2 = new TeamEntity("team2","Seoul",BelongType.fromValue("H"),"2팀 입니다.");
		TeamEntity team3 = new TeamEntity("team3","Seoul",BelongType.fromValue("U"),"3팀 입니다.");
		teamRepository.save(team1);
		teamRepository.save(team2);
		teamRepository.save(team3);
		PlayerEntity player1 = new PlayerEntity("가선수","220504-1111111",1,team1);
		PlayerEntity player2 = new PlayerEntity("나선수","220504-1111111",1,team2);
		PlayerEntity player3 = new PlayerEntity("다선수","220504-1111111",2,team1);
		PlayerEntity player4 = new PlayerEntity("라선수","220504-1111111",2,team2);
		PlayerEntity player5 = new PlayerEntity("마선수","220504-1111111",1,team3);
		PlayerEntity player6 = new PlayerEntity("바선수","220504-1111111",3,team1);
		PlayerEntity player7 = new PlayerEntity("사선수","220504-1111111",3,team2);
		PlayerEntity player8 = new PlayerEntity("아선수","220504-1111111",2,team3);
		PlayerEntity player9 = new PlayerEntity("자선수","220504-1111111",4,team2);
		PlayerEntity player10 = new PlayerEntity("차선수","220504-1111111",4,team1);
		PlayerEntity player11 = new PlayerEntity("카선수","220504-1111111",5,team2);
		PlayerEntity player12 = new PlayerEntity("타선수","220504-1111111",3,team3);
		PlayerEntity player13 = new PlayerEntity("파선수","220504-1111111",4,team3);
		PlayerEntity player14 = new PlayerEntity("하선수","220504-1111111",6,team2);
		playerRepository.save(player1);
		playerRepository.save(player2);
		playerRepository.save(player3);
		playerRepository.save(player4);
		playerRepository.save(player5);
		playerRepository.save(player6);
		playerRepository.save(player7);
		playerRepository.save(player8);
		playerRepository.save(player9);
		playerRepository.save(player10);
		playerRepository.save(player11);
		playerRepository.save(player12);
		playerRepository.save(player13);
		playerRepository.save(player14);
		
		// when
		PageRequest pageRequest = PageRequest.of(1, 2);
		List<PlayerEntity> playerList1 =  playerRepositoryCustom.findPlayer("선수", null, null,pageRequest);
		List<PlayerEntity> playerList2 =  playerRepositoryCustom.findPlayer(null, 3, null,pageRequest);
		List<PlayerEntity> playerList3 =  playerRepositoryCustom.findPlayer(null, null, "team2",pageRequest);
		
		// then
		Assertions.assertThat(playerList1.size()).isEqualTo(2);
		Assertions.assertThat(playerList2.size()).isEqualTo(1);
		Assertions.assertThat(playerList3.size()).isEqualTo(2);
		Assertions.assertThat(playerList1.get(0)).isEqualTo(player3);
		Assertions.assertThat(playerList2.get(0)).isEqualTo(player12);
		Assertions.assertThat(playerList3.get(0)).isEqualTo(player7);
	}
}
