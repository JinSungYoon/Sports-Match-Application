package core.player.service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import core.common.encryption.AES256Util;
import core.player.dto.PlayerDto;
import core.player.entity.PlayerEntity;
import core.player.repository.PlayerRepository;
import core.player.repository.PlayerRepositoryCustom;
import core.team.dto.TeamDto;
import core.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
	
	private final PlayerRepository playerRepository;
	private final TeamRepository teamRepository;
	private final PlayerRepositoryCustom playerReppositoryCustom;
	private final AES256Util aes256Util;
	
	@Override
	@Transactional(readOnly=true)	// JPA 변경감지 내부 기능 비활성화, update시 정합성을 유지 
	public PlayerDto searchOnePlayer(Long id) {
		PlayerEntity playerEntity = playerRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Id가 존재하지 않습니다."));
		return playerEntity.toDto();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<PlayerDto> searchPlayerAll(Pageable pageable) {
		Page<PlayerEntity> entityList = playerRepository.findAll(pageable);
		List<PlayerDto> dtoList = entityList.stream().map(PlayerEntity::toDto).collect(Collectors.toList());
		return dtoList;
	}

	
	@Override
	@Transactional
	public PlayerDto registerPlayer(PlayerDto player) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		// 개인정보를 암호화 한다.
		player = personalInformationEncryption(player);
		
		// 만일 존재하는 Player ID라면 저장하지 않음.
		if(player.getTeam()!=null) {
			// player가 속한 team에 팀원을 조회하여, uniform번호가 중복되는지 확인.
			List<PlayerEntity> playerList =  playerRepository.findByTeam_teamName(player.getTeam().getTeamName());
			// 만일 팀이 존재하지 않는다면, 팀을 등록하고 선수를 등록해준다.
			if(playerList.size() == 0) {
				PlayerEntity playerEntity = playerRepository.save(player.toEntity());
				return playerEntity.toDto();
			}else {
				List<Integer> uniformNoList =  playerList.stream().map(PlayerEntity::getUniformNo).collect(Collectors.toList());
				// 같은 팀에 동일한 유니폼 번호를 가질 수 없으므로 검사 필요.
				if(uniformNoList.contains(player.getUniformNo())) {
					//throw new Exception("Uniform 번호는 중복 될 수 없습니다.");
					throw new ResponseStatusException (HttpStatus.BAD_REQUEST,"Uniform 번호는 중복 될 수 없습니다.",new IllegalArgumentException());
				}else {
					PlayerEntity playerEntity = playerRepository.save(player.toEntity());
					return playerEntity.toDto();
				}
			}
		}else {
			PlayerEntity playerEntity = playerRepository.save(player.toEntity());
			return playerEntity.toDto();
		}
	}
	

	@Override
	@Transactional
	public List<PlayerDto> registerPlayers(List<PlayerDto> players) throws Exception {
		// 개인정보를 암호화 한다.
		players = players.stream().map(item-> {
			try {
				return personalInformationEncryption(item);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return item;
		}).collect(Collectors.toList());
		
		List<PlayerDto> list= new ArrayList<PlayerDto>();
		
		// team이 존재하는 선수들 먼저 저장.
		Map<TeamDto, List<PlayerDto>> havingTeam =  players.stream().filter(dto->dto.getTeam()!=null).collect(Collectors.groupingBy(dto->dto.getTeam()));
		for(TeamDto team : havingTeam.keySet()) {
			List<PlayerEntity> existingPlayerList = playerRepository.findByTeam_teamName(team.getTeamName());
			List<Integer> existingUniformList =  existingPlayerList.stream().map(PlayerEntity::getUniformNo).collect(Collectors.toList());
			List<Integer> newUniformList = havingTeam.get(team).stream().map(PlayerDto::getUniformNo).collect(Collectors.toList());
			// DB에 존재하는 값과 새로 등록하려는 값의 중복값만 existingUniformList에 남긴다. 중복값이 있으면 error message를 / 중복값이 없으면 저장을 한다.
			existingUniformList.retainAll(newUniformList);
			if(existingUniformList.isEmpty()) {
				List<PlayerEntity> newPlayerEntityList =  havingTeam.get(team).stream().map(PlayerDto::toEntity).collect(Collectors.toList());
				List<PlayerEntity> savePlayerEntityList = playerRepository.saveAll(newPlayerEntityList);
				List<PlayerDto> dtoList =  savePlayerEntityList.stream().map(item->item.toDto()).collect(Collectors.toList());
				list.addAll(dtoList);
			}else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Uniform 번호와 중복 될 수 없습니다.\n"+existingUniformList.toString(),new Exception());
			}
		}
		// team이 존재하지 않는 선수들 저장.
		List<PlayerDto> nonHavingTeam =  players.stream().filter(dto->dto.getTeam()==null).collect(Collectors.toList());
		List<PlayerEntity> newPlayerEntityList =  nonHavingTeam.stream().map(PlayerDto::toEntity).collect(Collectors.toList());
		List<PlayerEntity> savePlayerEntityList = playerRepository.saveAll(newPlayerEntityList);
		List<PlayerDto> dtoList =  savePlayerEntityList.stream().map(item->item.toDto()).collect(Collectors.toList());
		list.addAll(dtoList);
		
		return list;
		
	}
	
	@Override
	@Transactional
	public PlayerDto updatePlayer(Long id,PlayerDto player) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException, IllegalArgumentException{
		// 개인정보를 암호화 한다.
		player = personalInformationEncryption(player);

		// DB에 Id에 해당하는 Player가 존재하는지 찾은 후 Update를 진행한다.
		PlayerEntity originPlayer = playerRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Id가 존재하지 않습니다.",new IllegalArgumentException()));
		originPlayer.setPlayerEntity(player);
		return originPlayer.toDto();
	}
	
	
	@Override
	@Transactional
	public Long deletePlyaer(Long id) {
		playerRepository.deleteById(id);
		return id;
	}


	@Override
	public PlayerDto personalInformationEncryption(PlayerDto player) throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException {
		// 암호화할 개인정보 정보를 암호화 후 다시 셋팅한다.
		player.setResRegNo(aes256Util.encrypt(player.getResRegNo()));
		return player;
	}

	@Override
	public List<PlayerDto> searchPlayers(String playerName,Integer uniformNo,String teamName,Pageable pageable) {
		List<PlayerEntity> list = playerReppositoryCustom.findPlayer(playerName, uniformNo, teamName,pageable);
		return list.stream().map(item->item.toDto()).collect(Collectors.toList());
	}

}
