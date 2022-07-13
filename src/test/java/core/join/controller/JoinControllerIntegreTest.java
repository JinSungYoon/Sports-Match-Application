package core.join.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.join.dto.JoinDto;
import core.join.dto.JoinSearchCondition;
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import core.join.repository.JoinRepository;
import core.join.service.JoinService;
import core.player.entity.BelongType;
import core.player.entity.PlayerEntity;
import core.player.repository.PlayerRepository;
import core.team.entity.TeamEntity;
import core.team.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
public class JoinControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private PlayerRepository playerRepository;
	@Autowired
	private TeamRepository teamRepository;
	@Autowired
	private JoinRepository joinRepository;
	@Autowired
	private JoinService joinService;
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		joinRepository.deleteAll();
		playerRepository.deleteAll();
		teamRepository.deleteAll();
		entityManager
			.createNativeQuery("ALTER TABLE joining AUTO_INCREMENT = 1;")
			.executeUpdate();
		entityManager
			.createNativeQuery("ALTER TABLE player AUTO_INCREMENT = 1;")
			.executeUpdate();
		entityManager
			.createNativeQuery("ALTER TABLE team AUTO_INCREMENT = 1;")
			.executeUpdate();
	}
	
	@Test
	@DisplayName("가입 신청하기")
	public void requestJoin() throws Exception {
		// given
		TeamEntity team 	= new TeamEntity("team1","Seoul",BelongType.CLUB,"Our team is the best"); 
		PlayerEntity player = new PlayerEntity("player1","220713-1111111",1,team);
		JoinDto join = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,1L);
		teamRepository.save(team);
		playerRepository.save(player);
		
		String content = new ObjectMapper().writeValueAsString(join);
		
		// when
		ResultActions resultAction = mockMvc.perform(post("/requestJoin")
							.contentType(MediaType.APPLICATION_JSON_UTF8)
							.content(content)
							.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.requesterType").value("PLAYER"))
				.andExpect(jsonPath("$.statusType").value("PROPOSAL"))
				.andExpect(jsonPath("$.teamName").value("team1"))
				.andExpect(jsonPath("$.playerName").value("player1"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("가입 거절하기")
	public void rejectJoin() throws Exception{
		// given
		TeamEntity team 	= new TeamEntity("team1","Seoul",BelongType.CLUB,"Our team is the best"); 
		PlayerEntity player = new PlayerEntity("player1","220713-1111111",1,team);
		JoinDto proposalJoin = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,1L);
		
		teamRepository.save(team);
		playerRepository.save(player);
		joinService.requestJoin(proposalJoin);
		
		JoinDto rejectJoin = new JoinDto(RequesterType.PLAYER,StatusType.REJECT,1L,1L);
		String content = new ObjectMapper().writeValueAsString(rejectJoin);
		
		// when
		ResultActions resultAction = mockMvc.perform(patch("/rejectJoin")
											.contentType(MediaType.APPLICATION_JSON_UTF8)
											.content(content)
											.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.requesterType").value("PLAYER"))
				.andExpect(jsonPath("$.statusType").value("REJECT"))
				.andExpect(jsonPath("$.teamName").value("team1"))
				.andExpect(jsonPath("$.playerName").value("player1"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("Player 가입 신청 조회")
	public void searchPlayerJoinApplication() throws Exception {
		// given
		TeamEntity noLegs = new TeamEntity("No Leg Team","ocean",BelongType.PROTEAM,"We don't have leg");
		TeamEntity twoLegs = new TeamEntity("Two Leg Team","land",BelongType.PROTEAM,"We are two legs team");
		TeamEntity fourLegs = new TeamEntity("Four Leg Team","land",BelongType.PROTEAM,"We are four legs team");
		teamRepository.save(noLegs);
		teamRepository.save(twoLegs);
		teamRepository.save(fourLegs);
		
		PlayerEntity snake		= new PlayerEntity("snake","220713-1111111",1,noLegs);
		PlayerEntity shark		= new PlayerEntity("shark","220713-1111111",2,noLegs);
		PlayerEntity kangaroo	= new PlayerEntity("kangaroo","220713-1111111",1,twoLegs);
		PlayerEntity penguin	= new PlayerEntity("penguin","220713-1111111",2,twoLegs);
		PlayerEntity hippo		= new PlayerEntity("hippo","220713-1111111",1,fourLegs);
		PlayerEntity camel		= new PlayerEntity("camel","220713-1111111",2,fourLegs);
		
		playerRepository.save(snake);
		playerRepository.save(shark);
		playerRepository.save(kangaroo);
		playerRepository.save(penguin);
		playerRepository.save(hippo);
		playerRepository.save(camel);
		
		JoinDto join1 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,2L);
		JoinDto join2 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,3L);
		JoinDto join3 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,3L,1L);
		JoinDto join4 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,3L,3L);
		JoinDto join5 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,5L,1L);
		JoinDto join6 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,5L,2L);
		joinService.requestJoin(join1);
		joinService.requestJoin(join2);
		joinService.requestJoin(join3);
		joinService.requestJoin(join4);
		joinService.requestJoin(join5);
		joinService.requestJoin(join6);
		
		JoinSearchCondition cond = new JoinSearchCondition();
		cond.setPlayerId(3L);
		
		String content = new ObjectMapper().writeValueAsString(cond);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/searchPlayerJoinApplication?page={page}&size={size}",0,10)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.totalElements").value(2))
				.andExpect(jsonPath("$.size").value(10))
				.andExpect(jsonPath("$.content",Matchers.hasSize(2)))
				.andExpect(jsonPath("$.content.[0].playerName").value("kangaroo"))
				.andExpect(jsonPath("$.content.[0].teamName").value("No Leg Team"))
				.andExpect(jsonPath("$.content.[1].teamName").value("Four Leg Team"))
				.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	@DisplayName("Player 가입 제안 조회")
	public void searchPlayerJoinOffer() throws Exception {
		// given
		TeamEntity noLegs = new TeamEntity("No Leg Team","ocean",BelongType.PROTEAM,"We don't have leg");
		TeamEntity twoLegs = new TeamEntity("Two Leg Team","land",BelongType.PROTEAM,"We are two legs team");
		TeamEntity fourLegs = new TeamEntity("Four Leg Team","land",BelongType.PROTEAM,"We are four legs team");
		teamRepository.save(noLegs);
		teamRepository.save(twoLegs);
		teamRepository.save(fourLegs);
		
		PlayerEntity snake		= new PlayerEntity("snake","220713-1111111",1,noLegs);
		PlayerEntity shark		= new PlayerEntity("shark","220713-1111111",2,noLegs);
		PlayerEntity kangaroo	= new PlayerEntity("kangaroo","220713-1111111",1,twoLegs);
		PlayerEntity penguin	= new PlayerEntity("penguin","220713-1111111",2,twoLegs);
		PlayerEntity hippo		= new PlayerEntity("hippo","220713-1111111",1,fourLegs);
		PlayerEntity camel		= new PlayerEntity("camel","220713-1111111",2,fourLegs);
		
		playerRepository.save(snake);
		playerRepository.save(shark);
		playerRepository.save(kangaroo);
		playerRepository.save(penguin);
		playerRepository.save(hippo);
		playerRepository.save(camel);
		
		JoinDto join1 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,3L,1L);
		JoinDto join2 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,4L,1L);
		JoinDto join3 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,5L,1L);
		JoinDto join4 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,6L,1L);
		JoinDto join5 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,1L,2L);
		JoinDto join6 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,2L,2L);
		JoinDto join7 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,5L,2L);
		JoinDto join8 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,6L,2L);
		JoinDto join9 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,1L,3L);
		JoinDto join10 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,2L,3L);
		JoinDto join11 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,3L,3L);
		JoinDto join12 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,4L,3L);
		joinService.requestJoin(join1);
		joinService.requestJoin(join2);
		joinService.requestJoin(join3);
		joinService.requestJoin(join4);
		joinService.requestJoin(join5);
		joinService.requestJoin(join6);
		joinService.requestJoin(join7);
		joinService.requestJoin(join8);
		joinService.requestJoin(join9);
		joinService.requestJoin(join10);
		joinService.requestJoin(join11);
		joinService.requestJoin(join12);
		
		JoinSearchCondition cond = new JoinSearchCondition();
		cond.setPlayerId(6L);
		
		String content = new ObjectMapper().writeValueAsString(cond);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/searchPlayerJoinOffer?page={page}&size={size}",0,10)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.totalElements").value(2))
				.andExpect(jsonPath("$.size").value(10))
				.andExpect(jsonPath("$.content",Matchers.hasSize(2)))
				.andExpect(jsonPath("$.content.[0].playerName").value("camel"))
				.andExpect(jsonPath("$.content.[0].teamName").value("No Leg Team"))
				.andExpect(jsonPath("$.content.[1].teamName").value("Two Leg Team"))
				.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	@DisplayName("Team 가입 신청 조회")
	public void searchTeamJoinApplication() throws Exception {
		// given
		TeamEntity noLegs = new TeamEntity("No Leg Team","ocean",BelongType.PROTEAM,"We don't have leg");
		TeamEntity twoLegs = new TeamEntity("Two Leg Team","land",BelongType.PROTEAM,"We are two legs team");
		TeamEntity fourLegs = new TeamEntity("Four Leg Team","land",BelongType.PROTEAM,"We are four legs team");
		teamRepository.save(noLegs);
		teamRepository.save(twoLegs);
		teamRepository.save(fourLegs);
		
		PlayerEntity snake		= new PlayerEntity("snake","220713-1111111",1,noLegs);
		PlayerEntity shark		= new PlayerEntity("shark","220713-1111111",2,noLegs);
		PlayerEntity kangaroo	= new PlayerEntity("kangaroo","220713-1111111",1,twoLegs);
		PlayerEntity penguin	= new PlayerEntity("penguin","220713-1111111",2,twoLegs);
		PlayerEntity hippo		= new PlayerEntity("hippo","220713-1111111",1,fourLegs);
		PlayerEntity camel		= new PlayerEntity("camel","220713-1111111",2,fourLegs);
		
		playerRepository.save(snake);
		playerRepository.save(shark);
		playerRepository.save(kangaroo);
		playerRepository.save(penguin);
		playerRepository.save(hippo);
		playerRepository.save(camel);
		
		JoinDto join1 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,3L,1L);
		JoinDto join2 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,4L,1L);
		JoinDto join3 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,5L,1L);
		JoinDto join4 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,6L,1L);
		JoinDto join5 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,1L,2L);
		JoinDto join6 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,2L,2L);
		JoinDto join7 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,5L,2L);
		JoinDto join8 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,6L,2L);
		JoinDto join9 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,1L,3L);
		JoinDto join10 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,2L,3L);
		JoinDto join11 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,3L,3L);
		JoinDto join12 = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,4L,3L);
		joinService.requestJoin(join1);
		joinService.requestJoin(join2);
		joinService.requestJoin(join3);
		joinService.requestJoin(join4);
		joinService.requestJoin(join5);
		joinService.requestJoin(join6);
		joinService.requestJoin(join7);
		joinService.requestJoin(join8);
		joinService.requestJoin(join9);
		joinService.requestJoin(join10);
		joinService.requestJoin(join11);
		joinService.requestJoin(join12);
		
		JoinSearchCondition cond = new JoinSearchCondition();
		cond.setTeamId(2L);
		
		String content = new ObjectMapper().writeValueAsString(cond);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/searchTeamJoinApplication?page={page}&size={size}",0,10)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.totalElements").value(4))
				.andExpect(jsonPath("$.size").value(10))
				.andExpect(jsonPath("$.content",Matchers.hasSize(4)))
				.andExpect(jsonPath("$.content.[0].teamName").value("Two Leg Team"))
				.andExpect(jsonPath("$.content.[0].playerName").value("snake"))
				.andExpect(jsonPath("$.content.[1].playerName").value("shark"))
				.andExpect(jsonPath("$.content.[2].playerName").value("hippo"))
				.andExpect(jsonPath("$.content.[3].playerName").value("camel"))
				.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	@DisplayName("Team 가입 제안 조회")
	public void searchTeamJoinOffer() throws Exception {
		// given
		TeamEntity noLegs = new TeamEntity("No Leg Team","ocean",BelongType.PROTEAM,"We don't have leg");
		TeamEntity twoLegs = new TeamEntity("Two Leg Team","land",BelongType.PROTEAM,"We are two legs team");
		TeamEntity fourLegs = new TeamEntity("Four Leg Team","land",BelongType.PROTEAM,"We are four legs team");
		teamRepository.save(noLegs);
		teamRepository.save(twoLegs);
		teamRepository.save(fourLegs);
		
		PlayerEntity snake		= new PlayerEntity("snake","220713-1111111",1,noLegs);
		PlayerEntity shark		= new PlayerEntity("shark","220713-1111111",2,noLegs);
		PlayerEntity kangaroo	= new PlayerEntity("kangaroo","220713-1111111",1,twoLegs);
		PlayerEntity penguin	= new PlayerEntity("penguin","220713-1111111",2,twoLegs);
		PlayerEntity hippo		= new PlayerEntity("hippo","220713-1111111",1,fourLegs);
		PlayerEntity camel		= new PlayerEntity("camel","220713-1111111",2,fourLegs);
		
		playerRepository.save(snake);
		playerRepository.save(shark);
		playerRepository.save(kangaroo);
		playerRepository.save(penguin);
		playerRepository.save(hippo);
		playerRepository.save(camel);
		
		JoinDto join1 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,2L);
		JoinDto join2 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,3L);
		JoinDto join3 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,3L,1L);
		JoinDto join4 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,3L,3L);
		JoinDto join5 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,5L,1L);
		JoinDto join6 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,5L,2L);
		joinService.requestJoin(join1);
		joinService.requestJoin(join2);
		joinService.requestJoin(join3);
		joinService.requestJoin(join4);
		joinService.requestJoin(join5);
		joinService.requestJoin(join6);
		
		JoinSearchCondition cond = new JoinSearchCondition();
		cond.setTeamId(2L);
		
		String content = new ObjectMapper().writeValueAsString(cond);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/searchTeamJoinOffer?page={page}&size={size}",0,10)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.totalElements").value(2))
				.andExpect(jsonPath("$.size").value(10))
				.andExpect(jsonPath("$.content",Matchers.hasSize(2)))
				.andExpect(jsonPath("$.content.[0].teamName").value("Two Leg Team"))
				.andExpect(jsonPath("$.content.[0].playerName").value("snake"))
				.andExpect(jsonPath("$.content.[1].playerName").value("hippo"))
				.andDo(MockMvcResultHandlers.print());
		
	}
}
