package com.olympics.mvc.model.dao;

import java.util.List;
import java.util.Map;

import com.olympics.mvc.model.dto.OlympicsSetup;
import com.olympics.mvc.model.dto.Player;

public interface PlayerDao {

	void insertOlympics(OlympicsSetup olympics);

	int addPlayers(List<Player> players);

	List<Integer> nameToId(List<String> playerNames);

	int modifyPlayers(Map<String, Object> ModifyOlympic);

	int modifyOlympics(Map<String, Object> ModifyOlympic);

	List<Integer> getPlayerIdByOlympics(int olympicId);

	List<Map<String, Object>> getPlayersByOlympicsId(int olympicsId);

	Integer findOlympicsIdByUserId(int UserId);

	int getOlympicCreatorUserId(int olympicId);

	int deleteOlympics(int olympicId);
	
}
