package Capstone.VoQal.domain.challenge.service;

import Capstone.VoQal.domain.challenge.domain.ChallengePost;
import Capstone.VoQal.domain.challenge.dto.CreateChallengeRequestDTO;
import Capstone.VoQal.domain.challenge.dto.GetAllChallengeResponseDTO;
import Capstone.VoQal.domain.challenge.dto.GetMyChallengeResponseDTO;
import Capstone.VoQal.domain.challenge.dto.UpdateChallengeRequestDTO;
import Capstone.VoQal.domain.challenge.repository.challenge.ChallengePostRepository;
import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.service.MemberService;
import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import Capstone.VoQal.infra.s3upload.service.S3UploadService;
import Capstone.VoQal.infra.s3upload.utils.UploadUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final MemberService memberService;
    private final KeywordService keywordService;
    private final ChallengePostRepository challengePostRepository;
    private final S3UploadService s3UploadService;

    // 모든 사용자의 챌린지 조회 + 일정 시간마다 랜덤한 순서로 인덱싱 후 가져와야함
    @Transactional
    public List<GetAllChallengeResponseDTO> getAllChallengePosts(int page, int size) {
        Member currentMember = memberService.getCurrentMember();
        Pageable pageable = PageRequest.of(page, size);
        Page<GetAllChallengeResponseDTO> allNonDeletedPosts = challengePostRepository.findAllNonDeletedPostsWithLikes(currentMember.getId(), pageable);


        // 페이지네이션 정보 없이 컨텐츠 리스트만 반환
        return allNonDeletedPosts.getContent();
    }

    @Transactional
    public void randomizePostOrder() {
        challengePostRepository.randomizePostOrder();
    }

    @Transactional
    public void deleteOldChallengePosts(LocalDateTime start, LocalDateTime end) {
        challengePostRepository.deleteOldChallengePosts(start, end);
    }

    //본인이 올릴 챌린지 생성
    @Transactional
    public void createChallengePost(MultipartFile thumbnail, MultipartFile record, CreateChallengeRequestDTO createChallengeRequestDTO) {
        Member currentMember = memberService.getCurrentMember();

        if (thumbnail.isEmpty() || record.isEmpty()) {
            throw new BusinessException(ErrorCode.MULTIPART_FILE_NOT_FOUND);
        }
        String uploadThumbnail = s3UploadService.uploadFile(thumbnail, UploadUtils.CHALLENGE_THUMBNAIL, currentMember.getId());
        String uploadRecord = s3UploadService.uploadFile(record, UploadUtils.CHALLENGE_RECORD, currentMember.getId());

        ChallengePost challengePost = ChallengePost.builder()
                .thumbnailUrl(uploadThumbnail)
                .challengeRecordUrl(uploadRecord)
                .member(currentMember)
                .singer(createChallengeRequestDTO.getSinger())
                .songTitle(createChallengeRequestDTO.getSongTitle())
                .build();

        challengePostRepository.save(challengePost);

    }

    //본인이 올렸던 챌린지 조회
    @Transactional
    public List<GetMyChallengeResponseDTO> getMyChallengePost() {
        Member currentMember = memberService.getCurrentMember();
        return challengePostRepository.findAllNonDeletedPostById(currentMember.getId());
    }


    //본인이 올린 챌린지 수정
    @Transactional
    public void updateChallengePost(Long challengePostId, MultipartFile thumbnail, MultipartFile record, UpdateChallengeRequestDTO updateChallengeRequestDTO) {
        Member currentMember = memberService.getCurrentMember();

        // 기존 게시물 가져오기
        ChallengePost existingPost = challengePostRepository.findById(challengePostId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHALLENGE_POST_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fiveMinutesAgo = now.minusMinutes(5);

        if (existingPost.getCreatedAt().isBefore(fiveMinutesAgo)) {
            throw new BusinessException(ErrorCode.CAN_NOT_UPDATE);
        }

        // 썸네일 파일이 있으면 수정, 없으면 기존 파일 유지
        String updateThumbnail;
        if (thumbnail != null && !thumbnail.isEmpty()) {
            // 기존 썸네일 백업 및 삭제
            s3UploadService.copyFile(existingPost.getThumbnailUrl(), UploadUtils.CHALLENGE_THUMBNAIL, UploadUtils.CHALLENGE_THUMBNAIL_DELETED);
            s3UploadService.deleteFile(existingPost.getThumbnailUrl());
            // 새 썸네일 업로드
            updateThumbnail = s3UploadService.uploadFile(thumbnail, UploadUtils.CHALLENGE_THUMBNAIL, currentMember.getId());
        } else {
            updateThumbnail = existingPost.getThumbnailUrl(); // 수정하지 않을 경우 기존 값 유지
        }

        // 녹음 파일이 있으면 수정, 없으면 기존 파일 유지
        String updateRecord;
        if (record != null && !record.isEmpty()) {
            // 기존 녹음 파일 백업 및 삭제
            s3UploadService.copyFile(existingPost.getChallengeRecordUrl(), UploadUtils.CHALLENGE_RECORD, UploadUtils.CHALLENGE_RECORD_DELETED);
            s3UploadService.deleteFile(existingPost.getChallengeRecordUrl());
            // 새 녹음 파일 업로드
            updateRecord = s3UploadService.uploadFile(record, UploadUtils.CHALLENGE_RECORD, currentMember.getId());
        } else {
            updateRecord = existingPost.getChallengeRecordUrl(); // 수정하지 않을 경우 기존 값 유지
        }

        // 제목이나 가수가 수정되지 않았다면 기존 값 유지
        String updateSongTitle = updateChallengeRequestDTO.getUpdateSongTitle() != null ?
                updateChallengeRequestDTO.getUpdateSongTitle() : existingPost.getSongTitle();
        String updateSinger = updateChallengeRequestDTO.getUpdateSinger() != null ?
                updateChallengeRequestDTO.getUpdateSinger() : existingPost.getSinger();

        // 최종 업데이트
        challengePostRepository.updateChallengePost(challengePostId, updateThumbnail, updateRecord, updateSongTitle, updateSinger);
    }



    //본인이 올린 챌린지 삭제
    @Transactional
    public void deleteChallengePost(Long challengePostId) {
        ChallengePost existingPost = challengePostRepository.findById(challengePostId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHALLENGE_POST_NOT_FOUND));


        s3UploadService.copyFile(existingPost.getChallengeRecordUrl(), UploadUtils.CHALLENGE_RECORD, UploadUtils.CHALLENGE_RECORD_DELETED);
        s3UploadService.copyFile(existingPost.getThumbnailUrl(), UploadUtils.CHALLENGE_THUMBNAIL, UploadUtils.CHALLENGE_THUMBNAIL_DELETED);

        s3UploadService.deleteFile(existingPost.getThumbnailUrl());
        s3UploadService.deleteFile(existingPost.getChallengeRecordUrl());

        challengePostRepository.deleteChallengePost(challengePostId);

    }

}
