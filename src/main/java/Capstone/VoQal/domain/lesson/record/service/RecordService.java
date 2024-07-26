package Capstone.VoQal.domain.lesson.record.service;



import Capstone.VoQal.domain.lesson.note.domain.LessonNote;
import Capstone.VoQal.domain.lesson.record.domain.LessonRecord;
import Capstone.VoQal.domain.lesson.record.dto.*;
import Capstone.VoQal.domain.lesson.record.repository.RecordRepository;
import Capstone.VoQal.domain.member.domain.CoachAndStudent;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.service.MemberService;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import Capstone.VoQal.infra.s3upload.service.S3UploadService;
import Capstone.VoQal.infra.s3upload.utils.UploadUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RecordService {

    private final MemberService memberService;
    private final S3UploadService s3UploadService;
    private final RecordRepository recordRepository;


    //coach
    @Transactional
    public UploadResponseDTO uploadRecord(MultipartFile multipartFile, UploadRequestDTO uploadRequestDTO) {
        Member currentCoach = memberService.getCurrentCoach();
        if (multipartFile.isEmpty()) {
            throw new BusinessException(ErrorCode.MULTIPART_FILE_NOT_FOUND);
        }
        CoachAndStudent coachAndStudent = memberService.getCoachAndStudent(currentCoach.getId(), uploadRequestDTO.getStudentId());


        String uploadFile = s3UploadService.uploadFile(multipartFile, UploadUtils.LESSON_NOTE_RECORD, currentCoach.getId());
        LessonRecord uploadLessonRecord = LessonRecord.builder()
                .recordDate(uploadRequestDTO.getRecordDate())
                .recordTitle(uploadRequestDTO.getRecordTitle())
                .coachAndStudent(coachAndStudent)
                .recordUrl(uploadFile)
                .build();

        recordRepository.save(uploadLessonRecord);
        return UploadResponseDTO.builder()
                .s3Url(uploadFile)
                .recordDate(uploadLessonRecord.getRecordDate())
                .recordTitle(uploadLessonRecord.getRecordTitle())
                .build();
    }

    //coach
    @Transactional
    public List<LessonRecordResponseDTO> getAllRecordsByCoach(GetRecordUrlDTO getRecordUrlDTO) {
        Member currentCoach = memberService.getCurrentCoach();

        List<LessonRecord> RecordList = recordRepository.findNonDeletedRecordByCoachIdAndStudentId(currentCoach.getId(),getRecordUrlDTO.getStudentId());
        List<LessonRecordResponseDTO> responseDTOS = new ArrayList<>();
        for (LessonRecord lessonRecord : RecordList) {
            responseDTOS.add(new LessonRecordResponseDTO(
                    lessonRecord.getId(),
                    lessonRecord.getRecordDate(),
                    lessonRecord.getRecordTitle(),
                    lessonRecord.getRecordUrl()));
        }
        return responseDTOS;
    }


    //coach
    @Transactional
    public UploadResponseDTO updateRecord(Long recordId, MultipartFile multipartFile, UpdateUploadRequestDTO updateUploadRequestDTO) {
        Member currentCoach = memberService.getCurrentCoach();
        LessonRecord existingRecord = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        if (multipartFile.isEmpty()) {
            throw new BusinessException(ErrorCode.MULTIPART_FILE_NOT_FOUND);
        }

        s3UploadService.deleteFile(existingRecord.getRecordUrl());

        String uploadFile = s3UploadService.uploadFile(multipartFile, UploadUtils.LESSON_NOTE_RECORD, currentCoach.getId());

        LessonRecord updatedRecord = recordRepository.updateLessonRecord(recordId, updateUploadRequestDTO.getUpdateRecordDate(), updateUploadRequestDTO.getUpdateRecordTitle(), uploadFile);

        return UploadResponseDTO.builder()
                .s3Url(updatedRecord.getRecordUrl())
                .recordDate(updatedRecord.getRecordDate())
                .recordTitle(updatedRecord.getRecordTitle())
                .build();
    }

    //coach, student
    @Transactional
    public void deleteRecord(Long recordId) {
        LessonRecord existingRecord = recordRepository.findById(recordId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RECORD_NOT_FOUND));

        s3UploadService.deleteFile(existingRecord.getRecordUrl());

        recordRepository.deleteRecord(recordId);
    }

    //student
    @Transactional
    public List<LessonRecordResponseDTO> getAllRecordsForStudent() {
        Member currentStudent = memberService.getCurrentMember();
        Long coach = memberService.getCoachIdByStudentId(currentStudent.getId());

        List<LessonRecord> RecordList = recordRepository.findNonDeletedRecordByCoachIdAndStudentId(coach,currentStudent.getId());
        List<LessonRecordResponseDTO> responseDTOS = new ArrayList<>();
        for (LessonRecord lessonRecord : RecordList) {
            responseDTOS.add(new LessonRecordResponseDTO(
                    lessonRecord.getId(),
                    lessonRecord.getRecordDate(),
                    lessonRecord.getRecordTitle(),
                    lessonRecord.getRecordUrl()));
        }
        return responseDTOS;
    }


}
