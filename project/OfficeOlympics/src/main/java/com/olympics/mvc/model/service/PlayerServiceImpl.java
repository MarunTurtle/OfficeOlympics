package com.olympics.mvc.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.olympics.mvc.model.dao.PlayerDao;
import com.olympics.mvc.model.dto.OlympicsSetup;
import com.olympics.mvc.model.dto.Player;

@Service
public class PlayerServiceImpl implements PlayerService{
	
	private final PlayerDao playerDao;
	
	public PlayerServiceImpl(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}
	
	// 올림픽 팀 생성
	@Override
	public int insertOlympics(int userId, String olympicsName) {
		// 틀 생성
		OlympicsSetup olympics = new OlympicsSetup();
		// olympics 기본 설정
	    olympics.setUserId(userId);
	    olympics.setOlympicsName(olympicsName);

	    playerDao.insertOlympics(olympics); // 자동 생성된 olympicsId가 olympics 객체에 설정됨
	    return olympics.getOlympicsId(); // 생성된 olympics ID 반환
	}

	// 여러 플레이어 삽입
	@Override
	public boolean insertPlayers(List<Player> players) {
		int result = playerDao.insertPlayers(players);
		return result == players.size();
	}
	
	// 특정 올림픽의 팀원 조회
	@Override
	public List<Player> getPlayersByOlympicsId(int olympicsId) {
		return playerDao.getPlayersByOlympicsId(olympicsId);
	}
	
	// 올림픽 팀 삭제
	@Override
	public boolean deleteOlympics(int id) {
		int isDeleted = playerDao.deleteOlympics(id);
		return isDeleted == 1;
	}

	@Override
	public int selectGenerateUserId(int olympicsId) {
		return playerDao.selectGenerateUserId(olympicsId);
	}

}
