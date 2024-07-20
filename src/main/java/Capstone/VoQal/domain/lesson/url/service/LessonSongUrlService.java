package Capstone.VoQal.domain.lesson.url.service;

import Capstone.VoQal.domain.lesson.url.dto.GetLessonSongRequestDTO;
import Capstone.VoQal.domain.lesson.url.dto.LessonSongResponseDTO;
import Capstone.VoQal.domain.lesson.url.dto.SetLessonSongUrlDTO;
import Capstone.VoQal.domain.lesson.url.dto.UpdateLessonSongUrlDTO;
import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.repository.Member.MemberRepository;
import Capstone.VoQal.domain.member.service.MemberService;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonSongUrlService {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Transactional
    public LessonSongResponseDTO getLessonSongUrl(GetLessonSongRequestDTO getLessonSongRequestDTO) {
        Member currentCoach = memberService.getCurrentCoach();

        CoachAndStudent coachAndStudent = memberService.getCoachAndStudent(currentCoach.getId(), getLessonSongRequestDTO.getStudentId());
        return LessonSongResponseDTO.builder()
                .status(200)
                .lessonSongUrl(coachAndStudent.getLessonSongUrl())
                .build();
    }

    @Transactional
    public LessonSongResponseDTO createLessonSongUrl(SetLessonSongUrlDTO setLessonSongUrlDTO) {

        Member currentCoach = memberService.getCurrentCoach();

        CoachAndStudent coachAndStudent = memberService.getCoachAndStudent(currentCoach.getId(), setLessonSongUrlDTO.getStudentId());

        coachAndStudent.toBuilder()
                .lessonSongUrl(setLessonSongUrlDTO.getLessonSongUrl())
                .build();

        return LessonSongResponseDTO.builder()
                .status(200)
                .lessonSongUrl(coachAndStudent.getLessonSongUrl())
                .build();
    }

    @Transactional
    public LessonSongResponseDTO updateLessonSongUrl(Long studentId, UpdateLessonSongUrlDTO updateLessonSongUrlDTO) {
        Member currentCoach = memberService.getCurrentCoach();

        CoachAndStudent coachAndStudent = memberService.getCoachAndStudent(currentCoach.getId(), studentId);

        coachAndStudent.updateLessonSongUrl(updateLessonSongUrlDTO.getLessonSongUrl());
        return LessonSongResponseDTO.builder()
                .status(200)
                .lessonSongUrl(coachAndStudent.getLessonSongUrl())
                .build();
    }

    @Transactional
    public void deleteLessonSongUrl(Long studentId) {
        Member currentCoach = memberService.getCurrentCoach();

        CoachAndStudent coachAndStudent = memberService.getCoachAndStudent(currentCoach.getId(), studentId);
        coachAndStudent.updateLessonSongUrl(null);
    }

    @Transactional
    public LessonSongResponseDTO getLessonSongUrlForStudent() {
        Long currentMemberId = memberService.getCurrentMemberId();

        Optional<Member> memberId = memberRepository.findByMemberId(currentMemberId);
        if (memberId.isEmpty()) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }

        Long caochId = memberService.getCoachIdByStudentId(currentMemberId);

        CoachAndStudent coachAndStudent = memberService.getCoachAndStudent(caochId, currentMemberId);
        return LessonSongResponseDTO.builder()
                .lessonSongUrl(coachAndStudent.getLessonSongUrl())
                .status(200)
                .build();
    }
}
