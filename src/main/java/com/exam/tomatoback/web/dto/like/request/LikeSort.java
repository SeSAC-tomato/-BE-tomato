package com.exam.tomatoback.web.dto.like.request;

public enum LikeSort {
    LIKE_CREATED_AT("LIKE_CREATED_AT"), // LIKE 생성 시각
    POST_CREATED_AT("post.createdAt"), // 게시글 생성 시각. 이건 post.createdAt값을 사용함
    PRICE("PRICE"), // 게시글 가격
    POPULARITY("POPULARITY");   // 게시글의 총 좋아요 수

    private final String fieldPath;

    // 생성자: enum 상수 초기화용
    LikeSort(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    // getter: 외부에서 enum과 연결된 값 꺼내기
    public String getFieldPath() {
        return fieldPath;
    }
}
