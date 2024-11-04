package com.olympics.mvc.model.dao;

import java.util.List;

import com.olympics.mvc.model.dto.Player;

public interface PlayerDao {
	
	// 올림픽 팀 생성
	void insertOlympics(Player olympics);
	
	// 올림픽 팀 설정
	int insertPlayers(List<Player> players);
	
	// 특정 올림픽에 속한 모든 플레이어 조회
	List<Player> getPlayersByOlympicsId(int olympics_id);
	
	// 특정 player_id로 플레이어 조회
	Player getPlayerById(int playerId);

	// 올림픽 팀 삭제
	int deleteOlympics(int id);
	
}
