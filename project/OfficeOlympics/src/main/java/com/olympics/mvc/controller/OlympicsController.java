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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
	
	
	
	// 올림픽 팀 생성 및 설정
	@PostMapping("/team")
    @Operation(summary = "올림픽 팀 생성 로직", description = "올림픽 팀 이름, 멤버 이름을 json형식으로 전송하여 팀 생성 후 멤버를 추가합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "올림픽 팀 생성 성공"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
	@Parameters({
        @Parameter(name = "userId", description = "팀 생성할 사용자의 ID", required = true),
        @Parameter(name = "olympicsName", description = "팀 이름", required = true),
        @Parameter(name = "playerNames", description = "플레이어 이름 (List형태)", required = true),
    })
    public ResponseEntity<String> generateOlympics(@RequestBody OlympicsSetup setup, HttpSession session) {
        // 세션에서 로그인된 사용자 ID를 가져와 설정 정보에 추가
        setup.setUserId((int) session.getAttribute("loginUserId"));
        
        // 새로운 올림픽 팀 생성 및 ID 반환
        int olympicsId = playerService.insertOlympics(setup.getUserId(), setup.getOlympicsName());
        
        // 플레이어 목록을 설정 정보에서 받아와 Player 객체로 변환 후 리스트에 추가
        List<Player> players = new ArrayList<>();
        if (setup.getPlayerNames() != null) {
            for (String playerName : setup.getPlayerNames()) {
                if (playerName != null && !playerName.trim().isEmpty()) { // 프론트에서 유효성 검사 하면 해당 조건문없이 바로 추가하는 작업 진행 가능
                    players.add(new Player(olympicsId, 0, playerName, 0, null));
                }
            }
        }
        
        // 비어 있지 않은 경우 플레이어 목록을 DB에 일괄 삽입
        if (!players.isEmpty()) {
            playerService.insertPlayers(players);
        }

        return new ResponseEntity<>("Olympics 팀 생성 완료", HttpStatus.OK);
    }
		
	
	
    // 특정 올림픽 팀의 플레이어 조회
	@GetMapping("/team/{olympicId}")
    @Operation(summary = "특정 올림픽 팀의 플레이어 조회", description = "올림픽 id 값을 통해 해당 올림픽 내의 플레이어를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "플레이어 목록 반환"),
        @ApiResponse(responseCode = "204", description = "플레이어 목록 없음"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    @Parameter(name = "olympicId", description = "조회할 올림픽 팀의 ID", required = true)
    public ResponseEntity<?> getPlayers(@PathVariable("olympicId") int olympicId) {
        // 주어진 올림픽 ID에 속한 모든 플레이어를 조회
        List<Player> players = playerService.getPlayersByOlympicsId(olympicId);
        
        // 플레이어가 없는 경우 204 No Content 상태 코드 반환
        if (players.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // 플레이어가 있는 경우 목록과 함께 200 OK 상태 코드 반환
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

	
	
    // 올림픽 팀 삭제
	@DeleteMapping("/team/{olympic_id}")
    @Operation(summary = "올림픽 팀 삭제", description = "session에 등록된 userId를 대조하여 해당 userId가 생성한 팀일 경우에만 팀을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "올림픽 팀 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청: 본인이 생성한 팀이 아님"),
        @ApiResponse(responseCode = "404", description = "올림픽 팀을 찾을 수 없음"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    @Parameter(name = "olympicId", description = "삭제할 올림픽 팀의 ID", required = true)
    public ResponseEntity<String> deleteOlympics(@PathVariable("olympicId") int olympicsId, HttpSession session) {
        Integer sessionUserId = (Integer) session.getAttribute("loginUserId");
        
        // 세션의 사용자 ID와 팀 생성자 ID를 비교하여 일치하는 경우에만 삭제
        if (sessionUserId == playerService.selectGenerateUserId(olympicsId)) {
            boolean isDeleted = playerService.deleteOlympics(olympicsId);
            if (isDeleted) {
                return ResponseEntity.noContent().build(); // 성공 시 204 No Content 반환
            }
            return new ResponseEntity<>("올림픽 팀을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);        		
        }
        // 요청한 사용자가 생성한 팀이 아닌 경우 400 Bad Request 반환
        return ResponseEntity.badRequest().build();
    }
}