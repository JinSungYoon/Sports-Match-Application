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

import com.querydsl.jpa.impl.JPAQueryFactory;

import core.player.entity.BelongType;
import core.player.entity.PlayerEntity;
import core.team.entity.TeamEntity;
import core.team.repository.TeamRepository;
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
	private TeamRepository teamRepository;
	
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
		
		TeamEntity rtnTeam = teamRepository.findByTeamName("team1");
		Assertions.assertThat(rtnTeam).isEqualTo(team1);
		
		PlayerEntity rtnPlayer = playerRepository.findByPlayerName("player1");
		Assertions.assertThat(rtnPlayer).isEqualTo(player1);
		Assertions.assertThat(rtnPlayer.getId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("Player 찾기")
	public void findPlayerTest() {
		log.debug("============================================ Start findByPlayrNameLike ============================================");
		TeamEntity team1 = new TeamEntity("team1","Seoul",BelongType.fromValue("C"),"1팀 입니다.");
		TeamEntity team2 = new TeamEntity("team2","Seoul",BelongType.fromValue("H"),"2팀 입니다.");
		teamRepository.save(team1);
		teamRepository.save(team2);
		PlayerEntity player1 = new PlayerEntity("player1","220504-1111111",1,team1);
		PlayerEntity player2 = new PlayerEntity("player2","220504-1111111",2,team2);
		PlayerEntity player3 = new PlayerEntity("player3","220504-1111111",3,team1);
		PlayerEntity player4 = new PlayerEntity("player4","220504-1111111",4,team2);
		PlayerEntity player5 = new PlayerEntity("player5","220504-1111111",5,team2);
		PlayerEntity player6 = new PlayerEntity("player6","220504-1111111",6,team1);
		PlayerEntity player7 = new PlayerEntity("player7","220504-1111111",7,team2);
		List<PlayerEntity> repository = new ArrayList<>();
		repository.add(player1);
		repository.add(player2);
		repository.add(player3);
		repository.add(player4);
		repository.add(player5);
		repository.add(player6);
		repository.add(player7);
		playerRepository.saveAll(repository);
		
		List<PlayerEntity> list =  playerRepository.findByPlayerNameContaining("player");
		
		assertThat(list.get(1)).isEqualTo(player2); 
		
		list.forEach(item -> System.out.println(item));
		log.debug("============================================ End findByPlayrNameLike ============================================");
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
	
}
