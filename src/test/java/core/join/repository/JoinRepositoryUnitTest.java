package core.join.repository;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import core.join.dto.JoinDto;
import core.join.entity.JoinEntity;
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import core.player.entity.BelongType;
import core.player.entity.PlayerEntity;
import core.player.repository.PlayerRepository;
import core.team.entity.TeamEntity;
import core.team.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class JoinRepositoryUnitTest {
	
	@Autowired
	private JoinRepository joinRepository;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	private JoinRepositoryCustom joinRepositoryCustom;
	
	@TestConfiguration
	static class TestConfig{
		@PersistenceContext
		private EntityManager entityManger;
		
		@Bean
		public JPAQueryFactory jpaQueryFactory() {
			return new JPAQueryFactory(entityManger);
		}
	}

	@AfterEach
	public void init() {
		joinRepository.deleteAll();
		playerRepository.deleteAll();
		teamRepository.deleteAll();
		entityManager.createNativeQuery("ALTER TABLE joining AUTO_INCREMENT = 1;").executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE player  AUTO_INCREMENT = 1;").executeUpdate();
		entityManager.createNativeQuery("ALTER TABLE team    AUTO_INCREMENT = 1;").executeUpdate();
	}
	
	@Test
	@DisplayName("Player to Team 가입신청 조회")
	void findPlayerJoinRequestTest() {
		// given
		TeamEntity rTeam = new TeamEntity("RedTeam","Seoul",BelongType.CLUB,"I'm a red team if you like red, will you join us?");
		TeamEntity yTeam = new TeamEntity("YelloTeam","Daejeon",BelongType.PROTEAM,"I'm a yellow team if you like yellow, will you join us?");
		TeamEntity gTeam = new TeamEntity("GreenTeam","Daegu",BelongType.UNIVERSITY,"I'm a green team if you like green, will you join us?");
		TeamEntity bTeam = new TeamEntity("BlueTeam","Seoul",BelongType.HIGH_SCHOOL,"I'm a blue team if you like blue, will you join us?");
		teamRepository.save(rTeam);
		teamRepository.save(yTeam);
		teamRepository.save(gTeam);
		teamRepository.save(bTeam);
		
		PlayerEntity player1 = new PlayerEntity("strawberry","220611-1111111",1,rTeam);
		PlayerEntity player2 = new PlayerEntity("apple","220611-2222222",2,rTeam);
		PlayerEntity player3 = new PlayerEntity("banana","220611-3333333",3,yTeam);
		PlayerEntity player4 = new PlayerEntity("lemon","220611-4444444",4,yTeam);
		PlayerEntity player5 = new PlayerEntity("lime","220611-5555555",5,gTeam);
		PlayerEntity player6 = new PlayerEntity("melon","220611-6666666",6,gTeam);
		PlayerEntity player7 = new PlayerEntity("blueberry","220611-7777777",7,bTeam);
		PlayerEntity player8 = new PlayerEntity("graph","220611-8888888",8,bTeam);
		playerRepository.save(player1);
		playerRepository.save(player2);
		playerRepository.save(player3);
		playerRepository.save(player4);
		playerRepository.save(player5);
		playerRepository.save(player6);
		playerRepository.save(player7);
		playerRepository.save(player8);
		
		JoinEntity joinEntity1 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player1,gTeam);
		JoinEntity joinEntity2 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player1,bTeam);
		JoinEntity joinEntity3 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player3,gTeam);
		JoinEntity joinEntity4 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player3,bTeam);
		JoinEntity joinEntity5 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player5,rTeam);
		JoinEntity joinEntity6 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player5,yTeam);
		JoinEntity joinEntity7 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player7,rTeam);
		JoinEntity joinEntity8 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player7,yTeam);
		joinRepository.save(joinEntity1);
		joinRepository.save(joinEntity2);
		joinRepository.save(joinEntity3);
		joinRepository.save(joinEntity4);
		joinRepository.save(joinEntity5);
		joinRepository.save(joinEntity6);
		joinRepository.save(joinEntity7);
		joinRepository.save(joinEntity8);
		PageRequest pageRequest = PageRequest.of(0, 2);
		
		List<JoinDto> expectPlayer1List = new ArrayList<>();
		expectPlayer1List.add(new JoinDto(player3.toDto(),rTeam.toDto(),RequesterType.PLAYER,StatusType.PROPOSAL));
		expectPlayer1List.add(new JoinDto(player4.toDto(),rTeam.toDto(),RequesterType.PLAYER,StatusType.PROPOSAL));
		
		// when
		Page<JoinDto> rtnList = joinRepositoryCustom.findPlayerJoinApplication(StatusType.PROPOSAL, 1L, pageRequest);

		System.out.println("Result : "+rtnList);
		
		// then
		for(JoinDto dto : rtnList) {
			System.out.println("Data : "+dto);
			System.out.println("createDate : "+dto.getCreatedDate());
			System.out.println("updateDate : "+dto.getUpdatedDate());
		}
		
		Assertions.assertThat(rtnList.getContent().get(0).getTeamId()).isEqualTo(3L);
		Assertions.assertThat(rtnList.getContent().get(1).getTeamId()).isEqualTo(4L);
		Assertions.assertThat(rtnList.getContent().get(0).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(rtnList.getContent().get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(rtnList.getContent().get(0).getActiveYN()).isEqualTo('Y');
		
	}
	
	@Test
	@DisplayName("Team to Player 가입신청 조회")
	void findTeamJoinRequestTest() {
		// given
		TeamEntity rTeam = new TeamEntity("RedTeam","Seoul",BelongType.CLUB,"I'm a red team if you like red, will you join us?");
		TeamEntity yTeam = new TeamEntity("YelloTeam","Daejeon",BelongType.PROTEAM,"I'm a yellow team if you like yellow, will you join us?");
		TeamEntity gTeam = new TeamEntity("GreenTeam","Daegu",BelongType.UNIVERSITY,"I'm a green team if you like green, will you join us?");
		TeamEntity bTeam = new TeamEntity("BlueTeam","Seoul",BelongType.HIGH_SCHOOL,"I'm a blue team if you like blue, will you join us?");
		teamRepository.save(rTeam);
		teamRepository.save(yTeam);
		teamRepository.save(gTeam);
		teamRepository.save(bTeam);
		
		PlayerEntity player1 = new PlayerEntity("strawberry","220611-1111111",1,rTeam);
		PlayerEntity player2 = new PlayerEntity("apple","220611-2222222",2,rTeam);
		PlayerEntity player3 = new PlayerEntity("banana","220611-3333333",3,yTeam);
		PlayerEntity player4 = new PlayerEntity("lemon","220611-4444444",4,yTeam);
		PlayerEntity player5 = new PlayerEntity("lime","220611-5555555",5,gTeam);
		PlayerEntity player6 = new PlayerEntity("melon","220611-6666666",6,gTeam);
		PlayerEntity player7 = new PlayerEntity("blueberry","220611-7777777",7,bTeam);
		PlayerEntity player8 = new PlayerEntity("graph","220611-8888888",8,bTeam);
		playerRepository.save(player1);
		playerRepository.save(player2);
		playerRepository.save(player3);
		playerRepository.save(player4);
		playerRepository.save(player5);
		playerRepository.save(player6);
		playerRepository.save(player7);
		playerRepository.save(player8);
		
		JoinEntity joinEntity1 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player1,gTeam);
		JoinEntity joinEntity2 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player1,bTeam);
		JoinEntity joinEntity3 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player3,gTeam);
		JoinEntity joinEntity4 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player3,bTeam);
		JoinEntity joinEntity5 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player5,rTeam);
		JoinEntity joinEntity6 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player5,yTeam);
		JoinEntity joinEntity7 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player7,rTeam);
		JoinEntity joinEntity8 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player7,yTeam);
		joinRepository.save(joinEntity1);
		joinRepository.save(joinEntity2);
		joinRepository.save(joinEntity3);
		joinRepository.save(joinEntity4);
		joinRepository.save(joinEntity5);
		joinRepository.save(joinEntity6);
		joinRepository.save(joinEntity7);
		joinRepository.save(joinEntity8);
		PageRequest pageRequest = PageRequest.of(0, 2);
		
		Page<JoinDto> rtnList = joinRepositoryCustom.findTeamJoinApplication(StatusType.PROPOSAL, 2L, pageRequest);
		
		for(JoinDto dto : rtnList) {
			System.out.println("dto = "+dto);
		}
		
		Assertions.assertThat(rtnList.getContent().get(0).getPlayerId()).isEqualTo(5L);
		Assertions.assertThat(rtnList.getContent().get(1).getPlayerId()).isEqualTo(7L);
		Assertions.assertThat(rtnList.getContent().get(0).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(rtnList.getContent().get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		
	}
	
	@Test
	@DisplayName("생성된 join 조회")
	void findById() {
		// given
		TeamEntity rTeam = new TeamEntity("RedTeam","Seoul",BelongType.CLUB,"I'm a red team if you like red, will you join us?");
		TeamEntity yTeam = new TeamEntity("YelloTeam","Daejeon",BelongType.PROTEAM,"I'm a yellow team if you like yellow, will you join us?");
		TeamEntity gTeam = new TeamEntity("GreenTeam","Daegu",BelongType.UNIVERSITY,"I'm a green team if you like green, will you join us?");
		TeamEntity bTeam = new TeamEntity("BlueTeam","Seoul",BelongType.HIGH_SCHOOL,"I'm a blue team if you like blue, will you join us?");
		teamRepository.save(rTeam);
		teamRepository.save(yTeam);
		teamRepository.save(gTeam);
		teamRepository.save(bTeam);
		
		PlayerEntity player1 = new PlayerEntity("strawberry","220611-1111111",1,rTeam);
		PlayerEntity player2 = new PlayerEntity("apple","220611-2222222",2,rTeam);
		PlayerEntity player3 = new PlayerEntity("banana","220611-3333333",3,yTeam);
		PlayerEntity player4 = new PlayerEntity("lemon","220611-4444444",4,yTeam);
		PlayerEntity player5 = new PlayerEntity("lime","220611-5555555",5,gTeam);
		PlayerEntity player6 = new PlayerEntity("melon","220611-6666666",6,gTeam);
		PlayerEntity player7 = new PlayerEntity("blueberry","220611-7777777",7,bTeam);
		PlayerEntity player8 = new PlayerEntity("graph","220611-8888888",8,bTeam);
		playerRepository.save(player1);
		playerRepository.save(player2);
		playerRepository.save(player3);
		playerRepository.save(player4);
		playerRepository.save(player5);
		playerRepository.save(player6);
		playerRepository.save(player7);
		playerRepository.save(player8);
		
		JoinEntity joinEntity1 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player1,gTeam);
		JoinEntity joinEntity2 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player1,bTeam);
		JoinEntity joinEntity3 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player3,gTeam);
		JoinEntity joinEntity4 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player3,bTeam);
		JoinEntity joinEntity5 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player5,rTeam);
		JoinEntity joinEntity6 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player5,yTeam);
		JoinEntity joinEntity7 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player7,rTeam);
		JoinEntity joinEntity8 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player7,yTeam);
		joinRepository.save(joinEntity1);
		joinRepository.save(joinEntity2);
		joinRepository.save(joinEntity3);
		joinRepository.save(joinEntity4);
		joinRepository.save(joinEntity5);
		joinRepository.save(joinEntity6);
		joinRepository.save(joinEntity7);
		joinRepository.save(joinEntity8);
		
		JoinEntity rtnEntity = joinRepository.findById(1L).orElse(null);
		System.out.println("=============================== findById ===============================");
		System.out.println(rtnEntity.toString());
		
	}
	
}
