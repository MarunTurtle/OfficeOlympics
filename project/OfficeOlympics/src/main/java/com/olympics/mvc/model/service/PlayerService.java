package com.olympics.mvc.model.service;

import java.util.List;
import java.util.Map;

import com.olympics.mvc.model.dto.OlympicsSetup;
import com.olympics.mvc.model.dto.Player;

public interface PlayerService {

	// 올림픽 팀 생성
	int insertOlympics(int userId, String olympicsName);
	
	// 올림픽 팀 설정
	boolean addPlayers(List<Player> players); 
	
	// 올림픽 팀 수정
	boolean modifyOlympics(OlympicsSetup olympicsSetup);
	
	// 올림픽 팀원 조회 (올림픽 ID 기준)
	List<Map<String, Object>> getPlayersByOlympicsId(int olympicsId);
	
	// 올림픽 ID 조회 (사용자 ID 기준)
	int findOlympicsIdByUserId(int userId);
	
	// 올림픽을 생성한 사용자 ID 조회
	int getOlympicCreatorUserId(int olympicsId);
	
	// 올림픽 팀 삭제
	boolean deleteOlympics(int olympicsId);
	
}
