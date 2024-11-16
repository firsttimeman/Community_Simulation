package zerobaseproject.community.posting.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import zerobaseproject.community.global.exception.SuccessCode;
import zerobaseproject.community.posting.dto.PostingRequestDTO;
import zerobaseproject.community.posting.dto.PostingResponseDTO;
import zerobaseproject.community.posting.service.PostingService;
import zerobaseproject.community.posting.type.Category;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 게시물 관련 REST API 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/posting")
public class PostingController {

    private final PostingService postingService;

    /**
     * 새로운 게시물을 생성합니다.
     *
     * @param requestDTO 게시물 생성 요청 데이터를 포함한 DTO
     * @return 생성된 게시물의 정보를 담은 응답 DTO와 HTTP 상태 코드 201 (Created)
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostingResponseDTO> createPosting(@Valid @RequestBody PostingRequestDTO requestDTO) {
        PostingResponseDTO responseDTO = postingService.createPosting(requestDTO, getCurrentUserEmail());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * 기존 게시물을 수정합니다.
     *
     * @param postingId  수정할 게시물의 ID
     * @param requestDTO 게시물 수정 요청 데이터를 포함한 DTO
     * @return 수정된 게시물의 정보를 담은 응답 DTO와 HTTP 상태 코드 200 (OK)
     */
    @PutMapping("/{postingId}")
    @PreAuthorize("@postingService.isAuthorOrAdminForUpdate(#postingId, authentication.principal.username)")
    public ResponseEntity<PostingResponseDTO> updatePosting(
            @PathVariable Long postingId,
            @RequestBody PostingRequestDTO requestDTO) {
        PostingResponseDTO updatedPost = postingService.updatePosting(postingId, requestDTO, getCurrentUserEmail());
        return ResponseEntity.ok(updatedPost);
    }

    /**
     * 게시물을 삭제합니다.
     *
     * @param postingId 삭제할 게시물의 ID
     * @return 성공 메시지와 HTTP 상태 코드 200 (OK)
     */
    @DeleteMapping("/{postingId}")
    @PreAuthorize("@postingService.isAuthorOrAdminForDelete(#postingId, authentication.principal.username)")
    public ResponseEntity<String> deletePosting(@PathVariable Long postingId) {
        postingService.deletePosting(postingId, getCurrentUserEmail());
        return new ResponseEntity<>(SuccessCode.POST_DELETE_SUCCESS.getMessage(), SuccessCode.POST_DELETE_SUCCESS.getStatus());
    }

    /**
     * 조건에 따라 게시물을 검색합니다.
     *
     * @param email     작성자 이메일 (선택적)
     * @param startDate 작성일 시작 날짜 (선택적)
     * @param endDate   작성일 종료 날짜 (선택적)
     * @param category  게시물 카테고리 (선택적)
     * @param title     게시물 제목 (선택적)
     * @param pageable  페이지 정보
     * @return 검색 결과 게시물 목록과 HTTP 상태 코드 200 (OK)
     */
    @GetMapping("/search")
    public ResponseEntity<Page<PostingResponseDTO>> searchPostings(
            @RequestParam Optional<String> email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> endDate,
            @RequestParam Optional<Category> category,
            @RequestParam Optional<String> title,
            Pageable pageable) {

        Page<PostingResponseDTO> postings = postingService.searchPostings(email, startDate, endDate, category, title, pageable);
        return ResponseEntity.ok(postings);
    }

    /**
     * 모든 게시물 목록을 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 모든 게시물 목록과 HTTP 상태 코드 200 (OK)
     */
    @GetMapping("/all")
    public ResponseEntity<Page<PostingResponseDTO>> getAllPostings(Pageable pageable) {
        Page<PostingResponseDTO> postings = postingService.getAllPostings(pageable);
        return ResponseEntity.ok(postings);
    }

    /**
     * 게시물 ID로 특정 게시물을 조회합니다.
     *
     * @param postingId 조회할 게시물의 ID
     * @return 조회된 게시물의 정보를 담은 응답 DTO와 HTTP 상태 코드 200 (OK)
     */
    @GetMapping("/{postingId}")
    public ResponseEntity<PostingResponseDTO> getPostingById(@PathVariable Long postingId) {
        PostingResponseDTO posting = postingService.getPostingById(postingId);
        return ResponseEntity.ok(posting);
    }

    /**
     * 현재 로그인한 사용자의 게시물 목록을 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 사용자의 게시물 목록과 HTTP 상태 코드 200 (OK)
     */
    @GetMapping("/user-posts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<PostingResponseDTO>> getUserPosts(Pageable pageable) {
        Page<PostingResponseDTO> userPosts = postingService.getUserPosts(getCurrentUserEmail(), pageable);
        return ResponseEntity.ok(userPosts);
    }

    /**
     * 현재 로그인된 사용자의 이메일을 반환합니다.
     *
     * @return 현재 사용자 이메일
     */
    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
