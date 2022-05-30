package core.team.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.player.entity.BelongType;
import core.team.dto.TeamDto;
import core.team.service.TeamService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(TeamController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class TeamControllerUnitTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TeamService teamService;
	
	@Test
	@DisplayName("Team 등록하기")
	public void registerTeam() throws Exception {
		log.info("========================== Start Register Team ==========================");
		// given
		TeamDto teamDto = new TeamDto("team1","Seoul",BelongType.CLUB,"1팀 입니다 잘 부탁드립니다.");
		String content = new ObjectMapper().writeValueAsString(teamDto);
		when(teamService.registerTeam(teamDto)).thenReturn(new TeamDto("team1","Seoul",BelongType.CLUB,"1팀 입니다 잘 부탁드립니다."));
		
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
	@DisplayName("Team Id로 팀 찾기")
	void searchOneTeamsTest() throws Exception {
		log.info("========================== End Search Team ==========================");
		// given
		Long id = 1L;
		TeamDto teamDto = new TeamDto("Team1","Busan",BelongType.HIGH_SCHOOL,"Team1 입니다.");
		String content = new ObjectMapper().writeValueAsString(teamDto);
		when(teamService.searchTeamById(1L)).thenReturn(new TeamDto("Team1","Busan",BelongType.HIGH_SCHOOL,"Team1 입니다."));
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/team/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.introduction").value("Team1 입니다."))
			.andDo(MockMvcResultHandlers.print());
		
		log.info("========================== End Search Team ==========================");
	}
	
	@Test
	@DisplayName("모든 Team 조회하기")	
	void searchAllTeamsTest() throws Exception {
		// given
		List<TeamDto> list = new ArrayList<>();
		TeamDto teamDto1 = new TeamDto("BlueTeam","Seoul",BelongType.CLUB,"블루팀입니다.");
		TeamDto teamDto2 = new TeamDto("RedTeam","Seoul",BelongType.CLUB,"레드팀입니다.");
		TeamDto teamDto3 = new TeamDto("YellowTeam","Seoul",BelongType.CLUB,"옐로우팀입니다.");
		TeamDto teamDto4 = new TeamDto("GreenTeam","Seoul",BelongType.CLUB,"그린팀입니다.");
		when(teamService.searchAllTeams()).thenReturn(list);
		list.add(teamDto1);
		list.add(teamDto2);
		list.add(teamDto3);
		list.add(teamDto4);
		String content = new ObjectMapper().writeValueAsString(list);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/teams/all")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content));
				
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].introduction").value("블루팀입니다."))
			.andExpect(jsonPath("$.[1].teamName").value("RedTeam"))
			.andExpect(jsonPath("$.[2].belongType").value("CLUB"))
			.andExpect(jsonPath("$.[3].location").value("Seoul"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("팀 조회하기")
	void searchTeams() throws JsonProcessingException,Exception {
		// given
		List<TeamDto> list = new ArrayList<>();
		List<TeamDto> rtnlist = new ArrayList<>();
		TeamDto teamDto1 = new TeamDto("BlueTeam","서울 서대문구",BelongType.CLUB,"블루팀입니다.");
		TeamDto teamDto2 = new TeamDto("RedTeam","경기 동안구",BelongType.CLUB,"레드팀입니다.");
		TeamDto teamDto3 = new TeamDto("YellowTeam","서울 구로구",BelongType.CLUB,"옐로우팀입니다.");
		TeamDto teamDto4 = new TeamDto("GreenTeam","부산 금정구",BelongType.CLUB,"그린팀입니다.");
		TeamDto teamDto5 = new TeamDto("PinkTeam","경기 팔달구",BelongType.HIGH_SCHOOL,"핑크팀입니다.");
		rtnlist.add(teamDto2);
		rtnlist.add(teamDto5);
		when(teamService.searchTeams(null, "경기", null, null)).thenReturn(rtnlist);
		
		String content = new ObjectMapper().writeValueAsString(rtnlist);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/teams?location={location}","경기")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content));
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
		teamDto.setTeamName("홍팀");
		teamDto.setBelongType(BelongType.UNIVERSITY);
		String content = new ObjectMapper().writeValueAsString(teamDto);
		when(teamService.updateTeam(1L, teamDto)).thenReturn(teamDto);
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
		String content = new ObjectMapper().writeValueAsString(teamDto);
		when(teamService.deleteTeam(1L)).thenReturn(1L);
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
}
