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
import java.util.ArrayList;
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
    public List<GetMyChallengeResponseDTO> getMyChallengePost () {
        Member currentMember = memberService.getCurrentMember();
        List<ChallengePost> challengePosts = challengePostRepository.findAllNonDeletedPostById(currentMember.getId());
        List<GetMyChallengeResponseDTO> responseDTOS = new ArrayList<>();
        String todayKeyword = keywordService.getTodayKeyword();
        for (ChallengePost challengePost : challengePosts) {
            responseDTOS.add(new GetMyChallengeResponseDTO(
                    challengePost.getThumbnailUrl(),
                    challengePost.getChallengeRecordUrl(),
                    challengePost.getId(),
                    todayKeyword,
                    challengePost.getSongTitle(),
                    challengePost.getSinger(),
                    challengePost.getMember().getNickName()
            ));
        }

        return responseDTOS;
    }

    //본인이 올린 챌린지 수정
    @Transactional
    public void updateChallengePost(Long challengePostId, MultipartFile thumbnail, MultipartFile record, UpdateChallengeRequestDTO updateChallengeRequestDTO) {
        Member currentMember = memberService.getCurrentMember();
        if (thumbnail.isEmpty() || record.isEmpty()) {
            throw new BusinessException(ErrorCode.MULTIPART_FILE_NOT_FOUND);
        }

        ChallengePost existingPost = challengePostRepository.findById(challengePostId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHALLENGE_POST_NOT_FOUND));

        s3UploadService.copyFile(existingPost.getChallengeRecordUrl(), UploadUtils.CHALLENGE_RECORD, UploadUtils.CHALLENGE_RECORD_DELETED);
        s3UploadService.copyFile(existingPost.getThumbnailUrl(), UploadUtils.CHALLENGE_THUMBNAIL, UploadUtils.CHALLENGE_THUMBNAIL_DELETED);

        s3UploadService.deleteFile(existingPost.getChallengeRecordUrl());
        s3UploadService.deleteFile(existingPost.getThumbnailUrl());

        String updateThumbnail = s3UploadService.uploadFile(thumbnail, UploadUtils.CHALLENGE_THUMBNAIL, currentMember.getId());
        String updateRecord = s3UploadService.uploadFile(record, UploadUtils.CHALLENGE_RECORD, currentMember.getId());

        challengePostRepository.updateChallengePost(challengePostId,updateThumbnail,updateRecord,updateChallengeRequestDTO.getUpdateSongTitle(),updateChallengeRequestDTO.getUpdateSinger() );

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
