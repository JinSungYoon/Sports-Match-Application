package core.join.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import core.join.dto.JoinSearchCondition;
import core.join.service.JoinService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class JoinController {
	
	private final JoinService joinService;
	
	@GetMapping("searchPlayerJoinApplication")
	public ResponseEntity<?> sarchJoinList(@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchPlayerJoinApplication(condition, page),HttpStatus.OK);
	}
	
}
