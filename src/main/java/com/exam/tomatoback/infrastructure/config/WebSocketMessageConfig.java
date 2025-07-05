package com.exam.tomatoback.infrastructure.config;


import com.exam.tomatoback.infrastructure.interceptor.JwtChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;


/**
 *
 */

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketMessageConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;

    /**
     * ⭐ 이 빈(Bean)이 특정 에러를 해결하는 핵심입니다.
     * 이 빈은 기반 서블릿 컨테이너의 WebSocket 버퍼 크기를 구성합니다.
     * Base64 인코딩된 이미지의 경우 `setMaxTextMessageBufferSize`가 특히 중요합니다.
     * `setMaxBinaryMessageBufferSize`는 순수 바이너리 WebSocket 메시지를 보낼 때 사용됩니다.
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean factoryBean = new ServletServerContainerFactoryBean();
        // 텍스트 메시지(Base64 이미지 데이터가 여기에 해당)에 대한 버퍼 크기 설정
        factoryBean.setMaxTextMessageBufferSize(10 * 1024 * 1024); // 예: 10MB (Base64 이미지에 충분한 크기)

        // 바이너리 메시지(직접 바이너리 데이터를 보낼 경우 중요)에 대한 버퍼 크기 설정
        factoryBean.setMaxBinaryMessageBufferSize(10 * 1024 * 1024); // 예: 10MB

        // 큰 전송에 시간이 오래 걸릴 경우 세션 유휴 시간(idle timeout)을 늘릴 수도 있습니다.
        // factoryBean.setMaxSessionIdleTimeout(60 * 1000L); // 예: 60초
        return factoryBean;
    }


    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        // ⭐ 이 설정은 *STOMP 메시지*의 크기 제한입니다. 설정하는 것이 좋지만,
        // 현재 로그는 *WebSocket 프레임* 버퍼 문제임을 지적하고 있습니다.
        // Base64로 인코딩된 이미지는 클 수 있으므로 10MB는 큰 이미지에 대한 좋은 시작점입니다.
        registry.setMessageSizeLimit(10 * 1024 * 1024); // 예: 10MB
        registry.setSendBufferSizeLimit(10 * 1024 * 1024); // 예: 10MB
        registry.setSendTimeLimit(20 * 1000); // 예: 큰 전송에 시간이 오래 걸
    }


    /**
     * 구독, 퍼블리싱 prefix 설
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독 엔드포인트
        registry.enableSimpleBroker("/ws/sub");

        // 전송 엔드포인트
        registry.setApplicationDestinationPrefixes("/ws/pub");
    }

    /**
     * STOMP 연결 엔드포인드 설정
     * Origins -> CORS -> 이후 프론트로 변경해야함
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // STOMP 연결 엔드포인트
        registry.addEndpoint("/websocket").setAllowedOrigins("http://172.16.15.71:3000", "http://192.168.0.2:3000", "http://localhost:3000").withSockJS();
    }

    /**
     * 인터셉터 설정 -> 인증, 인가 설정
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // 채널 인터셉터 -> 구독, 전송등 권한 검사 등
        registration.interceptors(jwtChannelInterceptor);
    }
}
