package core.api.team.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.api.join.dto.JoinDto;
import core.api.join.dto.JoinSearchCondition;
import core.api.join.service.JoinService;
import core.api.player.entity.BelongType;
import core.api.team.dto.TeamDto;
import core.api.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name ="TEAM API",description = "Team과 관련된 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeamController {
	
	private final TeamService teamService;
	private final JoinService joinService; 
	
	// team을 생성
	@Tag(name ="TEAM API")
	@Operation(summary = "Register Team",description = "Team 정보를 등록하는 API입니다.")
	@PostMapping("/team")
	public ResponseEntity<?> registerTeam(@RequestBody TeamDto dto){
		return new ResponseEntity<>(teamService.registerTeam(dto),HttpStatus.CREATED);
	}
	
	// team을 id로 검색
	@Tag(name ="TEAM API")
	@Operation(summary = "Search one team",description = "조건에 맞는 한명의 Team을 찾는 API입니다.")
	@GetMapping("/team/{id}")
	public ResponseEntity<?> searchOneTeams(@PathVariable Long id){
		return new ResponseEntity<>(teamService.searchTeamById(id),HttpStatus.OK);
	}
	
	// 모든 팀의 정보를 조회
	@Tag(name ="TEAM API")
	@Operation(summary = "Search all Team",description = "모든 팀을 찾는 API입니다.")
	@GetMapping("/teams/all")
	public ResponseEntity<?> searchAllTeams(@PageableDefault(page = 0, size = 10) Pageable pageable){
		return new ResponseEntity<>(teamService.searchAllTeams(pageable),HttpStatus.OK);
	}
	
	// team 정보를 조건 검색
	@Tag(name ="TEAM API")
	@Operation(summary = "Search multi-teams",description="조건에 해당하는 다수의 팀을 찾는 API입니다.")
	@GetMapping("/teams")
	public ResponseEntity<?> searchTeams(@RequestParam(value="teamName",required=false) String teamName,@RequestParam(value="location",required=false) String location,@RequestParam(value="belongType",required=false) BelongType belongType,@RequestParam(value="introduction",required=false) String introduction,@PageableDefault(page=0,size=10)Pageable pageable){
		return new ResponseEntity<>(teamService.searchTeams(teamName, location, belongType, introduction,pageable),HttpStatus.OK);
	}
	
	// team 정보를 수정
	@Tag(name ="TEAM API")
	@Operation(summary = "Update team information",description="Team의 정보를 업데이트하는 API입니다.")
	@PatchMapping("/team/{id}")
	public ResponseEntity<?> updateTeam(@PathVariable Long id,@RequestBody TeamDto dto){
		return new ResponseEntity<>(teamService.updateTeam(id, dto),HttpStatus.OK);
	}
	
	// team을 삭제
	@Tag(name ="TEAM API")
	@Operation(summary = "Delete team information",description="Team 정보를 삭제하는 API입니다.")
	@DeleteMapping("/team/{id}")
	public ResponseEntity<?> deleteTeam(@PathVariable Long id){
		return new ResponseEntity<>(teamService.deleteTeam(id),HttpStatus.OK);
	}
	
	// team 가입 제안
	@Tag(name ="TEAM API")
	@Operation(summary = "Request join",description="Team이 다른 Player를 가입 제안하는 API입니다.")
	@PostMapping("/team/{id}/request-join")
	public ResponseEntity<?> requestJoin(@PathVariable Long id,@RequestBody JoinDto dto) throws Exception{
		return new ResponseEntity<>(joinService.requestTeamJoin(id, dto),HttpStatus.CREATED);
	}
	
	// team 가입 제안 거절
	@Tag(name ="TEAM API")
	@Operation(summary = "Reject join",description = "Team에게 제안 온 가입제안을 거절하는 API입니다.")
	@PatchMapping("/team/{id}/reject-join/{playerId}")
	public ResponseEntity<?> rejectJoin(@PathVariable Long id,@PathVariable Long playerId) throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setTeamId(id);
		joinDto.setPlayerId(playerId);
		return new ResponseEntity<>(joinService.rejectPlayerJoin(joinDto),HttpStatus.OK);
	}
	
	// team 가입 제안 승인
	@Tag(name ="TEAM API")
	@Operation(summary = "Approve join",description = "제안 온 가입제안을 승인하는 API입니다.")
	@PatchMapping("/team/{id}/approve-join/{playerId}")
	public ResponseEntity<?> approveJoin(@PathVariable Long id,@PathVariable Long playerId) throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setTeamId(id);
		joinDto.setPlayerId(playerId);
		return new ResponseEntity<>(joinService.approvePlayerJoin(joinDto),HttpStatus.OK);
	}
	
	// team 가입 제안 철회
	@Tag(name ="TEAM API")
	@Operation(summary = "Withdraw join",description="제안 한 가입제안을 철회하는 API입니다.")
	@PatchMapping("/team/{id}/withdraw-approve/{playerId}")
	public ResponseEntity<?> withdrawJoin(@PathVariable Long id,@PathVariable Long playerId) throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setTeamId(id);
		joinDto.setPlayerId(playerId);
		return new ResponseEntity<>(joinService.withdrawTeamApprove(joinDto),HttpStatus.OK);
	}
	
	// team 가입 제안 반려
	@Tag(name ="TEAM API")
	@Operation(summary = "Return join",description ="승인 된 가입제안을 반려하는 API입니다.")
	@PatchMapping("/team/{id}/return-join/{playerId}")
	public ResponseEntity<?> returnJoin(@PathVariable Long id,@PathVariable Long playerId)throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setTeamId(id);
		joinDto.setPlayerId(playerId);
		return new ResponseEntity<>(joinService.returnTeamApprove(joinDto),HttpStatus.OK);
	}
	
	// team 가입 제안 확정
	@Tag(name ="TEAM API")
	@Operation(summary = "Confirm join",description = "승인 된 가입제안을 확정하는 API입니다.")
	@PatchMapping("/team/{id}/confirm-join/{playerId}")
	public ResponseEntity<?> confirmJoin(@PathVariable Long id,@PathVariable Long playerId)throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setTeamId(id);
		joinDto.setPlayerId(playerId);
		return new ResponseEntity<>(joinService.confirmPlayerApprove(joinDto),HttpStatus.OK);
	}
	
	// team이 제안한 가입 조회
	@Tag(name ="TEAM API")
	@Operation(summary = "Search join application",description="Team이 제안한 가입을 찾는 API입니다.")
	@GetMapping("/team/{id}/search-join-application")
	public ResponseEntity<?> searchJoinApplication(@PathVariable Long id,@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchTeamJoinApplication(id,condition, page),HttpStatus.OK);
	}
	
	// team에게 제안된 가입 조회
	@Tag(name ="TEAM API")
	@Operation(summary = "Search join offer",description="Team에게 제안된 가입을 찾는 API입니다.")
	@GetMapping("/team/{id}/search-join-offer")
	public ResponseEntity<?> searchJoinOffer(@PathVariable Long id,@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchTeamJoinOffer(id,condition, page),HttpStatus.OK);
	}
	
	
}
