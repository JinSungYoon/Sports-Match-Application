package core.join.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import core.join.dto.JoinDto;
import core.join.dto.JoinSearchCondition;
import core.join.service.JoinService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JoinController {
	
	private final JoinService joinService;
		
	@GetMapping("searchPlayerJoinApplication")
	public ResponseEntity<?> searchPlayerJoinApplicationList(@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchPlayerJoinApplication(condition, page),HttpStatus.OK);
	}
	
	@GetMapping("searchPlayerJoinOffer")
	public ResponseEntity<?> searchPlayerJoinOfferList(@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchPlayerJoinOffer(condition, page),HttpStatus.OK);
	}
	
	@GetMapping("searchTeamJoinApplication")
	public ResponseEntity<?> searchTeamJoinApplicationList(@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchTeamJoinApplication(condition, page),HttpStatus.OK);
	}
	
	@GetMapping("searchTeamJoinOffer")
	public ResponseEntity<?> searchTeamJoinOfferList(@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchTeamJoinOffer(condition, page),HttpStatus.OK);
	}
	
	@PatchMapping("rejectJoin")
	public ResponseEntity<?> rejectJoin(@RequestBody JoinDto dto){
		return new ResponseEntity<>(joinService.rejectJoin(dto),HttpStatus.OK);
	} 
	
}
