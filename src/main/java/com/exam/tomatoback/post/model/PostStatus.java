package com.exam.tomatoback.post.model;

import lombok.Getter;

@Getter
public enum PostStatus {
        SELLING("SELLING"),
        BOOKED("BOOKED"),
        END("END");

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
