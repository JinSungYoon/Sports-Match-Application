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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.player.entity.BelongType;
import core.team.dto.TeamDto;
import core.team.service.TeamService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TeamController {
	
	private final TeamService teamService;
	
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
	
}
