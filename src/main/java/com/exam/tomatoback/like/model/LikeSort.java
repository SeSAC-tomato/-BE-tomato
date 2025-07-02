package com.exam.tomatoback.like.model;

public enum LikeSort {
    LIKED_AT("likes.createdAt"),
    CREATED_AT("createdAt"),
    PRICE("price"),
    POPULARITY("likeCounts");

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
