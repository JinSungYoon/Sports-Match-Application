package core.team.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import core.player.entity.BelongType;
import core.team.dto.TeamDto;
import core.team.repository.TeamRepository;
import core.team.repository.TeamRepositoryImpl;
import lombok.extern.slf4j.Slf4j;

/*
 * 단위 테스트(Service와 관련된 Bean들만 메모리에 올린다.)
 * playerRepositry -> 가짜 객체로 만들 수 있음.
 * */

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TeamServiceUnitTest {
	
	@Mock
	private TeamRepository teamRepository;
	
	@Mock
	private TeamRepositoryImpl teamRepositoryImpl;
	
	@InjectMocks	// Service객체가 만들어질 때 해당 파일에 @Mock로 등록된 모든 bean을 주입받는다.
	private TeamServiceImpl teamService;
	
	
	@Test
	@DisplayName("팀 정보로 찾기")
	public void findTeamTest() {
		// given
		List<TeamDto> list = new ArrayList<>();
		List<TeamDto> rtnList = new ArrayList<>();
		TeamDto team1 = new TeamDto("team1","서울시 송파구",BelongType.CLUB,"서울시에 40,50대로 구성된 팀입니다.");
		TeamDto team2 = new TeamDto("team2","경기도 분당구",BelongType.CLUB,"경기도에 20,30대로 구성된 팀입니다.");
		TeamDto team3 = new TeamDto("team3","서울시 은평구",BelongType.CLUB,"서울시에 30,40대로 구성된 팀입니다.");
		TeamDto team4 = new TeamDto("team4","경기도 동안구",BelongType.CLUB,"서울시에 20,30대로 구성된 팀입니다.");
		TeamDto team5 = new TeamDto("team5","서울시 구로구",BelongType.CLUB,"서울시에 30,40대로 구성된 팀입니다.");
		rtnList.add(team1);
		rtnList.add(team3);
		rtnList.add(team5);
		PageRequest pageRequest = PageRequest.of(0, 2);
		when(teamRepositoryImpl.findTeam(null, "서울시", null, null,pageRequest)).thenReturn(rtnList.stream().map(TeamDto::toEntity).collect(Collectors.toList()));
		// when
		List<TeamDto> expectDto = teamService.searchTeams(null, "서울시", null, null,pageRequest);
		// then
		assertThat(expectDto.size()).isEqualTo(3);
		assertThat(expectDto.get(0)).isEqualTo(rtnList.get(0));
		assertThat(expectDto.get(1)).isEqualTo(rtnList.get(1));
		assertThat(expectDto.get(2)).isEqualTo(rtnList.get(2));
		IndexOutOfBoundsException outboundException = assertThrows(IndexOutOfBoundsException.class,()->expectDto.get(3));
		
		assertEquals("Index 3 out of bounds for length 3",outboundException.getMessage());
		
	}
	
	@Test
	@DisplayName("모든 팀 조회")
	void findAllTeamTest() {
		// given
		List<TeamDto> list = new ArrayList<>();
		List<TeamDto> rtnList = new ArrayList<>();
		TeamDto team1 = new TeamDto("team1","서울시 송파구",BelongType.CLUB,"서울시에 40,50대로 구성된 팀입니다.");
		TeamDto team2 = new TeamDto("team2","경기도 분당구",BelongType.PROTEAM,"경기도에 20,30대로 구성된 팀입니다.");
		TeamDto team3 = new TeamDto("team3","서울시 은평구",BelongType.UNIVERSITY,"서울시에 30,40대로 구성된 팀입니다.");
		TeamDto team4 = new TeamDto("team4","경기도 동안구",BelongType.CLUB,"경기도에 20,30대로 구성된 팀입니다.");
		TeamDto team5 = new TeamDto("team5","서울시 구로구",BelongType.PROTEAM,"서울시에 30,40대로 구성된 팀입니다.");
		TeamDto team6 = new TeamDto("team6","인천시 계양구",BelongType.UNIVERSITY,"인천시에 40,50대로 구성된 팀입니다.");
		TeamDto team7 = new TeamDto("team7","부산시 해운대구",BelongType.CLUB,"부산시에 20,30대로 구성된 팀입니다.");
		TeamDto team8 = new TeamDto("team8","서울시 서대문구",BelongType.PROTEAM,"서울시에 30,40대로 구성된 팀입니다.");
		TeamDto team9 = new TeamDto("team9","경기도 장안구",BelongType.UNIVERSITY,"경기도에 20,30대로 구성된 팀입니다.");
		TeamDto team10 = new TeamDto("team10","인천시 미추홀구",BelongType.CLUB,"인천시에 30,40대로 구성된 팀입니다.");
		rtnList.add(team4);
		rtnList.add(team5);
		rtnList.add(team6);
		
		// when 
		PageRequest pageRequest = PageRequest.of(1, 3);
		when(teamRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(rtnList.stream().map(TeamDto::toEntity).collect(Collectors.toList()),pageRequest,rtnList.size()));
		List<TeamDto> expectDtoList =  teamService.searchAllTeams(pageRequest);
		System.out.println(expectDtoList.toString());
		// then
		assertEquals(expectDtoList.size(),3);
		assertThat(expectDtoList.get(1).getTeamName()).isEqualTo("team5");
		assertThat(expectDtoList.get(2).getLocation()).isEqualTo("인천시 계양구");
		assertThat(expectDtoList.get(0).getBelongType()).isEqualTo(BelongType.CLUB);
		assertThat(expectDtoList.get(1).getIntroduction()).isEqualTo("서울시에 30,40대로 구성된 팀입니다.");
		
	}
	
	
}
