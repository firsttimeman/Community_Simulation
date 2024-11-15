package zerobaseproject.community.posting.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.*;
import zerobaseproject.community.comment.entity.Comment;
import zerobaseproject.community.global.entity.BaseEntity;
import zerobaseproject.community.member.entity.Member;
import zerobaseproject.community.posting.type.Category;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Posting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSTING_ID")
    private Long postingId;

    @Column(nullable = false)
    @Size(max = 20, message = "제목은 최대 20자까지 입력할수 있습니다.")
    private String title;

    @Column(nullable = false)
    @Size(max = 200, message = "글은 최대 200자까지 입력할수 있습니다.")
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


}
