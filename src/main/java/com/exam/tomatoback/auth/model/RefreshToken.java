package com.exam.tomatoback.auth.model;

import com.exam.tomatoback.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // 외래키를 pk 로 사용하기 위한 어노테이션
    @JoinColumn(name = "user_id")
    private User user;

    private String token;
}

