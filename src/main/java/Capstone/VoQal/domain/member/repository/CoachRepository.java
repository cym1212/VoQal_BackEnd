package Capstone.VoQal.domain.member.repository;

import Capstone.VoQal.domain.member.domain.Coach;
import Capstone.VoQal.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, Long> {
    Coach findByMemberId(Long memberId);
    Coach findByMember(Member member);
}
