package com.exam.tomatoback.infrastructure.util;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;
import java.io.IOException;

@Component
public class PostImageServer {
   //Base64인코딩, return 경로, 저장 실패 Exception
   //data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...형태로 전달

    public String saveBase64PostImage(String base64PostImage) throws IOException {
        String[] parts = base64PostImage.split(",");
        String postImageString = parts.length > 1 ? parts[1]:parts[0];


        String extension = "png";
        if(parts[0].contains("image/")){
            String[] headerParts = parts[0].split("/");
            if(headerParts.length>1){
                extension = headerParts[1].split(";")[0];
            }
        }

        String fileName = UUID.randomUUID().toString()+"."+extension;
        Path imageSavedPath = Paths.get(Constants.POST_IMAGE_DIR, fileName);

        byte[] postImageBytes = Base64.getDecoder().decode(postImageString);
        Files.createDirectories(imageSavedPath.getParent());
        Files.write(imageSavedPath, postImageBytes);

        return fileName;
    }
}
