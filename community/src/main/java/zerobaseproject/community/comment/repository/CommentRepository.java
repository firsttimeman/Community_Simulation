package zerobaseproject.community.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import zerobaseproject.community.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPosting_PostingId(Long postingId, Pageable pageable);

    Page<Comment> findByMember_Email(String email, Pageable pageable);
}
