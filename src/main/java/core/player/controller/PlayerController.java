package core.player.controller;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

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
import core.player.dto.PlayerDto;
import core.player.dto.PlayerListDto;
import core.player.service.PlayerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PlayerController {
	
	private final PlayerService playerService;
	private final JoinService joinService;
	
	// player 등록
	@PostMapping("/player")
	public ResponseEntity<?> register(@RequestBody PlayerDto dto) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException{
		return new ResponseEntity<>(playerService.registerPlayer(dto),HttpStatus.CREATED);
	}
	
	// player 여러명 등록
	@PostMapping("/players")
	public ResponseEntity<?> registers(@RequestBody PlayerListDto players) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException, Exception{
		return new ResponseEntity<>(playerService.registerPlayers(players.getPlayers()),HttpStatus.CREATED);
	}

	// player 한명 찾기
	@GetMapping("/player/{id}")
	public ResponseEntity<?> searchOnePlayer(@PathVariable Long id){
		return new ResponseEntity<>(playerService.searchOnePlayer(id),HttpStatus.OK);
	}
	
	// 모든 player 정보 찾기
	@GetMapping("/players/all")
	public ResponseEntity<?> searchPlayerAll(@PageableDefault(page = 0, size = 10) Pageable pageable){
		return new ResponseEntity<>(playerService.searchPlayerAll(pageable),HttpStatus.OK);
	}
	
	// player 정보 찾기
	@GetMapping("/players")
	public ResponseEntity<?> searchPlayers(@RequestParam(value="playerName",required=false)String playerName,@RequestParam(value="uniformNo",required=false)Integer uniformNo,@RequestParam(value="teamName",required=false)String teamName,@PageableDefault(page=0,size=10) Pageable pageable){
		return new ResponseEntity<>(playerService.searchPlayers(playerName, uniformNo, teamName,pageable),HttpStatus.OK);
	}
	
	// player 정보 업데이트
	@PatchMapping("/player/{id}")
	public ResponseEntity<?> update(@PathVariable Long id,@RequestBody PlayerDto dto) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException{
		return new ResponseEntity<>(playerService.updatePlayer(id, dto),HttpStatus.OK);
	}
	
	// player 삭제
	@DeleteMapping("/player/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		return new ResponseEntity<>(playerService.deletePlyaer(id),HttpStatus.OK);
	};	
	
	// player 가입 신청
	@PostMapping("/player/{id}/request-join")
	public ResponseEntity<?> requestJoin(@PathVariable Long id,@RequestBody JoinDto dto){
		return new ResponseEntity<>(joinService.requestPlayerJoin(id, dto),HttpStatus.CREATED);
	}
	
	// player 가입 제안 거절
	@PatchMapping("/player/{id}/reject-join/{teamId}")
	public ResponseEntity<?> rejectJoin(@PathVariable Long id,@PathVariable Long teamId){
		return new ResponseEntity<>(joinService.rejectPlayerJoin(id, teamId),HttpStatus.OK);
	}
	
	@GetMapping("/player/{id}/search-join-application")
	public ResponseEntity<?> searchJoinApplication(@PathVariable Long id,@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchPlayerJoinApplication(id,condition, page),HttpStatus.OK);
	}
	
	@GetMapping("/player/{id}/search-join-offer")
	public ResponseEntity<?> searchJoinOffer(@PathVariable Long id,@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchPlayerJoinOffer(id,condition, page),HttpStatus.OK);
	}
}
