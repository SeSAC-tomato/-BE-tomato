package com.exam.tomatoback.post.model;

import lombok.Getter;

@Getter
public enum PostStatus {
        SELLING("판매중"),
        BOOKED("예약중"),
        END("판매완료");

        private final String currentStatus;

        PostStatus(String currentStatus){
                this.currentStatus = currentStatus;
        }

        public PostStatus nextStatus(){
                return switch(this) {
                        case SELLING -> BOOKED;
                        case BOOKED -> END;
                        case END -> null;
                };
        }
}
