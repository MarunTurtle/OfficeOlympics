package com.olympics.mvc.model.dao;

import java.util.List;

import com.olympics.mvc.model.dto.OlympicsSetup;
import com.olympics.mvc.model.dto.Player;

public interface PlayerDao {
	
	// 올림픽 팀 생성 (성공 시 1 반환)
	void insertOlympics(OlympicsSetup olympics);
	
	// 올림픽 팀 설정 (성공한 행 수 반환)
	int insertPlayers(List<Player> players);
	
	// 특정 올림픽에 속한 모든 플레이어 조회
	List<Player> getPlayersByOlympicsId(int olympics_id);
	
	// 해당 올림픽 생성한 user 조회
	int selectGenerateUserId(int olympicId);
	
	// 올림픽 팀 삭제 (성공 시 1 반환)
	int deleteOlympics(int id);
	
}
