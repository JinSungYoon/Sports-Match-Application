package core.player.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;


import core.player.MockEntity;
import core.player.dto.PlayerDto;
import core.player.entity.BelongType;
import core.player.entity.PlayerEntity;
import core.player.repository.PlayerRepository;
import core.team.dto.TeamDto;
import core.team.entity.TeamEntity;
import core.team.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;

/*
 * 단위 테스트(Service와 관련된 Bean들만 메모리에 올린다.)
 * playerRepositry -> 가짜 객체로 만들 수 있음.
 * */

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplUnitTest {

	@InjectMocks	// Service객체가 만들어질 때 해당 파일에 @Mock로 등록된 모든 bean을 주입받는다.
	private PlayerServiceImpl playerService;
	
	@Mock
	private PlayerRepository playerRepository;
	
	@Mock
	private TeamRepository teamRepository;
		
	@Test
	@DisplayName("Player 생성 테스트")
	public void registerPlayerTest() {
		// given
		TeamDto team = new TeamDto("fakeTeam","Earth",BelongType.CLUB,"Fake team 입니다");
		PlayerDto player = new PlayerDto("fakePlayer","111111-1111111",0,team);
		
		// mocking
		when(playerRepository.save(any())).thenReturn(player.toEntity());
		
		// when
		PlayerDto newPlayer = playerService.registerPlayer(player);
		
		//then
		Assertions.assertThat(newPlayer.getPlayerName()).isEqualTo(player.getPlayerName());
		Assertions.assertThat(newPlayer.getResRegNo()).isEqualTo(player.getResRegNo());
		Assertions.assertThat(newPlayer.getUniformNo()).isEqualTo(player.getUniformNo());
		
	}
	
	@Test
	@DisplayName("한명의 플레이어 찾기")
	public void searchOnePlayer() {
		// given
		TeamDto team = new TeamDto("fakeTeam","Earth",BelongType.CLUB,"Fake team 입니다");
		PlayerDto player = new PlayerDto("fakePlayer","111111-1111111",0,team);
		
		// mocking
		when(playerRepository.findById(any())).thenReturn(Optional.of(player.toEntity()));
		
		// when
		PlayerDto findPlayer = playerService.searchOnePlayer(1L);
		
		// then
		assertThat(findPlayer.getPlayerName()).isEqualTo(player.getPlayerName());
		assertThat(findPlayer.getResRegNo()).isEqualTo(player.getResRegNo());
		assertThat(findPlayer.getUniformNo()).isEqualTo(player.getUniformNo());
		assertThat(findPlayer.getTeam()).isEqualTo(player.getTeam());
	}
	
	@Test
	@DisplayName("존재하지 않는 Player찾기")
	public void searchNotExistPlayer() {
		// given
		TeamDto team = new TeamDto("fakeTeam","Earth",BelongType.CLUB,"Fake team 입니다");
		PlayerDto player = new PlayerDto("fakePlayer","111111-1111111",0,team);
		when(playerRepository.save(any())).thenReturn(player.toEntity());
		playerService.registerPlayer(player);
		when(playerRepository.findById(3L)).thenThrow(new IllegalArgumentException("Id가 존재하지 않습니다."));
		
		// when
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,()->playerService.searchOnePlayer(3L)); 
		
		// then
		assertEquals("Id가 존재하지 않습니다.",exception.getMessage());
	}
	
	@Test
	@DisplayName("Player 전체 조회")
	public void searchPlayerAll() throws Exception {
		// given
		List<PlayerDto> list = new ArrayList<>();
		TeamDto team = new TeamDto("fakeTeam","Earth",BelongType.CLUB,"Fake team 입니다");
		PlayerDto player1 = new PlayerDto("fakePlayer","111111-1111111",1,team);
		PlayerDto player2 = new PlayerDto("fakePlayer","111111-2222222",2,team);
		PlayerDto player3 = new PlayerDto("fakePlayer","111111-3333333",3,team);
		PlayerDto player4 = new PlayerDto("fakePlayer","111111-4444444",4,team);
		list.add(player1);
		list.add(player2);
		list.add(player3);
		list.add(player4);
		when(playerRepository.saveAll(any())).thenReturn(list.stream().map(PlayerDto::toEntity).collect(Collectors.toList()));
		playerService.registerPlayers(list);
		when(playerRepository.findAll()).thenReturn(list.stream().map(PlayerDto::toEntity).collect(Collectors.toList()));
		// when
		List<PlayerDto> playerList = playerService.searchPlayerAll();
		
		IndexOutOfBoundsException outboundException = assertThrows(IndexOutOfBoundsException.class,()->playerList.get(4));
		
		assertThat(playerList.get(0)).isEqualTo(player1);
		assertThat(playerList.get(1)).isEqualTo(player2);
		assertThat(playerList.get(2)).isEqualTo(player3);
		assertThat(playerList.get(3)).isEqualTo(player4);
		
		assertEquals("Index 4 out of bounds for length 4",outboundException.getMessage());
		
	}
	
	@Test
	@DisplayName("여러명의 Player 가입하기")
	public void registersPlayer() throws Exception {
		// given
		List<PlayerDto> list = new ArrayList<>();
		TeamDto team = new TeamDto("fakeTeam","Earth",BelongType.CLUB,"Fake team 입니다");
		PlayerDto player1 = new PlayerDto("fakePlayer","111111-1111111",1,team);
		PlayerDto player2 = new PlayerDto("fakePlayer","111111-2222222",2,team);
		PlayerDto player3 = new PlayerDto("fakePlayer","111111-3333333",3,team);
		PlayerDto player4 = new PlayerDto("fakePlayer","111111-4444444",4,team);
		list.add(player1);
		list.add(player2);
		list.add(player3);
		list.add(player4);
		when(playerRepository.saveAll(any())).thenReturn(list.stream().map(PlayerDto::toEntity).collect(Collectors.toList()));
		
		// when
		List<PlayerDto> rtnPlayers = playerService.registerPlayers(list);
		
		// then
		assertThat(rtnPlayers.get(0)).isEqualTo(player1);
		assertThat(rtnPlayers.get(1)).isEqualTo(player2);
		assertThat(rtnPlayers.get(2)).isEqualTo(player3);
		assertThat(rtnPlayers.get(3)).isEqualTo(player4);
		
	}
	
	@Test
	@DisplayName("Player 업데이트 하기")
	public void updatePlayer() {
		// given
		TeamDto team = new TeamDto("fakeTeam","Earth",BelongType.CLUB,"Fake team 입니다");
		PlayerDto originPlayer = new PlayerDto("fakePlayer","111111-1111111",1,team);
		when(playerRepository.save(any())).thenReturn(originPlayer.toEntity());
		PlayerDto rtnPlayer = playerService.registerPlayer(originPlayer);
		when(playerRepository.findById(1L)).thenReturn(Optional.of(originPlayer.toEntity()));
		rtnPlayer.setPlayerName("fakePlayer5");
		rtnPlayer.setUniformNo(5);
		rtnPlayer.setResRegNo("555555-5555555");
		
		// when
		PlayerDto updatePlayer =  playerService.updatePlayer(1L, rtnPlayer);
		
		// then
		assertThat(updatePlayer.getPlayerName()).isEqualTo(rtnPlayer.getPlayerName());
		assertThat(updatePlayer.getResRegNo()).isEqualTo(rtnPlayer.getResRegNo());
		assertThat(updatePlayer.getUniformNo()).isEqualTo(rtnPlayer.getUniformNo());
		
	}
	
	@Test
	@DisplayName("Player 정보 제거하기")
	public void deletePlyaer() {
		// given
		TeamDto team = new TeamDto("fakeTeam","Earth",BelongType.CLUB,"Fake team 입니다");
		PlayerDto originPlayer = new PlayerDto("fakePlayer","111111-1111111",1,team);
		when(playerRepository.save(any())).thenReturn(originPlayer.toEntity());
		playerService.registerPlayer(originPlayer);
		
		// when
		Long rtnId = playerService.deletePlyaer(1L);
		
		// then
		assertThat(rtnId).isEqualTo(1L);
	}
		
}
