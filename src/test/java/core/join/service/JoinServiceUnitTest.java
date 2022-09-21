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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	@DisplayName("Player 가입 신청 테스트")
	void requestPlayerJoin() {
		// given
		TeamEntity rTeam = new TeamEntity("redTeam","Busan",BelongType.CLUB,"We are Red team");
		TeamEntity gTeam = new TeamEntity("greenTeam","Wonju",BelongType.CLUB,"We are Green team");
		PlayerEntity player = new PlayerEntity("apple","220619-1111111",1,gTeam);
		
		rTeam.initId(1L);
		gTeam.initId(2L);
		player.initId(1L);
		
		JoinDto proposal = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,1L);
		
		List<JoinDto> searhList = new ArrayList<>();
		
		Page<JoinDto> searchPage = new PageImpl(searhList,PageRequest.of(0,1),0);
		
		// mocking
		when(teamRepository.findById(1L)).thenReturn(Optional.of(rTeam));
		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findPlayerJoinApplication(StatusType.PROPOSAL, 1L, PageRequest.of(0, 1))).thenReturn(searchPage);
		when(joinRepository.save(any())).thenReturn(new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player,rTeam));
		
		// when
		JoinDto rtnJoin = joinService.requestPlayerJoin(1L,proposal);
		
		// then
		Assertions.assertThat(rtnJoin.getPlayerId()).isEqualTo(1L);
		Assertions.assertThat(rtnJoin.getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(rtnJoin.getTeamId()).isEqualTo(1L);
		Assertions.assertThat(rtnJoin.getTeamName()).isEqualTo("redTeam");
	}
	
	@Test
	@DisplayName("Team 가입 신청 테스트")
	void requestTeamJoin() {
		// given
		TeamEntity rTeam = new TeamEntity("redTeam","Busan",BelongType.CLUB,"We are Red team");
		TeamEntity gTeam = new TeamEntity("greenTeam","Wonju",BelongType.CLUB,"We are Green team");
		PlayerEntity player = new PlayerEntity("apple","220619-1111111",1,gTeam);
		
		rTeam.initId(1L);
		gTeam.initId(2L);
		player.initId(1L);
		
		JoinDto proposal = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,1L,2L);
		
		List<JoinDto> searhList = new ArrayList<>();
		
		Page<JoinDto> searchPage = new PageImpl(searhList,PageRequest.of(0,1),0);
		
		// mocking
		when(teamRepository.findById(2L)).thenReturn(Optional.of(rTeam));
		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findTeamJoinApplication(StatusType.PROPOSAL, 2L, PageRequest.of(0, 1))).thenReturn(searchPage);
		when(joinRepository.save(any())).thenReturn(new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player,gTeam));
		
		// when
		JoinDto rtnJoin = joinService.requestTeamJoin(2L,proposal);
		
		// then
		Assertions.assertThat(rtnJoin.getPlayerId()).isEqualTo(1L);
		Assertions.assertThat(rtnJoin.getPlayerName()).isEqualTo("apple");
		Assertions.assertThat(rtnJoin.getTeamId()).isEqualTo(2L);
		Assertions.assertThat(rtnJoin.getTeamName()).isEqualTo("greenTeam");
	}
	
	@Test
	@DisplayName("기가입 신청 예외 테스트")
	void requestJoinException() throws Exception {
		// given
		TeamEntity rTeam = new TeamEntity("redTeam","Busan",BelongType.CLUB,"We are Red team");
		TeamEntity gTeam = new TeamEntity("greenTeam","Wonju",BelongType.CLUB,"We are Green team");
		PlayerEntity player = new PlayerEntity("apple","220619-1111111",1,gTeam);
		
		rTeam.initId(1L);
		gTeam.initId(2L);
		player.initId(1L);
		
		JoinDto proposal1 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,1L);
		JoinDto proposal2 = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,1L);
		JoinEntity expectJoin1 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player,rTeam);
		
		
		expectJoin1.initId(1L);
		
		PageRequest pageRequest = PageRequest.of(0,1);
		List<JoinDto> searchList = new ArrayList<>();
		Page<JoinDto> searchPage = new PageImpl<>(searchList,pageRequest,searchList.size());
		
		// mocking
		when(teamRepository.findById(1L)).thenReturn(Optional.of(rTeam));
		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findPlayerJoinApplication(StatusType.PROPOSAL, 1L, PageRequest.of(0, 1))).thenReturn(searchPage);
		when(joinRepository.save(any())).thenReturn(expectJoin1);
		JoinDto rtnJoin1 = joinService.requestPlayerJoin(1L,proposal1);
		
		searchList.add(rtnJoin1);
		searchPage = new PageImpl<>(searchList,pageRequest,searchList.size());
		when(teamRepository.findById(1L)).thenReturn(Optional.of(rTeam));
		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findPlayerJoinApplication(StatusType.PROPOSAL, 1L, pageRequest)).thenReturn(searchPage);
		
		// when
		Exception existProposal = assertThrows(Exception.class,()->joinService.requestPlayerJoin(1L,proposal2));
		
		// then
		org.junit.jupiter.api.Assertions.assertEquals("400 BAD_REQUEST "+'"'+"이미 요청한 제안입니다."+'"'+"; nested exception is java.lang.Exception", existProposal.getMessage());
		
	}
	
	@Test
	@DisplayName("선수 가입 거절")
	void rejectPlayerRequest() {
		// given
		TeamEntity team1 = new TeamEntity("reptileTeam","Ocean",BelongType.CLUB,"We are Reptile team");
		TeamEntity team2 = new TeamEntity("fishTeam","Ocean",BelongType.CLUB,"We are Fish team");
		PlayerEntity player = new PlayerEntity("turtle","220719-1111111",10,team1);
		team1.initId(1L);
		team2.initId(2L);
		player.initId(1L);
		
		JoinEntity join1 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player,team2);
		join1.initId(1L);
		PageRequest pageRequest = PageRequest.of(0, 1);
		List<JoinDto> expectList = new ArrayList<>();
		JoinDto joinDto = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,1L,2L);
		expectList.add(joinDto);
		PageImpl expectPage = new PageImpl<>(expectList,pageRequest,expectList.size());
		
		// when
		when(teamRepository.findById(2L)).thenReturn(Optional.of(team2));
		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findPlayerJoinOffer(StatusType.PROPOSAL,1L,pageRequest)).thenReturn(expectPage);
		when(joinRepository.findById(any())).thenReturn(Optional.of(join1));
		join1.updateStatus(StatusType.REJECT);
		when(joinRepository.save(join1)).thenReturn(join1);
		
		JoinDto returnJoin = joinService.rejectPlayerJoin(joinDto);		
		// then
		Assertions.assertThat(returnJoin.getPlayerId()).isEqualTo(1L);
		Assertions.assertThat(returnJoin.getTeamId()).isEqualTo(2L);
		Assertions.assertThat(returnJoin.getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(returnJoin.getStatusType()).isEqualTo(StatusType.REJECT);
	}
	
	@Test
	@DisplayName("팀 가입 거절")
	void rejectTeamRequest() {
		// given
		TeamEntity team1 = new TeamEntity("reptileTeam","Ocean",BelongType.CLUB,"We are Reptile team");
		TeamEntity team2 = new TeamEntity("fishTeam","Ocean",BelongType.CLUB,"We are Fish team");
		PlayerEntity player = new PlayerEntity("turtle","220719-1111111",10,team1);
		team1.initId(1L);
		team2.initId(2L);
		player.initId(1L);
		
		JoinEntity join1 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player,team2);
		join1.initId(1L);
		PageRequest pageRequest = PageRequest.of(0, 1);
		List<JoinDto> expectList = new ArrayList<>();
		JoinDto joinDto = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,2L);
		expectList.add(joinDto);
		PageImpl expectPage = new PageImpl<>(expectList,pageRequest,expectList.size());
		
		// when
		when(teamRepository.findById(any())).thenReturn(Optional.of(team2));
		when(playerRepository.findById(any())).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findTeamJoinOffer(StatusType.PROPOSAL,2L,pageRequest)).thenReturn(expectPage);
		when(joinRepository.findById(any())).thenReturn(Optional.of(join1));
		join1.updateStatus(StatusType.REJECT);
		when(joinRepository.save(join1)).thenReturn(join1);
		
		JoinDto returnJoin = joinService.rejectTeamJoin(joinDto);		
		// then
		Assertions.assertThat(returnJoin.getPlayerId()).isEqualTo(1L);
		Assertions.assertThat(returnJoin.getTeamId()).isEqualTo(2L);
		Assertions.assertThat(returnJoin.getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(returnJoin.getStatusType()).isEqualTo(StatusType.REJECT);
	}
	
	@Test
	@DisplayName("선수 가입 승인")
	void approvePlayerRequest() {
		// given
		TeamEntity team1 = new TeamEntity("reptileTeam","Ocean",BelongType.CLUB,"We are Reptile team");
		TeamEntity team2 = new TeamEntity("fishTeam","Ocean",BelongType.CLUB,"We are Fish team");
		PlayerEntity player = new PlayerEntity("turtle","220719-1111111",10,team1);
		team1.initId(1L);
		team2.initId(2L);
		player.initId(1L);
		
		JoinEntity join1 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,player,team2);
		join1.initId(1L);
		PageRequest pageRequest = PageRequest.of(0, 1);
		List<JoinDto> expectList = new ArrayList<>();
		JoinDto joinDto = new JoinDto(RequesterType.TEAM,StatusType.PROPOSAL,1L,2L);
		expectList.add(joinDto);
		PageImpl expectPage = new PageImpl<>(expectList,pageRequest,expectList.size());
		
		// when
		when(teamRepository.findById(2L)).thenReturn(Optional.of(team2));
		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findPlayerJoinOffer(StatusType.PROPOSAL,1L,pageRequest)).thenReturn(expectPage);
		when(joinRepository.findById(any())).thenReturn(Optional.of(join1));
		join1.updateStatus(StatusType.APPROVAL);
		when(joinRepository.save(join1)).thenReturn(join1);
		
		JoinDto returnJoin = joinService.approvePlayerJoin(joinDto);		
		// then
		Assertions.assertThat(returnJoin.getPlayerId()).isEqualTo(1L);
		Assertions.assertThat(returnJoin.getTeamId()).isEqualTo(2L);
		Assertions.assertThat(returnJoin.getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(returnJoin.getStatusType()).isEqualTo(StatusType.APPROVAL);
	}
	
	@Test
	@DisplayName("팀 가입 승인")
	void approveTeamRequest() {
		// given
		TeamEntity team1 = new TeamEntity("reptileTeam","Ocean",BelongType.CLUB,"We are Reptile team");
		TeamEntity team2 = new TeamEntity("fishTeam","Ocean",BelongType.CLUB,"We are Fish team");
		PlayerEntity player = new PlayerEntity("turtle","220719-1111111",10,team1);
		team1.initId(1L);
		team2.initId(2L);
		player.initId(1L);
		
		JoinEntity join1 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player,team2);
		join1.initId(1L);
		PageRequest pageRequest = PageRequest.of(0, 1);
		List<JoinDto> expectList = new ArrayList<>();
		JoinDto joinDto = new JoinDto(RequesterType.PLAYER,StatusType.PROPOSAL,1L,2L);
		expectList.add(joinDto);
		PageImpl expectPage = new PageImpl<>(expectList,pageRequest,expectList.size());
		
		// when
		when(teamRepository.findById(any())).thenReturn(Optional.of(team2));
		when(playerRepository.findById(any())).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findTeamJoinOffer(StatusType.PROPOSAL,2L,pageRequest)).thenReturn(expectPage);
		when(joinRepository.findById(any())).thenReturn(Optional.of(join1));
		join1.updateStatus(StatusType.APPROVAL);
		when(joinRepository.save(join1)).thenReturn(join1);
		
		JoinDto returnJoin = joinService.approveTeamJoin(joinDto);		
		// then
		Assertions.assertThat(returnJoin.getPlayerId()).isEqualTo(1L);
		Assertions.assertThat(returnJoin.getTeamId()).isEqualTo(2L);
		Assertions.assertThat(returnJoin.getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(returnJoin.getStatusType()).isEqualTo(StatusType.APPROVAL);
	}
	
	@Test
	@DisplayName("선수 가입 신청 조회")
	void searchPlayerJoinApplicationTest() {
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
		
		JoinSearchCondition condition = new JoinSearchCondition(StatusType.PROPOSAL,RequesterType.PLAYER,' ',null,null);
		
		when(joinRepositoryCustom.findPlayerJoinApplication(condition.getStatusType(), null, page)).thenReturn(expectPage);
		
		Page<JoinDto> rtnList = joinService.searchPlayerJoinApplication(null,condition, page);
		
		Assertions.assertThat(rtnList).isEqualTo(expectPage);
		
	}
	
	@Test
	@DisplayName("선수 가입 제안 조회")
	void searchPlayerJoinOfferTest() {
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
		
		JoinEntity join1 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,graph,rTeam);
		join1.initId(1L);
		JoinEntity join2 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,graph,pTeam);
		join2.initId(2L);
		JoinEntity join3 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,graph,yTeam);
		join2.initId(3L);
		JoinEntity join4 = new JoinEntity(StatusType.PROPOSAL,RequesterType.TEAM,banana,yTeam);
		join2.initId(4L);
		
		
		PageRequest page = PageRequest.of(0,10);
		List<JoinDto> expectList = new ArrayList<>();
		expectList.add(join1.toDto());
		expectList.add(join2.toDto());
		expectList.add(join3.toDto());
		Page<JoinDto> expectPage = new PageImpl<>(expectList,page,expectList.size());
		
		JoinSearchCondition condition = new JoinSearchCondition(StatusType.PROPOSAL,RequesterType.TEAM,' ',null,null);
		
		when(joinRepositoryCustom.findPlayerJoinOffer(condition.getStatusType(), 2L, page)).thenReturn(expectPage);
		
		Page<JoinDto> rtnList = joinService.searchPlayerJoinOffer(2L,condition, page);
		
		Assertions.assertThat(rtnList).isEqualTo(expectPage);
		
	}
}
