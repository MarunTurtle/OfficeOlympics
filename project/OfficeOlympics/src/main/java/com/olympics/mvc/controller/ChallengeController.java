package com.olympics.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.olympics.mvc.model.dto.Rank;
import com.olympics.mvc.model.dto.Score;

import com.olympics.mvc.model.service.ChallengeScoreService;
import com.olympics.mvc.model.service.CommentsService;

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
//@CrossOrigin("*")
public class ChallengeController {
	
	private final ChallengeScoreService challengeService;
	private final CommentsService commentService;
	
	public ChallengeController(ChallengeScoreService challengeService, CommentsService commentService) {
		super();
		this.challengeService = challengeService;
		this.commentService = commentService;
	}
	
	// 챌린지 세부 정보 조회
	@GetMapping("/{challengeId}")
	@Operation(summary = "챌린지 세부 정보 조회", description = "각 챌린지의 세부 정보를 확인할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "챌린지 조회 성공"),
        @ApiResponse(responseCode = "204", description = "챌린지 없음"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
	@Parameter(name = "challengeId", description = "조회할 챌린지의 ID", required = true)
	public ResponseEntity<?> getChallenge(@PathVariable("challengeId") int challengeId){
		// id에 부합하는 챌린지를 조회
		Challenge challenge = challengeService.selectChallenge(challengeId);
		
		if(challenge == null) {
			return ResponseEntity.noContent().build();
		}
		
		
		List<Comments> commentList = commentService.getComments(challengeId);
		
		if(commentList == null || commentList.isEmpty() ){
			return ResponseEntity.ok(challenge);			
		}
		
		Map<String, Object> challengeData = new HashMap<>();
		challengeData.put("challenge", challenge);
		challengeData.put("commentList", commentList);
		
		return ResponseEntity.ok(challengeData);
	}
	
	// 점수 기록 페이지
	@GetMapping("/{challengeId}/score")@Operation(summary = "점수 기록 페이지 반환", description = "점수 기록 페이지를 반환합니다.")
	public ResponseEntity<String> getRecordForm(@PathVariable("challengeId") int challengeId){
		return ResponseEntity.ok("점수 기록 페이지입니다.");
	}
	
	// 챌린지 기록 제출 (+DB 점수 갱신)
	@PostMapping("/{challengeId}/score")
	@Operation(summary = "챌린지 기록 제출", description = "멤버 이름, 점수를 json형식으로 전송하여 해당 챌린지의 점수를 기록합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "챌린지 성공"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
	@Parameters({
        @Parameter(name = "challengeId", description = "챌린지 고유 ID", required = true),
        @Parameter(name = "playerNames", description = "플레이어 이름 (List형태)", required = true),
        @Parameter(name = "scores", description = "점수 (List형태)", required = true),
    })
	public ResponseEntity<?> recordScores(@PathVariable("challengeId") int challengeId, @RequestBody Score score){
		score.setChallengeId(challengeId);
	    boolean success = challengeService.upsertScores(score);
	    
	    if (success) {
	    	// DB total 점수 갱신
	    	challengeService.updateTotalScore();
	    	return ResponseEntity.ok("챌린지 점수 기록 완료");	    			    	
	    } 
	    else {
	    	
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}
	
	// 해당 챌린지 리더보드 조회
	@GetMapping("/{challengeId}/rank")
	@Operation(summary = "챌린지 세부 리더보드 조회", description = "현재 챌린지 내 등수를 확인할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "챌린지 조회 성공"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
	@Parameter(name = "challengeId", description = "조회할 챌린지의 ID", required = true)
	public ResponseEntity<?> getChallengesRank(@PathVariable("challengeId") int challengeId){
		List<Rank> rank = challengeService.selectChallengeScore(challengeId);
		System.out.println(rank);
		return ResponseEntity.ok(rank);
	}
	
	// 해당 챌린지 종료 시 최종 리더보드 조회
	@GetMapping("/{challengeId}/final-rank")
	@Operation(summary = "챌린지 종료 시 최종 리더보드 조회", description = "내 최종 등수를 확인할 수 있습니다.")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "챌린지 조회 성공"),
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

	///////////////////////////////////// 댓글 /////////////////////////////////////////
	
	// 댓글 쓰기
	@PostMapping("/{challengeId}/comments")
	@Operation(summary = "챌린지 댓글 작성", description = "선택한 챌린지 영상 하단에 댓글을 작성할 수 있습니다.")
	@Parameters({
        @Parameter(name = "challengeId", description = "챌린지 고유 ID", required = true),
        @Parameter(name = "commentText", description = "댓글내용", required = true)
    })
	public ResponseEntity<String> insertComment(@PathVariable("challengeId") int challengeId, 
			HttpSession session, @RequestBody Comments comments){
		
		int userId = (int) session.getAttribute("loginUserId");
		comments.setChallengeId(challengeId);
		comments.setUserId(userId);
		
		boolean isInserted = commentService.insertComment(comments);
		
		if (isInserted)
			return ResponseEntity.ok("댓글이 정상적으로 등록되었습니다.");
		
		return ResponseEntity.badRequest().build();

	}
	
	// 댓글 수정
	@PutMapping("/{challengeId}/comments")
	@Operation(summary = "댓글 수정", description = "작성한 댓글을 수정할 수 있습니다.")
	@Parameters({
        @Parameter(name = "challengeId", description = "챌린지 고유 ID", required = true),
        @Parameter(name = "commentId", description = "댓글 고유 ID", required = true),
        @Parameter(name = "commentText", description = "댓글내용", required = true)
    })
	public ResponseEntity<String> modifyComment(@PathVariable("challengeId") int challengeId, 
			HttpSession session, @RequestBody Comments comments){
		
		int userId = (int) session.getAttribute("loginUserId");
		comments.setChallengeId(challengeId);
		comments.setUserId(userId);
		
		boolean isModified = commentService.modifyComment(comments);
		
		if (isModified)
			return ResponseEntity.ok("댓글이 정상적으로 수정되었습니다.");
		
		return ResponseEntity.badRequest().build();
	}
	
	
	// 댓글 삭제
	@DeleteMapping("/{challengeId}/comments/{commentId}")
	@Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제합니다.")
	@Parameters({
        @Parameter(name = "challengeId", description = "챌린지 고유 ID", required = true),
        @Parameter(name = "commentId", description = "댓글 고유 ID", required = true)
    })
	public ResponseEntity<?> deleteComment(@PathVariable("challengeId") int challengeId, 
										   @PathVariable("commentId") int commentId){
		
		boolean comments = commentService.findUserComments(challengeId);

		if (!comments) {
			return ResponseEntity.badRequest().build();
		}
		
		boolean isDeleted = commentService.deleteComment(commentId, challengeId);
		
		if (isDeleted) {
			return ResponseEntity.noContent().build();			
		}
		
		return ResponseEntity.badRequest().build();
	}
	
}
