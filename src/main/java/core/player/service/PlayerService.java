package core.player.service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import core.player.dto.PlayerDto;

public interface PlayerService {
	PlayerDto searchOnePlayer(Long id);
	List<PlayerDto> searchPlayerAll();
	List<PlayerDto> searchPlayers(String playerName,Integer uniformNo,String teamName);
	PlayerDto registerPlayer(PlayerDto player) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException;
	List<PlayerDto> registerPlayers(List<PlayerDto> players) throws Exception;
	PlayerDto updatePlayer(Long id,PlayerDto player) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException;
	Long deletePlyaer(Long id);
	PlayerDto personalInformationEncryption(PlayerDto player) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException;
}
