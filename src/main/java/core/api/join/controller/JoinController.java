package core.api.join.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import core.api.join.dto.JoinSearchCondition;
import core.api.join.service.JoinService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JoinController {
	
// JoinController 필요성 검토 후 불필요할 경우 제거 예정
	
//	private final JoinService joinService;
	
//	@GetMapping("searchPlayerJoinApplication")
//	public ResponseEntity<?> searchPlayerJoinApplicationList(@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
//		return new ResponseEntity<>(joinService.searchPlayerJoinApplication(condition, page),HttpStatus.OK);
//	}
//	
//	@GetMapping("searchPlayerJoinOffer")
//	public ResponseEntity<?> searchPlayerJoinOfferList(@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
//		return new ResponseEntity<>(joinService.searchPlayerJoinOffer(condition, page),HttpStatus.OK);
//	}
//	
//	@GetMapping("searchTeamJoinApplication")
//	public ResponseEntity<?> searchTeamJoinApplicationList(@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
//		return new ResponseEntity<>(joinService.searchTeamJoinApplication(condition, page),HttpStatus.OK);
//	}
//	
//	@GetMapping("searchTeamJoinOffer")
//	public ResponseEntity<?> searchTeamJoinOfferList(@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
//		return new ResponseEntity<>(joinService.searchTeamJoinOffer(condition, page),HttpStatus.OK);
//	}
	
}
