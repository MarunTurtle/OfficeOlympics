package com.olympics.mvc.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olympics.mvc.model.dto.OlympicsSetup;
import com.olympics.mvc.model.dto.Player;
import com.olympics.mvc.model.service.PlayerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

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
    @Operation(summary = "올림픽 팀 생성 로직", description = "올림픽 팀 이름, 멤버 이름을 json형식으로 전송하여 팀 생성 후 멤버를 추가합니다.")
    public ResponseEntity<String> generateOlympics(@RequestBody OlympicsSetup setup, HttpSession session) {
        try {
        	System.out.println(session.getAttribute("loginUserId"));
        	setup.setUserId((int) session.getAttribute("loginUserId"));
        	// 사용자가 새로운 올림픽 팀을 생성
            int olympicsId = playerService.insertOlympics(setup.getUserId(), setup.getOlympicsName());
            System.out.println(olympicsId);
            // 플레이어 목록을 Player 객체로 변환하여 List에 추가
            List<Player> players = new ArrayList<>();
            if (setup.getPlayerNames() != null) {
                for (String playerName : setup.getPlayerNames()) {
                    if (playerName != null && !playerName.trim().isEmpty()) { // 프론트에서 유효성 검사 하면 해당 조건문없이 바로 추가하는 작업 진행 가능
                    	// 유효한 플레이어 이름이 있는 경우 Player 객체로 변환하여 추가
                        players.add(new Player(olympicsId, 0, playerName, 0, null));
                    }
                }
            }
            
            // 플레이어 목록이 비어있지 않은 경우에 DB에 일괄 삽입
            if (!players.isEmpty()) {
                playerService.insertPlayers(players);
            }

            return new ResponseEntity<>("Olympics 팀 생성 완료", HttpStatus.OK);
            
        } catch(DataAccessException dae) {
        	// 데이터 접근 예외 발생 시 처리
        	dae.printStackTrace();
            return new ResponseEntity<>("데이터베이스 오류가 발생했습니다.", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
        	// 예외 발생 시 에러 메시지와 500 상태 코드 반환
        	e.printStackTrace();
            return new ResponseEntity<>("요청 처리 중 오류가 발생했습니다. 관리자에게 문의하세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
		
	
    // 특정 올림픽 팀의 플레이어 조회
    @GetMapping("/team/{olympic_id}")
    @Operation(summary = "특정 올림픽 팀의 플레이어 조회", description = "올림픽 id 값을 통해 해당 올림픽 내의 플레이어를 조회합니다.")
    public ResponseEntity<?> getPlayers(@PathVariable("olympic_id") int olympicId) {
        try {
        	// 올림픽 ID에 속한 플레이어 목록 조회
            List<Player> players = playerService.getPlayersByOlympicsId(olympicId);
            if (players.isEmpty()) {
                return ResponseEntity.noContent().build(); // No Content 상태 코드 반환
            }
            return new ResponseEntity<>(players, HttpStatus.OK);
        } catch (DataAccessException dae) {
            return new ResponseEntity<>("데이터베이스 오류가 발생했습니다.", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("요청 처리 중 오류가 발생했습니다. 관리자에게 문의하세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	
    // 올림픽 팀 삭제
    @DeleteMapping("/team/{olympic_id}")
    @Operation(summary = "올림픽 팀 삭제", description = "session에 등록된 userId를 대조하여 해당 userId가 생성한 팀일 경우에만 팀을 삭제합니다.")
    public ResponseEntity<String> deleteOlympics(@PathVariable("olympic_id") int olympicsId, HttpSession session) {
        try {
        	
        	Integer sessionUserId = (Integer) session.getAttribute("loginUserId");
        	
        	// 해당 유저가 만든 올림픽 팀만 삭제 가능하도록 확인
        	if (sessionUserId == playerService.selectGenerateUserId(olympicsId)) {
        		// 올림픽 ID에 해당하는 팀 삭제 시도
        		boolean isDeleted = playerService.deleteOlympics(olympicsId);
        		if (isDeleted) {
        			return ResponseEntity.noContent().build(); // 성공 시 No Content 반환
        		}
        		return new ResponseEntity<>("올림픽 팀을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);        		
        	}
//        	return new ResponseEntity<>("본인이 생성한 팀만 삭제 가능.", HttpStatus.BAD_REQUEST);
        	return ResponseEntity.badRequest().build(); // 다른 사람이 만든 팀을 삭제하려했을경우 Bad Request 반환
        } catch (DataAccessException dae) {
            return new ResponseEntity<>("데이터베이스 오류가 발생했습니다.", HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("요청 처리 중 오류가 발생했습니다. 관리자에게 문의하세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}