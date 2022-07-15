package core.join.controller;

import static org.mockito.Mockito.when;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
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
import core.join.service.JoinService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(controllers = JoinController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class JoinControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private JoinService joinService;
	
	@Test
	@DisplayName("가입 신청하기")
	public void requestJoin() throws Exception {
		// given
		JoinDto join = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,1L);
		String content = new ObjectMapper().writeValueAsString(join);
		when(joinService.requestJoin(join)).thenReturn(new JoinDto(1L,1L,"player1",1L,"team1",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
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
		JoinDto join = new JoinDto(RequesterType.PLAYER,StatusType.REJECT,1L,1L);
		String content = new ObjectMapper().writeValueAsString(join);
		when(joinService.rejectJoin(join)).thenReturn(new JoinDto(1L,1L,"player1",1L,"team1",RequesterType.PLAYER,StatusType.REJECT,'Y',LocalDateTime.now(),LocalDateTime.now()));
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
		JoinSearchCondition cond = new JoinSearchCondition();
		cond.setStatusType(StatusType.PROPOSAL);
		cond.setPlayerId(1L);
		PageRequest pageRequest = PageRequest.of(0, 10);
		String content = new ObjectMapper().writeValueAsString(cond);
		List<JoinDto> expectJoinList = new ArrayList<>();
		expectJoinList.add(new JoinDto(1L,1L,"player1",1L,"team1",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		Page<JoinDto> expectList = new PageImpl<>(expectJoinList,pageRequest,expectJoinList.size());
		
		when(joinService.searchPlayerJoinApplication(cond,pageRequest)).thenReturn(expectList);
		// when
		ResultActions resultAction = mockMvc.perform(get("/searchPlayerJoinApplication?page={page}&size={size}",0,10)
											.contentType(MediaType.APPLICATION_JSON_UTF8)
											.content(content)
											.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.content").isArray())
					.andExpect(jsonPath("$.content",Matchers.hasSize(1)))
					.andExpect(jsonPath("$.content.[0].playerName").value("player1"))
					.andExpect(jsonPath("$.content.[0].teamName").value("team1"))
					.andExpect(jsonPath("$.content.[0].requesterType").value("PLAYER"))
					.andExpect(jsonPath("$.content.[0].statusType").value("PROPOSAL"))
					.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("Player 가입 제안 조회")
	public void searchPlayerJoinOffer() throws Exception {
		// given
		JoinSearchCondition cond = new JoinSearchCondition();
		cond.setStatusType(StatusType.PROPOSAL);
		cond.setPlayerId(1L);
		PageRequest pageRequest = PageRequest.of(0, 10);
		String content = new ObjectMapper().writeValueAsString(cond);
		List<JoinDto> expectJoinList = new ArrayList<>();
		expectJoinList.add(new JoinDto(1L,1L,"player1",1L,"team1",RequesterType.TEAM,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		Page<JoinDto> expectList = new PageImpl<>(expectJoinList,pageRequest,expectJoinList.size());
		
		when(joinService.searchPlayerJoinOffer(cond,pageRequest)).thenReturn(expectList);
		// when
		ResultActions resultAction = mockMvc.perform(get("/searchPlayerJoinOffer?page={page}&size={size}",0,10)
											.contentType(MediaType.APPLICATION_JSON_UTF8)
											.content(content)
											.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.content").isArray())
					.andExpect(jsonPath("$.content",Matchers.hasSize(1)))
					.andExpect(jsonPath("$.content.[0].playerName").value("player1"))
					.andExpect(jsonPath("$.content.[0].teamName").value("team1"))
					.andExpect(jsonPath("$.content.[0].requesterType").value("TEAM"))
					.andExpect(jsonPath("$.content.[0].statusType").value("PROPOSAL"))
					.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("Team 가입 신청 조회")
	public void searchTeamJoinApplication() throws Exception {
		// given
		JoinSearchCondition cond = new JoinSearchCondition();
		cond.setStatusType(StatusType.PROPOSAL);
		cond.setTeamId(1L);
		PageRequest pageRequest = PageRequest.of(0, 10);
		String content = new ObjectMapper().writeValueAsString(cond);
		List<JoinDto> expectJoinList = new ArrayList<>();
		expectJoinList.add(new JoinDto(1L,1L,"player1",1L,"team1",RequesterType.TEAM,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		expectJoinList.add(new JoinDto(2L,2L,"player2",1L,"team1",RequesterType.TEAM,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		Page<JoinDto> expectList = new PageImpl<>(expectJoinList,pageRequest,expectJoinList.size());
		
		when(joinService.searchTeamJoinApplication(cond,pageRequest)).thenReturn(expectList);
		// when
		ResultActions resultAction = mockMvc.perform(get("/searchTeamJoinApplication?page={page}&size={size}",0,10)
											.contentType(MediaType.APPLICATION_JSON_UTF8)
											.content(content)
											.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.content").isArray())
					.andExpect(jsonPath("$.content",Matchers.hasSize(2)))
					.andExpect(jsonPath("$.content.[0].playerName").value("player1"))
					.andExpect(jsonPath("$.content.[0].teamName").value("team1"))
					.andExpect(jsonPath("$.content.[0].requesterType").value("TEAM"))
					.andExpect(jsonPath("$.content.[0].statusType").value("PROPOSAL"))
					.andExpect(jsonPath("$.content.[1].playerName").value("player2"))
					.andExpect(jsonPath("$.content.[1].teamName").value("team1"))
					.andExpect(jsonPath("$.content.[1].requesterType").value("TEAM"))
					.andExpect(jsonPath("$.content.[1].statusType").value("PROPOSAL"))
					.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("Team 가입 신청 조회")
	public void searchTeamJoinOffer() throws Exception {
		// given
		JoinSearchCondition cond = new JoinSearchCondition();
		cond.setStatusType(StatusType.PROPOSAL);
		cond.setTeamId(2L);
		PageRequest pageRequest = PageRequest.of(0, 10);
		String content = new ObjectMapper().writeValueAsString(cond);
		List<JoinDto> expectJoinList = new ArrayList<>();
		expectJoinList.add(new JoinDto(1L,1L,"player1",2L,"team2",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		expectJoinList.add(new JoinDto(2L,2L,"player2",2L,"team2",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		expectJoinList.add(new JoinDto(3L,3L,"player3",2L,"team2",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		expectJoinList.add(new JoinDto(4L,4L,"player4",2L,"team2",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		expectJoinList.add(new JoinDto(5L,5L,"player5",2L,"team2",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		expectJoinList.add(new JoinDto(6L,6L,"player6",2L,"team2",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		expectJoinList.add(new JoinDto(7L,7L,"player7",2L,"team2",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		expectJoinList.add(new JoinDto(8L,8L,"player8",2L,"team2",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		expectJoinList.add(new JoinDto(9L,9L,"player9",2L,"team2",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		expectJoinList.add(new JoinDto(10L,10L,"player10",2L,"team2",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
//		expectJoinList.add(new JoinDto(11L,11L,"player11",2L,"team2",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
//		expectJoinList.add(new JoinDto(12L,12L,"player12",2L,"team2",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now()));
		Page<JoinDto> expectList = new PageImpl<>(expectJoinList,pageRequest,expectJoinList.size());
		
		when(joinService.searchTeamJoinOffer(cond,pageRequest)).thenReturn(expectList);
		// when
		ResultActions resultAction = mockMvc.perform(get("/searchTeamJoinOffer?page={page}&size={size}",0,10)
											.contentType(MediaType.APPLICATION_JSON_UTF8)
											.content(content)
											.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.content").isArray())
					.andExpect(jsonPath("$.content",Matchers.hasSize(10)))
					.andExpect(jsonPath("$.content.[0].playerName").value("player1"))
					.andExpect(jsonPath("$.content.[0].teamName").value("team2"))
					.andExpect(jsonPath("$.content.[0].requesterType").value("PLAYER"))
					.andExpect(jsonPath("$.content.[0].statusType").value("PROPOSAL"))
					.andExpect(jsonPath("$.content.[2].playerName").value("player3"))
					.andExpect(jsonPath("$.content.[2].teamName").value("team2"))
					.andExpect(jsonPath("$.content.[2].requesterType").value("PLAYER"))
					.andExpect(jsonPath("$.content.[2].statusType").value("PROPOSAL"))
					.andExpect(jsonPath("$.content.[5].playerName").value("player6"))
					.andExpect(jsonPath("$.content.[5].teamName").value("team2"))
					.andExpect(jsonPath("$.content.[5].requesterType").value("PLAYER"))
					.andExpect(jsonPath("$.content.[5].statusType").value("PROPOSAL"))
					.andExpect(jsonPath("$.content.[9].playerName").value("player10"))
					.andExpect(jsonPath("$.content.[9].teamName").value("team2"))
					.andExpect(jsonPath("$.content.[9].requesterType").value("PLAYER"))
					.andExpect(jsonPath("$.content.[9].statusType").value("PROPOSAL"))
					.andDo(MockMvcResultHandlers.print());
	}
	
}
