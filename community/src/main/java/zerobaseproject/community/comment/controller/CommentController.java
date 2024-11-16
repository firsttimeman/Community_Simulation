package zerobaseproject.community.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zerobaseproject.community.comment.dto.CommentRequestDto;
import zerobaseproject.community.comment.dto.CommentResponseDto;
import zerobaseproject.community.comment.service.CommentService;
import zerobaseproject.community.global.exception.SuccessCode;

/**
 * 댓글 관련 기능을 처리하는 컨트롤러 클래스입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 새로운 댓글을 생성합니다.
     *
     * @param requestDto 생성할 댓글 내용과 관련된 정보를 담은 DTO
     * @return 생성된 댓글 정보와 상태 코드 201 (Created)를 반환합니다.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponseDto> createComment(@Valid @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto responseDto = commentService.createComment(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 특정 게시물에 대한 모든 댓글을 페이징 처리하여 조회합니다.
     *
     * @param postingId 조회할 게시물의 ID
     * @param pageable  페이지 요청 정보
     * @return 페이징 처리된 댓글 목록을 반환합니다.
     */
    @GetMapping("/posting/{postingId}")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByPosting(
            @PathVariable Long postingId, Pageable pageable) {
        Page<CommentResponseDto> comments = commentService.getCommentsByPosting(postingId, pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * 사용자가 작성한 모든 댓글을 페이징 처리하여 조회합니다.
     *
     * @param email    사용자의 이메일
     * @param pageable 페이지 요청 정보
     * @return 페이징 처리된 댓글 목록을 반환합니다.
     */
    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByUser(
            @RequestParam String email, Pageable pageable) {
        Page<CommentResponseDto> comments = commentService.getCommentsByUser(email, pageable);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글을 수정합니다.
     *
     * @param commentId  수정할 댓글의 ID
     * @param requestDto 수정할 댓글 내용
     * @return 수정된 댓글 정보를 반환합니다.
     */
    @PutMapping("/{commentId}")
    @PreAuthorize("#requestDto.email == authentication.principal.username")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long commentId, @Valid @RequestBody CommentRequestDto requestDto) {
        CommentResponseDto updatedComment = commentService.updateComment(commentId, requestDto);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글의 ID
     * @param email     요청을 보낸 사용자의 이메일
     * @return 댓글 삭제 성공 메시지를 반환합니다.
     */
    @DeleteMapping("/{commentId}")
    @PreAuthorize("#email == authentication.principal.username or hasRole('ADMIN')")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId, @RequestParam String email) {
        commentService.deleteComment(commentId, email);
        return new ResponseEntity<>(SuccessCode.COMMENT_DELETE_SUCCESS.getMessage(), SuccessCode.COMMENT_DELETE_SUCCESS.getStatus());
    }
}
