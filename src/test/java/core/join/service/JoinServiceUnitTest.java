package core.join.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;

import core.common.entity.MockEntity;
import core.join.dto.JoinDto;
import core.join.entity.JoinEntity;
import core.join.entity.RequesterType;
import core.join.entity.StatusType;
import core.join.repository.JoinRepository;
import core.join.repository.JoinRepositoryCustom;
import core.player.entity.BelongType;
import core.player.entity.PlayerEntity;
import core.player.repository.PlayerRepository;
import core.team.entity.TeamEntity;
import core.team.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class JoinServiceUnitTest {
	
	@InjectMocks
	private JoinServiceImpl joinService;
	
	@Mock
	private PlayerRepository playerRepository;
	
	@Mock
	private TeamRepository teamRepository;
	
	@Mock
	private JoinRepository joinRepository;
	
	@Mock
	private JoinRepositoryCustom joinRepositoryCustom;
	
	@Test
	@DisplayName("등록 신청 테스트")
	void requestJoin() {
		// given
		TeamEntity rTeam = new TeamEntity("redTeam","Busan",BelongType.CLUB,"We are Red team");
		TeamEntity gTeam = new TeamEntity("greenTeam","Wonju",BelongType.CLUB,"We are Green team");
		PlayerEntity player = new PlayerEntity("apple","220619-1111111",1,gTeam);
		
		rTeam.initId(1L);
		gTeam.initId(2L);
		player.initId(1L);
		
		JoinDto proposal = new JoinDto(StatusType.PROPOSAL,RequesterType.Player,1L,1L);
		JoinEntity expectJoin = new JoinEntity(StatusType.PROPOSAL,RequesterType.Player,player,rTeam,'Y');
		expectJoin.initId(1L);
		
		// mocking
		when(joinRepository.save(any())).thenReturn(expectJoin);
		
		// when
		JoinDto rtnJoin = joinService.requestJoin(proposal);
		
		// then
		Assertions.assertThat(rtnJoin.getPlayerId()).isEqualTo(1L);
		Assertions.assertThat(rtnJoin.getTeamId()).isEqualTo(1L);
		Assertions.assertThat(rtnJoin.getRequesterType()).isEqualTo(RequesterType.Player);
		Assertions.assertThat(rtnJoin.getStatusType()).isEqualTo(StatusType.PROPOSAL);
	}
}
