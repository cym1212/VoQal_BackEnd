package Capstone.VoQal.domain.member.repository;

import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.global.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNickName(String nickName);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);
    Optional<Member> findByPhoneNumber(String phoneNumber);

    List<Member> findByRole(Role role);

    Optional<Member> findByNameAndPhoneNumber(String name, String phoneNumber);


    Optional<Member> findByNameAndPhoneNumberAndEmail(String name, String phoneNumber, String email);


    @Query("SELECT m FROM Member m WHERE m.id = :memberId")
    Optional<Member> findByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE Member m SET m.refreshToken = :refreshToken WHERE m.id = :id")
    void updateRefreshToken(@Param("id") Long id, @Param("refreshToken") String refreshToken);

    @Modifying
    @Query("UPDATE Member m SET m.role = :role WHERE m.id = :id")
    void updateRole(@Param("id") Long id, @Param("role") Role role);

    List<Member> findAllByIdIn(List<Long> id);
}
