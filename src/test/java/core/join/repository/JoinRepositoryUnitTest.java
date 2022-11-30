package core.join.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
		condition.setRequesterType(RequesterType.PLAYER);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// when
		
		Page<JoinDto> rtnResults = joinRepositoryCustom.findJoinApplication(condition, 1L,null, pageRequest);
		List<JoinDto> rtnList = rtnResults.getContent();
		
		System.out.println("Result : "+rtnList);
		
		// then
		for(JoinDto dto : rtnList) {
			System.out.println("Data : "+dto);
			System.out.println("createDate : "+dto.getCreatedDate());
			System.out.println("updateDate : "+dto.getUpdatedDate());
		}
		
		Assertions.assertThat(rtnList.get(0).getTeamId()).isEqualTo(3L);
		Assertions.assertThat(rtnList.get(1).getTeamId()).isEqualTo(4L);
		Assertions.assertThat(rtnList.get(0).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(rtnList.get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(rtnList.get(0).getActiveYN()).isEqualTo('Y');
		
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
		condition.setRequesterType(RequesterType.TEAM);
		condition.setStatusType(StatusType.PROPOSAL);
		
		Page<JoinDto> rtnList = joinRepositoryCustom.findJoinApplication(condition, null, 2L, pageRequest);
		
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
		TeamEntity oteam = new TeamEntity("OrangeTeam","Seoul",BelongType.CLUB,"I'm a orange team if you like red, will you join us?");
		TeamEntity yteam = new TeamEntity("YellowTeam","Seoul",BelongType.CLUB,"I'm a yellow team if you like red, will you join us?");
		TeamEntity gteam = new TeamEntity("GreenTeam","Seoul",BelongType.CLUB,"I'm a green team if you like red, will you join us?");
		TeamEntity bteam = new TeamEntity("BlueTeam","Seoul",BelongType.CLUB,"I'm a blue team if you like red, will you join us?");
		TeamEntity iteam = new TeamEntity("IndigoTeam","Seoul",BelongType.CLUB,"I'm a indigo team if you like red, will you join us?");
		TeamEntity pteam = new TeamEntity("PurpleTeam","Seoul",BelongType.CLUB,"I'm a purple team if you like red, will you join us?");
		
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
		
		//PageRequest page = PageRequest.of(0, 10, Sort.by("updatedDate").descending().and(Sort.by("joinId")));
		PageRequest page = PageRequest.of(0, 10, Sort.by("id"));
		
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
		TeamEntity oteam = new TeamEntity("OrangeTeam","Seoul",BelongType.CLUB,"I'm a orange team if you like red, will you join us?");
		TeamEntity yteam = new TeamEntity("YellowTeam","Seoul",BelongType.CLUB,"I'm a yellow team if you like red, will you join us?");
		TeamEntity gteam = new TeamEntity("GreenTeam","Seoul",BelongType.CLUB,"I'm a green team if you like red, will you join us?");
		TeamEntity bteam = new TeamEntity("BlueTeam","Seoul",BelongType.CLUB,"I'm a blue team if you like red, will you join us?");
		TeamEntity iteam = new TeamEntity("IndigoTeam","Seoul",BelongType.CLUB,"I'm a indigo team if you like red, will you join us?");
		TeamEntity pteam = new TeamEntity("PurpleTeam","Seoul",BelongType.CLUB,"I'm a purple team if you like red, will you join us?");
		
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
		// purple
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
	
	@Test
	@DisplayName("요청받은 join 조회")
	public void findJoinOffer() {
		// given
		// Team,Player,Join Entity 생성
		TeamEntity rteam = new TeamEntity("RedTeam","Seoul",BelongType.CLUB,"I'm a red team if you like red, will you join us?");
		TeamEntity oteam = new TeamEntity("OrangeTeam","Seoul",BelongType.CLUB,"I'm a orange team if you like red, will you join us?");
		TeamEntity yteam = new TeamEntity("YellowTeam","Seoul",BelongType.CLUB,"I'm a yellow team if you like red, will you join us?");
		TeamEntity gteam = new TeamEntity("GreenTeam","Seoul",BelongType.CLUB,"I'm a green team if you like red, will you join us?");
		TeamEntity bteam = new TeamEntity("BlueTeam","Seoul",BelongType.CLUB,"I'm a blue team if you like red, will you join us?");
		TeamEntity iteam = new TeamEntity("IndigoTeam","Seoul",BelongType.CLUB,"I'm a indigo team if you like red, will you join us?");
		TeamEntity pteam = new TeamEntity("PurpleTeam","Seoul",BelongType.CLUB,"I'm a purple team if you like red, will you join us?");
		
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
		
		// Player -> Team
		// apple
		JoinEntity joinAP = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,apple,gteam);
		JoinEntity joinAR = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,apple,rteam);
		// banana
		JoinEntity joinBG = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,banana,gteam);
		JoinEntity joinBB = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,banana,bteam);
		JoinEntity joinBY = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,banana,yteam);
		// graph
		JoinEntity joinGG = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,graph,gteam);
		JoinEntity joinGP = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,graph,pteam);
		// mango
		JoinEntity joinMG = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,mango,gteam);
		JoinEntity joinMY = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,mango,yteam);
		// paprika
		JoinEntity joinPG = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,paprika,gteam);
		JoinEntity joinPT = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,paprika,rteam);
		JoinEntity joinPY = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,paprika,yteam);
		
		// Team -> Player
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
		JoinEntity joinBb = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,banana,bteam);
		// purple
		JoinEntity joinPg = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,graph,pteam);
		
		joinRepository.save(joinAP);
		joinRepository.save(joinAR);
		joinRepository.save(joinBG);
		joinRepository.save(joinBB);
		joinRepository.save(joinBY);
		joinRepository.save(joinGG);
		joinRepository.save(joinGP);
		joinRepository.save(joinMG);
		joinRepository.save(joinMY);
		joinRepository.save(joinPG);
		joinRepository.save(joinPT);
		joinRepository.save(joinPY);
		
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
		joinRepository.save(joinBb);
		joinRepository.save(joinPg);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.TEAM);
		condition.setStatusType(StatusType.PROPOSAL);
		condition.setActiveYN('Y');
		
		
		PageRequest page = PageRequest.of(0, 10, Sort.by("updatedDate").descending().and(Sort.by("id")));
		//PageRequest page = PageRequest.of(0, 10, Sort.by("player.playerName").descending().and(Sort.by("id")));
		
		// when
		// apple
		Page<JoinDto> aResults = joinRepositoryCustom.findJoinOffer(condition, apple.getId(), null, page);
		List<JoinDto> aList = aResults.getContent();
		// banana
		Page<JoinDto> bResults = joinRepositoryCustom.findJoinOffer(condition, banana.getId(), null, page);
		List<JoinDto> bList = bResults.getContent();
		// graph
		Page<JoinDto> gResults = joinRepositoryCustom.findJoinOffer(condition, graph.getId(), null, page);
		List<JoinDto> gList = gResults.getContent();
		// mango
		Page<JoinDto> mResults = joinRepositoryCustom.findJoinOffer(condition, mango.getId(), null, page);
		List<JoinDto> mList = mResults.getContent();
		// paprika
		Page<JoinDto> pResults = joinRepositoryCustom.findJoinOffer(condition, paprika.getId(), null, page);
		List<JoinDto> pList = pResults.getContent();
				
		// then
		Assertions.assertThat(aList.get(0).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(aList.get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(aList.get(0).getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(aList.get(0).getTeamName()).isEqualTo("RedTeam");
		Assertions.assertThat(aList.get(1).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(aList.get(1).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(aList.get(1).getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(aList.get(1).getTeamName()).isEqualTo("GreenTeam");
		
		// banana
		Assertions.assertThat(bList.get(0).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(bList.get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(bList.get(0).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(bList.get(0).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(bList.get(1).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(bList.get(1).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(bList.get(1).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(bList.get(1).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(bList.get(2).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(bList.get(2).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(bList.get(2).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(bList.get(2).getTeamName()).isEqualTo("BlueTeam");
			
	}
	
	@Test
	@DisplayName("요청받은 join 조회")
	public void findJoinOffers() {
		// given
		// Team,Player,Join Entity 생성
		TeamEntity rteam = new TeamEntity("RedTeam","Seoul",BelongType.CLUB,"I'm a red team if you like red, will you join us?");
		TeamEntity oteam = new TeamEntity("OrangeTeam","Seoul",BelongType.CLUB,"I'm a orange team if you like red, will you join us?");
		TeamEntity yteam = new TeamEntity("YellowTeam","Seoul",BelongType.CLUB,"I'm a yellow team if you like red, will you join us?");
		TeamEntity gteam = new TeamEntity("GreenTeam","Seoul",BelongType.CLUB,"I'm a green team if you like red, will you join us?");
		TeamEntity bteam = new TeamEntity("BlueTeam","Seoul",BelongType.CLUB,"I'm a blue team if you like red, will you join us?");
		TeamEntity iteam = new TeamEntity("IndigoTeam","Seoul",BelongType.CLUB,"I'm a indigo team if you like red, will you join us?");
		TeamEntity pteam = new TeamEntity("PurpleTeam","Seoul",BelongType.CLUB,"I'm a purple team if you like red, will you join us?");
		
		teamRepository.save(rteam);
		teamRepository.save(oteam);
		teamRepository.save(yteam);
		teamRepository.save(gteam);
		teamRepository.save(bteam);
		teamRepository.save(iteam);
		teamRepository.save(pteam);
		
		PlayerEntity apple  	= new PlayerEntity("apple","221114-1111111",1,null);
		PlayerEntity banana		= new PlayerEntity("banana","221114-1111111",1,null);
		PlayerEntity graph 		= new PlayerEntity("graph","221114-1111111",1,null);
		PlayerEntity kiwi  		= new PlayerEntity("kiwi","221114-1111111",1,null);
		PlayerEntity mango 		= new PlayerEntity("mango","221114-1111111",1,null);
		PlayerEntity paprika 	= new PlayerEntity("paprika","221114-1111111",1,null);
		PlayerEntity tomato		= new PlayerEntity("tomato","221114-1111111",1,null);
		
		playerRepository.save(apple);
		playerRepository.save(banana);
		playerRepository.save(graph);
		playerRepository.save(kiwi);
		playerRepository.save(mango);
		playerRepository.save(paprika);
		playerRepository.save(tomato);
		
		//Player, Proposal(홀수)
		JoinEntity PP1 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,apple,rteam);
		JoinEntity PP2 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,graph,rteam);
		JoinEntity PP3 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,mango,rteam);
		JoinEntity PP4 = new JoinEntity(RequesterType.PLAYER,StatusType.PROPOSAL,tomato,rteam);
		
		//Player, Reject(짝수)
		JoinEntity PRJ1 = new JoinEntity(RequesterType.PLAYER,StatusType.REJECT,banana,oteam);
		JoinEntity PRJ2 = new JoinEntity(RequesterType.PLAYER,StatusType.REJECT,kiwi,oteam);
		JoinEntity PRJ3 = new JoinEntity(RequesterType.PLAYER,StatusType.REJECT,paprika,oteam);
		
		//Player, Approval(홀수)
		JoinEntity PA1 = new JoinEntity(RequesterType.PLAYER,StatusType.APPROVAL,apple,yteam);
		JoinEntity PA2 = new JoinEntity(RequesterType.PLAYER,StatusType.APPROVAL,graph,yteam);
		JoinEntity PA3 = new JoinEntity(RequesterType.PLAYER,StatusType.APPROVAL,mango,yteam);
		JoinEntity PA4 = new JoinEntity(RequesterType.PLAYER,StatusType.APPROVAL,tomato,yteam);

		//Player, Withdraw(짝수)
		JoinEntity PW1 = new JoinEntity(RequesterType.PLAYER,StatusType.WITHDRAW,banana,gteam);
		JoinEntity PW2 = new JoinEntity(RequesterType.PLAYER,StatusType.WITHDRAW,kiwi,gteam);
		JoinEntity PW3 = new JoinEntity(RequesterType.PLAYER,StatusType.WITHDRAW,paprika,gteam);
		
		//Player, Return(홀수)
		JoinEntity PRT1 = new JoinEntity(RequesterType.PLAYER,StatusType.RETURN,apple,bteam);
		JoinEntity PRT2 = new JoinEntity(RequesterType.PLAYER,StatusType.RETURN,graph,bteam);
		JoinEntity PRT3 = new JoinEntity(RequesterType.PLAYER,StatusType.RETURN,mango,bteam);
		JoinEntity PRT4 = new JoinEntity(RequesterType.PLAYER,StatusType.RETURN,tomato,bteam);
		
		//Player, Confirmation(짝수)
		JoinEntity PC1 = new JoinEntity(RequesterType.PLAYER,StatusType.CONFIRMATION,banana,pteam);
		JoinEntity PC2 = new JoinEntity(RequesterType.PLAYER,StatusType.CONFIRMATION,kiwi,pteam);
		JoinEntity PC3 = new JoinEntity(RequesterType.PLAYER,StatusType.CONFIRMATION,paprika,pteam);
		
		// Team, Proposal(홀수)
		JoinEntity TP1 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,apple,rteam);
		JoinEntity TP2 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,apple,yteam);
		JoinEntity TP3 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,apple,bteam);
		JoinEntity TP4 = new JoinEntity(RequesterType.TEAM,StatusType.PROPOSAL,apple,pteam);
		
		// Team, Reject(짝수)
		JoinEntity TRJ1 = new JoinEntity(RequesterType.TEAM,StatusType.REJECT,banana,oteam);
		JoinEntity TRJ2 = new JoinEntity(RequesterType.TEAM,StatusType.REJECT,banana,gteam);
		JoinEntity TRJ3 = new JoinEntity(RequesterType.TEAM,StatusType.REJECT,banana,iteam);
		
		// Team, Approval(홀수)
		JoinEntity TA1 = new JoinEntity(RequesterType.TEAM,StatusType.APPROVAL,graph,rteam);
		JoinEntity TA2 = new JoinEntity(RequesterType.TEAM,StatusType.APPROVAL,graph,yteam);
		JoinEntity TA3 = new JoinEntity(RequesterType.TEAM,StatusType.APPROVAL,graph,bteam);
		JoinEntity TA4 = new JoinEntity(RequesterType.TEAM,StatusType.APPROVAL,graph,pteam);

		// Team, Withdraw(짝수)
		JoinEntity TW1 = new JoinEntity(RequesterType.TEAM,StatusType.WITHDRAW,kiwi,oteam);
		JoinEntity TW2 = new JoinEntity(RequesterType.TEAM,StatusType.WITHDRAW,kiwi,gteam);
		JoinEntity TW3 = new JoinEntity(RequesterType.TEAM,StatusType.WITHDRAW,kiwi,iteam);

		// Team, Return(홀수)
		JoinEntity TRT1 = new JoinEntity(RequesterType.TEAM,StatusType.RETURN,paprika,rteam);
		JoinEntity TRT2 = new JoinEntity(RequesterType.TEAM,StatusType.RETURN,paprika,yteam);
		JoinEntity TRT3 = new JoinEntity(RequesterType.TEAM,StatusType.RETURN,paprika,bteam);
		JoinEntity TRT4 = new JoinEntity(RequesterType.TEAM,StatusType.RETURN,paprika,pteam);
		
		// Team, Confirmation(짝수)
		JoinEntity TC1 = new JoinEntity(RequesterType.TEAM,StatusType.CONFIRMATION,tomato,oteam);
		JoinEntity TC2 = new JoinEntity(RequesterType.TEAM,StatusType.CONFIRMATION,tomato,gteam);
		JoinEntity TC3 = new JoinEntity(RequesterType.TEAM,StatusType.CONFIRMATION,tomato,iteam);
		
		joinRepository.save(PP1);
		joinRepository.save(PP2);
		joinRepository.save(PP3);
		joinRepository.save(PP4);
		joinRepository.save(PRJ1);
		joinRepository.save(PRJ2);
		joinRepository.save(PRJ3);
		joinRepository.save(PA1);
		joinRepository.save(PA2);
		joinRepository.save(PA3);
		joinRepository.save(PA4);
		joinRepository.save(PW1);
		joinRepository.save(PW2);
		joinRepository.save(PW3);
		joinRepository.save(PRT1);
		joinRepository.save(PRT2);
		joinRepository.save(PRT3);
		joinRepository.save(PRT4);
		joinRepository.save(PC1);
		joinRepository.save(PC2);
		joinRepository.save(PC3);
		
		joinRepository.save(TP1);
		joinRepository.save(TP2);
		joinRepository.save(TP3);
		joinRepository.save(TP4);
		joinRepository.save(TRJ1);
		joinRepository.save(TRJ2);
		joinRepository.save(TRJ3);
		joinRepository.save(TA1);
		joinRepository.save(TA2);
		joinRepository.save(TA3);
		joinRepository.save(TA4);
		joinRepository.save(TW1);
		joinRepository.save(TW2);
		joinRepository.save(TW3);
		joinRepository.save(TRT1);
		joinRepository.save(TRT2);
		joinRepository.save(TRT3);
		joinRepository.save(TRT4);
		joinRepository.save(TC1);
		joinRepository.save(TC2);
		joinRepository.save(TC3);
		
		JoinSearchCondition  playerProposalCondition = new JoinSearchCondition();
		playerProposalCondition.setRequesterType(RequesterType.PLAYER);
		playerProposalCondition.setStatusType(StatusType.PROPOSAL);
		playerProposalCondition.setActiveYN('Y');
		JoinSearchCondition  playerRejectCondition = new JoinSearchCondition();
		playerRejectCondition.setRequesterType(RequesterType.PLAYER);
		playerRejectCondition.setStatusType(StatusType.REJECT);
		playerRejectCondition.setActiveYN('Y');
		JoinSearchCondition  playerApprovalCondition = new JoinSearchCondition();
		playerApprovalCondition.setRequesterType(RequesterType.PLAYER);
		playerApprovalCondition.setStatusType(StatusType.APPROVAL);
		playerApprovalCondition.setActiveYN('Y');
		JoinSearchCondition  playerWithdrawCondition = new JoinSearchCondition();
		playerWithdrawCondition.setRequesterType(RequesterType.PLAYER);
		playerWithdrawCondition.setStatusType(StatusType.WITHDRAW);
		playerWithdrawCondition.setActiveYN('Y');
		JoinSearchCondition  playerReturnCondition = new JoinSearchCondition();
		playerReturnCondition.setRequesterType(RequesterType.PLAYER);
		playerReturnCondition.setStatusType(StatusType.RETURN);
		playerReturnCondition.setActiveYN('Y');
		JoinSearchCondition  playerConfirmationCondition = new JoinSearchCondition();
		playerConfirmationCondition.setRequesterType(RequesterType.PLAYER);
		playerConfirmationCondition.setStatusType(StatusType.CONFIRMATION);
		playerConfirmationCondition.setActiveYN('Y');

		JoinSearchCondition  teamProposalCondition = new JoinSearchCondition();
		teamProposalCondition.setRequesterType(RequesterType.TEAM);
		teamProposalCondition.setStatusType(StatusType.PROPOSAL);
		teamProposalCondition.setActiveYN('Y');
		JoinSearchCondition  teamRejectCondition = new JoinSearchCondition();
		teamRejectCondition.setRequesterType(RequesterType.TEAM);
		teamRejectCondition.setStatusType(StatusType.REJECT);
		teamRejectCondition.setActiveYN('Y');
		JoinSearchCondition  teamApprovalCondition = new JoinSearchCondition();
		teamApprovalCondition.setRequesterType(RequesterType.TEAM);
		teamApprovalCondition.setStatusType(StatusType.APPROVAL);
		teamApprovalCondition.setActiveYN('Y');
		JoinSearchCondition  teamWithdrawCondition = new JoinSearchCondition();
		teamWithdrawCondition.setRequesterType(RequesterType.TEAM);
		teamWithdrawCondition.setStatusType(StatusType.WITHDRAW);
		teamWithdrawCondition.setActiveYN('Y');
		JoinSearchCondition  teamReturnCondition = new JoinSearchCondition();
		teamReturnCondition.setRequesterType(RequesterType.TEAM);
		teamReturnCondition.setStatusType(StatusType.RETURN);
		teamReturnCondition.setActiveYN('Y');
		JoinSearchCondition  teamConfirmationCondition = new JoinSearchCondition();
		teamConfirmationCondition.setRequesterType(RequesterType.TEAM);
		teamConfirmationCondition.setStatusType(StatusType.CONFIRMATION);
		teamConfirmationCondition.setActiveYN('Y');
		
		//PageRequest sortUpdate = PageRequest.of(0, 10, Sort.by("updatedDate").descending().and(Sort.by("id")));
		PageRequest sortUpdate = PageRequest.of(0, 10, Sort.by("id"));
		PageRequest sortPlayer = PageRequest.of(0, 10, Sort.by("player.playerName").descending().and(Sort.by("id")));
		PageRequest sortTeam = PageRequest.of(0, 10, Sort.by("team.teamName").descending().and(Sort.by("id")));
		
		// then
		// 검색 parameter에 따라서 결과가 잘 나오는지
		// RequesterType에 따라 결과가 잘 나오는지			// StatusType에 따라 결과가 잘 나오는지
		Page<JoinDto> rResults = joinRepositoryCustom.findJoinOffer(playerProposalCondition, null, rteam.getId(), sortUpdate);
		List<JoinDto> rList = rResults.getContent();
		Page<JoinDto> iResults = joinRepositoryCustom.findJoinOffer(playerProposalCondition, null, iteam.getId(), sortUpdate);
		List<JoinDto> iList = iResults.getContent();
		Assertions.assertThat(rList.get(0).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(rList.get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(rList.get(0).getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(rList.get(0).getTeamName()).isEqualTo("RedTeam");
		Assertions.assertThat(rList.get(1).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(rList.get(1).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(rList.get(1).getPlayerName()).isEqualTo("graph");
		Assertions.assertThat(rList.get(1).getTeamName()).isEqualTo("RedTeam");
		Assertions.assertThat(rList.get(2).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(rList.get(2).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(rList.get(2).getPlayerName()).isEqualTo("mango");
		Assertions.assertThat(rList.get(2).getTeamName()).isEqualTo("RedTeam");
		Assertions.assertThat(rList.get(3).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(rList.get(3).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(rList.get(3).getPlayerName()).isEqualTo("tomato");
		Assertions.assertThat(rList.get(3).getTeamName()).isEqualTo("RedTeam");
		Assertions.assertThatThrownBy(() -> iList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
		
		Page<JoinDto> orResults = joinRepositoryCustom.findJoinOffer(playerRejectCondition, null, oteam.getId(), sortUpdate);
		List<JoinDto> orList = orResults.getContent();
		Page<JoinDto> irResults = joinRepositoryCustom.findJoinOffer(playerRejectCondition, null, iteam.getId(), sortUpdate);
		List<JoinDto> irList = irResults.getContent();
		Assertions.assertThat(orList.get(0).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(orList.get(0).getStatusType()).isEqualTo(StatusType.REJECT);
		Assertions.assertThat(orList.get(0).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(orList.get(0).getTeamName()).isEqualTo("OrangeTeam");
		Assertions.assertThat(orList.get(1).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(orList.get(1).getStatusType()).isEqualTo(StatusType.REJECT);
		Assertions.assertThat(orList.get(1).getPlayerName()).isEqualTo("kiwi");
		Assertions.assertThat(orList.get(1).getTeamName()).isEqualTo("OrangeTeam");
		Assertions.assertThat(orList.get(2).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(orList.get(2).getStatusType()).isEqualTo(StatusType.REJECT);
		Assertions.assertThat(orList.get(2).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(orList.get(2).getTeamName()).isEqualTo("OrangeTeam");
		Assertions.assertThatThrownBy(() -> irList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
		
		Page<JoinDto> yaResults = joinRepositoryCustom.findJoinOffer(playerApprovalCondition, null, yteam.getId(), sortUpdate);
		List<JoinDto> yaList = yaResults.getContent();
		Page<JoinDto> iaResults = joinRepositoryCustom.findJoinOffer(playerApprovalCondition, null, iteam.getId(), sortUpdate);
		List<JoinDto> iaList = iaResults.getContent();
		Assertions.assertThat(yaList.get(0).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(yaList.get(0).getStatusType()).isEqualTo(StatusType.APPROVAL);
		Assertions.assertThat(yaList.get(0).getPlayerName()).isEqualTo("apple"); //
		Assertions.assertThat(yaList.get(0).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(yaList.get(1).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(yaList.get(1).getStatusType()).isEqualTo(StatusType.APPROVAL);
		Assertions.assertThat(yaList.get(1).getPlayerName()).isEqualTo("graph");
		Assertions.assertThat(yaList.get(1).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(yaList.get(2).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(yaList.get(2).getStatusType()).isEqualTo(StatusType.APPROVAL);
		Assertions.assertThat(yaList.get(2).getPlayerName()).isEqualTo("mango");
		Assertions.assertThat(yaList.get(2).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(yaList.get(3).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(yaList.get(3).getStatusType()).isEqualTo(StatusType.APPROVAL);
		Assertions.assertThat(yaList.get(3).getPlayerName()).isEqualTo("tomato");
		Assertions.assertThat(yaList.get(3).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThatThrownBy(() -> iaList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
		
		Page<JoinDto> gwResults = joinRepositoryCustom.findJoinOffer(playerWithdrawCondition, null, gteam.getId(), sortUpdate);
		List<JoinDto> gwList = gwResults.getContent();
		Page<JoinDto> iwResults = joinRepositoryCustom.findJoinOffer(playerWithdrawCondition, null, iteam.getId(), sortUpdate);
		List<JoinDto> iwList = iwResults.getContent();
		Assertions.assertThat(gwList.get(0).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(gwList.get(0).getStatusType()).isEqualTo(StatusType.WITHDRAW);
		Assertions.assertThat(gwList.get(0).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(gwList.get(0).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(gwList.get(1).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(gwList.get(1).getStatusType()).isEqualTo(StatusType.WITHDRAW);
		Assertions.assertThat(gwList.get(1).getPlayerName()).isEqualTo("kiwi");
		Assertions.assertThat(gwList.get(1).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(gwList.get(2).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(gwList.get(2).getStatusType()).isEqualTo(StatusType.WITHDRAW);
		Assertions.assertThat(gwList.get(2).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(gwList.get(2).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThatThrownBy(() -> iwList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
		
		Page<JoinDto> brResults = joinRepositoryCustom.findJoinOffer(playerReturnCondition, null, bteam.getId(), sortUpdate);
		List<JoinDto> brList = brResults.getContent();
		Page<JoinDto> iRResults = joinRepositoryCustom.findJoinOffer(playerReturnCondition, null, iteam.getId(), sortUpdate);
		List<JoinDto> iRList = iRResults.getContent();
		Assertions.assertThat(brList.get(0).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(brList.get(0).getStatusType()).isEqualTo(StatusType.RETURN);
		Assertions.assertThat(brList.get(0).getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(brList.get(0).getTeamName()).isEqualTo("BlueTeam");
		Assertions.assertThat(brList.get(1).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(brList.get(1).getStatusType()).isEqualTo(StatusType.RETURN);
		Assertions.assertThat(brList.get(1).getPlayerName()).isEqualTo("graph");
		Assertions.assertThat(brList.get(1).getTeamName()).isEqualTo("BlueTeam");
		Assertions.assertThat(brList.get(2).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(brList.get(2).getStatusType()).isEqualTo(StatusType.RETURN);
		Assertions.assertThat(brList.get(2).getPlayerName()).isEqualTo("mango");
		Assertions.assertThat(brList.get(2).getTeamName()).isEqualTo("BlueTeam");
		Assertions.assertThat(brList.get(3).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(brList.get(3).getStatusType()).isEqualTo(StatusType.RETURN);
		Assertions.assertThat(brList.get(3).getPlayerName()).isEqualTo("tomato");
		Assertions.assertThat(brList.get(3).getTeamName()).isEqualTo("BlueTeam");
		Assertions.assertThatThrownBy(() -> iRList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
		
		Page<JoinDto> pcResults = joinRepositoryCustom.findJoinOffer(playerConfirmationCondition, null, pteam.getId(), sortUpdate);
		List<JoinDto> pcList = pcResults.getContent();
		Page<JoinDto> icResults = joinRepositoryCustom.findJoinOffer(playerConfirmationCondition, null, iteam.getId(), sortUpdate);
		List<JoinDto> icList = icResults.getContent();
		Assertions.assertThat(pcList.get(0).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(pcList.get(0).getStatusType()).isEqualTo(StatusType.CONFIRMATION);
		Assertions.assertThat(pcList.get(0).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(pcList.get(0).getTeamName()).isEqualTo("PurpleTeam");
		Assertions.assertThat(pcList.get(1).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(pcList.get(1).getStatusType()).isEqualTo(StatusType.CONFIRMATION);
		Assertions.assertThat(pcList.get(1).getPlayerName()).isEqualTo("kiwi");
		Assertions.assertThat(pcList.get(1).getTeamName()).isEqualTo("PurpleTeam");
		Assertions.assertThat(pcList.get(2).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(pcList.get(2).getStatusType()).isEqualTo(StatusType.CONFIRMATION);
		Assertions.assertThat(pcList.get(2).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(pcList.get(2).getTeamName()).isEqualTo("PurpleTeam");
		Assertions.assertThatThrownBy(() -> icList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
		
		//
		Page<JoinDto> APResults = joinRepositoryCustom.findJoinOffer(teamProposalCondition, apple.getId(), null, sortUpdate);
		List<JoinDto> APList = APResults.getContent();
		Page<JoinDto> MPResults = joinRepositoryCustom.findJoinOffer(teamProposalCondition, mango.getId(), null, sortUpdate);
		List<JoinDto> MPList = MPResults.getContent();
		Assertions.assertThat(APList.get(0).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(APList.get(0).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(APList.get(0).getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(APList.get(0).getTeamName()).isEqualTo("RedTeam");
		Assertions.assertThat(APList.get(1).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(APList.get(1).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(APList.get(1).getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(APList.get(1).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(APList.get(2).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(APList.get(2).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(APList.get(2).getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(APList.get(2).getTeamName()).isEqualTo("BlueTeam");
		Assertions.assertThat(APList.get(3).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(APList.get(3).getStatusType()).isEqualTo(StatusType.PROPOSAL);
		Assertions.assertThat(APList.get(3).getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(APList.get(3).getTeamName()).isEqualTo("PurpleTeam");
		Assertions.assertThatThrownBy(() -> MPList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
		
		Page<JoinDto> BRResults = joinRepositoryCustom.findJoinOffer(teamRejectCondition, banana.getId(), null, sortUpdate);
		List<JoinDto> BRList = BRResults.getContent();
		Page<JoinDto> MRResults = joinRepositoryCustom.findJoinOffer(teamRejectCondition, mango.getId(), null, sortUpdate);
		List<JoinDto> MRList = MRResults.getContent();
		Assertions.assertThat(BRList.get(0).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(BRList.get(0).getStatusType()).isEqualTo(StatusType.REJECT);
		Assertions.assertThat(BRList.get(0).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(BRList.get(0).getTeamName()).isEqualTo("OrangeTeam");
		Assertions.assertThat(BRList.get(1).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(BRList.get(1).getStatusType()).isEqualTo(StatusType.REJECT);
		Assertions.assertThat(BRList.get(1).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(BRList.get(1).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(BRList.get(2).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(BRList.get(2).getStatusType()).isEqualTo(StatusType.REJECT);
		Assertions.assertThat(BRList.get(2).getPlayerName()).isEqualTo("banana");
		Assertions.assertThat(BRList.get(2).getTeamName()).isEqualTo("IndigoTeam");
		Assertions.assertThatThrownBy(() -> MRList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
		
		Page<JoinDto> GAResults = joinRepositoryCustom.findJoinOffer(playerApprovalCondition, null, yteam.getId(), sortUpdate);
		List<JoinDto> GAList = GAResults.getContent();
		Page<JoinDto> MAResults = joinRepositoryCustom.findJoinOffer(playerApprovalCondition, null, iteam.getId(), sortUpdate);
		List<JoinDto> MAList = MAResults.getContent();
		Assertions.assertThat(GAList.get(0).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(GAList.get(0).getStatusType()).isEqualTo(StatusType.APPROVAL);
		Assertions.assertThat(GAList.get(0).getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(GAList.get(0).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(GAList.get(1).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(GAList.get(1).getStatusType()).isEqualTo(StatusType.APPROVAL);
		Assertions.assertThat(GAList.get(1).getPlayerName()).isEqualTo("graph");
		Assertions.assertThat(GAList.get(1).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(GAList.get(2).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(GAList.get(2).getStatusType()).isEqualTo(StatusType.APPROVAL);
		Assertions.assertThat(GAList.get(2).getPlayerName()).isEqualTo("mango");
		Assertions.assertThat(GAList.get(2).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(GAList.get(3).getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(GAList.get(3).getStatusType()).isEqualTo(StatusType.APPROVAL);
		Assertions.assertThat(GAList.get(3).getPlayerName()).isEqualTo("tomato");
		Assertions.assertThat(GAList.get(3).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThatThrownBy(() -> MAList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
		
		Page<JoinDto> KWResults = joinRepositoryCustom.findJoinOffer(teamWithdrawCondition, kiwi.getId(), null, sortUpdate);
		List<JoinDto> KWList = KWResults.getContent();
		Page<JoinDto> MWResults = joinRepositoryCustom.findJoinOffer(teamWithdrawCondition, mango.getId(), null, sortUpdate);
		List<JoinDto> MWList = MWResults.getContent();
		Assertions.assertThat(KWList.get(0).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(KWList.get(0).getStatusType()).isEqualTo(StatusType.WITHDRAW);
		Assertions.assertThat(KWList.get(0).getPlayerName()).isEqualTo("kiwi");
		Assertions.assertThat(KWList.get(0).getTeamName()).isEqualTo("OrangeTeam");
		Assertions.assertThat(KWList.get(1).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(KWList.get(1).getStatusType()).isEqualTo(StatusType.WITHDRAW);
		Assertions.assertThat(KWList.get(1).getPlayerName()).isEqualTo("kiwi");
		Assertions.assertThat(KWList.get(1).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(KWList.get(2).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(KWList.get(2).getStatusType()).isEqualTo(StatusType.WITHDRAW);
		Assertions.assertThat(KWList.get(2).getPlayerName()).isEqualTo("kiwi");
		Assertions.assertThat(KWList.get(2).getTeamName()).isEqualTo("IndigoTeam");
		Assertions.assertThatThrownBy(() -> MWList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
		
		Page<JoinDto> PRResults = joinRepositoryCustom.findJoinOffer(teamReturnCondition, paprika.getId(), null, sortUpdate);
		List<JoinDto> PRList = PRResults.getContent();
		Page<JoinDto> MReResults = joinRepositoryCustom.findJoinOffer(teamReturnCondition, mango.getId(), null, sortUpdate);
		List<JoinDto> MReList = MReResults.getContent();
		Assertions.assertThat(PRList.get(0).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(PRList.get(0).getStatusType()).isEqualTo(StatusType.RETURN);
		Assertions.assertThat(PRList.get(0).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(PRList.get(0).getTeamName()).isEqualTo("RedTeam");
		Assertions.assertThat(PRList.get(1).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(PRList.get(1).getStatusType()).isEqualTo(StatusType.RETURN);
		Assertions.assertThat(PRList.get(1).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(PRList.get(1).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(PRList.get(2).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(PRList.get(2).getStatusType()).isEqualTo(StatusType.RETURN);
		Assertions.assertThat(PRList.get(2).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(PRList.get(2).getTeamName()).isEqualTo("BlueTeam");
		Assertions.assertThat(PRList.get(3).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(PRList.get(3).getStatusType()).isEqualTo(StatusType.RETURN);
		Assertions.assertThat(PRList.get(3).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(PRList.get(3).getTeamName()).isEqualTo("PurpleTeam");
		Assertions.assertThatThrownBy(() -> MReList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
		
		Page<JoinDto> TCResults = joinRepositoryCustom.findJoinOffer(teamConfirmationCondition , tomato.getId(), null, sortUpdate);
		List<JoinDto> TCList = TCResults.getContent();
		Page<JoinDto> MCResults = joinRepositoryCustom.findJoinOffer(teamConfirmationCondition , mango.getId(), null, sortUpdate);
		List<JoinDto> MCList = MCResults.getContent();
		Assertions.assertThat(TCList.get(0).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(TCList.get(0).getStatusType()).isEqualTo(StatusType.CONFIRMATION);
		Assertions.assertThat(TCList.get(0).getPlayerName()).isEqualTo("tomato");
		Assertions.assertThat(TCList.get(0).getTeamName()).isEqualTo("OrangeTeam");
		Assertions.assertThat(TCList.get(1).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(TCList.get(1).getStatusType()).isEqualTo(StatusType.CONFIRMATION);
		Assertions.assertThat(TCList.get(1).getPlayerName()).isEqualTo("tomato");
		Assertions.assertThat(TCList.get(1).getTeamName()).isEqualTo("GreenTeam");
		Assertions.assertThat(TCList.get(2).getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(TCList.get(2).getStatusType()).isEqualTo(StatusType.CONFIRMATION);
		Assertions.assertThat(TCList.get(2).getPlayerName()).isEqualTo("tomato");
		Assertions.assertThat(TCList.get(2).getTeamName()).isEqualTo("IndigoTeam");
		Assertions.assertThatThrownBy(() -> MCList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
		
		// Sorting의 순서대로 잘 나오는지
		Page<JoinDto> ppspResults = joinRepositoryCustom.findJoinOffer(playerProposalCondition , null, rteam.getId(), sortPlayer);
		List<JoinDto> ppspList = ppspResults.getContent();
		Assertions.assertThat(ppspList.get(0).getPlayerName()).isEqualTo("tomato");
		Assertions.assertThat(ppspList.get(1).getPlayerName()).isEqualTo("mango");
		Assertions.assertThat(ppspList.get(2).getPlayerName()).isEqualTo("graph");
		Assertions.assertThat(ppspList.get(3).getPlayerName()).isEqualTo("apple");
				
		Page<JoinDto> prjspResults = joinRepositoryCustom.findJoinOffer(playerRejectCondition , null, oteam.getId(), sortPlayer);
		List<JoinDto> prjspList = prjspResults.getContent();
		Assertions.assertThat(prjspList.get(0).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(prjspList.get(1).getPlayerName()).isEqualTo("kiwi");
		Assertions.assertThat(prjspList.get(2).getPlayerName()).isEqualTo("banana");
		
		Page<JoinDto> paspResults = joinRepositoryCustom.findJoinOffer(playerApprovalCondition , null, yteam.getId(), sortPlayer);
		List<JoinDto> paspList = paspResults.getContent();
		Assertions.assertThat(paspList.get(0).getPlayerName()).isEqualTo("tomato");
		Assertions.assertThat(paspList.get(1).getPlayerName()).isEqualTo("mango");
		Assertions.assertThat(ppspList.get(2).getPlayerName()).isEqualTo("graph");
		Assertions.assertThat(ppspList.get(3).getPlayerName()).isEqualTo("apple");
		
		Page<JoinDto> pwspResults = joinRepositoryCustom.findJoinOffer(playerWithdrawCondition , null, gteam.getId(), sortPlayer);
		List<JoinDto> pwspList = pwspResults.getContent();
		Assertions.assertThat(pwspList.get(0).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(pwspList.get(1).getPlayerName()).isEqualTo("kiwi");
		Assertions.assertThat(pwspList.get(2).getPlayerName()).isEqualTo("banana");
		
		Page<JoinDto> prtspResults = joinRepositoryCustom.findJoinOffer(playerReturnCondition , null, bteam.getId(), sortPlayer);
		List<JoinDto> prtspList = prtspResults.getContent();
		Assertions.assertThat(prtspList.get(0).getPlayerName()).isEqualTo("tomato");
		Assertions.assertThat(prtspList.get(1).getPlayerName()).isEqualTo("mango");
		Assertions.assertThat(prtspList.get(2).getPlayerName()).isEqualTo("graph");
		Assertions.assertThat(prtspList.get(3).getPlayerName()).isEqualTo("apple");
		
		Page<JoinDto> pcspResults = joinRepositoryCustom.findJoinOffer(playerConfirmationCondition , null, pteam.getId(), sortPlayer);
		List<JoinDto> pcspList = pcspResults.getContent();
		Assertions.assertThat(pcspList.get(0).getPlayerName()).isEqualTo("paprika");
		Assertions.assertThat(pcspList.get(1).getPlayerName()).isEqualTo("kiwi");
		Assertions.assertThat(pcspList.get(2).getPlayerName()).isEqualTo("banana");
		
		Page<JoinDto> TPSTResults = joinRepositoryCustom.findJoinOffer(teamProposalCondition , apple.getId(), null, sortTeam);
		List<JoinDto> TPSTList = TPSTResults.getContent();
		Assertions.assertThat(TPSTList.get(0).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(TPSTList.get(1).getTeamName()).isEqualTo("RedTeam");
		Assertions.assertThat(TPSTList.get(2).getTeamName()).isEqualTo("PurpleTeam");
		Assertions.assertThat(TPSTList.get(3).getTeamName()).isEqualTo("BlueTeam");
		
		Page<JoinDto> TRJSTResults = joinRepositoryCustom.findJoinOffer(teamRejectCondition , banana.getId(), null, sortTeam);
		List<JoinDto> TRJSTList = TRJSTResults.getContent();
		Assertions.assertThat(TRJSTList.get(0).getTeamName()).isEqualTo("OrangeTeam");
		Assertions.assertThat(TRJSTList.get(1).getTeamName()).isEqualTo("IndigoTeam");
		Assertions.assertThat(TRJSTList.get(2).getTeamName()).isEqualTo("GreenTeam");
		
		Page<JoinDto> TASTResults = joinRepositoryCustom.findJoinOffer(teamApprovalCondition , graph.getId(), null, sortTeam);
		List<JoinDto> TASTList = TASTResults.getContent();
		Assertions.assertThat(TASTList.get(0).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(TASTList.get(1).getTeamName()).isEqualTo("RedTeam");
		Assertions.assertThat(TASTList.get(2).getTeamName()).isEqualTo("PurpleTeam");
		Assertions.assertThat(TASTList.get(3).getTeamName()).isEqualTo("BlueTeam");
		
		Page<JoinDto> TWSTResults = joinRepositoryCustom.findJoinOffer(teamWithdrawCondition , kiwi.getId(), null, sortTeam);
		List<JoinDto> TWSTList = TWSTResults.getContent();
		Assertions.assertThat(TWSTList.get(0).getTeamName()).isEqualTo("OrangeTeam");
		Assertions.assertThat(TWSTList.get(1).getTeamName()).isEqualTo("IndigoTeam");
		Assertions.assertThat(TWSTList.get(2).getTeamName()).isEqualTo("GreenTeam");
		
		Page<JoinDto> TRSTResults = joinRepositoryCustom.findJoinOffer(teamReturnCondition , paprika.getId(), null, sortTeam);
		List<JoinDto> TRSTList = TRSTResults.getContent();
		Assertions.assertThat(TRSTList.get(0).getTeamName()).isEqualTo("YellowTeam");
		Assertions.assertThat(TRSTList.get(1).getTeamName()).isEqualTo("RedTeam");
		Assertions.assertThat(TRSTList.get(2).getTeamName()).isEqualTo("PurpleTeam");
		Assertions.assertThat(TRSTList.get(3).getTeamName()).isEqualTo("BlueTeam");
		
		Page<JoinDto> TCSTResults = joinRepositoryCustom.findJoinOffer(teamConfirmationCondition , tomato.getId(), null, sortTeam);
		List<JoinDto> TCSTList = TCSTResults.getContent();
		Assertions.assertThat(TCSTList.get(0).getTeamName()).isEqualTo("OrangeTeam");
		Assertions.assertThat(TCSTList.get(1).getTeamName()).isEqualTo("IndigoTeam");
		Assertions.assertThat(TCSTList.get(2).getTeamName()).isEqualTo("GreenTeam");
		
	}
}
