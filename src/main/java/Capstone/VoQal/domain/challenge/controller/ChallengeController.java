package Capstone.VoQal.domain.challenge.controller;

import Capstone.VoQal.domain.challenge.dto.*;
import Capstone.VoQal.domain.challenge.service.ChallengeService;
import Capstone.VoQal.domain.challenge.service.KeywordService;
import Capstone.VoQal.global.dto.MessageDTO;
import Capstone.VoQal.global.dto.ResponseWrapper;
import Capstone.VoQal.global.error.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/challenge")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final KeywordService keywordService;

    @GetMapping
    @Operation(summary = "앱 사용자가 올린 모든 챌린지 게시물을 조회합니다 ", description = "앱 사용자가 올린 게시물을 페이징 하여 조회합니다. 페이지 갯수와 사이즈를 지정하면 그것에 맞게 조회합니다.")
    public ResponseEntity<ResponseWrapper<List<GetAllChallengeResponseDTO>>> getAllChallengePosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        List<GetAllChallengeResponseDTO> response =  challengeService.getAllChallengePosts(page, size);
        ResponseWrapper<List<GetAllChallengeResponseDTO>> responseWrapper = ResponseWrapper.<List<GetAllChallengeResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
        return ResponseEntity.ok(responseWrapper);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "챌린지 작성 로직 ", description = " 챌린지를 업로드 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "Multipart File을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 멤버 ID 입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> createChallengePost(
            @RequestPart(value = "thumbnail") MultipartFile thumbnail,
            @RequestPart(value = "record") MultipartFile record,
            @RequestPart(value = "data") CreateChallengeRequestDTO createChallengeRequestDTO) {
        challengeService.createChallengePost(thumbnail, record, createChallengeRequestDTO);
        return ResponseEntity.ok().body(MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("성공적으로 작성되었습니다.")
                .build());
    }

    @GetMapping("/my")
    @Operation(summary = "본인이 작성한 챌린지 조회 로직 ", description = "본인이 작성한 챌린지를 조회합니다.")
    public ResponseEntity<ResponseWrapper<List<GetMyChallengeResponseDTO>>> getMyChallengePost() {
        List<GetMyChallengeResponseDTO> response = challengeService.getMyChallengePost();
        ResponseWrapper<List<GetMyChallengeResponseDTO>> responseWrapper = ResponseWrapper.<List<GetMyChallengeResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
        return ResponseEntity.ok(responseWrapper);
    }

    @PatchMapping(value = "/{challengePostId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "챌린지 수정 로직 ", description = "본인이 작성한 챌린지를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "요청에 실패했습니다. 다시 요청해주세요", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Multipart File을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "시간이 지나 챌린지 게시물을 수정할 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "챌린지 게시물을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> updateChallengePost(
            @PathVariable("challengePostId") Long challengePostId,
            @RequestPart(value ="thumbnail") MultipartFile thumbnail,
            @RequestPart(value ="record") MultipartFile record,
            @RequestPart(value = "data") UpdateChallengeRequestDTO updateChallengeRequestDTO) {
        challengeService.updateChallengePost(challengePostId, thumbnail, record, updateChallengeRequestDTO);
        return ResponseEntity.ok().body(MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("성공적으로 수정되었습니다.")
                .build());
    }

    @DeleteMapping("/{challengePostId}")
    @Operation(summary = "챌린지 삭제 로직 ", description = "본인이 작성한 챌린지를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "요청에 실패했습니다. 다시 요청해주세요", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "400", description = "챌린지 게시물을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            })
    public ResponseEntity<MessageDTO> deleteChallengePost(@PathVariable("challengePostId") Long challengePostId) {
        challengeService.deleteChallengePost(challengePostId);
        return ResponseEntity.ok().body(MessageDTO.builder()
                .status(HttpStatus.OK.value())
                .message("성공적으로 삭제되었습니다.")
                .build());
    }

    @GetMapping("/keyword")
    @Operation(summary = "챌린지 키워드 조회 로직 ", description = "오늘의 챌린지 키워드를 조회합니다.")
    public KeywordDTO getKeyword() {
        return keywordService.getKeywordAndColor();
    }

}
