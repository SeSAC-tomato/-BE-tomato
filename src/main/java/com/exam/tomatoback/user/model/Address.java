package com.exam.tomatoback.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // 외래키를 pk 로 사용하기 위한 어노테이션
    @JoinColumn(name = "user_id")
    private User user;
    // 카카오 다음 주소 검색 api 에서 반환될 주소를 저장 (예: 경기 성남시 분당구 판교역로 166 or 경기 성남시 분당구 백현동 532)
    private String address;
    // 아래의 정보는 이메일 인증 후 위 주소를 api 를 사용하여 변환 후 저장이 될 예정
    @Column(name = "POINT", columnDefinition = "POINT SRID 4326")
    private Point point;
}
