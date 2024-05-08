package Capstone.VoQal.global.auth.controller;


import Capstone.VoQal.global.auth.dto.ChangeNicknameDTO;
import Capstone.VoQal.global.auth.dto.MemberListDTO;
import Capstone.VoQal.global.auth.dto.RoleDTO;
import Capstone.VoQal.global.auth.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profileService;
    @PostMapping("/role/coach")
    @Operation(summary = " 코치로 역할 설정 로직 ", description = "역할을 코치로 설정할 경우")
    public ResponseEntity<String> setRoleCoach() {
        profileService.setRoleToCoach();

        return ResponseEntity.ok("코치로 설정되었습니다");
    }

    @GetMapping("/role/coach")
    @Operation(summary = " 코치 조회 ", description = "학생일 경우를 선택했을 때 코치 리스트 조회")
    public List<MemberListDTO> getCoachList() {
        List<MemberListDTO> coachList = profileService.getCoachList();
        return coachList;
    }

    @PatchMapping("/{id}/change-nickname")
    @Operation(summary = " 닉네임 변경 ", description = "닉네임을 변경하는 로직")
    public ResponseEntity<String> updateNickname(@PathVariable("id") Long id, @RequestBody ChangeNicknameDTO changeNicknameDTO) {
        profileService.updateNickname(id, changeNicknameDTO);
        return ResponseEntity.ok().body("닉네임 변경에 성공했습니다.");

    }

    @PostMapping("/{coachId}/student")
    @Operation(summary = "학생으로 역할 설정(코치 선택) ", description = "코치를 선택하고 승인을 기다리는 로직")
    public ResponseEntity<String> requestCoachAssignment(@PathVariable("coachId") Long coachId, @RequestBody RoleDTO.StudentDTO studentDTO) {
        return null;
    }
}
