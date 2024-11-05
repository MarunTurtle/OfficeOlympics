package com.olympics.mvc.model.service;

import java.util.List;

import com.olympics.mvc.model.dto.Player;

public interface PlayerService {

	// 올림픽 팀 생성
	int insertOlympics(int userId, String olympicsName);
		
	// 올림픽 팀 설정
	boolean insertPlayers(List<Player> players); 
		
	// 올림픽 팀원 조회
	List<Player> getPlayersByOlympicsId(int olympicsId);

	// 해당 올림픽 생성한 user 조회
	int selectGenerateUserId(int olympicsId);
	
	// 올림픽 팀 삭제
	boolean deleteOlympics(int id);
	
}
