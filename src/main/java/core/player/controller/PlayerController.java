package core.player.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import core.player.dto.PlayerDto;
import core.player.entity.PlayerEntity;
import core.player.service.PlayerService;

@RestController
public class PlayerController {
	
	private final PlayerService playerService;
	
	public PlayerController(final PlayerService playerService) {
		this.playerService = playerService;
	};
	
	@PostMapping("/player")
	public ResponseEntity<?> register(@RequestBody PlayerDto dto){
		return new ResponseEntity<>(playerService.registerPlayer(dto),HttpStatus.OK);
	}

	@GetMapping("/player/{id}")
	public ResponseEntity<?> findPlayer(@PathVariable Long id){
		return new ResponseEntity<>(playerService.findOnePlayer(id),HttpStatus.OK);
	}
	
	@GetMapping("/players")
	public ResponseEntity<?> findAll(){
		return new ResponseEntity<>(playerService.findPlayerAll(),HttpStatus.OK);
	}
	
	@PutMapping("/player/{id}")
	public ResponseEntity<?> update(@PathVariable Long id,@RequestBody PlayerDto dto){
		return new ResponseEntity<>(playerService.updatePlayer(id, dto),HttpStatus.OK);
	}
	
	@DeleteMapping("/player/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		return new ResponseEntity<>(playerService.deletePlyaer(id),HttpStatus.OK);
	};
	
}
