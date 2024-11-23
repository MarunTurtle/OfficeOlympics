package com.olympics.mvc.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olympics.mvc.model.dto.Comments;

import com.olympics.mvc.model.service.CommentsService;
import com.olympics.mvc.util.Validate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/challenges/{challengeId}/comments")
@Tag(name="comments Restful API", description = "댓글 CRUD")
public class CommentsController {
	
	private final CommentsService commentService;
	
	public CommentsController(CommentsService commentService) {
		this.commentService = commentService;
	}
	
	
	/**
	 * 챌린지별 댓글 확인
	 * 
	 * @param challengeId 대상 챌린지 ID
	 * @return 댓글 목록
	 */
	@GetMapping("")
	@Operation(summary = "챌린지 세부 정보 조회", description = "각 챌린지의 세부 정보를 확인할 수 있습니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "챌린지 조회 성공"),
        @ApiResponse(responseCode = "204", description = "챌린지 없음"),
        @ApiResponse(responseCode = "503", description = "데이터베이스 오류 발생"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
	@Parameter(name = "challengeId", description = "조회할 챌린지의 ID", required = true)
	public ResponseEntity<?> getAllComments(@PathVariable("challengeId") int challengeId){
			
		List<Map<String, Object>> commentList = commentService.getComments(challengeId);		
		
		if (commentList == null || commentList.isEmpty()) {
		    return ResponseEntity.noContent().build();
		}
		
		return ResponseEntity.ok(commentList);
	}
	
	/**
     * 댓글 작성
     *
     * @param challengeId 대상 챌린지 ID
     * @param session     현재 사용자 세션
     * @param comments    작성 댓글 정보
     * @return 작성한 댓글 상태 메시지
     */
    @PostMapping("")
    @Operation(summary = "챌린지 댓글 작성", description = "선택한 챌린지에 댓글을 작성합니다.")
    @Parameters({
        @Parameter(name = "challengeId", description = "챌린지 ID", required = true),
        @Parameter(name = "commentText", description = "댓글 내용", required = true)
    })
    public ResponseEntity<String> insertComment(@PathVariable("challengeId") int challengeId,
                                                HttpSession session, @RequestBody Comments comments) {

        if (comments == null || comments.getCommentText() == null || comments.getCommentText().isEmpty()) {
            return ResponseEntity.badRequest().body("댓글 내용이 필요합니다.");
        }

        Integer userId = (Integer) session.getAttribute("loginUserId");
        comments.setChallengeId(challengeId);
        comments.setUserId(userId);

        boolean isInserted = commentService.insertComment(comments);

        return isInserted ? ResponseEntity.ok("댓글이 정상적으로 등록되었습니다.")
                          : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
	
	
    /**
     * 댓글 수정
     *
     * @param challengeId 대상 챌린지 ID
     * @param commentId   댓글 ID
     * @param session     현재 사용자 세션
     * @param comments    수정할 댓글 정보
     * @return 수정한 댓글 상태 메시지
     */
    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "작성한 댓글을 수정합니다.")
    @Parameters({
        @Parameter(name = "challengeId", description = "챌린지 ID", required = true),
        @Parameter(name = "commentId", description = "댓글 ID", required = true),
        @Parameter(name = "commentText", description = "수정할 댓글 내용", required = true)
    })
    public ResponseEntity<String> modifyComment(@PathVariable("challengeId") int challengeId,
                                                @PathVariable("commentId") int commentId,
                                                HttpSession session, @RequestBody Comments comments) {

        if (comments == null || comments.getCommentText() == null || comments.getCommentText().isEmpty()) {
            return ResponseEntity.badRequest().body("댓글 내용이 필요합니다.");
        }

        int userId = commentService.findWriter(commentId);
        if (!Validate.isValidSessionUser(session, userId)) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("본인이 작성한 댓글만 수정할 수 있습니다.");
        }
        
        comments.setChallengeId(challengeId);
        comments.setUserId(userId);
        comments.setCommentId(commentId);

        boolean isSoftDeleted = commentService.checkDeleted(comments);
        
        if (isSoftDeleted) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("삭제된 댓글은 수정할 수 없습니다.");
        }
        
        boolean isModified = commentService.modifyComment(comments);

        return isModified ? ResponseEntity.ok("댓글이 정상적으로 수정되었습니다.")
                          : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
	
	
    /**
     * 댓글 삭제
     *
     * @param challengeId 대상 챌린지 ID
     * @param commentId   삭제할 댓글 ID
     * @return 삭제 상태 메시지
     */
    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "작성한 댓글을 삭제합니다.")
    @Parameters({
        @Parameter(name = "challengeId", description = "챌린지 ID", required = true),
        @Parameter(name = "commentId", description = "댓글 ID", required = true)
    })
    public ResponseEntity<?> deleteComment(@PathVariable("challengeId") int challengeId,
            @PathVariable("commentId") int commentId,
            HttpSession session) {

		int userId = commentService.findWriter(commentId);
		if (!Validate.isValidSessionUser(session, userId)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("본인이 작성한 댓글만 삭제할 수 있습니다.");
		}
		
		boolean isDeleted = commentService.deleteCommentOrReply(commentId, userId);
		
		return isDeleted ? ResponseEntity.ok("댓글이 정상적으로 삭제되었습니다.")
               			 : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("댓글 삭제 실패");
	}
	
	
    /**
     * 대댓글 작성
     *
     * @param challengeId 대상 챌린지 ID
     * @param commentId   부모 댓글 ID
     * @param comments    작성할 대댓글 정보
     * @param session     현재 사용자 세션
     * @return 작성 상태 메시지
     */
    @PostMapping("/{commentId}/replies")
    @Operation(summary = "대댓글 작성", description = "특정 댓글에 대댓글을 작성합니다.")
    public ResponseEntity<?> insertReplies(@PathVariable("challengeId") int challengeId,
                                           @PathVariable("commentId") int commentId,
                                           @RequestBody Comments comments, HttpSession session) {

        if (comments == null || comments.getCommentText() == null || comments.getCommentText().isEmpty()) {
            return ResponseEntity.badRequest().body("댓글 내용이 필요합니다.");
        }

        Integer userId = (Integer) session.getAttribute("loginUserId");
        comments.setChallengeId(challengeId);
        comments.setCommentGroup(commentId);
        comments.setUserId(userId);

        boolean isInserted = commentService.insertReply(comments);

        return isInserted ? ResponseEntity.ok("댓글이 정상적으로 등록되었습니다.")
                          : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
	
    /**
     * 대댓글 수정
     *
     * @param challengeId 대상 챌린지 ID
     * @param commentId   부모 댓글 ID
     * @param replyId     수정할 대댓글 ID
     * @param comments    수정할 대댓글 정보
     * @param session     현재 사용자 세션
     * @return 수정 상태 메시지
     */
    @PutMapping("/{commentId}/replies/{replyId}")
    @Operation(summary = "대댓글 수정", description = "특정 대댓글을 수정합니다.")
    public ResponseEntity<?> modifyReplies(@PathVariable("challengeId") int challengeId,
            @PathVariable("commentId") int commentId,
            @PathVariable("replyId") int replyId,
            @RequestBody Comments comments, HttpSession session) {

		if (comments == null || comments.getCommentText() == null || comments.getCommentText().isEmpty()) {
			return ResponseEntity.badRequest().body("댓글 내용이 필요합니다.");
		}
		
		int userId = commentService.findWriter(commentId);
		if (!Validate.isValidSessionUser(session, userId)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("본인이 작성한 댓글만 수정할 수 있습니다.");
		}
		
		comments.setChallengeId(challengeId);
		comments.setUserId(userId);
		comments.setCommentGroup(commentId);
		comments.setCommentId(replyId);
		
		boolean isSoftDeleted = commentService.checkDeleted(comments);
        
        if (isSoftDeleted) {
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("삭제된 댓글은 수정할 수 없습니다.");
        }
		
		
		boolean isModified = commentService.modifyReply(comments);
		
		return isModified ? ResponseEntity.ok("댓글이 정상적으로 수정되었습니다.")
						  : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
}
