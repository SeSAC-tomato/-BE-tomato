package com.exam.tomatoback.web.controller.chat;

import com.exam.tomatoback.chat.service.ChatApiService;
import com.exam.tomatoback.infrastructure.util.Constants;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.service.UserService;
import com.exam.tomatoback.web.dto.chat.api.ChatListPageRequest;
import com.exam.tomatoback.web.dto.chat.api.ChatPageRequest;
import com.exam.tomatoback.web.dto.chat.api.ChatRoomRequest;
import com.exam.tomatoback.web.dto.chat.api.ChatRoomResponse;
import com.exam.tomatoback.web.dto.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatApiController {
    private final ChatApiService chatApiService;
    private final UserService userService;


    // login
    // 채팅 목록 불러오기
    @GetMapping
    public ResponseEntity<?> getChatList(@Valid ChatListPageRequest chatListPageRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(chatApiService.getChatList(chatListPageRequest)));
    }


    // login
    // 채팅룸에 있는 채팅 불러오기
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getChat(@Valid ChatPageRequest chatPageRequest, @PathVariable long roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(chatApiService.getChats(chatPageRequest, roomId)));
    }

    // login
    // 신규 채팅 룸 만들기
    @GetMapping("/room")
    public ResponseEntity<?> getChatRoom(ChatRoomRequest chatRoomRequest) {
        long chatRoomIdOrCreateRoomId = chatApiService.getChatRoomIdOrCreateRoomId(chatRoomRequest);
        User userByUserId = userService.getUserByUserId(chatRoomRequest.getTargetUserId());


        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(new ChatRoomResponse(chatRoomIdOrCreateRoomId, userByUserId.getId(), userByUserId.getNickname())));
    }

    // login
    // 마지막 읽은 챗
    @GetMapping("/room/{roomId}/chat/{chatId}")
    public ResponseEntity<?> setLastRead(@PathVariable long roomId, @PathVariable long chatId) {
        chatApiService.setLastRead(roomId, chatId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(null));
    }

    // 예약 진행 상황 확인
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getRoomInfo(@PathVariable long roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(chatApiService.getRoomInfo(roomId)));
    }

    // 판매중인 상품 확인
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserSellingList(@PathVariable long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(chatApiService.getUserSellingList(userId)));
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(Constants.CHAT_IMAGE_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 간단하게 이미지로 한정 (jpg/png/gif 등)
            String contentType = "application/octet-stream";
            if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (filename.endsWith(".png")) {
                contentType = "image/png";
            } else if (filename.endsWith(".gif")) {
                contentType = "image/gif";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
