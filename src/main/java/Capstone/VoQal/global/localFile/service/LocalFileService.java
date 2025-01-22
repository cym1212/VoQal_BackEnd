package Capstone.VoQal.global.localFile.service;

import Capstone.VoQal.global.enums.ErrorCode;
import Capstone.VoQal.global.error.exception.BusinessException;
import Capstone.VoQal.global.localFile.utils.LocalUploadUtils;
import Capstone.VoQal.infra.s3upload.utils.UploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocalFileService {

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Value("${file.deleted.dir}")
    private String deletedDir;


    public String uploadFile(MultipartFile multipartFile, String subPath, Long memberId) {
        String fileName = LocalUploadUtils.createFileName(multipartFile.getOriginalFilename(), memberId);
        Path destinationPath = Paths.get(uploadDir, subPath, fileName);

        try {
            // 경로가 없으면 디렉토리 생성
            Files.createDirectories(destinationPath.getParent());

            // 파일 저장
            Files.copy(multipartFile.getInputStream(), destinationPath);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.STORAGE_UPLOAD_FAILURE);
        }

        return destinationPath.toString();
    }


    public String copyFile(String fileUrl, String sourcePath, String newSubPath) {
        Path destinationDir = Paths.get(uploadDir, newSubPath); // 대상 디렉토리 경로

        try {
            // 복사 대상 디렉토리 생성
            Files.createDirectories(destinationDir);

            // 이동하려는 파일의 이름 추출
            String fileName = Paths.get(fileUrl).getFileName().toString();

            // 대상 파일 경로
            Path destinationFile = destinationDir.resolve(fileName);

            // 파일 존재 여부 확인
            if (!Files.exists(Path.of(fileUrl))) {
                throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
            }

            // 파일 복사
            Files.copy(Path.of(fileUrl), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.STORAGE_COPY_FAILURE);
        }

        return destinationDir.resolve(Paths.get(fileUrl).getFileName()).toString();
    }


    public void deleteFile(String fileUrl) {
        Path source = Paths.get(fileUrl); // 삭제할 파일 경로

        try {
            // 파일 존재 여부 확인
            if (!Files.exists(source)) {
                log.warn("파일이 존재하지 않아 삭제할 수 없습니다: {}", fileUrl);
                throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
            }

            // 파일 삭제
            Files.delete(source);
            log.info("파일이 삭제되었습니다: {}", fileUrl);
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.STORAGE_DELETE_FAILURE);
        }
    }
}