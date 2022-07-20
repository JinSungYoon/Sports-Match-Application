package core.team.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.join.dto.JoinDto;
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import core.join.repository.JoinRepository;
import core.join.service.JoinService;
import core.player.dto.PlayerDto;
import core.player.entity.BelongType;
import core.player.repository.PlayerRepository;
import core.player.service.PlayerService;
import core.team.dto.TeamDto;
import core.team.repository.TeamRepository;
import core.team.service.TeamService;
import lombok.extern.slf4j.Slf4j;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;


@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
public class TeamControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private JoinRepository joinRepository;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired 
	private JoinService joinService;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		// 각각의 테스트에서 id값 생성시 1로 시작할 수 있도록
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
	@DisplayName("Team 등록하기")
	public void registerTeam() throws Exception {
		log.info("========================== Start Register Team ==========================");
		// given
		TeamDto teamDto = new TeamDto("team1","Seoul",BelongType.CLUB,"1팀 입니다 잘 부탁드립니다.");
		String content = new ObjectMapper().writeValueAsString(teamDto);
		
		// when
		ResultActions resultAction = mockMvc.perform(post("/team")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(content)
					.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.location").value("Seoul"))
			.andDo(MockMvcResultHandlers.print());
		
		log.info("========================== End Register Team ==========================");
	}
	
	@Test
	@DisplayName("모든 Team 조회하기")	
	void searchAllTeamsTest() throws Exception {
		// given
		List<TeamDto> list = new ArrayList<>();
		TeamDto teamDto1 = new TeamDto("BlueTeam","Seoul",BelongType.CLUB,"블루팀입니다.");
		TeamDto teamDto2 = new TeamDto("RedTeam","Busan",BelongType.PROTEAM,"레드팀입니다.");
		TeamDto teamDto3 = new TeamDto("YellowTeam","Seoul",BelongType.UNIVERSITY,"옐로우팀입니다.");
		TeamDto teamDto4 = new TeamDto("GreenTeam","Busan",BelongType.CLUB,"그린팀입니다.");
		TeamDto teamDto5 = new TeamDto("SkyBlueTeam","Seoul",BelongType.PROTEAM,"스카이 블루팀입니다.");
		TeamDto teamDto6 = new TeamDto("PinkTeam","Busan",BelongType.UNIVERSITY,"핑크팀입니다.");
		TeamDto teamDto7 = new TeamDto("OrangeTeam","Seoul",BelongType.CLUB,"오렌지팀입니다.");
		TeamDto teamDto8 = new TeamDto("DarkGreenTeam","Busan",BelongType.PROTEAM,"다크그린팀입니다.");
		TeamDto teamDto9 = new TeamDto("BlackTeam","Seoul",BelongType.UNIVERSITY,"블랙팀입니다.");
		TeamDto teamDto10 = new TeamDto("WhiteTeam","Busan",BelongType.CLUB,"화이트입니다.");
		PageRequest pageRequest = PageRequest.of(1, 5);
		
		teamService.registerTeam(teamDto1);
		teamService.registerTeam(teamDto2);
		teamService.registerTeam(teamDto3);
		teamService.registerTeam(teamDto4);
		teamService.registerTeam(teamDto5);
		teamService.registerTeam(teamDto6);
		teamService.registerTeam(teamDto7);
		teamService.registerTeam(teamDto8);
		teamService.registerTeam(teamDto9);
		teamService.registerTeam(teamDto10);
		
		List<TeamDto> rtnList = teamService.searchAllTeams(pageRequest);
		
		String content = new ObjectMapper().writeValueAsString(rtnList);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/teams/all?page={page}&size={size}",1,5)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content));
				
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",hasSize(5)))
			.andExpect(jsonPath("$.[0].introduction").value("핑크팀입니다."))
			.andExpect(jsonPath("$.[1].teamName").value("OrangeTeam"))
			.andExpect(jsonPath("$.[2].belongType").value("PROTEAM"))
			.andExpect(jsonPath("$.[3].location").value("Seoul"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("팀 조회하기")
	void searchTeams() throws JsonProcessingException,Exception {
		// given
		
		List<TeamDto> rtnlist = new ArrayList<>();
		TeamDto teamDto1 = new TeamDto("BlueTeam","서울 서대문구",BelongType.CLUB,"블루팀입니다.");
		TeamDto teamDto2 = new TeamDto("RedTeam","경기 동안구",BelongType.CLUB,"레드팀입니다.");
		TeamDto teamDto3 = new TeamDto("YellowTeam","서울 구로구",BelongType.CLUB,"옐로우팀입니다.");
		TeamDto teamDto4 = new TeamDto("GreenTeam","부산 금정구",BelongType.CLUB,"그린팀입니다.");
		TeamDto teamDto5 = new TeamDto("PinkTeam","경기 팔달구",BelongType.HIGH_SCHOOL,"핑크팀입니다.");
		
		// 검색 조회 결과 ArrayList에 추가
		rtnlist.add(teamDto2);
		rtnlist.add(teamDto5);
		
		// 5개의 팀 정보 등록
		teamService.registerTeam(teamDto1);
		teamService.registerTeam(teamDto2);
		teamService.registerTeam(teamDto3);
		teamService.registerTeam(teamDto4);
		teamService.registerTeam(teamDto5);
		
		String content = new ObjectMapper().writeValueAsString(rtnlist);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/teams?location={location}","경기")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$",hasSize(2)))
			.andExpect(jsonPath("$.[0].teamName").value("RedTeam"))
			.andExpect(jsonPath("$.[1].belongType").value("HIGH_SCHOOL"))
			.andExpect(jsonPath("$.[0].location").value("경기 동안구"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("팀 정보 업데이트")
	void updateTeamTest() throws Exception {
		// given
		TeamDto teamDto = new TeamDto("청팀","Busan",BelongType.PROTEAM,"청색 팀입니다.");
		// 팀 등록
		teamService.registerTeam(teamDto);
		// 팀 정보 변경
		teamDto.setTeamName("홍팀");
		teamDto.setBelongType(BelongType.UNIVERSITY);
		String content = new ObjectMapper().writeValueAsString(teamDto);
		
		// when
		ResultActions resultAction = mockMvc.perform(patch("/team/{id}",1L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.teamName").value("홍팀"))
			.andExpect(jsonPath("$.belongType").value("UNIVERSITY"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("팀 삭제하기")
	void deleteTeamTest() throws Exception {
		// given
		Long id = 1L;
		TeamDto teamDto = new TeamDto("삭제팀","Universe",BelongType.UNIVERSITY,"삭제퇻 팅입니다.");
		// 팀 정보 등록
		teamService.registerTeam(teamDto);
		
		String content = new ObjectMapper().writeValueAsString(teamDto);
		
		// when
		ResultActions resultAction = mockMvc.perform(delete("/team/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(1L))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("가입 신청하기")
	public void requestTeamJoin() throws Exception {
		// given
		TeamDto team 	= new TeamDto("team1","Seoul",BelongType.CLUB,"Our team is the best"); 
		PlayerDto player = new PlayerDto("player1","220713-1111111",1,team);
		JoinDto join = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,1L);
		teamService.registerTeam(team);
		playerService.registerPlayer(player);
		
		String content = new ObjectMapper().writeValueAsString(join);
		
		// when
		ResultActions resultAction = mockMvc.perform(post("/team/{id}/request-join",1)
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
	@DisplayName("가입제안 거절하기")
	public void rejectTeamJoin() throws Exception{
		// given
		TeamDto team 	= new TeamDto("team","Seoul",BelongType.CLUB,"Our team is the best"); 
		PlayerDto player = new PlayerDto("player","220713-1111111",1,team);
		JoinDto join = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,1L);
		teamService.registerTeam(team);
		playerService.registerPlayer(player);
		Long playerId = 1L;
		Long teamId = 1L;
		JoinDto requestJoin = new JoinDto(1L,1L,"player",1L,"team",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now());
		joinService.requestPlayerJoin(playerId,requestJoin);
		// when
		ResultActions resultAction = mockMvc.perform(patch("/team/{id}/reject-join/{teamId}",teamId,playerId)
											.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.requesterType").value("PLAYER"))
				.andExpect(jsonPath("$.statusType").value("REJECT"))
				.andExpect(jsonPath("$.playerId").value(1L))
				.andExpect(jsonPath("$.playerName").value("player"))
				.andExpect(jsonPath("$.teamId").value(1L))
				.andExpect(jsonPath("$.teamName").value("team"))
				.andDo(MockMvcResultHandlers.print());
	}
}
