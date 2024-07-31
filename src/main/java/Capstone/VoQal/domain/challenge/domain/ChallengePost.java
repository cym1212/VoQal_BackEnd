package Capstone.VoQal.domain.challenge.domain;

import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "challenge_post")
public class ChallengePost extends BaseEntity {


    private String challengeRecordUrl;

    private String thumbnailUrl;

    private String songTitle;

    private String singer;


    @Column(name = "random_order")
    private Double randomOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challengeKeyword")
    private ChallengeKeyword challengeKeyword;

    @OneToMany(mappedBy = "challengePost")
    private Set<ChallengeLike> challengeLikes;

}
