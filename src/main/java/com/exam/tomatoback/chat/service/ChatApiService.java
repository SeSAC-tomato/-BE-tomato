package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.chat.enums.ChatType;
import com.exam.tomatoback.chat.model.*;
import com.exam.tomatoback.chat.repository.RoomProgressRepository;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.post.service.PostService;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.service.UserService;
import com.exam.tomatoback.web.dto.chat.api.*;
import com.exam.tomatoback.web.dto.post.post.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final PostService postService;
    private final UserService userService;
    private final RoomProgressRepository roomProgressRepository;
    private final ChatResponseService chatResponseService;

    // 방을 조회, 없으면 생성해서 조회
    public long getChatRoomIdOrCreateRoomId(ChatRoomRequest chatRoomRequest) {
        User requestUser = userService.getCurrentUser();

        User targetUser = userService.getUserByUserId(chatRoomRequest.getTargetUserId());

        Optional<Room> roomOp = roomService.getRoom(requestUser, targetUser);

        Room room;
        room = roomOp.orElseGet(() -> roomService.createRoom(targetUser, requestUser));

        return room.getId();
    }

    // 채팅 목록 조회
    public ChatListPageResponse getChatList(ChatListPageRequest chatListPageRequest) {
        Pageable pageable = PageRequest.of(chatListPageRequest.getPage(), chatListPageRequest.getSize());
        Page<Room> rooms = roomService.getRooms(pageable);

        ChatListPageResponse chatListPageResponse = new ChatListPageResponse();

        chatResponseService.setPageInfo(rooms, chatListPageResponse);

        List<ChatListSingleResponse> chatListSingleResponseList = new ArrayList<>();

        User currentUser = userService.getCurrentUser();

        for (Room room : rooms.getContent()) {
            User buyer = room.getBuyer();
            User seller = room.getSeller();
            boolean isBuyer = buyer.getId().equals(currentUser.getId());


            ChatListSingleResponse chatListSingleResponse = new ChatListSingleResponse();
            chatListSingleResponse.setRoomId(room.getId());
            chatListSingleResponse.setUserId(!isBuyer ? buyer.getId() : seller.getId());

            Optional<Chat> lastChatByRoomId = chatService.getLastChatByRoomId(room.getId());

            if (lastChatByRoomId.isPresent()) {
                Chat lastChat = lastChatByRoomId.get();

                ChatResponse chatResponse = new ChatResponse();

                chatResponse.setRoomId(room.getId());
                chatResponse.setSenderId(lastChat.getSender().getId());
                chatResponse.setChatType(lastChat.getChatType());

                chatResponse.setChatId(lastChat.getId());
                chatResponse.setCreatedAt(lastChat.getCreatedAt());
                chatResponse.setContent(lastChat.getContent());


                chatListSingleResponse.setLastChatTime(lastChat.getCreatedAt());



                boolean existLastRead = chatLastReadService.existLastReadByChatAndRoomAndUser(room, isBuyer ? buyer : seller);
                if (existLastRead) {
                    ChatLastRead chatLastRead = chatLastReadService.getLastReadByChatAndRoomAndUser(room, isBuyer ? buyer : seller);
                    long countAfterChatId = chatService.countAfterChatId(chatLastRead.getChat(), room);
                    chatListSingleResponse.setUnreadCount((int) countAfterChatId);
                } else {
                    long countAfterChatId = chatService.countAfterChatId(0, room);
                    chatListSingleResponse.setUnreadCount((int) countAfterChatId);
                }


                ChatType chatType = lastChat.getChatType();
                switch (chatType) {
                    case IMAGE -> {
                        List<String> chatImagesByChatId = chatImagesService.getChatImagesByChatId(lastChat.getId());
                        chatResponse.setImages(chatImagesByChatId);
                    }
                    case EVENT_BOOK, EVENT_CANCEL, EVENT_END -> {
                        ChatEvents chatEventByChatId = chatEventsService.getChatEventByChatId(lastChat.getId());

                        chatResponse.setTargetId(String.valueOf(chatEventByChatId.getTargetPost().getId()));
                        chatResponse.setPost(PostResponse.from(chatEventByChatId.getTargetPost()));

                        boolean isSender = lastChat.getSender().getId().equals(currentUser.getId());
                        boolean isDone = lastChat.getIsDone();

                        String message = chatResponseService.createMsg(isSender, isDone, chatType);

                        chatResponse.setContent(message);
                    }
                }
                chatListSingleResponse.setLastChat(chatResponse);

            }

            chatListSingleResponse.setUserNickname(isBuyer ? seller.getNickname() : buyer.getNickname());

            chatListSingleResponseList.add(chatListSingleResponse);
        }

        chatListPageResponse.setRooms(chatListSingleResponseList);

        return chatListPageResponse;
    }


    // 채팅 리스트 조회
    public ChatPageResponse getChats(ChatPageRequest chatPageRequest, long roomId) {
        Pageable pageable = PageRequest.of(chatPageRequest.getPage(), chatPageRequest.getSize());

        User requestUser = userService.getCurrentUser();

        // 룸 접근권한 검사
        roomService.hasAuth(roomId, requestUser);


        Page<Chat> chats = chatService.getChats(roomId, pageable);

        ChatPageResponse chatPageResponse = new ChatPageResponse();

        chatResponseService.setPageInfo(chats,chatPageResponse);


        chatPageResponse.setRoomId(roomId);

        List<ChatResponse> chatResponseList = new ArrayList<>();
        for (Chat chat : chats.getContent()) {
            ChatResponse chatResponse = new ChatResponse();

            chatResponse.setRoomId(roomId);
            chatResponse.setSenderId(chat.getSender().getId());
            chatResponse.setChatType(chat.getChatType());

            chatResponse.setChatId(chat.getId());
            chatResponse.setCreatedAt(chat.getCreatedAt());
            chatResponse.setContent(chat.getContent());


            if (chat.getChatType() == ChatType.IMAGE) {
                chatResponse.setImages(chatImagesService.getChatImagesByChatId(chat.getId()));
            }
            if (chat.getChatType() == ChatType.EVENT_BOOK || chat.getChatType() == ChatType.EVENT_END || chat.getChatType() == ChatType.EVENT_CANCEL) {
                ChatEvents chatEvents = chatEventsService.getChatEventByChatId(chat.getId());
                Post targetPost = chatEvents.getTargetPost();

                String msg = chatResponseService.createMsg(requestUser.getId().equals(chat.getSender().getId()), chat.getIsDone(), chat.getChatType());

                chatResponse.setContent(msg);

                chatResponse.setTargetId(String.valueOf(targetPost.getId()));
                chatResponse.setPost(PostResponse.from(targetPost));

            }
            chatResponseList.add(chatResponse);
        }

        chatPageResponse.setContent(chatResponseList);


        return chatPageResponse;
    }

    // 마지막 읽은 채팅 표시
    public void setLastRead(long roomId, long chatId) {
        User requestUser = userService.getCurrentUser();

        roomService.hasAuth(roomId, requestUser);

        Room roomById = roomService.getRoomById(roomId);

        Chat chatByIdAndRoomId = chatService.getChatByIdAndRoomId(chatId, roomId);

        chatLastReadService.setLastRead(chatByIdAndRoomId, roomById, requestUser);
    }


    // 거래 상황 조회
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

    // 판매중인 상품 목록 조회
    public ChatUserSellingListResponse getUserSellingList(long userId) {
        List<Post> sellingPostByUserId = postService.getSellingPostByUserId(userId);

        ChatUserSellingListResponse response = new ChatUserSellingListResponse();
        response.setTargetUserId(userId);
        response.setPosts(sellingPostByUserId.stream().map(PostResponse::from).toList());

        return response;
    }

}
