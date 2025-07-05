package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.chat.model.Room;
import com.exam.tomatoback.chat.repository.RoomRepository;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public Room getRoomById(long id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new TomatoException(TomatoExceptionCode.CHAT_ROOM_NOT_FOUND));

        return room;
    }

    public void setRoomActive(Room roomById) {
        roomById.setActive(true);
        roomRepository.save(roomById);
    }

    public Room createRoom(User seller, User buyer) {
        Room newRoom = new  Room();
        newRoom.setActive(false);
        newRoom.setSeller(seller);
        newRoom.setBuyer(buyer);
        Room save = roomRepository.save(newRoom);
        return save;
    }

    public Page<Room> getRooms(Pageable pageable) {
        Authentication authentication = getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new TomatoException(TomatoExceptionCode.USER_NOT_FOUND));


        Page<Room> byIsActiveTrueAndSellerIdOrIsActiveTrueAndBuyerId = roomRepository.findByIsActiveTrueAndSellerIdOrIsActiveTrueAndBuyerId(user.getId(), user.getId(), pageable);


        return byIsActiveTrueAndSellerIdOrIsActiveTrueAndBuyerId;
    }

//    public long getRoomId(ChatRoomRequest chatRoomRequest) {
//        roomRepository.find
//    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Optional<Room> getRoom(User requestUser, User targetUser) {

     return roomRepository.findBySellerIdAndBuyerIdOrBuyerIdAndSellerId(requestUser.getId(), targetUser.getId(), requestUser.getId(), targetUser.getId());

    }

    public void hasAuth(long roomId, User requestUser) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new TomatoException(TomatoExceptionCode.CHAT_ROOM_NOT_FOUND));

        User buyer = room.getBuyer();
        User seller = room.getSeller();

        if(!(Objects.equals(buyer.getId(), requestUser.getId()) || Objects.equals(seller.getId(), requestUser.getId()))){
            throw new TomatoException(TomatoExceptionCode.CHAT_ACCESS_DENIED);
        }

    }
}
