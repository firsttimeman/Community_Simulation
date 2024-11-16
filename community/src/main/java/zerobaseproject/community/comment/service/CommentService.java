package zerobaseproject.community.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobaseproject.community.comment.dto.CommentRequestDto;
import zerobaseproject.community.comment.dto.CommentResponseDto;
import zerobaseproject.community.comment.entity.Comment;
import zerobaseproject.community.comment.repository.CommentRepository;
import zerobaseproject.community.global.exception.CommentException;
import zerobaseproject.community.global.exception.ErrorCode;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.member.repository.MemberRepository;
import zerobaseproject.community.member.type.UserRoles;
import zerobaseproject.community.posting.entity.Posting;
import zerobaseproject.community.posting.repository.PostingRepository;

import static zerobaseproject.community.comment.dto.CommentResponseDto.toResponseDto;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostingRepository postingRepository;


    public CommentResponseDto createComment(CommentRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CommentException(ErrorCode.USER_NOT_FOUND));
        Posting posting = postingRepository.findById(requestDto.getPostingId())
                .orElseThrow(() -> new CommentException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .member(member)
                .posting(posting)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return toResponseDto(savedComment);
    }

    // 특정 게시물의 모든 댓글 조회
    public Page<CommentResponseDto> getCommentsByPosting(Long postingId, Pageable pageable) {
        return commentRepository.findByPosting_PostingId(postingId, pageable)
                .map(CommentResponseDto::toResponseDto);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        // 작성자 확인
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CommentException(ErrorCode.USER_NOT_FOUND));


        if (!comment.getMember().getEmail().equals(requestDto.getEmail())) {
            throw new CommentException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        comment.setContent(requestDto.getContent());
        return CommentResponseDto.toResponseDto(comment);
    }


    public Page<CommentResponseDto> getCommentsByUser(String email, Pageable pageable) {
        return commentRepository.findByMember_Email(email, pageable)
                .map(CommentResponseDto::toResponseDto);
    }

    public void deleteComment(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CommentException(ErrorCode.USER_NOT_FOUND));

        // 작성자이거나 관리자 권한일 경우에만 삭제 허용
        if (!comment.getMember().getEmail().equals(email) && !member.getUserRoles().equals(UserRoles.ADMIN)) {
            throw new CommentException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        commentRepository.delete(comment);
    }


}
