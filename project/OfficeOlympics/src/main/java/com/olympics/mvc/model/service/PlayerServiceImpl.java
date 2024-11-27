package com.olympics.mvc.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olympics.mvc.model.dao.PlayerDao;
import com.olympics.mvc.model.dto.OlympicsSetup;
import com.olympics.mvc.model.dto.Player;

@Service
@Transactional(rollbackFor = {Exception.class})
public class PlayerServiceImpl implements PlayerService{
	
	private final PlayerDao playerDao;
	
	public PlayerServiceImpl(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	
	@Override
	@Transactional
	public int insertOlympics(int userId, String olympicsName) {
		OlympicsSetup olympics = new OlympicsSetup();
		// olympics 기본 설정
	    olympics.setUserId(userId);
	    olympics.setOlympicsName(olympicsName);

	    playerDao.insertOlympics(olympics); // 자동 생성된 olympicsId가 olympics 객체에 설정됨
	    return olympics.getOlympicsId(); // 생성된 olympics ID 반환
	}


	@Override
	@Transactional
	public boolean addPlayers(List<Player> players) {
		int result = playerDao.addPlayers(players);
		return result == players.size();
	}
	

	@Override
	@Transactional
	public boolean modifyOlympics(OlympicsSetup olympicsSetup) {
		
		List<Integer> playerIds = playerDao.getPlayerIdByOlympics(olympicsSetup.getOlympicsId());
		
		List<Map<String, Object>> playerDataList = new ArrayList<>();
		for (int i = 0; i < olympicsSetup.getPlayerNames().size(); i++) {
			Map<String, Object> playerData = new HashMap<>();
			playerData.put("playerName", olympicsSetup.getPlayerNames().get(i));
			playerData.put("playerId", playerIds.get(i));
			playerDataList.add(playerData);
		}
		
		Map<String, Object> params = new HashMap<>();
	    params.put("olympicsId", olympicsSetup.getOlympicsId());
	    params.put("playerDataList", playerDataList);
	    
	    // 플레이어 수정 결과
	    int playersModified = playerDao.modifyPlayers(params);
	    
	    // 올림픽 이름 수정
	    params.remove("playerNames");
	    params.remove("playerId");
	    params.put("olympicsName", olympicsSetup.getOlympicsName());
	    int nameModified = playerDao.modifyOlympics(params);
	    
	    return playersModified > 0 && nameModified > 0;
	}
	

	@Override
	public List<Map<String, Object>> getPlayersByOlympicsId(int olympicsId) {
		return playerDao.getPlayersByOlympicsId(olympicsId);
	}
	

	@Override
	public int findOlympicsIdByUserId(int userId) {
		Integer olympicsId = playerDao.findOlympicsIdByUserId(userId);
		if (olympicsId == null) {
			olympicsId = 0;
		}
		return olympicsId;
	}


	@Override
	public int getOlympicCreatorUserId(int olympicsId) {
		return playerDao.getOlympicCreatorUserId(olympicsId);
	}

	
	@Override
	@Transactional
	public boolean deleteOlympics(int id) {
		return playerDao.deleteOlympics(id) == 1;
	}
}
