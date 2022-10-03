package core.team.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.join.dto.JoinDto;
import core.join.dto.JoinSearchCondition;
import core.join.service.JoinService;
import core.player.entity.BelongType;
import core.team.dto.TeamDto;
import core.team.service.TeamService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TeamController {
	
	private final TeamService teamService;
	private final JoinService joinService; 
	
	// team을 생성
	@PostMapping("/team")
	public ResponseEntity<?> registerTeams(@RequestBody TeamDto dto){
		return new ResponseEntity<>(teamService.registerTeam(dto),HttpStatus.CREATED);
	}
	
	// team을 id로 검색
	@GetMapping("/team/{id}")
	public ResponseEntity<?> searchOneTeams(@PathVariable Long id){
		return new ResponseEntity<>(teamService.searchTeamById(id),HttpStatus.OK);
	}
	
	// 모든 팀의 정보를 조회
	@GetMapping("/teams/all")
	public ResponseEntity<?> searchAllTeams(@PageableDefault(page = 0, size = 10) Pageable pageable){
		return new ResponseEntity<>(teamService.searchAllTeams(pageable),HttpStatus.OK);
	}
	
	// team 정보를 조건 검색
	@GetMapping("/teams")
	public ResponseEntity<?> searchTeams(@RequestParam(value="teamName",required=false) String teamName,@RequestParam(value="location",required=false) String location,@RequestParam(value="belongType",required=false) BelongType belongType,@RequestParam(value="introduction",required=false) String introduction,@PageableDefault(page=0,size=10)Pageable pageable){
		return new ResponseEntity<>(teamService.searchTeams(teamName, location, belongType, introduction,pageable),HttpStatus.OK);
	}
	
	// team 정보를 수정
	@PatchMapping("/team/{id}")
	public ResponseEntity<?> updateTeam(@PathVariable Long id,@RequestBody TeamDto dto){
		return new ResponseEntity<>(teamService.updateTeam(id, dto),HttpStatus.OK);
	}
	
	// team을 삭제
	@DeleteMapping("/team/{id}")
	public ResponseEntity<?> deleteTeam(@PathVariable Long id){
		return new ResponseEntity<>(teamService.deleteTeam(id),HttpStatus.OK);
	}
	
	// team 가입 제안
	@PostMapping("/team/{id}/request-join")
	public ResponseEntity<?> requestJoin(@PathVariable Long id,@RequestBody JoinDto dto){
		return new ResponseEntity<>(joinService.requestTeamJoin(id, dto),HttpStatus.CREATED);
	}
	
	// team 가입 제안 거절
	@PatchMapping("/team/{id}/reject-join/{playerId}")
	public ResponseEntity<?> rejectJoin(@PathVariable Long id,@PathVariable Long playerId) throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setTeamId(id);
		joinDto.setPlayerId(playerId);
		return new ResponseEntity<>(joinService.rejectTeamJoin(joinDto),HttpStatus.OK);
	}
	
	// team 가입 제안 승인
	@PatchMapping("/team/{id}/approve-join/{playerId}")
	public ResponseEntity<?> approveJoin(@PathVariable Long id,@PathVariable Long playerId) throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setTeamId(id);
		joinDto.setPlayerId(playerId);
		return new ResponseEntity<>(joinService.approveTeamJoin(joinDto),HttpStatus.OK);
	}
	
	// team 가입 제안 철회
	@PatchMapping("/team/{id}/withdraw-approve/{playerId}")
	public ResponseEntity<?> withdrawJoin(@PathVariable Long id,@PathVariable Long playerId) throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setTeamId(id);
		joinDto.setPlayerId(playerId);
		return new ResponseEntity<>(joinService.withdrawTeamApprove(joinDto),HttpStatus.OK);
	}
	
	@GetMapping("/team/{id}/search-join-application")
	public ResponseEntity<?> searchJoinApplication(@PathVariable Long id,@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchTeamJoinApplication(id,condition, page),HttpStatus.OK);
	}
	
	@GetMapping("/team/{id}/search-join-offer")
	public ResponseEntity<?> searchJoinOffer(@PathVariable Long id,@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchTeamJoinOffer(id,condition, page),HttpStatus.OK);
	}
	
	
}
