package com.exam.tomatoback.web.controller.chat;

import com.exam.tomatoback.chat.service.ChatApiService;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.service.UserService;
import com.exam.tomatoback.web.dto.chat.api.ChatListPageRequest;
import com.exam.tomatoback.web.dto.chat.api.ChatPageRequest;
import com.exam.tomatoback.web.dto.chat.api.ChatRoomRequest;
import com.exam.tomatoback.web.dto.chat.api.ChatRoomResponse;
import com.exam.tomatoback.web.dto.common.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatApiController {
    private final ChatApiService chatApiService;
    private final UserService userService;


    // 채팅 목록 불러오기
    @GetMapping
    public ResponseEntity<?> getChatList(@Valid ChatListPageRequest chatListPageRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(chatApiService.getChatList(chatListPageRequest)));
    }


    // 채팅룸에 있는 채팅 불러오기
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getChat(@Valid ChatPageRequest chatPageRequest, @PathVariable long roomId) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(chatApiService.getChats(chatPageRequest, roomId)));
    }

    // 신규 채팅 룸 만들기
    @GetMapping("/room")
    public ResponseEntity<?> getChatRoom(ChatRoomRequest chatRoomRequest) {
        long chatRoomIdOrCreateRoomId = chatApiService.getChatRoomIdOrCreateRoomId(chatRoomRequest);
        User userByUserId = userService.getUserByUserId(chatRoomRequest.getTargetUserId());


        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(new ChatRoomResponse(chatRoomIdOrCreateRoomId, userByUserId.getId(), userByUserId.getNickname())));
    }

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


}
