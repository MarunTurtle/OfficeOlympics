package com.olympics.mvc.model.service;

import java.util.List;
import java.util.Map;

import com.olympics.mvc.model.dto.OlympicsSetup;
import com.olympics.mvc.model.dto.Player;

public interface PlayerService {

	int insertOlympics(int userId, String olympicsName);

	boolean addPlayers(List<Player> players); 

	boolean modifyOlympics(OlympicsSetup olympicsSetup);

	List<Map<String, Object>> getPlayersByOlympicsId(int olympicsId);

	int findOlympicsIdByUserId(int userId);

	int getOlympicCreatorUserId(int olympicsId);

	boolean deleteOlympics(int olympicsId);
	
}
