package core.join.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.join.dto.JoinDto;
import core.join.dto.JoinSearchCondition;
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
		
		JoinDto proposal = new JoinDto(StatusType.PROPOSAL,RequesterType.PLAYER,1L,1L);
		JoinEntity expectJoinEntity = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player,rTeam);
		expectJoinEntity.initId(1L);
		JoinDto expectJoinDto = new JoinDto(StatusType.PROPOSAL,RequesterType.PLAYER,1L,1L);
		
		List<JoinDto> expectList = new ArrayList<>();
		expectList.add(expectJoinDto);
		
		Page<JoinDto> expectPage = new PageImpl(expectList,PageRequest.of(0,1),expectList.size());
		
		// mocking
//		when(joinRepository.save(any())).thenReturn(expectJoinEntity);
		when(joinRepository.save(any())).thenReturn(new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player,rTeam));
		when(joinRepositoryCustom.findPlayerJoinApplication(StatusType.PROPOSAL, 1L, 1L, PageRequest.of(0, 1))).thenReturn(expectPage);
		
		// when
		JoinDto rtnJoin = joinService.requestJoin(proposal);
		
		// then
		Assertions.assertThat(rtnJoin).isEqualTo(expectJoinDto);
	}
	
	@Test
	@DisplayName("기등록 신청 예외 테스트")
	void requestJoinException() throws Exception {
		// given
		TeamEntity rTeam = new TeamEntity("redTeam","Busan",BelongType.CLUB,"We are Red team");
		TeamEntity gTeam = new TeamEntity("greenTeam","Wonju",BelongType.CLUB,"We are Green team");
		PlayerEntity player = new PlayerEntity("apple","220619-1111111",1,gTeam);
		
		rTeam.initId(1L);
		gTeam.initId(2L);
		player.initId(1L);
		
		JoinDto proposal1 = new JoinDto(StatusType.PROPOSAL,RequesterType.PLAYER,1L,1L);
		JoinDto proposal2 = new JoinDto(StatusType.PROPOSAL,RequesterType.PLAYER,1L,1L);
		JoinEntity expectJoin1 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player,rTeam);
		JoinEntity expectJoin2 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player,rTeam);
		
		expectJoin1.initId(1L);
		expectJoin2.initId(2L);
		PageRequest pageRequest = PageRequest.of(0,1);
		List<JoinDto> joinList = new ArrayList<>();
		joinList.add(expectJoin1.toDto());
		Page<JoinDto> expectPage = new PageImpl<>(joinList,pageRequest,joinList.size());
		
		// mocking
		when(joinRepositoryCustom.findPlayerJoinApplication(StatusType.PROPOSAL, 1L, 1L,pageRequest)).thenReturn(expectPage);
		when(joinRepository.save(any())).thenReturn(expectJoin1);
		JoinDto rtnJoin1 = joinService.requestJoin(proposal1);
		joinList.add(proposal1);
		when(joinRepositoryCustom.findPlayerJoinApplication(StatusType.PROPOSAL, 1L, 1L,pageRequest)).thenReturn(expectPage);
		when(joinRepository.save(any())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"이미 요청한 제안입니다.",new Exception()));
		
		// when
		Exception existProposal = assertThrows(Exception.class,()->joinService.requestJoin(proposal2));
		
		// then
		org.junit.jupiter.api.Assertions.assertEquals("400 BAD_REQUEST "+'"'+"이미 요청한 제안입니다."+'"'+"; nested exception is java.lang.Exception", existProposal.getMessage());
		
	}
	
	@Test
	@DisplayName("Join요청 조회")
	void searchJoinTest() {
		// given
		TeamEntity rTeam = new TeamEntity("redTeam","Busan",BelongType.CLUB,"We are Red team");
		TeamEntity gTeam = new TeamEntity("greenTeam","Wonju",BelongType.UNIVERSITY,"We are Green team");
		TeamEntity pTeam = new TeamEntity("purpleTeam","Seoul",BelongType.CLUB,"We are Purple team");
		TeamEntity yTeam = new TeamEntity("yellowTeam","Suwon",BelongType.PROTEAM,"We are Yellow team");
		
		PlayerEntity apple = new PlayerEntity("apple","220619-1111111",1,gTeam);
		PlayerEntity graph = new PlayerEntity("graph","220619-1111111",1,gTeam);
		PlayerEntity banana = new PlayerEntity("banana","220619-1111111",1,gTeam);
		
		rTeam.initId(1L);
		gTeam.initId(2L);
		pTeam.initId(3L);
		yTeam.initId(4L);
		apple.initId(1L);
		graph.initId(2L);
		banana.initId(3L);
		
		JoinEntity join1 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,apple,rTeam);
		join1.initId(1L);
		JoinEntity join2 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,graph,pTeam);
		join2.initId(2L);
		JoinEntity join3 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,graph,yTeam);
		join2.initId(3L);
		
		PageRequest page = PageRequest.of(0,10);
		List<JoinDto> expectList = new ArrayList<>();
		expectList.add(join1.toDto());
		expectList.add(join2.toDto());
		expectList.add(join3.toDto());
		Page<JoinDto> expectPage = new PageImpl<>(expectList,page,expectList.size());
		
		JoinSearchCondition condition = new JoinSearchCondition(StatusType.PROPOSAL,RequesterType.PLAYER,null,null,' ',null,null);
		
		when(joinRepositoryCustom.findPlayerJoinApplication(condition.getStatusType(), condition.getPlayerId(), condition.getTeamId(), page)).thenReturn(expectPage);
		
		Page<JoinDto> rtnList = joinService.searchPlayerJoinApplication(condition, page);
		
		Assertions.assertThat(rtnList).isEqualTo(expectPage);
		
	}
}
