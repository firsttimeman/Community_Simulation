package zerobaseproject.community.comment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import zerobaseproject.community.global.entity.BaseEntity;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.posting.entity.Posting;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long commentId;

    @Column(nullable = false)
    @Size(max = 100, message = "글은 최대 100자까지 입력할수 있습니다.")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POSTING_ID")
    private Posting posting;


}
