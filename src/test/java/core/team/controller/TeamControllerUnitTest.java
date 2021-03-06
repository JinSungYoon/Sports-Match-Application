package core.team.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.join.dto.JoinDto;
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import core.join.service.JoinService;
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
	
	@MockBean
	private JoinService joinService;
	
	@Test
	@DisplayName("Team ????????????")
	public void registerTeam() throws Exception {
		log.info("========================== Start Register Team ==========================");
		// given
		TeamDto teamDto = new TeamDto("team1","Seoul",BelongType.CLUB,"1??? ????????? ??? ??????????????????.");
		String content = new ObjectMapper().writeValueAsString(teamDto);
		when(teamService.registerTeam(teamDto)).thenReturn(new TeamDto("team1","Seoul",BelongType.CLUB,"1??? ????????? ??? ??????????????????."));
		
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
	@DisplayName("Team Id??? ??? ??????")
	void searchOneTeamsTest() throws Exception {
		log.info("========================== End Search Team ==========================");
		// given
		Long id = 1L;
		TeamDto teamDto = new TeamDto("Team1","Busan",BelongType.HIGH_SCHOOL,"Team1 ?????????.");
		String content = new ObjectMapper().writeValueAsString(teamDto);
		when(teamService.searchTeamById(1L)).thenReturn(new TeamDto("Team1","Busan",BelongType.HIGH_SCHOOL,"Team1 ?????????."));
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/team/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.introduction").value("Team1 ?????????."))
			.andDo(MockMvcResultHandlers.print());
		
		log.info("========================== End Search Team ==========================");
	}
	
	@Test
	@DisplayName("?????? Team ????????????")	
	void searchAllTeamsTest() throws Exception {
		// given
		List<TeamDto> list = new ArrayList<>();
		TeamDto teamDto1 = new TeamDto("BlueTeam","Seoul",BelongType.CLUB,"??????????????????.");
		TeamDto teamDto2 = new TeamDto("RedTeam","Busan",BelongType.PROTEAM,"??????????????????.");
		TeamDto teamDto3 = new TeamDto("YellowTeam","Seoul",BelongType.UNIVERSITY,"?????????????????????.");
		TeamDto teamDto4 = new TeamDto("GreenTeam","Busan",BelongType.CLUB,"??????????????????.");
		TeamDto teamDto5 = new TeamDto("SkyBlueTeam","Seoul",BelongType.PROTEAM,"????????? ??????????????????.");
		TeamDto teamDto6 = new TeamDto("PinkTeam","Busan",BelongType.UNIVERSITY,"??????????????????.");
		TeamDto teamDto7 = new TeamDto("OrangeTeam","Seoul",BelongType.CLUB,"?????????????????????.");
		TeamDto teamDto8 = new TeamDto("DarkGreenTeam","Busan",BelongType.PROTEAM,"????????????????????????.");
		TeamDto teamDto9 = new TeamDto("BlackTeam","Seoul",BelongType.UNIVERSITY,"??????????????????.");
		TeamDto teamDto10 = new TeamDto("WhiteTeam","Busan",BelongType.CLUB,"??????????????????.");
		PageRequest pageRequest = PageRequest.of(1, 5);
		when(teamService.searchAllTeams(pageRequest)).thenReturn(list);
		
		list.add(teamDto6);
		list.add(teamDto7);
		list.add(teamDto8);
		list.add(teamDto9);
		list.add(teamDto10);
		String content = new ObjectMapper().writeValueAsString(list);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/teams/all?page={page}&size={size}",1,5)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content));
				
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",hasSize(5)))
			.andExpect(jsonPath("$.[0].introduction").value("??????????????????."))
			.andExpect(jsonPath("$.[1].teamName").value("OrangeTeam"))
			.andExpect(jsonPath("$.[2].belongType").value("PROTEAM"))
			.andExpect(jsonPath("$.[3].location").value("Seoul"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("??? ????????????")
	void searchTeams() throws JsonProcessingException,Exception {
		// given
		List<TeamDto> list = new ArrayList<>();
		List<TeamDto> rtnlist = new ArrayList<>();
		TeamDto teamDto1 = new TeamDto("BlueTeam","?????? ????????????",BelongType.CLUB,"??????????????????.");
		TeamDto teamDto2 = new TeamDto("RedTeam","?????? ?????????",BelongType.CLUB,"??????????????????.");
		TeamDto teamDto3 = new TeamDto("YellowTeam","?????? ?????????",BelongType.CLUB,"?????????????????????.");
		TeamDto teamDto4 = new TeamDto("GreenTeam","?????? ?????????",BelongType.CLUB,"??????????????????.");
		TeamDto teamDto5 = new TeamDto("PinkTeam","?????? ?????????",BelongType.HIGH_SCHOOL,"??????????????????.");
		rtnlist.add(teamDto2);
		rtnlist.add(teamDto5);
		PageRequest pageRequest = PageRequest.of(0, 2);
		when(teamService.searchTeams(null, "??????", null, null,pageRequest)).thenReturn(rtnlist);
		
		String content = new ObjectMapper().writeValueAsString(rtnlist);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/teams?location={location}&page={page}&size={size}","??????",0,2)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content));
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$",hasSize(2)))
			.andExpect(jsonPath("$.[0].teamName").value("RedTeam"))
			.andExpect(jsonPath("$.[1].belongType").value("HIGH_SCHOOL"))
			.andExpect(jsonPath("$.[0].location").value("?????? ?????????"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("??? ?????? ????????????")
	void updateTeamTest() throws Exception {
		// given
		TeamDto teamDto = new TeamDto("??????","Busan",BelongType.PROTEAM,"?????? ????????????.");
		teamDto.setTeamName("??????");
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
			.andExpect(jsonPath("$.teamName").value("??????"))
			.andExpect(jsonPath("$.belongType").value("UNIVERSITY"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("??? ????????????")
	void deleteTeamTest() throws Exception {
		// given
		Long id = 1L;
		TeamDto teamDto = new TeamDto("?????????","Universe",BelongType.UNIVERSITY,"????????? ????????????.");
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
	
	@Test
	@DisplayName("?????? ????????????")
	public void requestTeamJoin() throws Exception {
		// given
		JoinDto join = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,1L);
		String content = new ObjectMapper().writeValueAsString(join);
		when(joinService.requestTeamJoin(1L,join)).thenReturn(new JoinDto(1L,1L,"player1",1L,"team1",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
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
	@DisplayName("???????????? ????????????")
	public void rejectTeamJoin() throws Exception{
		// given
		Long playerId = 5L;
		Long teamId = 3L;
		JoinDto rejectJoin = new JoinDto(1L,5L,"player",3L,"team",RequesterType.PLAYER,StatusType.REJECT,'Y',LocalDateTime.now(),LocalDateTime.now());
		when(joinService.rejectTeamJoin(playerId, teamId)).thenReturn(rejectJoin);
		// when
		ResultActions resultAction = mockMvc.perform(patch("/team/{id}/reject-join/{teamId}",playerId,teamId)
											.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.requesterType").value("PLAYER"))
				.andExpect(jsonPath("$.statusType").value("REJECT"))
				.andExpect(jsonPath("$.playerId").value(5L))
				.andExpect(jsonPath("$.playerName").value("player"))
				.andExpect(jsonPath("$.teamId").value(3L))
				.andExpect(jsonPath("$.teamName").value("team"))
				.andDo(MockMvcResultHandlers.print());
	}
}
