package Capstone.VoQal.domain.member.domain;

import Capstone.VoQal.domain.challenge.domain.ChallengeLike;
import Capstone.VoQal.domain.challenge.domain.ChallengePost;
import Capstone.VoQal.domain.reservation.domain.Reservation;
import Capstone.VoQal.global.domain.BaseEntity;
import Capstone.VoQal.global.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
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
    @Setter
    private Role role;

    @Column(length = 20, nullable = false)
    @Setter
    private String nickName;


    private String password;

    private String email;

    private String phoneNumber;

    private String name;

    private String fcmToken;

    @Setter
    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Student student;

    @Setter
    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Coach coach;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ChallengePost> challengePost;


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ChallengeLike> challengeLikes;


    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservations;


    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void saveFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
