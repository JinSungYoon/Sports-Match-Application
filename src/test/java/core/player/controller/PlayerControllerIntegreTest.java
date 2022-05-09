package core.player.controller;



import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import core.player.dto.PlayerDto;
import core.player.entity.BelongType;
import core.player.repository.PlayerRepository;
import core.player.service.PlayerService;
import core.team.dto.TeamDto;
import lombok.extern.slf4j.Slf4j;
/**
 * 통합 테스트(모든 Bean들을 똑같이 IoC에 올리고 테스트)
 * WebEnvironment.MOCK = 실제 톰캣을 사용하지 않고 가짜 톰캣을 사용
 * webEnvironment.RANDOM_PORT = 실제 톰캣으로 테스트
 * @AutoConfigureMockMvc MockMvc를 Ioc에 등록해줌.
 * @Trasnactional은 각 각의 테스트 함수가 종료될 때마다 트랜잭션을 rollback 해주는 어노테이션
 */

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
public class PlayerControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private EntityManager entityManager;
	
	@AfterEach
	public void init() {
		// 각각의 테스트에서 id값 생성시 1로 시작할 수 있도록
		playerRepository.deleteAll();
		entityManager
		.createNativeQuery("ALTER TABLE player AUTO_INCREMENT = 1;")
        .executeUpdate();
	}
	
	@Test
	public void register() throws Exception {
		
		// given
		TeamDto tDto = new TeamDto("team1","Seoul",BelongType.ELEMENTARY_SCHOOL,"Team1 입니다.");
		PlayerDto pDto = new PlayerDto("player1","220507-1111111",1,tDto);
		String content = new ObjectMapper().writeValueAsString(pDto);
		
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
		log.info("=============================== findAllTest test Start ===============================");
		//given
		List<PlayerDto> playerDtoList = new ArrayList<>();
		playerDtoList.add(new PlayerDto("player1","220507-1111111",1,null));
		playerDtoList.add(new PlayerDto("player2","220507-2222222",2,null));
		playerDtoList.add(new PlayerDto("player3","220507-3333333",3,null));
	
		List<PlayerDto> playerList =  playerService.registerPlayers(playerDtoList);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/player/all")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",Matchers.hasSize(3)))
			.andExpect(jsonPath("$.[2].playerName").value("player3"))
			.andDo(MockMvcResultHandlers.print());
		log.info("=============================== findAllTest test End ===============================");
	}
	
	@Test
	@DisplayName("Player Id로 찾아서 조회하기")
	public void searchOnePlayer() throws Exception{
		// given
		List<PlayerDto> playerDtoList = new ArrayList<>();
		playerDtoList.add(new PlayerDto("player1","220507-1111111",1,null));
		playerDtoList.add(new PlayerDto("player2","220507-2222222",2,null));
		playerDtoList.add(new PlayerDto("player3","220507-3333333",3,null));
	
		List<PlayerDto> playerList =  playerService.registerPlayers(playerDtoList);
		
		assertThat(playerList).isEqualTo(playerDtoList);
		
		Long id = 1L;
		
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
		players.add(new PlayerDto("player2","220509-2222222",2,null));
		
		playerService.registerPlayers(players);
		
		Long id = 1L;
		PlayerDto player = new PlayerDto("player4","220509-4444444",4,null);
		String content = new ObjectMapper().writeValueAsString(player);
		
		// when
		
		ResultActions resultAction = mockMvc.perform(put("/player/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.uniformNo").value(4))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("Player 정보 삭제하기")
	public void deletePlayer() throws Exception{
		// given
		List<PlayerDto> players = new ArrayList<>();
		players.add(new PlayerDto("player1","220509-1111111",1,null));
		players.add(new PlayerDto("player2","220509-2222222",2,null));
		
		List<PlayerDto> playerList =  playerService.registerPlayers(players);
		
		assertThat(playerList).isEqualTo(players);
		
		Long id = 1L;
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
