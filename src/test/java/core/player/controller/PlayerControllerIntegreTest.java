package core.player.controller;



import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
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

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import core.join.dto.JoinDto;
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import core.join.repository.JoinRepository;
import core.join.service.JoinService;
import core.player.dto.PlayerDto;
import core.player.dto.PlayerListDto;
import core.player.entity.BelongType;
import core.player.entity.PlayerEntity;
import core.player.repository.PlayerRepository;
import core.player.service.PlayerService;
import core.team.dto.TeamDto;
import core.team.entity.TeamEntity;
import core.team.repository.TeamRepository;
import core.team.service.TeamService;
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
	public void register() throws Exception {
		
		// given
		TeamDto tDto = new TeamDto("team1","Seoul",BelongType.ELEMENTARY_SCHOOL,"Team1 입니다.");
		PlayerDto pDto = new PlayerDto("player1","220507-1111111",1,tDto);
		String content = new ObjectMapper().writeValueAsString(pDto);
		
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
		log.info("=============================== Register test Start ===============================");
	}
	
	@Test
	public void registersTest() throws Exception {
		// given
		List<PlayerDto> playerList = new ArrayList<>();
		TeamDto team = new TeamDto("team1","Seoul",BelongType.ELEMENTARY_SCHOOL,"Team1 입니다.");
		PlayerDto player1 = new PlayerDto("player1","220516-1111111",1,team);
		PlayerDto player2 = new PlayerDto("player2","220516-2222222",2,team);
		PlayerDto player3 = new PlayerDto("player3","220516-3333333",3,team);
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		PlayerListDto dtoList = new PlayerListDto();
		dtoList.setPlayers(playerList);
		String content = new ObjectMapper().writeValueAsString(dtoList);
		
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
	public void findAllTest() throws Exception{
		log.info("=============================== findAllTest test Start ===============================");
		//given
		List<PlayerDto> playerDtoList = new ArrayList<>();
		playerDtoList.add(new PlayerDto("player1","220507-1111111",1,null));
		playerDtoList.add(new PlayerDto("player2","220507-2222222",2,null));
		playerDtoList.add(new PlayerDto("player3","220507-3333333",3,null));
		playerDtoList.add(new PlayerDto("player4","220507-4444444",4,null));
		playerDtoList.add(new PlayerDto("player5","220507-5555555",5,null));
		playerDtoList.add(new PlayerDto("player6","220507-6666666",6,null));
		playerDtoList.add(new PlayerDto("player7","220507-7777777",7,null));
		playerDtoList.add(new PlayerDto("player8","220507-8888888",8,null));
		playerDtoList.add(new PlayerDto("player9","220507-9999999",9,null));
		playerDtoList.add(new PlayerDto("player10","220507-0101010",10,null));
		playerDtoList.add(new PlayerDto("player11","220507-0111111",11,null));
		playerDtoList.add(new PlayerDto("player12","220507-0121212",12,null));
		playerDtoList.add(new PlayerDto("player13","220507-0131313",13,null));
		playerDtoList.add(new PlayerDto("player14","220507-0141414",14,null));
		playerDtoList.add(new PlayerDto("player15","220507-0151515",15,null));
	
		List<PlayerDto> playerList =  playerService.registerPlayers(playerDtoList);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/players/all?page={page}&size={size}",0,10)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$",Matchers.hasSize(10)))
			.andExpect(jsonPath("$.[2].playerName").value("player3"))
			.andDo(MockMvcResultHandlers.print());
		log.info("=============================== findAllTest test End ===============================");
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
		list.add(new PlayerDto("player6","220530-1111111",4,team2));
		list.add(new PlayerDto("player7","220530-1111111",5,team2));
		list.add(new PlayerDto("player8","220530-1111111",3,team1));
		list.add(new PlayerDto("player9","220530-1111111",6,team2));
		rtnList.add(new PlayerDto("player6","220530-1111111",4,team2));
		rtnList.add(new PlayerDto("player7","220530-1111111",5,team2));
		rtnList.add(new PlayerDto("player9","220530-1111111",6,team2));

		// 모든 Player를 등록
		playerService.registerPlayers(list);
		
		PageRequest pageRequest = PageRequest.of(1, 3);
		
		// when
		ResultActions resultAction = mockMvc.perform(get("/players?teamName={teamName}&page={page}&size={size}","team2","1","3")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$",hasSize(3)))
			.andExpect(jsonPath("$.[0].playerName").value("player6"))
			.andExpect(jsonPath("$.[1].uniformNo").value("5"))
			.andExpect(jsonPath("$.[2].team.teamName").value("team2"))
			.andDo(MockMvcResultHandlers.print());
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
		
		// Return될 ID값 미리 매핑해 놓기
		playerDtoList.get(0).setId(1L);
		playerDtoList.get(1).setId(2L);
		playerDtoList.get(2).setId(3L);
		
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
		
		ResultActions resultAction = mockMvc.perform(patch("/player/{id}",id)
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
		
		
		// Return될 ID값 미리 매핑해 놓기
		players.get(0).setId(1L);
		players.get(1).setId(2L);
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
	
	@Test
	@DisplayName("가입 신청하기")
	public void requestPlayerJoin() throws Exception {
		// given
		TeamDto team 	= new TeamDto("team1","Seoul",BelongType.CLUB,"Our team is the best"); 
		PlayerDto player = new PlayerDto("player1","220713-1111111",1,team);
		JoinDto join = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,1L);
		teamService.registerTeam(team);
		playerService.registerPlayer(player);
		
		String content = new ObjectMapper().writeValueAsString(join);
		
		// when
		ResultActions resultAction = mockMvc.perform(post("/player/{id}/request-join",1)
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
	public void rejectPlayerJoin() throws Exception{
		// given
		TeamDto team 	= new TeamDto("team","Seoul",BelongType.CLUB,"Our team is the best"); 
		PlayerDto player = new PlayerDto("player","220713-1111111",1,team);
		teamService.registerTeam(team);
		playerService.registerPlayer(player);
		Long playerId = 1L;
		Long teamId = 1L;
		JoinDto requestJoin = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,playerId,teamId);
		joinService.requestTeamJoin(teamId, requestJoin);
		
		JoinDto rejectJoin = new JoinDto(1L,1L,"player",1L,"team",RequesterType.TEAM,StatusType.REJECT,'Y',LocalDateTime.now(),LocalDateTime.now());
		String content = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(rejectJoin);
		
		// when
		ResultActions resultAction = mockMvc.perform(patch("/player/{id}/reject-join/{teamId}",playerId,teamId)
											.contentType(MediaType.APPLICATION_JSON_UTF8)
											.content(content)
											.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.requesterType").value("TEAM"))
				.andExpect(jsonPath("$.statusType").value("REJECT"))
				.andExpect(jsonPath("$.playerId").value(1L))
				.andExpect(jsonPath("$.playerName").value("player"))
				.andExpect(jsonPath("$.teamId").value(1L))
				.andExpect(jsonPath("$.teamName").value("team"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("가입제안 승인하기")
	public void approvePlayerJoin() throws Exception{
		// given
		Long playerId = 1L;
		Long teamId = 2L;
		
		TeamDto team = new TeamDto("sparta","space",BelongType.CLUB,"Hello our club is too strong");
		PlayerDto player = new PlayerDto("hulk","20220920-1111111",1,team);
		
		teamService.registerTeam(team);
		playerService.registerPlayer(player);
		
		JoinDto approveJoin = new JoinDto(1L,1L,"player",2L,"team",RequesterType.TEAM,StatusType.APPROVAL,'Y',LocalDateTime.now(),LocalDateTime.now()); 
		joinService.requestTeamJoin(teamId, approveJoin);
		
		String content = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(approveJoin);
		
		// when
		ResultActions resultAction = mockMvc.perform(patch("/player/{id}/approve-join/{teamId}",playerId,teamId)
											.contentType(MediaType.APPLICATION_JSON_UTF8)
											.content(content)
											.accept(MediaType.APPLICATION_JSON_UTF8));
		// then
		resultAction
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.requesterType").value("TEAM"))
				.andExpect(jsonPath("$.statusType").value("APPROVAL"))
				.andExpect(jsonPath("$.playerId").value(1L))
				.andExpect(jsonPath("$.playerName").value("hulk"))
				.andExpect(jsonPath("$.teamId").value(2L))
				.andExpect(jsonPath("$.teamName").value("sparta"))
				.andDo(MockMvcResultHandlers.print());
	}
}
