package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.chat.enums.ChatType;
import com.exam.tomatoback.chat.model.Chat;
import com.exam.tomatoback.chat.model.ChatLastRead;
import com.exam.tomatoback.chat.model.Room;
import com.exam.tomatoback.chat.model.RoomProgress;
import com.exam.tomatoback.chat.repository.RoomProgressRepository;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.service.PostService;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.repository.UserRepository;
import com.exam.tomatoback.user.service.UserService;
import com.exam.tomatoback.web.dto.chat.api.*;
import com.exam.tomatoback.web.dto.post.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatApiService {
    private final RoomService roomService;
    private final ChatService chatService;
    private final ChatLastReadService chatLastReadService;
    private final ChatImagesService chatImagesService;
    private final ChatEventsService chatEventsService;
    private final UserRepository userRepository;
    private final PostService postService;
    private final UserService userService;
    private final RoomProgressRepository  roomProgressRepository;

    public long getChatRoomIdOrCreateRoomId(ChatRoomRequest chatRoomRequest) {
        Authentication authentication = getAuthentication();

        String email = authentication.getName();

        User requestUser = userRepository.findByEmail(email).orElseThrow(() -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND));

        User targetUser = userRepository.findById(chatRoomRequest.getTargetUserId()).orElseThrow(() -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND));

        Optional<Room> roomOp = roomService.getRoom(requestUser, targetUser);

        Room room;
        room = roomOp.orElseGet(() -> roomService.createRoom(targetUser, requestUser));

        return room.getId();

    }

    public ChatListPageResponse getChatList(ChatListPageRequest chatListPageRequest) {
        Pageable pageable = PageRequest.of(chatListPageRequest.getPage(), chatListPageRequest.getSize());
        Page<Room> rooms = roomService.getRooms(pageable);

        ChatListPageResponse chatListPageResponse = new ChatListPageResponse();
        chatListPageResponse.setCurrentPage(rooms.getNumber());
        chatListPageResponse.setTotalPages(rooms.getTotalPages());
        chatListPageResponse.setTotalElements(rooms.getTotalElements());
        chatListPageResponse.setSize(rooms.getSize());

//        public class ChatListSingleResponse {
//            private String userNickname;
//            private LocalDateTime lastChatTime;
//            private ChatResponse lastChat;
//            private long roomId;
//            private int unreadCount;
//        }


        List<ChatListSingleResponse> chatListSingleResponseList = new ArrayList<>();

        Authentication authentication = getAuthentication();
        String requestUserEmail = authentication.getName();

        for (Room room : rooms.getContent()) {
            User buyer = room.getBuyer();
            User seller = room.getSeller();
            boolean isBuyer = buyer.getEmail().equals(requestUserEmail);


            ChatListSingleResponse chatListSingleResponse = new ChatListSingleResponse();
            chatListSingleResponse.setRoomId(room.getId());

            Optional<Chat> lastChatByRoomId = chatService.getLastChatByRoomId(room.getId());

            if (lastChatByRoomId.isPresent()) {
                Chat lastChat = lastChatByRoomId.get();

                ChatResponse chatResponse = new ChatResponse();
                chatResponse.setChatType(lastChat.getChatType());
                chatResponse.setContent(lastChat.getContent());
                chatResponse.setRoomId(room.getId());
                chatResponse.setSenderId(lastChat.getSender().getId());
                chatResponse.setChatId(lastChat.getId());
                chatResponse.setCreatedAt(lastChat.getCreatedAt());


                chatListSingleResponse.setLastChatTime(lastChat.getCreatedAt());


                boolean existLastRead = chatLastReadService.existLastReadByChatAndRoomAndUser(lastChat, room, isBuyer ? buyer : seller);
                if (existLastRead) {
                    ChatLastRead chatLastRead = chatLastReadService.getLastReadByChatAndRoomAndUser(lastChat, room, isBuyer ? buyer : seller);
                    long countAfterChatId = chatService.countAfterChatId(chatLastRead.getChat(), room);
                    chatListSingleResponse.setUnreadCount((int) countAfterChatId);
                } else {
                    long countAfterChatId = chatService.countAfterChatId(0, room);
                    chatListSingleResponse.setUnreadCount((int) countAfterChatId);
                }


                ChatType chatType = lastChat.getChatType();
                switch (chatType) {
                    case MESSAGE -> {
                    }
                    case IMAGE -> {
                        List<String> chatImagesByChatId = chatImagesService.getChatImagesByChatId(lastChat.getId());
                        chatResponse.setImages(chatImagesByChatId);
                    }
                    case EVENT_BOOK -> {
//                        chatResponse.setTargetId();
//                        chatEventsService.getChatEventBookByChatId(lastChat.getId());
                    }
                    case EVENT_END -> {
//                        chatResponse.setTargetId();
//                        chatEventsService.getChatEventEndByChatId(lastChat.getId());
                    }
                }
                chatListSingleResponse.setLastChat(chatResponse);

            }

//            chatListSingleResponse.setUnreadCount(); 나중에 생각
            chatListSingleResponse.setUserNickname(isBuyer ? seller.getNickname() : buyer.getNickname());

            chatListSingleResponseList.add(chatListSingleResponse);
        }

        chatListPageResponse.setRooms(chatListSingleResponseList);

        return chatListPageResponse;


    }


    public ChatPageResponse getChats(ChatPageRequest chatPageRequest, long roomId) {
        Pageable pageable = PageRequest.of(chatPageRequest.getPage(), chatPageRequest.getSize());
        Authentication authentication = getAuthentication();
        String requestUserEmail = authentication.getName();
        User requestUser = userRepository.findByEmail(requestUserEmail).orElseThrow(() -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND));

        // 룸 접근권한 검사
        roomService.hasAuth(roomId, requestUser);


        Page<Chat> chats = chatService.getChats(roomId, pageable);

        ChatPageResponse chatPageResponse = new ChatPageResponse();
        chatPageResponse.setCurrentPage(chats.getNumber());
        chatPageResponse.setTotalPages(chats.getTotalPages());
        chatPageResponse.setSize(chats.getSize());
        chatPageResponse.setTotalElements(chats.getTotalElements());
        chatPageResponse.setRoomId(roomId);

        List<ChatResponse> chatResponseList = new ArrayList<>();
        for (Chat chat : chats.getContent()) {
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setRoomId(roomId);
            chatResponse.setChatId(chat.getId());
            chatResponse.setCreatedAt(chat.getCreatedAt());
            chatResponse.setContent(chat.getContent());
            chatResponse.setChatType(chat.getChatType());
            chatResponse.setSenderId(chat.getSender().getId());
//
            if (chat.getChatType() == ChatType.IMAGE) {
                chatResponse.setImages(chatImagesService.getChatImagesByChatId(chat.getId()));
            }
            if (chat.getChatType() == ChatType.EVENT_BOOK || chat.getChatType() == ChatType.EVENT_END) {
//                chatResponse.setTargetId(c);
            }
            chatResponseList.add(chatResponse);
        }

        chatPageResponse.setContent(chatResponseList);


        return chatPageResponse;
    }

    public void setLastRead(long roomId, long chatId) {
        Authentication authentication = getAuthentication();
        String requestUserEmail = authentication.getName();
        User requestUser = userRepository.findByEmail(requestUserEmail).orElseThrow(() -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND));

        roomService.hasAuth(roomId, requestUser);

        Room roomById = roomService.getRoomById(roomId);

        Chat chatByIdAndRoomId = chatService.getChatByIdAndRoomId(chatId, roomId);

        chatLastReadService.setLastRead(chatByIdAndRoomId, roomById, requestUser);

    }


    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public ChatRoomInfoResponse getRoomInfo(long roomId) {

        Optional<RoomProgress> byId = roomProgressRepository.findById(roomId);

        ChatRoomInfoResponse chatRoomInfoResponse = new ChatRoomInfoResponse();
        if (byId.isPresent()) {
            RoomProgress roomProgress = byId.get();
            chatRoomInfoResponse.setRoomProgress(roomProgress.getRoomProgress());
            chatRoomInfoResponse.setRoomId(roomId);
            chatRoomInfoResponse.setTargetPost(PostResponse.from(roomProgress.getPost()));
            chatRoomInfoResponse.setRequestUserId(roomProgress.getUser().getId());
        }

        return chatRoomInfoResponse;
    }

    public ChatUserSellingListResponse getUserSellingList(long userId) {
        List<Post> sellingPostByUserId = postService.getSellingPostByUserId(userId);

        ChatUserSellingListResponse response = new ChatUserSellingListResponse();
        response.setTargetUserId(userId);
        response.setPosts(sellingPostByUserId.stream().map(PostResponse::from).toList());

        return response;
    }
}
