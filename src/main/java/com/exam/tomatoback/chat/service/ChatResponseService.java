package com.exam.tomatoback.chat.service;

import com.exam.tomatoback.chat.enums.ChatType;
import com.exam.tomatoback.web.dto.chat.api.DefaultPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatResponseService {

    // 채팅 메시지 생성
    // 이벤트의 경우
    public String createMsg(boolean isSender, boolean isDone, ChatType chatType) {
        String message = "";
        if (isSender) {
            // 내가 보낸 경우
            switch (chatType) {
                case EVENT_BOOK:
                    if (isDone) {
                        message = "예약이 확정되었습니다.";
                    } else {
                        message = "예약 요청을 보냈습니다.";
                    }
                    break;
                case EVENT_END:
                    if (isDone) {
                        message = "거래가 완료되었습니다.";
                    } else {
                        message = "거래 완료를 요청했습니다.";
                    }
                    break;
                case EVENT_CANCEL:
                    message = "거래를 취소했습니다.";
                    break;
            }
        } else {
            // 내가 받은 경우
            switch (chatType) {
                case EVENT_BOOK:
                    if (isDone) {
                        message = "예약 요청을 수락했습니다.";
                    } else {
                        message = "상대방의 예약 요청이 도착했습니다.";
                    }
                    break;
                case EVENT_END:
                    if (isDone) {
                        message = "거래가 완료되었습니다.";
                    } else {
                        message = "상대방이 거래 완료를 요청했습니다.";
                    }
                    break;
                case EVENT_CANCEL:
                    message = "상대방이 거래를 취소했습니다.";
                    break;
            }
        }
        return message;
    }

    public <T extends DefaultPageResponse> void setPageInfo(Page<?> pagedEntity, T pageResponse) {
        pageResponse.setCurrentPage(pagedEntity.getNumber());
        pageResponse.setSize(pagedEntity.getSize());
        pageResponse.setTotalPages(pagedEntity.getTotalPages());
        pageResponse.setTotalElements(pagedEntity.getTotalElements());
    }
}
