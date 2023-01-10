package core.api.player.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.api.join.dto.JoinDto;
import core.api.join.dto.JoinSearchCondition;
import core.api.join.service.JoinService;
import core.api.player.dto.PlayerDto;
import core.api.player.dto.PlayerListDto;
import core.api.player.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name ="PLAYER API",description = "Player와 관련된 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlayerController {
	
	private final PlayerService playerService;
	private final JoinService joinService;
	
	// player 등록
	@Tag(name ="PLAYER API")
	@Operation(summary = "Register player information",description="player 정보를 등록하는 API 입니다.")
	@PostMapping("/player")
	public ResponseEntity<?> register(@RequestBody PlayerDto dto) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException{
		return new ResponseEntity<>(playerService.registerPlayer(dto),HttpStatus.CREATED);
	}
	
	// player 여러명 등록
	@Tag(name ="PLAYER API")
	@Operation(summary ="Register multi-player information",description = "여러명의 player 정보를 등록할 수 있는 API 입니다.")
	@PostMapping("/players")
	public ResponseEntity<?> registers(@RequestBody PlayerListDto players) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException, Exception{
		return new ResponseEntity<>(playerService.registerPlayers(players.getPlayers()),HttpStatus.CREATED);
	}

	// player 한명 찾기
	@Tag(name ="PLAYER API")
	@Operation(summary ="Search player",description = "Player를 찾는 API입니다.")
	@GetMapping("/player/{id}")
	public ResponseEntity<?> searchOnePlayer(@PathVariable Long id){
		return new ResponseEntity<>(playerService.searchOnePlayer(id),HttpStatus.OK);
	}
	
	// 모든 player 정보 찾기
	@Tag(name ="PLAYER API")
	@Operation(summary ="Search player all",description = "모든 Player의 정보를 찾는 API 입니다.")
	@GetMapping("/players/all")
	public ResponseEntity<?> searchPlayerAll(@PageableDefault(page = 0, size = 10) Pageable pageable){
		return new ResponseEntity<>(playerService.searchPlayerAll(pageable),HttpStatus.OK);
	}
	
	// player 정보 찾기
	@Tag(name ="PLAYER API")
	@Operation(summary ="Search players",description = "조건에 해당하는 여러명의 Player 정보를 찾을 수 있는 API입니다.")
	@GetMapping("/players")
	public ResponseEntity<?> searchPlayers(@RequestParam(value="playerName",required=false)String playerName,@RequestParam(value="uniformNo",required=false)Integer uniformNo,@RequestParam(value="teamName",required=false)String teamName,@PageableDefault(page=0,size=10) Pageable pageable){
		return new ResponseEntity<>(playerService.searchPlayers(playerName, uniformNo, teamName,pageable),HttpStatus.OK);
	}
	
	// player 정보 업데이트
	@Tag(name ="PLAYER API")
	@Operation(summary ="Update player information",description = "Player의 정보를 업데이트 하는 API 입니다.")
	@PatchMapping("/player/{id}")
	public ResponseEntity<?> update(@PathVariable Long id,@RequestBody PlayerDto dto) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException{
		return new ResponseEntity<>(playerService.updatePlayer(id, dto),HttpStatus.OK);
	}
	
	// player 삭제
	@Tag(name ="PLAYER API")
	@Operation(summary ="Delete player",description = "Player의 정보를 삭제하는 API입니다.")
	@DeleteMapping("/player/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		return new ResponseEntity<>(playerService.deletePlyaer(id),HttpStatus.OK);
	};	
	
	// player 가입 신청
	@Tag(name ="PLAYER API")
	@Operation(summary ="Request join",description = "Player가 다른 팀에 가입을 요청하는 API입니다.")
	@PostMapping("/player/{id}/request-join")
	public ResponseEntity<?> requestJoin(@PathVariable Long id,@RequestBody JoinDto dto) throws Exception{
		return new ResponseEntity<>(joinService.requestPlayerJoin(id, dto),HttpStatus.CREATED);
	}
	
	// player 가입 제안 거절
	@Tag(name ="PLAYER API")
	@Operation(summary ="Reject join",description = "요청 온 가입을 거절하는 API입니다.")
	@PatchMapping("/player/{id}/reject-join/{teamId}")
	public ResponseEntity<?> rejectJoin(@PathVariable Long id,@PathVariable Long teamId) throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setPlayerId(id);
		joinDto.setTeamId(teamId);
		return new ResponseEntity<>(joinService.rejectTeamJoin(joinDto),HttpStatus.OK);
	}
	
	// player 가입 제안 승인
	@Tag(name ="PLAYER API")
	@Operation(summary ="Approve join",description = "요청 온 가입을 승인하는 API입니다.")
	@PatchMapping("/player/{id}/approve-join/{teamId}")
	public ResponseEntity<?> approveJoin(@PathVariable Long id,@PathVariable Long teamId) throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setPlayerId(id);
		joinDto.setTeamId(teamId);
		return new ResponseEntity<>(joinService.approveTeamJoin(joinDto),HttpStatus.OK);
	}
	
	// player 가입 제안 철회
	@Tag(name ="PLAYER API")
	@Operation(summary ="Withdraw join",description = "요청한 가입을 철회하는 API입니다.")
	@PatchMapping("/player/{id}/withdraw-approve/{teamId}")
	public ResponseEntity<?> withdrawJoin(@PathVariable Long id,@PathVariable Long teamId) throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setPlayerId(id);
		joinDto.setTeamId(teamId);
		return new ResponseEntity<>(joinService.withdrawPlayerApprove(joinDto),HttpStatus.OK);
	}
	
	
	// player 가입 제안 반려
	@Tag(name ="PLAYER API")
	@Operation(summary ="Return join",description = "승인된 제안을 반려하는 API입니다.")
	@PatchMapping("/player/{id}/return-join/{teamId}")
	public ResponseEntity<?> returnJoin(@PathVariable Long id,@PathVariable Long teamId) throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setPlayerId(id);
		joinDto.setTeamId(teamId);
		return new ResponseEntity<>(joinService.returnPlayerApprove(joinDto),HttpStatus.OK);
	}
	
	// player 가입 제안 확정 
	@Tag(name ="PLAYER API")
	@Operation(summary ="Confirm join",description="승인된 제안을 확정하는 API입니다.")
	@PatchMapping("/player/{id}/confirm-join/{teamId}")
	public ResponseEntity<?> confirmJoin(@PathVariable Long id, @PathVariable Long teamId)throws Exception{
		JoinDto joinDto = new JoinDto();
		joinDto.setPlayerId(id);
		joinDto.setTeamId(teamId);
		return new ResponseEntity<>(joinService.confirmTeamApprove(joinDto),HttpStatus.OK);
	}
	
	// player가 신청한 join 조회
	@Tag(name ="PLAYER API")
	@Operation(summary ="Search player join application",description = "Player가 요청한 제안을 찾는 API입니다.")
	@GetMapping("/player/{id}/search-join-application")
	public ResponseEntity<?> searchJoinApplication(@PathVariable Long id,@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchPlayerJoinApplication(id,condition, page),HttpStatus.OK);
	}
	
	// player에게 신청된 join 조회
	@Tag(name ="PLAYER API")
	@Operation(summary ="Search player join offer",description = "Player에게 요청된 제안을 찾는 API입니다.")
	@GetMapping("/player/{id}/search-join-offer")
	public ResponseEntity<?> searchJoinOffer(@PathVariable Long id,@RequestBody JoinSearchCondition condition,@PageableDefault(page = 0, size = 10) Pageable page){
		return new ResponseEntity<>(joinService.searchPlayerJoinOffer(id,condition, page),HttpStatus.OK);
	}
}
