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
public class PlayerServiceImpl implements PlayerService{
	
	private final PlayerDao playerDao;
	
	public PlayerServiceImpl(PlayerDao playerDao) {
		this.playerDao = playerDao;
	}
	
	// 올림픽 팀 생성
	@Transactional
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

	// 올림픽 팀 설정
	@Transactional
	@Override
	public boolean addPlayers(List<Player> players) {
		int result = playerDao.addPlayers(players);
		return result == players.size();
	}
	
	// 올림픽 팀 수정
	@Transactional
	@Override
	public boolean modifyOlympics(OlympicsSetup olympicsSetup) {
		
		List<Integer> playerIds = playerDao.nameToId(olympicsSetup.getPlayerNames());
		
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
	
	// 올림픽 팀원 조회 (올림픽 ID 기준)
	@Override
	public List<Player> getPlayersByOlympicsId(int olympicsId) {
		return playerDao.getPlayersByOlympicsId(olympicsId);
	}
	

	// 올림픽 ID 조회 (사용자 ID 기준)
	@Override
	public int findOlympicsIdByUserId(int userId) {
		return playerDao.findOlympicsIdByUserId(userId);
	}

	// 올림픽을 생성한 사용자 ID 조회
	@Override
	public int getOlympicCreatorUserId(int olympicsId) {
		return playerDao.getOlympicCreatorUserId(olympicsId);
	}
	
	// 올림픽 팀 삭제
	@Transactional
	@Override
	public boolean deleteOlympics(int id) {
		int isDeleted = playerDao.deleteOlympics(id);
		return isDeleted == 1;
	}
}
