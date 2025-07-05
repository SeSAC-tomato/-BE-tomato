package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.chat.enums.ChatType;
import com.exam.tomatoback.chat.model.Chat;
import com.exam.tomatoback.chat.model.ChatEvents;
import com.exam.tomatoback.chat.model.ChatImages;
import com.exam.tomatoback.chat.model.Room;
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
import com.exam.tomatoback.web.dto.post.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatWebSocketService {
    private final RoomService roomService;
    private final ChatService chatService;
    private final ChatLastReadService chatLastReadService;
    private final ChatImagesService chatImagesService;
    private final ChatEventsService chatEventsService;
    private final UserRepository userRepository;
    private final ChatImageSaver chatImageSaver;
    private final PostService postService;

    @Transactional(rollbackFor = Exception.class)
    public ChatResponse saveChat(long roomId, Principal principal, ChatRequest chatRequest) throws IOException {

        chatRequest.setContent(HtmlUtils.htmlEscape(chatRequest.getContent()));

        boolean equals = chatRequest.getRoomId().equals(roomId);
        if (!equals) throw new TomatoException(TomatoExceptionCode.CHAT_BAD_REQUEST);

        Room roomById = roomService.getRoomById(roomId);


        User sender = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND));


        if (!roomById.isActive()) roomService.setRoomActive(roomById);

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setRoomId(roomId);
        chatResponse.setSenderId(sender.getId());
        chatResponse.setChatType(chatRequest.getChatType());

        // 추
//        chatResponse.setTargetId();
//        chatResponse.setImages();

        switch (chatRequest.getChatType()) {
            case MESSAGE -> {

                Chat newChat = new Chat();
                newChat.setChatType(ChatType.MESSAGE);
                newChat.setRoom(roomById);
                newChat.setContent(chatRequest.getContent());
                newChat.setSender(sender);
                Chat save = chatService.save(newChat);

                chatResponse.setChatId(save.getId());
                chatResponse.setCreatedAt(save.getCreatedAt());
                chatResponse.setContent(save.getContent());


            }
            case IMAGE -> {
                Chat newChat = new Chat();
                newChat.setChatType(ChatType.IMAGE);
                newChat.setRoom(roomById);
                newChat.setContent(chatRequest.getContent());
                newChat.setSender(sender);
                Chat savedChat = chatService.save(newChat);

                // 이미지 저장 작업 해야함

                ChatImages chatImages = new ChatImages();
                chatImages.setUrl(Constants.CHAT_IMAGE_DIR);
                chatImages.setSavedName(chatImageSaver.saveBase64Image(chatRequest.getImages().get(0)));
                chatImages.setChat(savedChat);
                chatImages.setOriginalName("이름안가져왔");

                ChatImages savedChatImages = chatImagesService.save(chatImages);



                chatResponse.setChatId(savedChat.getId());
                chatResponse.setCreatedAt(savedChat.getCreatedAt());
                chatResponse.setContent(savedChat.getContent());

                chatResponse.setImages(List.of(savedChatImages.getSavedName()));

            }

            case EVENT_BOOK -> {
                Chat newChat = new Chat();
                newChat.setChatType(ChatType.EVENT_BOOK);
                newChat.setRoom(roomById);
                newChat.setContent(chatRequest.getContent());
                newChat.setSender(sender);
                Chat savedChat = chatService.save(newChat);

                ChatEvents chatEvents = new ChatEvents();
                chatEvents.setChat(savedChat);
                chatEvents.setEventDone(false);
                Post postNoMatterWhatById = postService.getPostNoMatterWhatById(chatRequest.getTargetId());

                chatEvents.setTargetPost(postNoMatterWhatById);
                ChatEvents saved = chatEventsService.save(chatEvents);

                chatResponse.setIsEventDone(saved.isEventDone());
                chatResponse.setPost(PostResponse.from(saved.getTargetPost()));

                chatResponse.setChatId(savedChat.getId());
                chatResponse.setCreatedAt(savedChat.getCreatedAt());
                chatResponse.setContent(savedChat.getContent());

            }

            case EVENT_END -> {
                Chat newChat = new Chat();
                newChat.setChatType(ChatType.EVENT_END);
                newChat.setRoom(roomById);
                newChat.setContent(chatRequest.getContent());
                newChat.setSender(sender);
                Chat savedChat = chatService.save(newChat);

//                ChatEvents chatEvents = new ChatEvents();
//                chatEvents.setChat(savedChat);

                chatResponse.setChatId(savedChat.getId());
                chatResponse.setCreatedAt(savedChat.getCreatedAt());
                chatResponse.setContent(savedChat.getContent());

            }
        }


        return chatResponse;
    }
}
