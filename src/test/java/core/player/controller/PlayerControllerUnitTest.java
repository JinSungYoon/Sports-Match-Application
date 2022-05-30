package core.player.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import core.player.dto.PlayerDto;
import core.player.dto.PlayerListDto;
import core.player.entity.BelongType;
import core.player.service.PlayerService;
import core.team.dto.TeamDto;
import lombok.extern.slf4j.Slf4j;

// 단위 테스트(Controller,Filter,ControllerAdvice 관련 로직만 테스트)

@Slf4j
@WebMvcTest(controllers = PlayerController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PlayerControllerUnitTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PlayerService playerService;
		
	@Test
	@DisplayName("Player 등록하기")
	public void register() throws Exception {
		log.info("=============================== Start Register test ===============================");
		// given
		PlayerDto dto = new PlayerDto("player1","220507-1111111",1,null);
		String content = new ObjectMapper().writeValueAsString(dto);
		when(playerService.registerPlayer(dto)).thenReturn(new PlayerDto("player1","220507-1111111",1,null));
		
		// when
		ResultActions resultAction = mockMvc.perform(post("/player")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.playerName").value("player1"))
			.andDo(MockMvcResultHandlers.print());
		log.info("=============================== End Register test ===============================");
	}
	
	@Test
	@DisplayName("여러명의 Player 등록하기")
	public void registersTest() throws Exception {
		// given
		List<PlayerDto> playerList = new ArrayList<>();
		PlayerDto player1 = new PlayerDto("player1","220516-1111111",1,null);
		PlayerDto player2 = new PlayerDto("player2","220516-2222222",2,null);
		PlayerDto player3 = new PlayerDto("player3","220516-3333333",3,null);
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		PlayerListDto dtoList = new PlayerListDto();
		dtoList.setPlayers(playerList);
		String content = new ObjectMapper().writeValueAsString(dtoList);
		when(playerService.registerPlayers(dtoList.getPlayers())).thenReturn(playerList);
		
		// when
		ResultActions resultAction = mockMvc.perform(post("/players")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
				
		// then
		resultAction
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.[0].playerName").value("player1"))
			.andExpect(jsonPath("$.[1].uniformNo").value(2))
			.andExpect(jsonPath("$.[2].playerName").value("player3"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("모든 Player 조회하기")
	public void findAllTest() throws Exception{
		//given
		List<PlayerDto> playerDtoList = new ArrayList<>();
		playerDtoList.add(new PlayerDto("player1","220507-1111111",1,null));
		playerDtoList.add(new PlayerDto("player2","220507-2222222",2,null));
		playerDtoList.add(new PlayerDto("player3","220507-3333333",3,null));
		when(playerService.searchPlayerAll()).thenReturn(playerDtoList);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/players/all")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",Matchers.hasSize(3)))
			.andExpect(jsonPath("$.[0].playerName").value("player1"))
			.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	@DisplayName("조건에 맞는 Player 조회하기")
	void findTeamTest() throws Exception {
		// given
		List<PlayerDto> list= new ArrayList<>();
		List<PlayerDto> rtnList= new ArrayList<>();
		TeamDto team1 = new TeamDto("team1","Seoul",BelongType.CLUB,"Our team belong to Seoul");
		TeamDto team2 = new TeamDto("team2","Busan",BelongType.CLUB,"Our team belong to Busan");
		list.add(new PlayerDto("player1","220530-1111111",1,team1));
		list.add(new PlayerDto("player2","220530-1111111",1,team2));
		list.add(new PlayerDto("player3","220530-1111111",2,team2));
		list.add(new PlayerDto("player4","220530-1111111",2,team1));
		list.add(new PlayerDto("player5","220530-1111111",3,team2));
		rtnList.add(new PlayerDto("player2","220530-1111111",1,team2));
		rtnList.add(new PlayerDto("player3","220530-1111111",2,team2));
		rtnList.add(new PlayerDto("player5","220530-1111111",3,team2));
		when(playerService.searchPlayers(null, null, "team2")).thenReturn(rtnList);
		// when
		ResultActions resultAction = mockMvc.perform(get("/players?teamName={teamName}","team2")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$",hasSize(3)))
			.andExpect(jsonPath("$.[0].playerName").value("player2"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void searchOnePlayer() throws Exception{
		// given
		Long id = 1L;
		when(playerService.searchOnePlayer(id)).thenReturn(new PlayerDto("player1","220507-3333333",3,null));
		// when
		ResultActions resultAction = mockMvc.perform(get("/player/{id}",id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.playerName").value("player1"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("Player 정보 업데이트")
	public void updatePlayer() throws Exception{
		// given
		List<PlayerDto> players = new ArrayList<>();
		players.add(new PlayerDto("player1","220509-1111111",1,null));
		players.add(new PlayerDto("player1","220509-1111111",1,null));
		
		playerService.registerPlayers(players);
		
		Long id = 1L;
		PlayerDto player = new PlayerDto("player1","220509-1111111",1,null);
		String content = new ObjectMapper().writeValueAsString(player);
		when(playerService.updatePlayer(id,player)).thenReturn(new PlayerDto("player1","220509-1111111",1,null));
		// when
		
		ResultActions resultAction = mockMvc.perform(patch("/player/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.uniformNo").value(1))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("Player 정보 삭제하기")
	public void deletePlayer() throws Exception{
		// given
		Long id = 1L;
		when(playerService.deletePlyaer(id)).thenReturn(1L);
		// when
		ResultActions resultAction = mockMvc.perform(delete("/player/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").value(1L))
			.andDo(MockMvcResultHandlers.print());
	}
	
}
