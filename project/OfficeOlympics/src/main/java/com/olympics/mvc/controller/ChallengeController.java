package com.olympics.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olympics.mvc.model.dto.Challenge;
import com.olympics.mvc.model.dto.Comments;
import com.olympics.mvc.model.dto.Player;
import com.olympics.mvc.model.dto.Rank;
import com.olympics.mvc.model.dto.Score;

import com.olympics.mvc.model.service.ChallengeScoreService;
import com.olympics.mvc.model.service.CommentsService;
import com.olympics.mvc.model.service.PlayerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/challenges")
@Tag(name="Challenges Restful API", description = "챌린지 CRUD")
public class ChallengeController {
	
	private final ChallengeScoreService challengeService;
	private final PlayerService playerService;
	
	public ChallengeController(ChallengeScoreService challengeService, PlayerService playerService) {
		super();
		this.challengeService = challengeService;
		this.playerService = playerService;
	}
	
	/**
     * 챌린지 세부 정보 조회
     *
     * @param challengeId 조회할 챌린지의 ID
     * @return 챌린지 세부 정보
     */
    @GetMapping("/{challengeId}")
    @Operation(summary = "챌린지 세부 정보 조회", description = "각 챌린지의 세부 정보를 확인할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "챌린지 조회 성공"),
        @ApiResponse(responseCode = "204", description = "챌린지 없음"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
	public ResponseEntity<?> getChallenge(@PathVariable("challengeId") int challengeId){
		
		Challenge challenge = challengeService.selectChallenge(challengeId);
		
		if(challenge == null) {
			return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(challenge);
	}
    
    
    /**
     * 챌린지 점수 기록 폼 반환
     * 
     * @param challengeId 챌린지 고유 ID
     * @return 챌린지 점수 기록 페이지 문자열
     */
    @GetMapping("/{challengeId}/score")
    public ResponseEntity<?> getScoreForm(@PathVariable("challengeId") int challengeId, HttpSession session ){
    	// Map<String, Object> olympic players 
    	int userId = (int) session.getAttribute("loginUserId");
    	int olympicId = playerService.findOlympicsIdByUserId(userId);
    	List<Map<String, Object>> players = playerService.getPlayersByOlympicsId(olympicId);
    	
    	Map<String, Object> Data = new HashMap<>();
    	Data.put("message", "챌린지 점수 기록 폼 반환");
    	Data.put("playerNames", players);
    	
    	return ResponseEntity.ok(Data);
    }
	
    
    /**
     * 챌린지 기록 제출
     *
     * @param challengeId 챌린지 고유 ID
     * @param score       기록할 점수 데이터
     * @return 점수 기록 결과 메시지
     */
    @PostMapping("/{challengeId}/score")
    @Operation(summary = "챌린지 기록 제출", description = "멤버 이름, 점수를 json 형식으로 전송하여 해당 챌린지의 점수를 기록합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "챌린지 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
	public ResponseEntity<?> recordScores(@PathVariable("challengeId") int challengeId, @RequestBody Score score){
    	
		score.setChallengeId(challengeId);
	    boolean success = challengeService.upsertScores(score);
	    
	    if (success) {
	    	challengeService.updateTotalScore();
	    	Map<String, Object> scoreData = new HashMap<>();
	    	scoreData.put("message", "챌린지 점수 기록 완료");
	    	scoreData.put("score", score);
	    	
	    	return ResponseEntity.ok(scoreData);
	    } 
	    else {
	    	
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}
	
    
    /**
     * 챌린지 세부 리더보드 조회
     *
     * @param challengeId 조회할 챌린지의 ID
     * @return 리더보드 리스트
     */
    @GetMapping("/{challengeId}/rank")
    @Operation(summary = "챌린지 세부 리더보드 조회", description = "현재 챌린지 내 등수를 확인할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "챌린지 조회 성공"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<?> getChallengesRank(@PathVariable("challengeId") int challengeId, HttpSession session){
    	Integer userId = (Integer) session.getAttribute("loginUserId");
    	
    	int olympicsId = playerService.findOlympicsIdByUserId(userId);
    	
		List<Rank> rank = challengeService.selectChallengeScore(challengeId, olympicsId);
		
		return ResponseEntity.ok(rank);
	}
	
    /**
     * 챌린지 종료 시 최종 리더보드 조회
     *
     * @param session 현재 사용자 세션
     * @return 최종 리더보드 리스트
     */
    @GetMapping("/{challengeId}/final-rank")
    @Operation(summary = "챌린지 종료 시 최종 리더보드 조회", description = "내 최종 등수를 확인할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "챌린지 조회 성공"),
        @ApiResponse(responseCode = "401", description = "로그인 필요"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
	public ResponseEntity<?> endChallengesRank(HttpSession session) {
	    Integer userId = (Integer) session.getAttribute("loginUserId");
	    
	    if (userId == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
	    }
	    
	    List<Rank> rank = challengeService.selectFinalScore(userId); 
	    
	    System.out.println(rank);
	    return ResponseEntity.ok(rank);
	}

}
