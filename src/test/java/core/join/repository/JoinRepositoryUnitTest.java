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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import core.join.dto.JoinDto;
import core.join.dto.JoinSearchCondition;
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
		
		JoinEntity joinEntity1 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player1,gTeam);
		JoinEntity joinEntity2 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player1,bTeam);
		JoinEntity joinEntity3 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player3,gTeam);
		JoinEntity joinEntity4 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player3,bTeam);
		JoinEntity joinEntity5 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player5,rTeam);
		JoinEntity joinEntity6 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player5,yTeam);
		JoinEntity joinEntity7 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player7,rTeam);
		JoinEntity joinEntity8 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,player7,yTeam);
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
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setStatusType(StatusType.PROPOSAL);
		
		// when
		Page<JoinDto> rtnList = joinRepositoryCustom.findPlayerJoinApplication(condition, 1L, pageRequest);

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
		
		JoinEntity joinEntity1 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player1,gTeam);
		JoinEntity joinEntity2 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player1,bTeam);
		JoinEntity joinEntity3 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player3,gTeam);
		JoinEntity joinEntity4 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player3,bTeam);
		JoinEntity joinEntity5 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player5,rTeam);
		JoinEntity joinEntity6 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player5,yTeam);
		JoinEntity joinEntity7 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player7,rTeam);
		JoinEntity joinEntity8 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player7,yTeam);
		joinRepository.save(joinEntity1);
		joinRepository.save(joinEntity2);
		joinRepository.save(joinEntity3);
		joinRepository.save(joinEntity4);
		joinRepository.save(joinEntity5);
		joinRepository.save(joinEntity6);
		joinRepository.save(joinEntity7);
		joinRepository.save(joinEntity8);
		PageRequest pageRequest = PageRequest.of(0, 2);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setStatusType(StatusType.PROPOSAL);
		
		Page<JoinDto> rtnList = joinRepositoryCustom.findTeamJoinApplication(condition, 2L, pageRequest);
		
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
		
		JoinEntity joinEntity1 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player1,gTeam);
		JoinEntity joinEntity2 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player1,bTeam);
		JoinEntity joinEntity3 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player3,gTeam);
		JoinEntity joinEntity4 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player3,bTeam);
		JoinEntity joinEntity5 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player5,rTeam);
		JoinEntity joinEntity6 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player5,yTeam);
		JoinEntity joinEntity7 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player7,rTeam);
		JoinEntity joinEntity8 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,player7,yTeam);
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
	
	@Test
	@DisplayName("Player가 요청한 join 조회")
	public void findPlayerJoinApplication() {
		TeamEntity rteam = new TeamEntity("RedTeam","Seoul",BelongType.CLUB,"I'm a red team if you like red, will you join us?");
		TeamEntity oteam = new TeamEntity("OrangedTeam","Seoul",BelongType.CLUB,"I'm a orange team if you like red, will you join us?");
		TeamEntity yteam = new TeamEntity("YellowTeam","Seoul",BelongType.CLUB,"I'm a yellow team if you like red, will you join us?");
		TeamEntity gteam = new TeamEntity("GreenTeam","Seoul",BelongType.CLUB,"I'm a green team if you like red, will you join us?");
		TeamEntity bteam = new TeamEntity("BlueTeam","Seoul",BelongType.CLUB,"I'm a blue team if you like red, will you join us?");
		TeamEntity iteam = new TeamEntity("IndigoTeam","Seoul",BelongType.CLUB,"I'm a indigo team if you like red, will you join us?");
		TeamEntity pteam = new TeamEntity("PuppleTeam","Seoul",BelongType.CLUB,"I'm a purple team if you like red, will you join us?");
		
		teamRepository.save(rteam);
		teamRepository.save(oteam);
		teamRepository.save(yteam);
		teamRepository.save(gteam);
		teamRepository.save(bteam);
		teamRepository.save(iteam);
		teamRepository.save(pteam);
		
		PlayerEntity apple  = new PlayerEntity("apple","221114-1111111",1,null);
		PlayerEntity banana = new PlayerEntity("banana","221114-1111111",1,null);
		PlayerEntity graph = new PlayerEntity("graph","221114-1111111",1,null);
		PlayerEntity mango = new PlayerEntity("mango","221114-1111111",1,null);
		PlayerEntity paprika = new PlayerEntity("paprika","221114-1111111",1,null);

		playerRepository.save(apple);
		playerRepository.save(banana);
		playerRepository.save(graph);
		playerRepository.save(mango);
		playerRepository.save(paprika);
		
		JoinEntity joinAg = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,apple,gteam);
		JoinEntity joinAr = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,apple,rteam);
		JoinEntity joinBg = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,banana,gteam);
		JoinEntity joinBy = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,banana,yteam);
		JoinEntity joinGg = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,graph,gteam);
		JoinEntity joinGp = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,graph,pteam);
		JoinEntity joinMg = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,mango,gteam);
		JoinEntity joinMy = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,mango,yteam);
		JoinEntity joinPg = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,paprika,gteam);
		JoinEntity joinPr = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,paprika,rteam);
		JoinEntity joinPy = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,paprika,yteam);

		joinRepository.save(joinAg);
		joinRepository.save(joinAr);
		joinRepository.save(joinBg);
		joinRepository.save(joinBy);
		joinRepository.save(joinGg);
		joinRepository.save(joinGp);
		joinRepository.save(joinMg);
		joinRepository.save(joinMy);
		joinRepository.save(joinPg);
		joinRepository.save(joinPr);
		joinRepository.save(joinPy);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.PLAYER);
		condition.setStatusType(StatusType.PROPOSAL);
		condition.setActiveYN('Y');
		
		PageRequest page = PageRequest.of(0, 10, Sort.by("updatedDate").descending().and(Sort.by("joinId")));
		
		Page<JoinDto> aResults = joinRepositoryCustom.findJoinApplication(condition, apple.getId(), null, page);
		List<JoinDto> list = aResults.getContent();
		
		Assertions.assertThat(list.get(0).getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(list.get(0).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(list.get(0).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(list.get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		
		Page<JoinDto> bResults = joinRepositoryCustom.findJoinApplication(condition, banana.getId(), null, page);
		List<JoinDto> bList = bResults.getContent();
		
		Assertions.assertThat(bList.get(0).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(bList.get(0).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(bList.get(0).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(bList.get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(bList.get(1).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(bList.get(1).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(bList.get(1).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(bList.get(1).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		
		Page<JoinDto> pResults = joinRepositoryCustom.findJoinApplication(condition, paprika.getId(), null, page);
		List<JoinDto> pList = pResults.getContent();
		
		Assertions.assertThat(pList.get(0).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(pList.get(0).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(pList.get(0).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(pList.get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(pList.get(1).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(pList.get(1).getTeamName()).isEqualTo("RedTeam");
		Assertions.assertThat(pList.get(1).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(pList.get(1).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(pList.get(2).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(pList.get(2).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(pList.get(2).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(pList.get(2).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		
	}
	
	@Test
	@DisplayName("Team이 요청한 join 조회")
	public void findTeamJoinApplication() {
		TeamEntity rteam = new TeamEntity("RedTeam","Seoul",BelongType.CLUB,"I'm a red team if you like red, will you join us?");
		TeamEntity oteam = new TeamEntity("OrangedTeam","Seoul",BelongType.CLUB,"I'm a orange team if you like red, will you join us?");
		TeamEntity yteam = new TeamEntity("YellowTeam","Seoul",BelongType.CLUB,"I'm a yellow team if you like red, will you join us?");
		TeamEntity gteam = new TeamEntity("GreenTeam","Seoul",BelongType.CLUB,"I'm a green team if you like red, will you join us?");
		TeamEntity bteam = new TeamEntity("BlueTeam","Seoul",BelongType.CLUB,"I'm a blue team if you like red, will you join us?");
		TeamEntity iteam = new TeamEntity("IndigoTeam","Seoul",BelongType.CLUB,"I'm a indigo team if you like red, will you join us?");
		TeamEntity pteam = new TeamEntity("PuppleTeam","Seoul",BelongType.CLUB,"I'm a purple team if you like red, will you join us?");
		
		teamRepository.save(rteam);
		teamRepository.save(oteam);
		teamRepository.save(yteam);
		teamRepository.save(gteam);
		teamRepository.save(bteam);
		teamRepository.save(iteam);
		teamRepository.save(pteam);
		
		PlayerEntity apple  = new PlayerEntity("apple","221114-1111111",1,null);
		PlayerEntity banana = new PlayerEntity("banana","221114-1111111",1,null);
		PlayerEntity graph = new PlayerEntity("graph","221114-1111111",1,null);
		PlayerEntity mango = new PlayerEntity("mango","221114-1111111",1,null);
		PlayerEntity paprika = new PlayerEntity("paprika","221114-1111111",1,null);

		playerRepository.save(apple);
		playerRepository.save(banana);
		playerRepository.save(graph);
		playerRepository.save(mango);
		playerRepository.save(paprika);
		
		// red
		JoinEntity joinRa = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,apple,rteam);
		JoinEntity joinRp = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,paprika,rteam);
		// yellow
		JoinEntity joinYb = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,banana,yteam);
		JoinEntity joinYp = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,paprika,yteam);
		JoinEntity joinYm = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,mango,yteam);
		// green
		JoinEntity joinGa = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,apple,gteam);
		JoinEntity joinGb = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,banana,gteam);
		JoinEntity joinGg = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,graph,gteam);
		JoinEntity joinGm = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,mango,gteam);
		JoinEntity joinGp = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,paprika,gteam);
		// blue
		// pupple
		JoinEntity joinPg = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,graph,pteam);
		
		joinRepository.save(joinRa);
		joinRepository.save(joinRp);
		joinRepository.save(joinYb);
		joinRepository.save(joinYp);
		joinRepository.save(joinYm);
		joinRepository.save(joinGa);
		joinRepository.save(joinGb);
		joinRepository.save(joinGg);
		joinRepository.save(joinGm);
		joinRepository.save(joinGp);
		joinRepository.save(joinPg);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.TEAM);
		condition.setStatusType(StatusType.PROPOSAL);
		condition.setActiveYN('Y');
		
		
		PageRequest page = PageRequest.of(0, 10, Sort.by("updatedDate").descending().and(Sort.by("id")));
		//PageRequest page = PageRequest.of(0, 10, Sort.by("player.playerName").descending().and(Sort.by("id")));
		
		Page<JoinDto> rResults = joinRepositoryCustom.findJoinApplication(condition, null, rteam.getId(), page);
		List<JoinDto> rList = rResults.getContent();
		
		Assertions.assertThat(rList.get(0).getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(rList.get(0).getTeamName()).isEqualTo("RedTeam");
		Assertions.assertThat(rList.get(0).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(rList.get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		
		Page<JoinDto> yResults = joinRepositoryCustom.findJoinApplication(condition, null, yteam.getId(), page);
		List<JoinDto> yList = yResults.getContent();
		
		Assertions.assertThat(yList.get(0).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(yList.get(0).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(yList.get(0).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(yList.get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(yList.get(1).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(yList.get(1).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(yList.get(1).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(yList.get(1).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(yList.get(2).getPlayerName()).isEqualTo("mango");
		Assertions.assertThat(yList.get(2).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(yList.get(2).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(yList.get(2).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		
		Page<JoinDto> gResults = joinRepositoryCustom.findJoinApplication(condition, null, gteam.getId(), page);
		List<JoinDto> gList = gResults.getContent();
		
		Assertions.assertThat(gList.get(0).getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(gList.get(0).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(gList.get(0).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(gList.get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(gList.get(1).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(gList.get(1).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(gList.get(1).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(gList.get(1).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(gList.get(2).getPlayerName()).isEqualTo("graph");
		Assertions.assertThat(gList.get(2).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(gList.get(2).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(gList.get(2).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(gList.get(3).getPlayerName()).isEqualTo("mango");
		Assertions.assertThat(gList.get(3).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(gList.get(3).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(gList.get(3).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(gList.get(4).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(gList.get(4).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(gList.get(4).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(gList.get(4).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		
	}
}
