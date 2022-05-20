package core.team.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
		teamRepository.saveAll(teamList);
		List<TeamEntity> rtnTeamList = teamRepositoryCustom.findTeam(null,null,BelongType.HIGH_SCHOOL,null);
		assertThat(rtnTeamList.get(0)).isEqualTo(team3);
	}
	
	@Test
	@DisplayName("지명에 해당하는 팀 찾기")
	public void findByLocation() {
		TeamEntity team1 = new TeamEntity("team1","Seoul",BelongType.CLUB,"Team1입니다.");
		TeamEntity team2 = new TeamEntity("team2","Seoul",BelongType.CLUB,"Team2입니다.");
		TeamEntity team3 = new TeamEntity("team3","Busan",BelongType.CLUB,"Team3입니다.");
		TeamEntity team4 = new TeamEntity("team4","Busan",BelongType.CLUB,"Team4입니다.");
		TeamEntity team5 = new TeamEntity("team5","Daejeon",BelongType.CLUB,"Team5입니다.");
		List<TeamEntity> teamList = new ArrayList<TeamEntity>();
		teamList.add(team1);
		teamList.add(team2);
		teamList.add(team3);
		teamList.add(team4);
		teamList.add(team5);
		teamRepository.saveAll(teamList);
		
		List<TeamEntity> Seoul = teamRepository.findByLocation("Seoul");
		List<TeamEntity> Busan = teamRepository.findByLocation("Busan");
		List<TeamEntity> Daejeon = teamRepository.findByLocation("Daejeon");
		
		System.out.println("서울지역 팀");
		Seoul.forEach(list->System.out.println(list));
		
		System.out.println("부산지역 팀");
		Busan.forEach(list->System.out.println(list));
		
		System.out.println("대전지역 팀");
		Daejeon.forEach(list->System.out.println(list));
	}
	
	@Test
	@DisplayName("소속구분으로 조회하기")
	public void findByBelongType() {
		TeamEntity team1 = new TeamEntity("team1","Seoul",BelongType.CLUB,"Team1입니다.");
		TeamEntity team2 = new TeamEntity("team2","Seoul",BelongType.UNIVERSITY,"Team2입니다.");
		TeamEntity team3 = new TeamEntity("team3","Busan",BelongType.UNIVERSITY,"Team3입니다.");
		TeamEntity team4 = new TeamEntity("team4","Busan",BelongType.PROTEAM,"Team4입니다.");
		TeamEntity team5 = new TeamEntity("team5","Daejeon",BelongType.MIDDLE_SCHOOL,"Team5입니다.");
		TeamEntity team6 = new TeamEntity("team6","Daejeon",BelongType.HIGH_SCHOOL,"Team6입니다.");
		TeamEntity team7 = new TeamEntity("team7","Daejeon",BelongType.CLUB,"Team7입니다.");
		TeamEntity team8 = new TeamEntity("team8","Daejeon",BelongType.PROTEAM,"Team8입니다.");
		TeamEntity team9 = new TeamEntity("team9","Daejeon",BelongType.CLUB,"Team9입니다.");
		TeamEntity team10 = new TeamEntity("team10","Daejeon",BelongType.ELEMENTARY_SCHOOL,"Team10입니다.");
		List<TeamEntity> teamList = new ArrayList<TeamEntity>();
		teamList.add(team1);
		teamList.add(team2);
		teamList.add(team3);
		teamList.add(team4);
		teamList.add(team5);
		teamList.add(team6);
		teamList.add(team7);
		teamList.add(team8);
		teamList.add(team9);
		teamList.add(team10);
		teamRepository.saveAll(teamList);
		
		System.out.println("프로팀 소속입니다.");
		List<TeamEntity> proTeam =  teamRepository.findByBelongType(BelongType.PROTEAM);
		proTeam.forEach(list->System.out.println(list));
		System.out.println("대학부 소속입니다.");
		List<TeamEntity> universityTeam =  teamRepository.findByBelongType(BelongType.UNIVERSITY);
		universityTeam.forEach(list->System.out.println(list));
		System.out.println("Club 소속입니다.");
		List<TeamEntity> clubTeam =  teamRepository.findByBelongType(BelongType.CLUB);
		clubTeam.forEach(list->System.out.println(list));
		System.out.println("고등부 소속입니다.");
		List<TeamEntity> highTeam =  teamRepository.findByBelongType(BelongType.HIGH_SCHOOL);
		highTeam.forEach(list->System.out.println(list));
		System.out.println("중등부 소속입니다.");
		List<TeamEntity> middleTeam =  teamRepository.findByBelongType(BelongType.MIDDLE_SCHOOL);
		middleTeam.forEach(list->System.out.println(list));
		System.out.println("초등부 소속입니다.");
		List<TeamEntity> elementTeam =  teamRepository.findByBelongType(BelongType.ELEMENTARY_SCHOOL);
		elementTeam.forEach(list->System.out.println(list));	
		
	}
	
	@Test
	@DisplayName("팀 설명 keyword로 찾기")
	public void findByIntroductionKeyword() {
		TeamEntity team1 = new TeamEntity("team1","Seoul",BelongType.CLUB,"Team1 최고입니다.");
		TeamEntity team2 = new TeamEntity("team2","Seoul",BelongType.UNIVERSITY,"Team2 짱입니다.");
		TeamEntity team3 = new TeamEntity("team3","Busan",BelongType.UNIVERSITY,"Team3 탑입니다.");
		TeamEntity team4 = new TeamEntity("team4","Busan",BelongType.PROTEAM,"Team4 최고입니다.");
		TeamEntity team5 = new TeamEntity("team5","Daejeon",BelongType.MIDDLE_SCHOOL,"Team5 탑입니다.");
		TeamEntity team6 = new TeamEntity("team6","Daejeon",BelongType.HIGH_SCHOOL,"Team6 winner입니다.");
		TeamEntity team7 = new TeamEntity("team7","Daejeon",BelongType.CLUB,"Team7 사랑입니다.");
		TeamEntity team8 = new TeamEntity("team8","Daejeon",BelongType.PROTEAM,"Team8 짱입니다.");
		TeamEntity team9 = new TeamEntity("team9","Daejeon",BelongType.CLUB,"Team9 최고입니다.");
		TeamEntity team10 = new TeamEntity("team10","Daejeon",BelongType.ELEMENTARY_SCHOOL,"Team10 사랑입니다.");
		List<TeamEntity> teamList = new ArrayList<TeamEntity>();
		teamList.add(team1);
		teamList.add(team2);
		teamList.add(team3);
		teamList.add(team4);
		teamList.add(team5);
		teamList.add(team6);
		teamList.add(team7);
		teamList.add(team8);
		teamList.add(team9);
		teamList.add(team10);
		teamRepository.saveAll(teamList);
		
		List<TeamEntity> list =  teamRepository.findByIntroductionLike("사랑");
		list.forEach(l -> System.out.println(l.toString()));
	}
}
