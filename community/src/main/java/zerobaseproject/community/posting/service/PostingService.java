package zerobaseproject.community.posting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import zerobaseproject.community.global.exception.ErrorCode;
import zerobaseproject.community.global.exception.MemberException;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.member.repository.MemberRepository;
import zerobaseproject.community.posting.dto.PostingRequestDTO;
import zerobaseproject.community.posting.dto.PostingResponseDTO;
import zerobaseproject.community.posting.entity.Posting;
import zerobaseproject.community.posting.repository.PostingRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostingService {

    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;

    public PostingResponseDTO createPosting(PostingRequestDTO requestDTO) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("email={}", email);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));


        Posting post = Posting.builder()
                .title(requestDTO.getTitle())
                .content(requestDTO.getContent())
                .category(requestDTO.getCategory())
                .member(member)
                .build();

        Posting save = postingRepository.save(post);


        return PostingResponseDTO.fromEntity(save);
    }
}
