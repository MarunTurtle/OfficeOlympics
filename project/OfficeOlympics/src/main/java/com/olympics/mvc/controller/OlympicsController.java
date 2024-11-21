package com.olympics.mvc.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	
	
	/**
	 * 올림픽 생성 폼 반환
	 * 
	 * @return 올림픽 생성 페이지 문자열
	 */
	@GetMapping("")
	@Operation(summary = "올림픽 생성 폼 반환", description = "올림픽 생성 폼을 반환합니다.")
	public ResponseEntity<String> getOlympicsForm(){
		return ResponseEntity.ok("올림픽 생성 폼 반환");
	}
	
	
	/**
	 * 올림픽 생성 로직
	 * 
	 * @param setup   올림픽 생성 정보 (사용자 ID, 올림픽 이름, 플레이어 이름 목록)
	 * @param session 현재 사용자 세션
	 * @return 생성된 올림픽 데이터 (메시지, 올림픽 ID, 플레이어 수)
	 */
	@PostMapping("")
    @Operation(summary = "올림픽 생성 로직", description = "올림픽 이름과 플레이어 이름을 JSON 형식으로 전송하여 팀 생성 및 멤버 추가를 수행합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "올림픽 생성 성공"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<?> generateOlympics(@RequestBody OlympicsSetup setup, HttpSession session) {

		Integer userId = (Integer) session.getAttribute("loginUserId");

        setup.setUserId(userId);
        
        int olympicsId = playerService.insertOlympics(setup.getUserId(), setup.getOlympicsName());
        
        List<Player> players = new ArrayList<>();
        if (setup.getPlayerNames() != null) {
            for (String playerName : setup.getPlayerNames()) {
                if (playerName != null && !playerName.trim().isEmpty()) {
                    players.add(new Player(olympicsId, 0, playerName, 0, null));
                }
            }
            if (!players.isEmpty()) {
                playerService.addPlayers(players);
            }
        }
        
        Map<String, Object> olympicsData = new HashMap<>();
        olympicsData.put("message", "Olympic 생성이 완료되었습니다.");
        olympicsData.put("olympicsId", olympicsId);
        olympicsData.put("playerCount", players.size());

        return ResponseEntity.ok(olympicsData);
    }
	
	
    /**
     * 올림픽 팀 삭제 로직
     * 
     * @param olympicsId 삭제할 올림픽 ID
     * @param session 	 현재 사용자 세션
     * @return 삭제 결과 메시지 또는 상태 코드
     */
	@DeleteMapping("/{olympicId}")
    @Operation(summary = "올림픽 삭제", description = "사용자 세션과 대조하여 본인이 생성한 올림픽만 삭제할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "올림픽 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청: 본인이 생성한 팀이 아님"),
        @ApiResponse(responseCode = "404", description = "올림픽을 찾을 수 없음"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    @Parameter(name = "olympicId", description = "삭제할 올림픽의 ID", required = true)
    public ResponseEntity<String> deleteOlympics(@PathVariable("olympicId") int olympicsId, HttpSession session) {
        
		Integer sessionUserId = (Integer) session.getAttribute("loginUserId");
        
        Integer creatorUserId = playerService.getOlympicCreatorUserId(olympicsId);
        if(!sessionUserId.equals(creatorUserId)) {
        	return  ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        boolean isDeleted = playerService.deleteOlympics(olympicsId);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}