package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.chat.enums.ChatType;
import com.exam.tomatoback.chat.enums.RoomProgressEnum;
import com.exam.tomatoback.chat.model.*;
import com.exam.tomatoback.chat.repository.RoomProgressRepository;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.infrastructure.util.ChatImageSaver;
import com.exam.tomatoback.infrastructure.util.Constants;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.service.PostService;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.repository.UserRepository;
import com.exam.tomatoback.web.dto.chat.api.ChatResponse;
import com.exam.tomatoback.web.dto.chat.websocket.ChatRequest;
import com.exam.tomatoback.web.dto.post.post.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatWebSocketService {
    private final RoomService roomService;
    private final ChatService chatService;
    private final ChatImagesService chatImagesService;
    private final ChatEventsService chatEventsService;
    private final UserRepository userRepository;
    private final ChatImageSaver chatImageSaver;
    private final PostService postService;
    private final RoomProgressRepository roomProgressRepository;

    @Transactional(rollbackFor = Exception.class)
    public ChatResponse saveChat(long roomId, Principal principal, ChatRequest chatRequest) throws IOException {

        // htmlEscape
        chatRequest.setContent(HtmlUtils.htmlEscape(chatRequest.getContent()));


        boolean equals = chatRequest.getRoomId().equals(roomId);
        if (!equals) throw new TomatoException(TomatoExceptionCode.CHAT_BAD_REQUEST);

        Room roomById = roomService.getRoomById(roomId);

        User sender = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND));

        if (!roomById.isActive()) roomService.setRoomActive(roomById);

        // 챗 저장
        Chat newChat = new Chat();
        newChat.setChatType(chatRequest.getChatType());
        newChat.setRoom(roomById);
        newChat.setContent(chatRequest.getContent());
        newChat.setSender(sender);
        newChat.setIsDone(chatRequest.getIsDone());
        Chat savedChat = chatService.save(newChat);


        // 응답
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setRoomId(roomId);
        chatResponse.setSenderId(sender.getId());
        chatResponse.setChatType(chatRequest.getChatType());

        chatResponse.setChatId(savedChat.getId());
        chatResponse.setCreatedAt(savedChat.getCreatedAt());
        chatResponse.setContent(savedChat.getContent());

        switch (chatRequest.getChatType()) {
            case IMAGE -> {

                // 이미지일 경우 저장
                ChatImages chatImages = new ChatImages();
                chatImages.setUrl(Constants.CHAT_IMAGE_DIR);
                chatImages.setSavedName(chatImageSaver.saveBase64Image(chatRequest.getImages().get(0)));
                chatImages.setChat(savedChat);
                chatImages.setOriginalName("unknown");

                ChatImages savedChatImages = chatImagesService.save(chatImages);
                //

                // 추가 응답
                chatResponse.setImages(List.of(savedChatImages.getSavedName()));
            }

            case EVENT_BOOK, EVENT_END, EVENT_CANCEL -> {

                Post postNoMatterWhatById = postService.getPostNoMatterWhatById(chatRequest.getTargetId());

                // 이벤트 일 경우 저장
                ChatEvents chatEvents = new ChatEvents();
                chatEvents.setChat(savedChat);
                chatEvents.setTargetPost(postNoMatterWhatById);

                ChatEvents saved = chatEventsService.save(chatEvents);
                //

                // BOOK 요청
                if (chatRequest.getChatType() == ChatType.EVENT_BOOK)
                    whenEventBook(roomById, sender, postNoMatterWhatById);

                // END 요청
                if (chatRequest.getChatType() == ChatType.EVENT_END)
                    whenEventEnd(roomById, sender, postNoMatterWhatById);

                // CANCEL 요청
                if (chatRequest.getChatType() == ChatType.EVENT_CANCEL)
                    whenEventCancel(roomById, sender, postNoMatterWhatById);

                // 추가 응답
                chatResponse.setTargetId(String.valueOf(saved.getTargetPost().getId()));
                chatResponse.setPost(PostResponse.from(saved.getTargetPost()));

                chatResponse.setContent("✅ 진행 상황을 확인해주세요!");
            }
        }

        return chatResponse;
    }


    public void whenEventBook(Room room, User sender, Post post) {
        Optional<RoomProgress> byId = roomProgressRepository.findById(room.getId());
        if (byId.isPresent()) {
            // 예약 이벤트 받았고, 이미 RoomProgress 있음 -> 예약 수락함
            RoomProgress roomProgress = byId.get();
            RoomProgressEnum roomProgressEnum = roomProgress.getRoomProgress();

            if (roomProgressEnum == RoomProgressEnum.BOOK_REQUEST) {
                roomProgress.setRoomProgress(RoomProgressEnum.BOOKED);
                RoomProgress save = roomProgressRepository.save(roomProgress);

                // 포스트 예약 상태로  여기서 바꾸면 될 듯 -> 추가 예정

            } else {
                System.out.println("잘못된 코드 흐름임");
                throw new TomatoException(TomatoExceptionCode.CHAT_BAD_REQUEST);
            }
        } else {
            // 예약 이벤트 받았고, 없음 -> 예약 요청
            RoomProgress roomProgress = new RoomProgress();
            roomProgress.setRoomProgress(RoomProgressEnum.BOOK_REQUEST);
            roomProgress.setUser(sender);

            roomProgressRepository.save(roomProgress);
        }
    }

    public void whenEventEnd(Room room, User sender, Post post) {
        Optional<RoomProgress> byId = roomProgressRepository.findById(room.getId());

        if (byId.isPresent()) {
            // 거래 완료 이벤트 받았고, 이미 있음 -> 요청 수락 -> 상황 종료로 지움
            roomProgressRepository.deleteById(room.getId());


            // 여기서 거래 완료 처리하면 될듯 -> 추가 예정


        } else {
            // 거래 완료 이벤트 받았고, 없음 -> 완료 요청
            RoomProgress roomProgress = new RoomProgress();
            roomProgress.setRoomProgress(RoomProgressEnum.END_REQUEST);
            roomProgress.setUser(sender);

            roomProgressRepository.save(roomProgress);
        }

    }

    public void whenEventCancel(Room room, User sender, Post post) {
        Optional<RoomProgress> byId = roomProgressRepository.findById(room.getId());

        if (byId.isPresent()) {
            // 거래 취소 요청 -> RoomProgress 없앰
            roomProgressRepository.deleteById(room.getId());

        } else {
            System.out.println("잘못된 코드 흐름");
            throw new TomatoException(TomatoExceptionCode.CHAT_BAD_REQUEST);
        }
    }


}
