package com.exam.tomatoback.infrastructure.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Component
public class ChatImageSaver {
    // 이미지 저장 기본 경로 (환경에 맞게 수정)

    /**
     * Base64 인코딩된 이미지 데이터를 파일로 저장하고, 저장된 파일 경로를 반환합니다.
     * @param base64Image Base64 인코딩된 이미지 데이터 (data:image/png;base64,... 형식도 지원)
     * @return 저장된 파일의 경로 (String)
     * @throws IOException 저장 실패 시 예외
     */
    public String saveBase64Image(String base64Image) throws IOException {
        // data:image/png;base64, 헤더 분리
        String[] parts = base64Image.split(",");
        String imageString = parts.length > 1 ? parts[1] : parts[0];

        // 파일 확장자 추출 (기본값: png)
        String extension = "png";
        if (parts[0].contains("image/")) {
            String[] headerParts = parts[0].split("/");
            if (headerParts.length > 1) {
                extension = headerParts[1].split(";")[0];
            }
        }

        // 파일명 생성 (UUID)
        String fileName = UUID.randomUUID().toString() + "." + extension;
        Path filePath = Paths.get(Constants.CHAT_IMAGE_DIR, fileName);

        // 디코딩 및 파일 저장
        byte[] imageBytes = Base64.getDecoder().decode(imageString);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, imageBytes);

        // 저장된 파일 경로 반환 (상대경로나 URL로 변환 필요시 추가 구현)
        return fileName;
    }




}
