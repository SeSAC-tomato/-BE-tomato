package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.chat.model.Room;
import com.exam.tomatoback.chat.repository.RoomRepository;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserService userService;

    public Room getRoomById(long id) {
        return roomRepository.findById(id).orElseThrow(() -> new TomatoException(TomatoExceptionCode.CHAT_ROOM_NOT_FOUND));
    }

    public void setRoomActive(Room roomById) {
        roomById.setActive(true);
        roomRepository.save(roomById);
    }

    public Room createRoom(User seller, User buyer) {
        Room newRoom = new Room();
        newRoom.setActive(false);
        newRoom.setSeller(seller);
        newRoom.setBuyer(buyer);
        return roomRepository.save(newRoom);
    }

    public Page<Room> getRooms(Pageable pageable) {
        User user = userService.getCurrentUser();
        return roomRepository.findByIsActiveTrueAndSellerIdOrIsActiveTrueAndBuyerId(user.getId(), user.getId(), pageable);
    }

    public Optional<Room> getRoom(User requestUser, User targetUser) {
        return roomRepository.findBySellerIdAndBuyerIdOrBuyerIdAndSellerId(requestUser.getId(), targetUser.getId(), requestUser.getId(), targetUser.getId());
    }

    public void hasAuth(long roomId, User requestUser) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new TomatoException(TomatoExceptionCode.CHAT_ROOM_NOT_FOUND));

        User buyer = room.getBuyer();
        User seller = room.getSeller();

        if (!(Objects.equals(buyer.getId(), requestUser.getId()) || Objects.equals(seller.getId(), requestUser.getId()))) {
            throw new TomatoException(TomatoExceptionCode.CHAT_ACCESS_DENIED);
        }
    }
}
