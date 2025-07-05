package com.exam.tomatoback.chat.repository;

import com.exam.tomatoback.chat.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsBySellerIdAndBuyerIdOrBuyerIdAndSellerId(Long sellerId, Long buyerId, Long anotherBuyerId, Long anotherSellerId);

    Optional<Room> findBySellerIdAndBuyerIdOrBuyerIdAndSellerId(Long sellerId, Long buyerId, Long anotherBuyerId, Long anotherSellerId);


    Page<Room> findByIsActiveTrueAndSellerIdOrIsActiveTrueAndBuyerId(long sellerId, long buyerId, Pageable pageable);
}
