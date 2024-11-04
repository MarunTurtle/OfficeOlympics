package com.olympics.mvc.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olympics.mvc.model.dto.OlympicsSetup;
import com.olympics.mvc.model.dto.Player;
import com.olympics.mvc.model.service.PlayerService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/olympics")
@Tag(name="Olympics Restful API", description = "올림픽 팀 설정 CRUD")
public class OlympicsController {
	
	private final PlayerService playerService;

	public OlympicsController(PlayerService playerService) {
		super();
		this.playerService = playerService;
	}
	
	// 올림픽 팀 생성 및 설정
    @PostMapping("/team")
    public ResponseEntity<String> generateOlympics(@RequestBody OlympicsSetup setup) {
        try {
            int olympicsId = playerService.insertOlympics(setup.getUserId(), setup.getOlympicsName());

            List<Player> players = new ArrayList<>();
            if (setup.getPlayerNames() != null) {
                for (String playerName : setup.getPlayerNames()) {
                    if (playerName != null && !playerName.trim().isEmpty()) {
                        players.add(new Player(olympicsId, setup.getUserId(), playerName, 0, null));
                    }
                }
            }

            if (!players.isEmpty()) {
                playerService.insertPlayers(players);
            }

            return new ResponseEntity<>("Olympics 팀 생성 완료", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("요청 처리 중 오류가 발생했습니다. 관리자에게 문의하세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
		
	
 // 올림픽 팀원 조회
    @GetMapping("/team/{olympic_id}")
    public ResponseEntity<?> getPlayers(@PathVariable("olympic_id") int olympicId) {
        try {
            List<Player> players = playerService.getPlayersByOlympicsId(olympicId);
            if (players != null && !players.isEmpty()) {
                return new ResponseEntity<>(players, HttpStatus.OK);
            }
            return new ResponseEntity<>("등록된 팀원이 없습니다.", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("요청 처리 중 오류가 발생했습니다. 관리자에게 문의하세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	
 // 올림픽 팀 삭제
    @DeleteMapping("/team/{olympic_id}")
    public ResponseEntity<String> deleteOlympics(@PathVariable("olympic_id") int olympicId) {
        try {
            boolean res = playerService.deleteOlympics(olympicId);
            if (res) {
                return new ResponseEntity<>("Olympic 삭제가 완료되었습니다.", HttpStatus.OK);
            }
            return new ResponseEntity<>("Olympic 삭제 실패", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("요청 처리 중 오류가 발생했습니다. 관리자에게 문의하세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}