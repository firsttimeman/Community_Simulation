package zerobaseproject.community.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import zerobaseproject.community.comment.entity.Comment;
import zerobaseproject.community.global.entity.BaseEntity;
import zerobaseproject.community.member.type.UserRoles;
import zerobaseproject.community.posting.entity.Posting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long memberId;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 100)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRoles userRoles;

    @OneToMany(mappedBy = "member")
    private List<Posting> postings = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.userRoles.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}