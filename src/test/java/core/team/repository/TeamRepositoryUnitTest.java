package core.team.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.querydsl.jpa.impl.JPAQueryFactory;

import core.player.entity.BelongType;
import core.team.entity.TeamEntity;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TeamRepositoryUnitTest {
	
	@Autowired
	TeamRepository teamRepository;
	
	@Autowired
	TeamRepositoryCustom teamRepositoryCustom;
	
	@TestConfiguration
    static class TestConfig {

        @PersistenceContext
        private EntityManager entityManager;

        @Bean
        public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory(entityManager);
        }
    }
	
	@Test
	@DisplayName("팀 만들기")
	public void createTeams() {
		TeamEntity team1 = new TeamEntity("team1","서대문구",BelongType.CLUB,"Team1입니다.");
		TeamEntity team2 = new TeamEntity("team2","서대문구",BelongType.CLUB,"Team2입니다.");
		TeamEntity team3 = new TeamEntity("team3","은평구",BelongType.CLUB,"Team3입니다.");
		TeamEntity team4 = new TeamEntity("team4","서대문구",BelongType.CLUB,"Team4입니다.");
		TeamEntity team5 = new TeamEntity("team5","마포구",BelongType.CLUB,"Team5입니다.");
		List<TeamEntity> teamList = new ArrayList<TeamEntity>();
		teamList.add(team1);
		teamList.add(team2);
		teamList.add(team3);
		teamList.add(team4);
		teamList.add(team5);
		teamRepository.saveAll(teamList);
		List<TeamEntity> rtnTeamList = teamRepository.findAll();
		rtnTeamList.forEach(list -> System.out.println(list));
	}
	
	@Test()
	@DisplayName("팀 만들기(Not null에 null 입력에 대한 에러)")
	public void createTeamNullPointError() {
		TeamEntity team = new TeamEntity(null,"Naju",BelongType.MIDDLE_SCHOOL,"에러를 만드는 팀입니다.");
		Assertions.assertThrows(ConstraintViolationException.class, ()-> teamRepository.save(team));
	}
	
	@Test
	@DisplayName("팀 전체 찾기")
	void findTeamAllTest() {
		TeamEntity team1 = new TeamEntity("team1","서울시 서대문구",BelongType.CLUB,"Team1입니다. 서울시 화이팅!!!");
		TeamEntity team2 = new TeamEntity("team2","경기도 동안구",BelongType.CLUB,"Team2입니다. 경기도 화이팅!!!");
		TeamEntity team3 = new TeamEntity("team3","서울시 은평구",BelongType.HIGH_SCHOOL,"Team3입니다. 서울시 화이팅!!!");
		TeamEntity team4 = new TeamEntity("team4","경기도 분당구",BelongType.HIGH_SCHOOL,"Team4입니다. 경기도 화이팅!!!");
		TeamEntity team5 = new TeamEntity("team5","서울시 마포구",BelongType.CLUB,"Team5입니다. 서울시 화이팅!!!");
		List<TeamEntity> teamList = new ArrayList<TeamEntity>();
		teamList.add(team1);
		teamList.add(team2);
		teamList.add(team3);
		teamList.add(team4);
		teamList.add(team5);
		teamRepository.saveAll(teamList);
		PageRequest pageRequest = PageRequest.of(1, 2);
		Page<TeamEntity> list = teamRepository.findAll(pageRequest);
		
		assertEquals(list.getSize(), 2);
		assertEquals(list.getTotalPages(),3);
		assertEquals(list.getTotalElements(),5L);
	}
	
	@Test
	@DisplayName("팀 정보로 팀 찾기")
	public void findTeamTest() {
		TeamEntity team1 = new TeamEntity("team1","서울시 서대문구",BelongType.CLUB,"Team1입니다. 서울시 화이팅!!!");
		TeamEntity team2 = new TeamEntity("team2","경기도 동안구",BelongType.CLUB,"Team2입니다. 경기도 화이팅!!!");
		TeamEntity team3 = new TeamEntity("team3","서울시 은평구",BelongType.HIGH_SCHOOL,"Team3입니다. 서울시 화이팅!!!");
		TeamEntity team4 = new TeamEntity("team4","경기도 분당구",BelongType.HIGH_SCHOOL,"Team4입니다. 경기도 화이팅!!!");
		TeamEntity team5 = new TeamEntity("team5","서울시 마포구",BelongType.CLUB,"Team5입니다. 서울시 화이팅!!!");
		List<TeamEntity> teamList = new ArrayList<TeamEntity>();
		teamList.add(team1);
		teamList.add(team2);
		teamList.add(team3);
		teamList.add(team4);
		teamList.add(team5);
		PageRequest pageRequest = PageRequest.of(0, 2);
		teamRepository.saveAll(teamList);
		List<TeamEntity> rtnTeamList = teamRepositoryCustom.findTeam(null,"경기",null,null,pageRequest);
		assertThat(rtnTeamList.get(0)).isEqualTo(team2);
	}
	
}
