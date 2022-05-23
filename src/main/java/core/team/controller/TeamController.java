package core.team.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.player.entity.BelongType;
import core.team.dto.TeamDto;
import core.team.service.TeamService;

@RestController
@RequestMapping(value="/team")
public class TeamController {
	
	private final TeamService teamService;
	
	public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerTeams(@RequestBody TeamDto dto){
		return new ResponseEntity<>(teamService.registerTeam(dto),HttpStatus.CREATED);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<?> searchOneTeams(@PathVariable Long id){
		return new ResponseEntity<>(teamService.searchTeamById(id),HttpStatus.OK);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<?> searchTeamsByName(@PathVariable String name){
		return new ResponseEntity<>(teamService.searchTeamByName(name),HttpStatus.OK);
	}
	
	@GetMapping("/location/{location}")
	public ResponseEntity<?> searchTeamsByLocation(@PathVariable String location){
		return new ResponseEntity<>(teamService.searchLocationTeams(location),HttpStatus.OK);
	}
	
	@GetMapping("/belong/{belongType}")
	public ResponseEntity<?> searchTeamsByBelongType(@PathVariable BelongType type){
		return new ResponseEntity<>(teamService.searchBelongTypeTeams(type),HttpStatus.OK);
	}
	
	@GetMapping("/search/all")
	public ResponseEntity<?> searchAllTeams(){
		return new ResponseEntity<>(teamService.searchAllTeams(),HttpStatus.OK);
	}
	
	@GetMapping("/search")
	public ResponseEntity<?> searchTeams(@RequestParam(value="teamName",required=false) String teamName,@RequestParam(value="location",required=false) String location,@RequestParam(value="belongType",required=false) BelongType belongType,@RequestParam(value="introduction",required=false) String introduction){
		return new ResponseEntity<>(teamService.searchTeams(teamName, location, belongType, introduction),HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateTeam(@PathVariable Long id,@RequestBody TeamDto dto){
		return new ResponseEntity<>(teamService.updateTeam(id, dto),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTeam(@PathVariable Long id){
		return new ResponseEntity<>(teamService.deleteTeam(id),HttpStatus.OK);
	}
	
}
