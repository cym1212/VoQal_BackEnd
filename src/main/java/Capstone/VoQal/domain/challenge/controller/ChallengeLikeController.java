package Capstone.VoQal.domain.challenge.controller;

import Capstone.VoQal.domain.challenge.dto.ChallengePostWithLikesDTO;
import Capstone.VoQal.domain.challenge.dto.GetLikedPostDTO;
import Capstone.VoQal.domain.challenge.service.ChallengeLikeService;
import Capstone.VoQal.global.dto.MessageDTO;
import Capstone.VoQal.global.dto.ResponseWrapper;
import Capstone.VoQal.global.error.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChallengeLikeController {

    private final ChallengeLikeService challengeLikeService;

    @PostMapping("/{challengePostId}/like")
    @Operation(summary = "좋아요 추가", description = "본인 혹은 다른사람의 게시물에 대한 좋아요를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 멤버 ID입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "이미 좋아요를 눌렀습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "챌린지 게시물을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> likeChallenge(@PathVariable(value = "challengePostId") Long challengePostId) {
        challengeLikeService.likeChallenge(challengePostId);
        return ResponseEntity.ok().body(MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("좋아요를 눌렀습니다")
                .build());
    }

    @DeleteMapping("/{challengePostId}/unlike")
    @Operation(summary = "좋아요 취소", description = "본인이 누른 게시물의 좋아요를 취소합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "좋아요를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "챌린지 게시물을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> unlikeChallenge(@PathVariable (value = "challengePostId") Long challengePostId) {
        challengeLikeService.unlikeChallenge(challengePostId);
        return ResponseEntity.ok().body(MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("좋아요를 취소했습니다")
                .build());
    }

    @GetMapping("/liked")
    @Operation(summary = "좋아요 누른 게시물 조회 ", description = "본인이 좋아요를 누른 게시물을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 멤버 ID입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<ResponseWrapper<List<GetLikedPostDTO>>> getLikedChallenges() {
        List<GetLikedPostDTO> likedChallenges = challengeLikeService.getLikedChallenges();
        ResponseWrapper<List<GetLikedPostDTO>> response = ResponseWrapper.<List<GetLikedPostDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(likedChallenges)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count/likes")
    @Operation(summary = "본인이 작성한 게시물의 좋아요 갯수를 확인하는 로직 ", description = "본인의 게시물에 받은 좋아요 갯수를 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 멤버 ID입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<ResponseWrapper<List<ChallengePostWithLikesDTO>>> getAllChallengePostsWithLikeCount() {
        List<ChallengePostWithLikesDTO> postsWithLikes = challengeLikeService.getAllChallengePostsWithLikeCount();
        ResponseWrapper<List<ChallengePostWithLikesDTO>> response = ResponseWrapper.<List<ChallengePostWithLikesDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(postsWithLikes)
                .build();
        return ResponseEntity.ok(response);
    }
}

