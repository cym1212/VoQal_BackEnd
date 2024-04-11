package Capstone.VoQal.domain.member.domain;

import Capstone.VoQal.domain.challenge.domain.ChallengePost;
import Capstone.VoQal.domain.challenge.domain.MemberAndPostLike;
import Capstone.VoQal.domain.chatting.domain.MemberAndChatRoom;
import Capstone.VoQal.global.domain.BaseEntity;
import Capstone.VoQal.global.enums.Provider;
import Capstone.VoQal.global.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity {


    @Setter
    private String refreshToken;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 20, nullable = true)
    private String nickName;

    @Column(length = 10, nullable = true)
    @Enumerated(EnumType.STRING)
    private Provider provider; //erd cloud에 추가 해야함

    @Column(unique = true, nullable = true)
    private String providerId; //erd cloud에 추가 해야함

    private String email; //erd cloud에 추가 해야함

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private Student student;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private Trainer trainer;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private ChallengePost challengePost;


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private Set<MemberAndPostLike> memberAndPostLike;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private Set<MemberAndChatRoom> memberAndChatRoom;


    public static Member createGuest(Provider provider, String providerId, String email) {
        return Member.builder().provider(provider).role(Role.GUEST).email(email).providerId(providerId).build();
    }
}
