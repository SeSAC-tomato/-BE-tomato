package com.exam.tomatoback.infrastructure.interceptor;

import com.exam.tomatoback.chat.model.Room;
import com.exam.tomatoback.chat.service.RoomService;
import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.exception.TomatoExceptionCode;
import com.exam.tomatoback.infrastructure.util.Constants;
import com.exam.tomatoback.infrastructure.util.JwtUtil;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.web.dto.chat.websocket.ChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private static final String SUB_DESTINATION = "/ws/sub/room/";
    private static final String PUB_DESTINATION = "/ws/pub";

    private final UserDetailsService userDetailsService;
    private final RoomService roomService;
    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        // 권한 검증 검사 해야함

        log.debug("JwtChannelInterceptor");

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;


        String destination = accessor.getDestination();

        log.debug("destination: {}", destination);


        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // 연결 권한 검사
            String authorization = accessor.getFirstNativeHeader("Authorization");

            String token = Optional.ofNullable(authorization)
                    .filter(t -> t.startsWith(Constants.ACCESS_TOKEN_PREFIX))
                    .map(t -> t.substring(Constants.ACCESS_TOKEN_PREFIX.length()))
                    .orElseThrow(() -> new TomatoException(TomatoExceptionCode.CHAT_ACCESS_DENIED));

            String userEmail = jwtUtil.getUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            accessor.setUser(authentication);


            return message;

        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            // 구독 권한 검사

            if(!destination.contains(SUB_DESTINATION)){
                return message;
            }

            if (destination == null) {
                // 부적합
                // bad request
                throw new TomatoException(TomatoExceptionCode.CHAT_BAD_REQUEST);
            }

            String roomId = getRoomId(destination);

            String userEmail = accessor.getUser().getName();


            Room roomById = roomService.getRoomById(Long.parseLong(roomId));

            // 룸 권한 검사
            User seller = roomById.getSeller();
            User buyer = roomById.getBuyer();

            if (!(seller.getEmail().equals(userEmail) || buyer.getEmail().equals(userEmail))) {
                // access Denied
                throw new TomatoException(TomatoExceptionCode.CHAT_ACCESS_DENIED);
            }

        }

        if (StompCommand.SEND.equals(accessor.getCommand())) {
            // 전송 권한 검사
            ChatRequest chatRequest = message.getPayload() instanceof ChatRequest ? (ChatRequest) message.getPayload() : null;

//            || chatRequest == null ||
//                    chatRequest.getRoomId().equals(Long.parseLong(getRoomId(destination
            if (destination == null || !destination.contains(PUB_DESTINATION)) {

                // 부적합
                // bad request
                throw new TomatoException(TomatoExceptionCode.CHAT_BAD_REQUEST);
            }

            if(destination.contains("room")){
                String roomId = getRoomId(destination);

                Room roomById = roomService.getRoomById(Long.parseLong(roomId));

                String userEmail = accessor.getUser().getName();

//            chatRequest.getRoomId().equals(roomId);

                // 룸 권한 검사
                User seller = roomById.getSeller();
                User buyer = roomById.getBuyer();

                if (!(seller.getEmail().equals(userEmail) || buyer.getEmail().equals(userEmail))) {
                    // access Denied
                    throw new TomatoException(TomatoExceptionCode.CHAT_ACCESS_DENIED);
                }
            }



        }


        return message;
    }

    private String getRoomId(String destination) {
        String[] parts = destination.split("/");
        return parts[parts.length - 1];
    }

}
