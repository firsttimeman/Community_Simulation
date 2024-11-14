package zerobaseproject.community.posting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobaseproject.community.global.exception.ErrorCode;
import zerobaseproject.community.global.exception.MemberException;
import zerobaseproject.community.global.exception.PostingException;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.member.repository.MemberRepository;
import zerobaseproject.community.member.type.UserRoles;
import zerobaseproject.community.posting.dto.PostingRequestDTO;
import zerobaseproject.community.posting.dto.PostingResponseDTO;
import zerobaseproject.community.posting.entity.Posting;
import zerobaseproject.community.posting.repository.PostingRepository;
import zerobaseproject.community.posting.type.Category;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostingService {

    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;

    public PostingResponseDTO createPosting(PostingRequestDTO requestDTO, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));

        Posting post = Posting.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .category(requestDTO.getCategory())
                .member(member)
                .build();

        Posting savedPost = postingRepository.save(post);
        return PostingResponseDTO.fromEntity(savedPost);
    }


    @Transactional
    public PostingResponseDTO updatePosting(Long postingId, PostingRequestDTO requestDTO, String email) {
        Posting post = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingException(ErrorCode.POST_NOT_FOUND));

        if (!post.getMember().getEmail().equals(email)) {
            throw new PostingException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        post.setTitle(requestDTO.getTitle());
        post.setContent(requestDTO.getContent());
        post.setCategory(requestDTO.getCategory());

        return PostingResponseDTO.fromEntity(post);
    }


    @Transactional
    public void deletePosting(Long postingId, String email) {
        Posting post = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingException(ErrorCode.POST_NOT_FOUND));

        if (!post.getMember().getEmail().equals(email) && !isAdmin(email)) {
            throw new PostingException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        postingRepository.delete(post);
    }


    @Transactional(readOnly = true)
    public Page<PostingResponseDTO> searchPostings(Optional<String> email, Optional<LocalDateTime> startDate,
                                                   Optional<LocalDateTime> endDate, Optional<Category> category,
                                                   Optional<String> title, Pageable pageable) {
        if (email.isPresent()) {
            return postingRepository.findByMember_Email(email.get(), pageable).map(PostingResponseDTO::fromEntity);
        } else if (startDate.isPresent() && endDate.isPresent()) {
            return postingRepository.findByCreateDateBetween(startDate.get(), endDate.get(), pageable).map(PostingResponseDTO::fromEntity);
        } else if (category.isPresent()) {
            return postingRepository.findByCategory(category.get(), pageable).map(PostingResponseDTO::fromEntity);
        } else if (title.isPresent()) {
            return postingRepository.findByTitleContaining(title.get(), pageable).map(PostingResponseDTO::fromEntity);
        }
        return postingRepository.findAll(pageable).map(PostingResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public PostingResponseDTO getPostingById(Long postingId) {
        Posting post = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingException(ErrorCode.POST_NOT_FOUND));

        return PostingResponseDTO.fromEntity(post);
    }

    @Transactional(readOnly = true)
    public Page<PostingResponseDTO> getAllPostings(Pageable pageable) {
        return postingRepository.findAll(pageable).map(PostingResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<PostingResponseDTO> getUserPosts(String email, Pageable pageable) {
        return postingRepository.findByMember_Email(email, pageable).map(PostingResponseDTO::fromEntity);
    }


    private boolean isAdmin(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));
        return member.getUserRoles() == UserRoles.ADMIN;
    }

    public boolean isAuthorOrAdminForUpdate(Long postingId, String email) {
        Posting post = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingException(ErrorCode.POST_NOT_FOUND));

        // 글 작성자의 이메일과 현재 사용자의 이메일이 일치하거나, 운영자이고 글 작성자의 이메일과 일치하는 경우에만 true 반환
        return post.getMember().getEmail().equals(email) || (isAdmin(email) && post.getMember().getEmail().equals(email));
    }

    public boolean isAuthorOrAdminForDelete(Long postingId, String email) {
        Posting post = postingRepository.findById(postingId)
                .orElseThrow(() -> new PostingException(ErrorCode.POST_NOT_FOUND));

        // 운영자는 모든 글을 삭제할 수 있으며, 일반 사용자는 자신의 글만 삭제 가능
        return post.getMember().getEmail().equals(email) || isAdmin(email);
    }





}
