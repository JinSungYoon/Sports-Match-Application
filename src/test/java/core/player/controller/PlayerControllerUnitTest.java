package core.player.controller;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import core.player.dto.PlayerDto;
import core.player.service.PlayerService;
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
	public void register() throws Exception {
		
		// given
		PlayerDto dto = new PlayerDto("player1","220507-1111111",1,null);
		String content = new ObjectMapper().writeValueAsString(dto);
		when(playerService.registerPlayer(dto)).thenReturn(new PlayerDto("player1","220507-1111111",1,null));
		
		// when
		ResultActions resultAction = mockMvc.perform(post("/player/register")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.playerName").value("player1"))
			.andDo(MockMvcResultHandlers.print());
		log.info("=============================== Register test Start ===============================");
	}
	
	@Test
	public void findAllTest() throws Exception{
		//given
		List<PlayerDto> playerDtoList = new ArrayList<>();
		playerDtoList.add(new PlayerDto("player1","220507-1111111",1,null));
		playerDtoList.add(new PlayerDto("player2","220507-2222222",2,null));
		playerDtoList.add(new PlayerDto("player3","220507-3333333",3,null));
		when(playerService.searchPlayerAll()).thenReturn(playerDtoList);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/player/all")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",Matchers.hasSize(3)))
			.andExpect(jsonPath("$.[0].playerName").value("player1"))
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
	
}
