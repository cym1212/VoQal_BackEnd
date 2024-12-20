package Capstone.VoQal.domain.member.repository.Member;

import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.global.auth.dto.MemberInfromationDTO;
import Capstone.VoQal.global.enums.Role;

import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<Member> findByMemberId(Long memberId);

    void updateRefreshToken(Long id, String refreshToken);

    void updateRole(Long id, Role role);

    String findRoleById(Long memberId);

    MemberInfromationDTO getCurrentUserDetails(Long memberId);
    void updateFcmToken(Long id, String fcmToken);
}
