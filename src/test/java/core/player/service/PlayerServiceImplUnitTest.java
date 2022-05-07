package core.player.service;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import core.player.MockEntity;
import core.player.dto.PlayerDto;
import core.player.dto.TeamDto;
import core.player.entity.BelongType;
import core.player.entity.PlayerEntity;
import core.player.entity.TeamEntity;
import core.player.repository.PlayerRepository;
import core.player.repository.TeamRepository;


@ExtendWith(MockitoExtension.class)
//@SpringBootTest
public class PlayerServiceImplTest {

	@InjectMocks
	//@Autowired
	private PlayerServiceImpl playerService;
	
	@Mock
	//@Autowired
	private PlayerRepository playerRepository;
	
	@Mock
	//@Autowired
	private TeamRepository teamRepository;
	
	@Test
	@Rollback(false)
	@DisplayName("Player 생성 테스트")
	public void createPlayerTest() {
		
		// given
		TeamDto team = new TeamDto("team1","Seoul",BelongType.CLUB,"팀 등록합니다.");
		PlayerDto player = new PlayerDto("player1","111111-1111111",1,team);
		
		TeamEntity mockTeam = MockEntity.mock(TeamEntity.class,1L);
		mockTeam.updateInfo("fakeTeam", "Earth", BelongType.CLUB, "Fake team 입니다");
		
		PlayerEntity mockPlayer = MockEntity.mock(PlayerEntity.class, 1L);
		mockPlayer.updateInfo("fakePlayeer", "111111-1111111", 0, mockTeam);
		
		
		// mocking
		given(playerRepository.save(mockPlayer)).willReturn(mockPlayer);
		given(playerRepository.findById(1L)).willReturn(Optional.ofNullable(mockPlayer));
		
		// when
		Long newPlayerId = playerService.registerPlayer(player);
		System.out.println(newPlayerId);
		//then
//		PlayerEntity newPlayer = playerRepository.findById(newPlayerId).orElse(null);
//		TeamEntity newTeam = teamRepository.findById(newPlayer.getTeam().getId()).orElse(null);
//		
//		
//		
//		Assertions.assertThat(newTeam.id).isEqualTo(1L);
//		Assertions.assertThat(newTeam.getTeamName()).isEqualTo("fakeTeam");
//		Assertions.assertThat(newTeam.getLocation()).isEqualTo("Earth");
//		Assertions.assertThat(newTeam.getBelongType()).isEqualTo(BelongType.CLUB);
//		Assertions.assertThat(newTeam.getIntroduction()).isEqualTo("Fake team 입니다");
		
	}
		
}
