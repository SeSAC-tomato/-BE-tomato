package com.exam.tomatoback.chat.enums;

/**
 * 채팅 메시지 타입
 *
 * @author insu kim
 * @version 1.0
 * @since 2025-06-30
 */
public enum ChatType {
    /**
     * 단순 메시지
     */
    MESSAGE,
    /**
     * 이미지가 포함된 메시
     */
    IMAGE,
    /**
     * 예약 요청
     */
    EVENT_BOOK,
    /**
     * 거래 종료 요청(정상)
     */
    EVENT_END,
    /**
     * 거래 취소
     */
    EVENT_CANCEL


}
