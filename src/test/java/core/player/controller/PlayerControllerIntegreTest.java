package core.player.controller;



import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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
	
	@BeforeEach
	public void init() {
		// 각각의 테스트에서 id값 생성시 1로 시작할 수 있도록
		//entityManager.createNamedQuery("ALTER TABLE PLAYER AUTO_INCREMENT = 1").executeUpdate();
		//entityManager.createNamedQuery("ALTER TABLE PLAYER ALTER COLUMN ID RESTART WITH 1").executeUpdate();
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
	
		playerService.registerPlayers(playerDtoList);
		
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
}
