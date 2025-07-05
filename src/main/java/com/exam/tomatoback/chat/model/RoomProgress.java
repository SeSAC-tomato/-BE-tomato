package com.exam.tomatoback.chat.model;


import com.exam.tomatoback.chat.enums.RoomProgressEnum;
import com.exam.tomatoback.post.model.Post;
import com.exam.tomatoback.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomProgress {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId  // User의 ID를 이 엔티티의 ID로 사용
    @JoinColumn(name = "id",  nullable = false, updatable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_user_id", nullable = false)
    private User user;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "room_progress")
    private RoomProgressEnum roomProgress;

    @ManyToOne(fetch =  FetchType.LAZY )
    @JoinColumn(name = "target_post",nullable = false, updatable = false)
    private Post post;



}
