package Capstone.VoQal.infra.s3upload.utils;

import java.util.UUID;

public class UploadUtils {
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String MEMBER_UUID_SEPARATOR = "-";
    public static final String LESSON_NOTE_RECORD = "lesson-note-record/";
    public static final String CHALLENGE = "challenge/";

    public static String createFileName(String originalFileName, Long memberId) {
        String ext = getFileExtension(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return memberId + MEMBER_UUID_SEPARATOR + uuid + ext;
    }

    public static String getFileExtension(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR));
    }
}