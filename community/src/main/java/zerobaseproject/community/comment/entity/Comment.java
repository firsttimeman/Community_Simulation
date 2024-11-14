package zerobaseproject.community.comment.entity;

import jakarta.persistence.*;
import zerobaseproject.community.global.entity.BaseEntity;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.posting.entity.Posting;

@Entity
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long commentId;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POSTING_ID")
    private Posting posting;



}
