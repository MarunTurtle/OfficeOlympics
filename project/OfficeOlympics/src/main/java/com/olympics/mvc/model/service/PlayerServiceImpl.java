package com.olympics.mvc.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.olympics.mvc.model.dao.PlayerDao;
import com.olympics.mvc.model.dto.Player;

@Service
public class PlayerServiceImpl implements PlayerService{
	
	private final PlayerDao playerDao;
	
	public PlayerServiceImpl(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	@Override
	public int insertOlympics(int userId, String olympicsName) {
		Player olympics = new Player();
	    olympics.setUser_id(userId);
	    olympics.setOlympics_name(olympicsName);

	    playerDao.insertOlympics(olympics); // 자동 생성된 olympicsId가 olympics 객체에 설정됨
	    return olympics.getOlympics_id(); // 생성된 ID 반환
	}
	
	@Override
	public int getOlympicsIdByUserId(int userId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertPlayers(List<Player> players) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Player> getPlayersByOlympicsId(int olympicsId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteOlympics(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
