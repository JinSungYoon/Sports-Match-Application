package core.player.controller;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.player.dto.PlayerDto;
import core.player.dto.PlayerListDto;
import core.player.service.PlayerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PlayerController {
	
	private final PlayerService playerService;
	
	@PostMapping("/player")
	public ResponseEntity<?> register(@RequestBody PlayerDto dto) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException{
		return new ResponseEntity<>(playerService.registerPlayer(dto),HttpStatus.CREATED);
	}
	
	@PostMapping("/players")
	public ResponseEntity<?> registers(@RequestBody PlayerListDto players) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException, Exception{
		return new ResponseEntity<>(playerService.registerPlayers(players.getPlayers()),HttpStatus.CREATED);
	}

	@GetMapping("/player/{id}")
	public ResponseEntity<?> searchOnePlayer(@PathVariable Long id){
		return new ResponseEntity<>(playerService.searchOnePlayer(id),HttpStatus.OK);
	}
	
	@GetMapping("/players/all")
	public ResponseEntity<?> searchPlayerAll(){
		return new ResponseEntity<>(playerService.searchPlayerAll(),HttpStatus.OK);
	}
	
	@GetMapping("/players")
	public ResponseEntity<?> searchPlayers(@RequestParam(value="playerName",required=false)String playerName,@RequestParam(value="uniformNo",required=false)Integer uniformNo,@RequestParam(value="teamName",required=false)String teamName){
		return new ResponseEntity<>(playerService.searchPlayers(playerName, uniformNo, teamName),HttpStatus.OK);
	}
	
	@PatchMapping("/player/{id}")
	public ResponseEntity<?> update(@PathVariable Long id,@RequestBody PlayerDto dto) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException{
		return new ResponseEntity<>(playerService.updatePlayer(id, dto),HttpStatus.OK);
	}
	
	@DeleteMapping("/player/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		return new ResponseEntity<>(playerService.deletePlyaer(id),HttpStatus.OK);
	};	
	
}
