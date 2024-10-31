package com.olympics.mvc.model.dao;

import java.util.List;

import com.olympics.mvc.model.dto.Player;

public interface PlayerDao {
	
	// 올림픽 팀 설정
	int insertPlayers(List<Player> players); 
	
	// 올림픽 팀원 조회
	List<Player> getPlayersByOlympicsId(int olympics_id);

	// 올림픽 팀 삭제
	int deleteOlympics(int id);
	
}
