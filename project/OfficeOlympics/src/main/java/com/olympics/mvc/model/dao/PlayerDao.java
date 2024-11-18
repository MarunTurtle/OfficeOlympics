package com.olympics.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.olympics.mvc.model.dto.OlympicsSetup;
import com.olympics.mvc.model.dto.Player;

public interface PlayerDao {
	
	// 올림픽 팀 생성
	void insertOlympics(OlympicsSetup olympics);
	
	// 올림픽 팀 설정
	int addPlayers(List<Player> players);
	
	// 이름->id 추출
	List<Integer> nameToId(List<String> playerNames);
	
	// 올림픽 팀 멤버 수정
	int modifyPlayers(Map<String, Object> ModifyOlympic);
	
	// 올림픽 팀 이름 수정
	int modifyOlympics(Map<String, Object> ModifyOlympic);
	
	// 올림픽 playerID 조회
	List<Integer> getPlayerIdByOlympics(int olympicId);
	
	// 올림픽 팀원 조회 (올림픽 ID 기준)
	List<Map<String, Object>> getPlayersByOlympicsId(int olympicsId);
	
	// 올림픽 ID 조회 (사용자 ID 기준)
	Integer findOlympicsIdByUserId(int UserId);
	
	// 올림픽을 생성한 사용자 ID 조회
	int getOlympicCreatorUserId(int olympicId);
	
	// 올림픽 팀 삭제
	int deleteOlympics(int olympicId);
	
}
