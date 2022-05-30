package core.player.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
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

import core.common.encryption.AES256Util;
import core.player.dto.PlayerDto;
import core.player.entity.BelongType;
import core.player.repository.PlayerRepository;
import core.player.repository.PlayerRepositoryCustom;
import core.team.dto.TeamDto;
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
	private PlayerRepositoryCustom playerRepositoryCustom;
	
	@Mock
	private TeamRepository teamRepository;
	
	@Mock
	private AES256Util aes256Util;
		
	@Test
	@DisplayName("Player 생성 테스트")
	public void registerPlayerTest() throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		// given
		// 주민번호 암호화
		String key = "verystrongsecretkey";
		AES256Util aes256 = new AES256Util(key);
		String encryptResRegNo = aes256.encrypt("111111-1111111");
		// Player 객체 생성
		TeamDto team = new TeamDto("fakeTeam","Earth",BelongType.CLUB,"Fake team 입니다");
		PlayerDto player = new PlayerDto("fakePlayer","111111-1111111",0,team);
		
		// mocking
		when(playerRepository.save(any())).thenReturn(new PlayerDto("fakePlayer",encryptResRegNo,0,team).toEntity());
		
		// when
		PlayerDto newPlayer = playerService.registerPlayer(player);
		log.info(newPlayer.getResRegNo());

		//then
		Assertions.assertThat(newPlayer.getPlayerName()).isEqualTo(player.getPlayerName());
		Assertions.assertThat(newPlayer.getResRegNo()).isEqualTo(encryptResRegNo);
		Assertions.assertThat(newPlayer.getUniformNo()).isEqualTo(player.getUniformNo());
		
	}
	
	@Test
	@DisplayName("한명의 플레이어 찾기")
	public void searchOnePlayer() throws UnsupportedEncodingException,NoSuchAlgorithmException, GeneralSecurityException {
		// given
		// 주민번호 암호화
		String key = "verystrongsecretkey";
		AES256Util aes256 = new AES256Util(key);
		String encryptResRegNo = aes256.encrypt("111111-1111111");
		// Player 객체생성
		TeamDto team = new TeamDto("fakeTeam","Earth",BelongType.CLUB,"Fake team 입니다");
		PlayerDto player = new PlayerDto("fakePlayer","111111-1111111",0,team);
		
		// mocking
		when(playerRepository.findById(any())).thenReturn(Optional.of(new PlayerDto("fakePlayer",encryptResRegNo,0,team).toEntity()));
		
		// when
		PlayerDto findPlayer = playerService.searchOnePlayer(1L);
		
		// then
		assertThat(findPlayer.getPlayerName()).isEqualTo(player.getPlayerName());
		assertThat(findPlayer.getResRegNo()).isEqualTo(encryptResRegNo);
		assertThat(findPlayer.getUniformNo()).isEqualTo(player.getUniformNo());
		assertThat(findPlayer.getTeam()).isEqualTo(player.getTeam());
	}
	
	@Test
	@DisplayName("존재하지 않는 Player찾기")
	public void searchNotExistPlayer() throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		// given
		// 주민번호 암호화
		String key = "verystrongsecretkey";
		AES256Util aes256 = new AES256Util(key);
		String encryptResRegNo = aes256.encrypt("111111-1111111");
		// Player 객체생성
		TeamDto team = new TeamDto("fakeTeam","Earth",BelongType.CLUB,"Fake team 입니다");
		PlayerDto player = new PlayerDto("fakePlayer","111111-1111111",0,team);
		when(playerRepository.save(any())).thenReturn(new PlayerDto("fakePlayer",encryptResRegNo,0,team).toEntity());
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
		// 주민번호 암호화
		String key = "verystrongsecretkey";
		AES256Util aes256 = new AES256Util(key);
		// Player List 객체 생성
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
	@DisplayName("Player 조건 검색")
	void findPlayer() throws UnsupportedEncodingException {
		// given
		// 주민번호 암호화
		String key = "verystrongsecretkey";
		AES256Util aes256 = new AES256Util(key);
		// Player List 객체 생성
		List<PlayerDto> list = new ArrayList<>();
		TeamDto team1 = new TeamDto("fakeTeam1","Earth",BelongType.CLUB,"Fake team1 입니다");
		TeamDto team2 = new TeamDto("fakeTeam2","Space",BelongType.PROTEAM,"Fake team2 입니다");
		PlayerDto player1 = new PlayerDto("bluePlayer","111111-1111111",1,team1);
		PlayerDto player2 = new PlayerDto("skyBluePlayer","111111-2222222",1,team2);
		PlayerDto player3 = new PlayerDto("pinkPlayer","111111-3333333",2,team2);
		PlayerDto player4 = new PlayerDto("orangeYellowPlayer","111111-4444444",2,team1);
		PlayerDto player5 = new PlayerDto("redPlayer","111111-5555555",3,team1);
		list.add(player1);
		list.add(player4);
		list.add(player5);

		// when
		when(playerRepositoryCustom.findPlayer(null, null, "fakeTeam1")).thenReturn(list.stream().map(PlayerDto::toEntity).collect(Collectors.toList()));
		List<PlayerDto> rtnList = playerService.searchPlayers(null, null, "fakeTeam1");
		
		assertThat(rtnList.size()).isEqualTo(3);
		assertThat(rtnList.get(0).getPlayerName()).isEqualTo("bluePlayer");
		assertThat(rtnList.get(1).getUniformNo()).isEqualTo(2);
		assertThat(rtnList.get(2).getTeam().getTeamName()).isEqualTo("fakeTeam1");
		
	}
	
	@Test
	@DisplayName("여러명의 Player 가입하기")
	public void registersPlayer() throws Exception {
		// given
		// 주민번호 암호화
		String key = "verystrongsecretkey";
		AES256Util aes256 = new AES256Util(key);
		// Player 객체 생성
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
		
		for(PlayerDto item : list) {
			item.setResRegNo(aes256.encrypt(item.getResRegNo()));  
		}
		
		when(playerRepository.saveAll(any())).thenReturn(list.stream().map(PlayerDto::toEntity).collect(Collectors.toList()));
		
		// when
		List<PlayerDto> rtnPlayers = playerService.registerPlayers(list);
		
		log.info(rtnPlayers.toString());
		
		// then
		assertThat(rtnPlayers.get(0)).isEqualTo(new PlayerDto("fakePlayer",aes256.encrypt("111111-1111111"),1,team));
		assertThat(rtnPlayers.get(1)).isEqualTo(new PlayerDto("fakePlayer",aes256.encrypt("111111-2222222"),2,team));
		assertThat(rtnPlayers.get(2)).isEqualTo(new PlayerDto("fakePlayer",aes256.encrypt("111111-3333333"),3,team));
		assertThat(rtnPlayers.get(3)).isEqualTo(new PlayerDto("fakePlayer",aes256.encrypt("111111-4444444"),4,team));
		
	}
	
	@Test
	@DisplayName("Player 업데이트 하기")
	public void updatePlayer() throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		// given
		// 주민번호 암호화
		String key = "verystrongsecretkey";
		AES256Util aes256 = new AES256Util(key);
		// Player 객체 생성
		TeamDto team = new TeamDto("fakeTeam","Earth",BelongType.CLUB,"Fake team 입니다");
		PlayerDto originPlayer = new PlayerDto("fakePlayer",aes256.encrypt("111111-1111111"),1,team);
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
	public void deletePlyaer() throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
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
