package core.join.service;

import org.springframework.stereotype.Service;

import core.common.encryption.AES256Util;
import core.join.dto.JoinDto;
import core.join.entity.JoinEntity;
import core.join.repository.JoinRepository;
import core.player.repository.PlayerRepository;
import core.player.repository.PlayerRepositoryCustom;
import core.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinServiceImpl implements JoinService {

	private final JoinRepository joinRepository;
	
	@Override
	public JoinDto requestJoin(JoinDto joinDto) {
		JoinEntity join = joinRepository.save(joinDto.toEntity());
		return join.toDto();
	}

	@Override
	public JoinDto rejectJoin(JoinDto joinDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JoinDto approveJoin(JoinDto joinDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JoinDto confirmApprove(JoinDto joinDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JoinDto rejectApprove(JoinDto joinDto) {
		// TODO Auto-generated method stub
		return null;
	}

}
