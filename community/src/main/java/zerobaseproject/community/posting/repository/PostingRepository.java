package zerobaseproject.community.posting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobaseproject.community.posting.entity.Posting;
import zerobaseproject.community.posting.type.Category;

import java.time.LocalDateTime;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {
    Page<Posting> findByMember_Email(String email, Pageable pageable);

    Page<Posting> findByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Posting> findByCategory(Category category, Pageable pageable);

    Page<Posting> findByTitleContaining(String title, Pageable pageable);

}
