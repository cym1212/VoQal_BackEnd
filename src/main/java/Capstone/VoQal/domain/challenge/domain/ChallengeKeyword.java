package Capstone.VoQal.domain.challenge.domain;

import Capstone.VoQal.global.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "challenge_keyword")
public class ChallengeKeyword extends BaseEntity {

    @OneToMany(mappedBy = "challengeKeyword")
    private List<ChallengePost> challengePost;

    private String keyword;
    private boolean used;
    private LocalDate usedDate;

    public ChallengeKeyword(String keyword, boolean used, LocalDate usedDate) {
        this.keyword = keyword;
        this.used = used;
        this.usedDate = usedDate;
    }
}
