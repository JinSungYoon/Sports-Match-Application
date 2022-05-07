package core.player.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.player.dto.PlayerDto;
import core.player.service.PlayerService;

@RestController
@RequestMapping(value="/player")
public class PlayerController {
	
	private final PlayerService playerService;
	
	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	};
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody PlayerDto dto){
		return new ResponseEntity<>(playerService.registerPlayer(dto),HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> searchOnePlayer(@PathVariable Long id){
		return new ResponseEntity<>(playerService.searchOnePlayer(id),HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> searchPlayerAll(){
		return new ResponseEntity<>(playerService.searchPlayerAll(),HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id,@RequestBody PlayerDto dto){
		return new ResponseEntity<>(playerService.updatePlayer(id, dto),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		return new ResponseEntity<>(playerService.deletePlyaer(id),HttpStatus.OK);
	};
	
}
