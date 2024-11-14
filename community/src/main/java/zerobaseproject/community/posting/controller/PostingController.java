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
import zerobaseproject.community.posting.dto.PostingRequestDTO;
import zerobaseproject.community.posting.dto.PostingResponseDTO;
import zerobaseproject.community.posting.service.PostingService;
import zerobaseproject.community.posting.type.Category;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@RestController
@RequiredArgsConstructor
@RequestMapping("/posting")
public class PostingController {

    private final PostingService postingService;

    // 게시물 생성
    @PostMapping("/createpost")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostingResponseDTO> createPosting(@Valid @RequestBody PostingRequestDTO requestDTO) {
        PostingResponseDTO responseDTO = postingService.createPosting(requestDTO, getCurrentUserEmail());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // 게시물 수정
    @PutMapping("/update/{postingId}")
    @PreAuthorize("@postingService.isAuthorOrAdminForUpdate(#postingId, authentication.principal.username)")
    public ResponseEntity<PostingResponseDTO> updatePosting(
            @PathVariable Long postingId,
            @RequestBody PostingRequestDTO requestDTO) {
        PostingResponseDTO updatedPost = postingService.updatePosting(postingId, requestDTO, getCurrentUserEmail());
        return ResponseEntity.ok(updatedPost);
    }


    // 게시물 삭제
    @DeleteMapping("/delete/{postingId}")
    @PreAuthorize("@postingService.isAuthorOrAdminForDelete(#postingId, authentication.principal.username)")
    public ResponseEntity<Map<String, String>> deletePosting(@PathVariable Long postingId) {
        postingService.deletePosting(postingId, getCurrentUserEmail());
        Map<String, String> response = new HashMap<>();
        response.put("message", "게시물이 성공적으로 삭제되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 게시물 검색 (조건부 검색)
    @GetMapping("/search")
    public ResponseEntity<Page<PostingResponseDTO>> searchPostings(
            @RequestParam Optional<String> email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<LocalDateTime> endDate,
            @RequestParam Optional<Category> category,
            @RequestParam Optional<String> title,  // 타이틀 검색 조건 추가
            Pageable pageable) {

        Page<PostingResponseDTO> postings = postingService.searchPostings(email, startDate, endDate, category, title, pageable);
        return ResponseEntity.ok(postings);
    }

    // 모든 게시물 목록 조회 (페이징 처리)
    @GetMapping("/all")
    public ResponseEntity<Page<PostingResponseDTO>> getAllPostings(Pageable pageable) {
        Page<PostingResponseDTO> postings = postingService.getAllPostings(pageable);
        return ResponseEntity.ok(postings);
    }

    // 단일 게시물 조회
    @GetMapping("/{postingId}")
    public ResponseEntity<PostingResponseDTO> getPostingById(@PathVariable Long postingId) {
        PostingResponseDTO posting = postingService.getPostingById(postingId);
        return ResponseEntity.ok(posting);
    }

    @GetMapping("/user-posts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<PostingResponseDTO>> getUserPosts(Pageable pageable) {
        Page<PostingResponseDTO> userPosts = postingService.getUserPosts(getCurrentUserEmail(), pageable);
        return ResponseEntity.ok(userPosts);
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
