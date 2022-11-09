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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
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
import core.player.repository.PlayerRepositoryCustom;
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
	private PlayerRepositoryCustom playerRepositoryCustom;
	
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
		
		JoinDto proposal = new JoinDto(player.toDto(),rTeam.toDto(),RequesterType.PLAYER,StatusType.PROPOSAL);
		
		List<JoinDto> searhList = new ArrayList<>();
		
		Page<JoinDto> searchPage = new PageImpl(searhList,PageRequest.of(0,1),0);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.PLAYER);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// mocking
		when(teamRepository.findById(1L)).thenReturn(Optional.of(rTeam));
		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findPlayerJoinApplication(condition, 1L, PageRequest.of(0, 1))).thenReturn(searchPage);
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
		
		JoinDto proposal = new JoinDto(player.toDto(),gTeam.toDto(),RequesterType.TEAM,StatusType.PROPOSAL);
		
		List<JoinDto> searhList = new ArrayList<>();
		
		Page<JoinDto> searchPage = new PageImpl(searhList,PageRequest.of(0,1),0);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.TEAM);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// mocking
		when(teamRepository.findById(2L)).thenReturn(Optional.of(rTeam));
		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findTeamJoinApplication(condition, 2L, PageRequest.of(0, 1))).thenReturn(searchPage);
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
		
		JoinDto proposal1 = new JoinDto(player.toDto(),rTeam.toDto(),RequesterType.PLAYER,StatusType.PROPOSAL);
		JoinDto proposal2 = new JoinDto(player.toDto(),rTeam.toDto(),RequesterType.PLAYER,StatusType.PROPOSAL);
		JoinEntity expectJoin1 = new JoinEntity(StatusType.PROPOSAL,RequesterType.PLAYER,player,rTeam);
		
		
		expectJoin1.initId(1L);
		
		PageRequest pageRequest = PageRequest.of(0,1);
		List<JoinDto> searchList = new ArrayList<>();
		Page<JoinDto> searchPage = new PageImpl<>(searchList,pageRequest,searchList.size());
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.PLAYER);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// mocking
		when(teamRepository.findById(1L)).thenReturn(Optional.of(rTeam));
		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findPlayerJoinApplication(condition, 1L, PageRequest.of(0, 1))).thenReturn(searchPage);
		when(joinRepository.save(any())).thenReturn(expectJoin1);
		JoinDto rtnJoin1 = joinService.requestPlayerJoin(1L,proposal1);
		
		searchList.add(rtnJoin1);
		searchPage = new PageImpl<>(searchList,pageRequest,searchList.size());
		when(teamRepository.findById(1L)).thenReturn(Optional.of(rTeam));
		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findPlayerJoinApplication(condition, 1L, pageRequest)).thenReturn(searchPage);
		
		// when
		Exception existProposal = assertThrows(Exception.class,()->joinService.requestPlayerJoin(1L,proposal2));
		
		// then
		org.junit.jupiter.api.Assertions.assertEquals("400 BAD_REQUEST "+'"'+"이미 요청한 제안입니다."+'"'+"; nested exception is java.lang.Exception", existProposal.getMessage());
		
	}
	
	@Test
	@DisplayName("선수 가입 거절")
	void rejectPlayerRequest() throws Exception {
		// given
		TeamEntity team1 = new TeamEntity("reptileTeam","Ocean",BelongType.CLUB,"We are Reptile team");
		TeamEntity team2 = new TeamEntity("fishTeam","Ocean",BelongType.CLUB,"We are Fish team");
		PlayerEntity player = new PlayerEntity("turtle","220719-1111111",10,team1);
		team1.initId(1L);
		team2.initId(2L);
		player.initId(1L);
		
		PageRequest pageRequest = PageRequest.of(0, 100);
		List<JoinDto> expectList = new ArrayList<>();
		JoinDto joinDto = new JoinDto(player.toDto(),team2.toDto(),RequesterType.TEAM,StatusType.PROPOSAL);
		JoinEntity joinEntity = joinDto.toEntity(joinDto, player, team2);
		expectList.add(joinDto);
		Page<JoinDto> expectPage = new PageImpl<>(expectList,pageRequest,expectList.size());
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.PLAYER);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// when
		when(teamRepository.findById(2L)).thenReturn(Optional.of(team2));
		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findPlayerJoinOffer(condition,1L,pageRequest)).thenReturn(expectPage);
		joinEntity.updateStatus(StatusType.REJECT);
		when(joinRepository.save(any())).thenReturn(joinEntity);
		
		JoinDto returnJoin = joinService.rejectPlayerJoin(joinDto);		
		// then
		Assertions.assertThat(returnJoin.getPlayerId()).isEqualTo(1L);
		Assertions.assertThat(returnJoin.getTeamId()).isEqualTo(2L);
		Assertions.assertThat(returnJoin.getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(returnJoin.getStatusType()).isEqualTo(StatusType.REJECT);
	}
	
	@Test
	@DisplayName("팀 가입 거절")
	void rejectTeamRequest() throws Exception {
		// given
		TeamEntity team1 = new TeamEntity("reptileTeam","Ocean",BelongType.CLUB,"We are Reptile team");
		TeamEntity team2 = new TeamEntity("fishTeam","Ocean",BelongType.CLUB,"We are Fish team");
		PlayerEntity player = new PlayerEntity("turtle","220719-1111111",10,team1);
		team1.initId(1L);
		team2.initId(2L);
		player.initId(1L);
				
		PageRequest pageRequest = PageRequest.of(0, 100);
		List<JoinDto> expectList = new ArrayList<>();
		JoinDto joinDto = new JoinDto(player.toDto(),team2.toDto(),RequesterType.PLAYER,StatusType.PROPOSAL);
		JoinEntity joinEntity = joinDto.toEntity(joinDto, player, team2); 
		expectList.add(joinDto);
		Page<JoinDto> expectPage = new PageImpl<>(expectList,pageRequest,expectList.size());
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.TEAM);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// when
		when(teamRepository.findById(any())).thenReturn(Optional.of(team2));
		when(playerRepository.findById(any())).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findTeamJoinOffer(condition,2L,pageRequest)).thenReturn(expectPage);
		joinEntity.updateStatus(StatusType.REJECT);
		when(joinRepository.save(any())).thenReturn(joinEntity);
		
		JoinDto returnJoin = joinService.rejectTeamJoin(joinDto);		
		// then
		Assertions.assertThat(returnJoin.getPlayerId()).isEqualTo(1L);
		Assertions.assertThat(returnJoin.getTeamId()).isEqualTo(2L);
		Assertions.assertThat(returnJoin.getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(returnJoin.getStatusType()).isEqualTo(StatusType.REJECT);
	}
	
	@Test
	@DisplayName("선수 가입 승인")
	void approvePlayerRequest() throws Exception {
		// given
		TeamEntity team1 = new TeamEntity("reptileTeam","Ocean",BelongType.CLUB,"We are Reptile team");
		TeamEntity team2 = new TeamEntity("fishTeam","Ocean",BelongType.CLUB,"We are Fish team");
		PlayerEntity player = new PlayerEntity("turtle","220719-1111111",10,team1);
		team1.initId(1L);
		team2.initId(2L);
		player.initId(1L);
		
		PageRequest pageRequest = PageRequest.of(0, 100);
		List<JoinDto> expectList = new ArrayList<>();
		JoinDto joinDto = new JoinDto(player.toDto(),team2.toDto(),RequesterType.TEAM,StatusType.PROPOSAL);
		JoinEntity entity = joinDto.toEntity(joinDto, player, team2);
		expectList.add(joinDto);
		Page<JoinDto> expectPage = new PageImpl<>(expectList,pageRequest,expectList.size());
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.PLAYER);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// when
		when(teamRepository.findById(2L)).thenReturn(Optional.of(team2));
		when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findPlayerJoinOffer(condition,1L,pageRequest)).thenReturn(expectPage);
		entity.updateStatus(StatusType.APPROVAL);
		when(joinRepository.save(any())).thenReturn(entity);
		
		JoinDto returnJoin = joinService.approvePlayerJoin(joinDto);		
		// then
		Assertions.assertThat(returnJoin.getPlayerId()).isEqualTo(1L);
		Assertions.assertThat(returnJoin.getTeamId()).isEqualTo(2L);
		Assertions.assertThat(returnJoin.getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(returnJoin.getStatusType()).isEqualTo(StatusType.APPROVAL);
	}
	
	@Test
	@DisplayName("팀 가입 승인")
	void approveTeamRequest() throws Exception {
		// given
		TeamEntity team1 = new TeamEntity("reptileTeam","Ocean",BelongType.CLUB,"We are Reptile team");
		TeamEntity team2 = new TeamEntity("fishTeam","Ocean",BelongType.CLUB,"We are Fish team");
		PlayerEntity player = new PlayerEntity("turtle","220719-1111111",10,team1);
		
		team1.initId(1L);
		team2.initId(2L);
		player.initId(1L);
		
		PageRequest pageRequest = PageRequest.of(0, 100);
		List<JoinDto> expectList = new ArrayList<>();
		JoinDto joinDto = new JoinDto(1L,1L,"turtle",2L,"fishTeam",RequesterType.PLAYER,StatusType.PROPOSAL,'Y',LocalDateTime.now(),LocalDateTime.now());
		JoinEntity entity = joinDto.toEntity(joinDto, player, team2);
		expectList.add(joinDto);
		Page<JoinDto> expectPage = new PageImpl<>(expectList,pageRequest,expectList.size());
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.TEAM);
		condition.setStatusType(StatusType.PROPOSAL);
		
		// when
		when(teamRepository.findById(any())).thenReturn(Optional.of(team2));
		when(playerRepository.findById(any())).thenReturn(Optional.of(player));
		when(joinRepositoryCustom.findTeamJoinOffer(condition,2L,pageRequest)).thenReturn(expectPage);
		entity.updateStatus(StatusType.APPROVAL);
		when(joinRepository.save(any())).thenReturn(entity);
		
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
		
		when(joinRepositoryCustom.findPlayerJoinApplication(condition, null, page)).thenReturn(expectPage);
		
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
		
		when(joinRepositoryCustom.findPlayerJoinOffer(condition, 2L, page)).thenReturn(expectPage);
		
		Page<JoinDto> rtnList = joinService.searchPlayerJoinOffer(2L,condition, page);
		
		Assertions.assertThat(rtnList).isEqualTo(expectPage);
		
	}
	
	@Test
	@DisplayName("선수 가입 철회하기")
	public void withdrawPlayerApprove() throws Exception{
		// given
		TeamEntity gteam = new TeamEntity("griffindor","south",BelongType.CLUB,"Teach all children who show courage worthy of their name.");
		TeamEntity steam = new TeamEntity("slydelin","north",BelongType.CLUB,"Teach only children of the purest bloodlines.");
		TeamEntity hteam = new TeamEntity("hufflepuff","east",BelongType.CLUB,"I will teach them the same.");
		TeamEntity rteam = new TeamEntity("ravenclaw","west",BelongType.CLUB,"Teach only the smartest kids.");
		
		gteam.initId(1L);
		steam.initId(2L);
		hteam.initId(3L);
		rteam.initId(4L);
		
		PlayerEntity player1 = new PlayerEntity("harry potter","220930-1111111",1,gteam);
		
		player1.initId(1L);
		
		PageRequest pageRequest = PageRequest.of(0,100);
		
		JoinDto joinDto = new JoinDto(1L,player1.getId(),player1.getPlayerName(),steam.getId(),steam.getTeamName(),RequesterType.TEAM,StatusType.APPROVAL,'Y',LocalDateTime.now(),LocalDateTime.now());
		JoinEntity joinEntity = joinDto.toEntity(joinDto, player1, steam);
		List<JoinDto> expectList = new ArrayList<>();
		expectList.add(joinDto);
		Page<JoinDto> expectPage = new PageImpl<>(expectList,pageRequest,expectList.size());
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.PLAYER);
		condition.setStatusType(StatusType.APPROVAL);
		
		// when
		when(teamRepository.findById(any())).thenReturn(Optional.of(steam));
		when(playerRepository.findById(any())).thenReturn(Optional.of(player1));
		when(joinRepositoryCustom.findPlayerJoinApplication(condition, player1.getId(), pageRequest)).thenReturn(expectPage);
		joinEntity.updateStatus(StatusType.WITHDRAW);
		when(joinRepository.save(any())).thenReturn(joinEntity);
		
		JoinDto rtnDto = joinService.withdrawPlayerApprove(joinDto);
		
		Assertions.assertThat(rtnDto.getPlayerName()).isEqualTo("harry potter");
		Assertions.assertThat(rtnDto.getTeamName()).isEqualTo("slydelin");
		Assertions.assertThat(rtnDto.getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(rtnDto.getStatusType()).isEqualTo(StatusType.WITHDRAW);
	}
	
	@Test
	@DisplayName("선수 승인 철회하기")
	public void withdrawTeamApprove() throws Exception {
		// given
		TeamEntity gteam = new TeamEntity("griffindor","south",BelongType.CLUB,"Teach all children who show courage worthy of their name.");
		TeamEntity steam = new TeamEntity("slydelin","north",BelongType.CLUB,"Teach only children of the purest bloodlines.");
		TeamEntity hteam = new TeamEntity("hufflepuff","east",BelongType.CLUB,"I will teach them the same.");
		TeamEntity rteam = new TeamEntity("ravenclaw","west",BelongType.CLUB,"Teach only the smartest kids.");
		
		gteam.initId(1L);
		steam.initId(2L);
		hteam.initId(3L);
		rteam.initId(4L);
		
		PlayerEntity player1 = new PlayerEntity("harry potter","220930-1111111",1,gteam);
		
		player1.initId(1L);
		
		PageRequest pageRequest = PageRequest.of(0,100);
		
		JoinDto joinDto = new JoinDto(1L,player1.getId(),player1.getPlayerName(),steam.getId(),steam.getTeamName(),RequesterType.PLAYER,StatusType.APPROVAL,'Y',LocalDateTime.now(),LocalDateTime.now());
		JoinEntity joinEntity = joinDto.toEntity(joinDto, player1, steam);
		List<JoinDto> expectList = new ArrayList<>();
		expectList.add(joinDto);
		Page<JoinDto> expectPage = new PageImpl<>(expectList,pageRequest,expectList.size());
		
		JoinSearchCondition condition = new JoinSearchCondition();
		condition.setRequesterType(RequesterType.TEAM);
		condition.setStatusType(StatusType.APPROVAL);
		
		// when
		when(teamRepository.findById(any())).thenReturn(Optional.of(steam));
		when(playerRepository.findById(any())).thenReturn(Optional.of(player1));
		when(joinRepositoryCustom.findTeamJoinApplication(condition, steam.getId(), pageRequest)).thenReturn(expectPage);
		joinEntity.updateStatus(StatusType.WITHDRAW);
		when(joinRepository.save(any())).thenReturn(joinEntity);
		
		JoinDto rtnDto = joinService.withdrawTeamApprove(joinDto);
		
		Assertions.assertThat(rtnDto.getPlayerName()).isEqualTo("harry potter");
		Assertions.assertThat(rtnDto.getTeamName()).isEqualTo("slydelin");
		Assertions.assertThat(rtnDto.getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(rtnDto.getStatusType()).isEqualTo(StatusType.WITHDRAW);
	}
	
	@Test
	@DisplayName("Player 승인 반려하기")
	public void returnPlayerApproval() throws Exception {
		// given
		TeamEntity gteam = new TeamEntity("griffindor","south",BelongType.CLUB,"Teach all children who show courage worthy of their name.");
		TeamEntity steam = new TeamEntity("slydelin","north",BelongType.CLUB,"Teach only children of the purest bloodlines.");
		TeamEntity hteam = new TeamEntity("hufflepuff","east",BelongType.CLUB,"I will teach them the same.");
		TeamEntity rteam = new TeamEntity("ravenclaw","west",BelongType.CLUB,"Teach only the smartest kids.");
		
		gteam.initId(1L);
		steam.initId(2L);
		hteam.initId(3L);
		rteam.initId(4L);
		
		PlayerEntity player1 = new PlayerEntity("Harry potter","221102-1111111",1,gteam);
		PlayerEntity player2 = new PlayerEntity("Ronald Bilius Weasley","221102-1111111",10,gteam);
		PlayerEntity player3 = new PlayerEntity("Hermione Jean Granger","221102-1111111",8,gteam);
		
		player1.initId(1L);
		player2.initId(2L);
		player3.initId(3L);

		PageRequest page = PageRequest.of(0, 100);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		Clock clock = Clock.systemDefaultZone();
		condition.setRequesterType(RequesterType.PLAYER);
		condition.setStatusType(StatusType.APPROVAL);
		condition.setFromToDate(LocalDateTime.now(clock), -1, 7);
		
		List<JoinDto> joinDtoList = new ArrayList<>();
		JoinDto approval1 = new JoinDto(1L,player1.getId(),player1.getPlayerName(),steam.getId(),steam.getTeamName(),RequesterType.TEAM,StatusType.APPROVAL,'Y',LocalDateTime.of(2022, Month.NOVEMBER, 6, 23, 59, 0),LocalDateTime.of(2022, Month.NOVEMBER, 6, 23, 59, 0));
		joinDtoList.add(approval1);
		Page<JoinDto> expectPage = new PageImpl<>(joinDtoList,page,joinDtoList.size());
		
		JoinEntity approvalEntity = approval1.toEntity(approval1,player1,steam);
		
		// when
		when(teamRepository.findById(any())).thenReturn(Optional.of(steam));
		when(playerRepository.findById(any())).thenReturn(Optional.of(player1));
		when(joinRepositoryCustom.findPlayerJoinApplication(condition,player1.getId(),page)).thenReturn(expectPage);
		approvalEntity.updateStatus(StatusType.RETURN);
		when(joinRepository.save(any())).thenReturn(approvalEntity);
		
		JoinDto rtnDto = joinService.returnPlayerApprove(approval1);
		
		// then
		Assertions.assertThat(rtnDto.getPlayerName()).isEqualTo("Harry potter");
		Assertions.assertThat(rtnDto.getTeamName()).isEqualTo("slydelin");
		Assertions.assertThat(rtnDto.getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(rtnDto.getStatusType()).isEqualTo(StatusType.RETURN);
		
	}
	
	@Test
	@DisplayName("Team 승인 반려하기")
	public void returnTeamApproval() throws Exception {
		// given
		TeamEntity gteam = new TeamEntity("griffindor","south",BelongType.CLUB,"Teach all children who show courage worthy of their name.");
		TeamEntity steam = new TeamEntity("slydelin","north",BelongType.CLUB,"Teach only children of the purest bloodlines.");
		TeamEntity hteam = new TeamEntity("hufflepuff","east",BelongType.CLUB,"I will teach them the same.");
		TeamEntity rteam = new TeamEntity("ravenclaw","west",BelongType.CLUB,"Teach only the smartest kids.");
		
		gteam.initId(1L);
		steam.initId(2L);
		hteam.initId(3L);
		rteam.initId(4L);
		
		PlayerEntity player1 = new PlayerEntity("Harry potter","221102-1111111",1,gteam);
		PlayerEntity player2 = new PlayerEntity("Ronald Bilius Weasley","221102-1111111",10,gteam);
		PlayerEntity player3 = new PlayerEntity("Hermione Jean Granger","221102-1111111",8,gteam);
		
		player1.initId(1L);
		player2.initId(2L);
		player3.initId(3L);

		PageRequest page = PageRequest.of(0, 100);
		
		JoinSearchCondition condition = new JoinSearchCondition();
		Clock clock = Clock.systemDefaultZone();
		condition.setRequesterType(RequesterType.TEAM);
		condition.setStatusType(StatusType.APPROVAL);
		condition.setFromToDate(LocalDateTime.now(clock), -1, 7);
		
		List<JoinDto> joinDtoList = new ArrayList<>();
		JoinDto approval1 = new JoinDto(1L,player2.getId(),player2.getPlayerName(),rteam.getId(),rteam.getTeamName(),RequesterType.PLAYER,StatusType.APPROVAL,'Y',LocalDateTime.of(2022, Month.NOVEMBER, 6, 23, 59, 0),LocalDateTime.of(2022, Month.NOVEMBER, 6, 23, 59, 0));
		joinDtoList.add(approval1);
		Page<JoinDto> expectPage = new PageImpl<>(joinDtoList,page,joinDtoList.size());
		
		JoinEntity approvalEntity = approval1.toEntity(approval1,player2,rteam);
		
		// when
		when(teamRepository.findById(any())).thenReturn(Optional.of(rteam));
		when(playerRepository.findById(any())).thenReturn(Optional.of(player2));
		when(joinRepositoryCustom.findTeamJoinApplication(condition,rteam.getId(),page)).thenReturn(expectPage);
		approvalEntity.updateStatus(StatusType.RETURN);
		when(joinRepository.save(any())).thenReturn(approvalEntity);
		
		JoinDto rtnDto = joinService.returnTeamApprove(approval1);
		
		// then
		Assertions.assertThat(rtnDto.getPlayerName()).isEqualTo("Ronald Bilius Weasley");
		Assertions.assertThat(rtnDto.getTeamName()).isEqualTo("ravenclaw");
		Assertions.assertThat(rtnDto.getRequesterType()).isEqualTo(RequesterType.PLAYER);
		Assertions.assertThat(rtnDto.getStatusType()).isEqualTo(StatusType.RETURN);

	}
	
	@Test
	@DisplayName("팀이 선수 승인 확정하기")
	public void confirmPlayerApprove() throws Exception {
		// given
		TeamEntity gteam = new TeamEntity("griffindor","south",BelongType.CLUB,"Teach all children who show courage worthy of their name.");
		TeamEntity steam = new TeamEntity("slydelin","north",BelongType.CLUB,"Teach only children of the purest bloodlines.");
		TeamEntity hteam = new TeamEntity("hufflepuff","east",BelongType.CLUB,"I will teach them the same.");
		TeamEntity rteam = new TeamEntity("ravenclaw","west",BelongType.CLUB,"Teach only the smartest kids.");
		
		gteam.initId(1L);
		steam.initId(2L);
		hteam.initId(3L);
		rteam.initId(4L);
		
		PlayerEntity player1 = new PlayerEntity("harry potter","221102-1111111",1,gteam);
		PlayerEntity player2 = new PlayerEntity("Ronald Bilius Weasley","221102-1111111",10,gteam);
		PlayerEntity player3 = new PlayerEntity("Hermione Jean Granger","221102-1111111",8,gteam);
		
		player1.initId(1L);
		player2.initId(2L);
		player3.initId(3L);
		
		List<PlayerEntity> playerList = new ArrayList<>();
		playerList.add(player2);
		playerList.add(player3);
		
		PageRequest page = PageRequest.of(0, 100);
		
		JoinSearchCondition confirmCondition = new JoinSearchCondition();
		confirmCondition.setRequesterType(RequesterType.TEAM);
		confirmCondition.setStatusType(StatusType.CONFIRMATION);
		
		List<JoinDto> emptyList = new ArrayList<>();
		Page<JoinDto> expectConfirmPage = new PageImpl<>(emptyList,page,emptyList.size());
		
		JoinSearchCondition approveCondition = new JoinSearchCondition();
		//Clock clock = Clock.fixed(Instant.parse("2022-11-08T06:08:00.00Z"), ZoneOffset.UTC);
		Clock clock = Clock.systemDefaultZone();
		approveCondition.setRequesterType(RequesterType.TEAM);
		approveCondition.setStatusType(StatusType.APPROVAL);
		approveCondition.setFromToDate(LocalDateTime.now(clock), -1, 7);
				
		JoinDto approveJoinDto = new JoinDto(1L,1L,"harry potter",1L,"griffindor",RequesterType.TEAM,StatusType.APPROVAL,'Y',LocalDateTime.of(2022,11,01,16,45,25),LocalDateTime.of(2022,11,01,16,45,25));
		List<JoinDto> expectApproveList = new ArrayList<>();
		expectApproveList.add(approveJoinDto);
		Page<JoinDto> expectApprovePage = new PageImpl<>(expectApproveList,page,expectApproveList.size()); 
		JoinEntity approveJoinEntity = approveJoinDto.toEntity(approveJoinDto, player1, gteam); 
		
		JoinDto requestJoinDto = new JoinDto(1L,player1.toDto(),gteam.toDto(),RequesterType.TEAM,StatusType.CONFIRMATION);
		
		// then
		when(teamRepository.findById(any())).thenReturn(Optional.of(gteam));
		when(playerRepository.findById(any())).thenReturn(Optional.of(player1));
		when(joinRepositoryCustom.findTeamJoinApplication(confirmCondition, requestJoinDto.getTeamId(), page)).thenReturn(expectConfirmPage);
		when(joinRepositoryCustom.findTeamJoinApplication(approveCondition, gteam.getId(), page)).thenReturn(expectApprovePage);
		when(playerRepositoryCustom.findPlayer(null,null,"griffindor",page)).thenReturn(playerList);
		approveJoinEntity.updateStatus(StatusType.CONFIRMATION);
		lenient().doReturn(approveJoinEntity).when(joinRepository).save(any());
		
		JoinDto rtnDto = joinService.confirmPlayerApprove(requestJoinDto);
		
		Assertions.assertThat(rtnDto.getPlayerName()).isEqualTo("harry potter");
		Assertions.assertThat(rtnDto.getTeamName()).isEqualTo("griffindor");
		Assertions.assertThat(rtnDto.getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(rtnDto.getStatusType()).isEqualTo(StatusType.CONFIRMATION);
		
	}
	
	@Test
	@DisplayName("선수가 팀 승인 확정하기")
	public void confirmTeamApprove() throws Exception{
		// given
		TeamEntity gteam = new TeamEntity("griffindor","south",BelongType.CLUB,"Teach all children who show courage worthy of their name.");
		TeamEntity steam = new TeamEntity("slydelin","north",BelongType.CLUB,"Teach only children of the purest bloodlines.");
		TeamEntity hteam = new TeamEntity("hufflepuff","east",BelongType.CLUB,"I will teach them the same.");
		TeamEntity rteam = new TeamEntity("ravenclaw","west",BelongType.CLUB,"Teach only the smartest kids.");
		
		gteam.initId(1L);
		steam.initId(2L);
		hteam.initId(3L);
		rteam.initId(4L);
		
		PlayerEntity player1 = new PlayerEntity("Harry potter","221102-1111111",1,gteam);
		PlayerEntity player2 = new PlayerEntity("Ronald Bilius Weasley","221102-1111111",10,gteam);
		PlayerEntity player3 = new PlayerEntity("Hermione Jean Granger","221102-1111111",8,gteam);
		
		player1.initId(1L);
		player2.initId(2L);
		player3.initId(3L);
		
		List<PlayerEntity> emptyPlayer = new ArrayList<>();
		
		PageRequest page = PageRequest.of(0,100);
		
		JoinSearchCondition confirmCondition = new JoinSearchCondition();
		confirmCondition.setRequesterType(RequesterType.PLAYER);
		confirmCondition.setStatusType(StatusType.CONFIRMATION);
		
		JoinSearchCondition approveCondition = new JoinSearchCondition();
		// 해당 국가의 시간대 설정
		Clock clock = Clock.systemDefaultZone();
		approveCondition.setRequesterType(RequesterType.PLAYER);
		approveCondition.setStatusType(StatusType.APPROVAL);
		approveCondition.setFromToDate(LocalDateTime.now(clock), -1, 7);
		
		JoinDto confirmJoinDto = new JoinDto(1L,player1.getId(),player1.getPlayerName(),gteam.getId(),gteam.getTeamName(),RequesterType.TEAM,StatusType.APPROVAL,'Y',LocalDateTime.of(2022,5,12,23,59,00),LocalDateTime.of(2022,5,12,23,59,00));
		List<JoinDto> expectConfirmList = new ArrayList<>();
		expectConfirmList.add(confirmJoinDto);
		Page<JoinDto> expectConfirmPage = new PageImpl<>(expectConfirmList,page,expectConfirmList.size());
		
		JoinDto approveJoinDto = new JoinDto(1L,player1.getId(),player1.getPlayerName(),steam.getId(),steam.getTeamName(),RequesterType.TEAM,StatusType.APPROVAL,'Y',LocalDateTime.of(2022,10,12,23,59,00),LocalDateTime.of(2022,10,12,23,59,00));
		List<JoinDto> expectApproveList = new ArrayList<>();
		expectApproveList.add(approveJoinDto);
		Page<JoinDto> expectApprovePage = new PageImpl<>(expectApproveList,page,expectApproveList.size());
		
		JoinEntity approveJoinEntity = approveJoinDto.toEntity(approveJoinDto, player1, steam);

		JoinDto requestJoinDto = new JoinDto(1L,player1.toDto(),steam.toDto(),RequesterType.PLAYER,StatusType.CONFIRMATION);
		
		// when
		when(teamRepository.findById(any())).thenReturn(Optional.of(steam));
		when(playerRepository.findById(any())).thenReturn(Optional.of(player1));
		when(joinRepositoryCustom.findPlayerJoinApplication(confirmCondition, requestJoinDto.getPlayerId(), page)).thenReturn(expectConfirmPage);
		when(joinRepositoryCustom.findPlayerJoinApplication(approveCondition, requestJoinDto.getPlayerId(), page)).thenReturn(expectApprovePage);
		when(playerRepositoryCustom.findPlayer(null, null, steam.getTeamName(), page)).thenReturn(emptyPlayer);
		approveJoinEntity.updateStatus(StatusType.CONFIRMATION);
		lenient().doReturn(approveJoinEntity).when(joinRepository).save(any());
		
		JoinDto rtnDto = joinService.confirmTeamApprove(requestJoinDto);
		
		// then
		Assertions.assertThat(rtnDto.getPlayerName()).isEqualTo("Harry potter");
		Assertions.assertThat(rtnDto.getTeamName()).isEqualTo("slydelin");
		Assertions.assertThat(rtnDto.getRequesterType()).isEqualTo(RequesterType.TEAM);
		Assertions.assertThat(rtnDto.getStatusType()).isEqualTo(StatusType.CONFIRMATION);
	}
}
