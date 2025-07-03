package com.exam.tomatoback.infrastructure.filter;

import com.exam.tomatoback.infrastructure.exception.TomatoException;
import com.exam.tomatoback.infrastructure.util.Constants;
import com.exam.tomatoback.infrastructure.util.JwtUtil;
import com.exam.tomatoback.web.dto.common.CommonResponse;
import com.exam.tomatoback.web.dto.common.ExceptionResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(Constants.AUTH_HEADER);
        final String access;
        final String username;
        if(authorization == null || !authorization.startsWith(Constants.ACCESS_TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        access = authorization.substring(Constants.ACCESS_TOKEN_PREFIX.length());
        try {
            username = jwtUtil.getUsername(access);
        } catch (TomatoException e) {
            log.warn("JWT 예외 발생: {}", e.getMessage());

            // JWT 관련 예외 응답 반환
            response.setStatus(e.getStatus().value());
            response.setContentType("application/json;charset=UTF-8");

            ExceptionResponse exceptionResponse = ExceptionResponse.of(
                e.getStatus(), e.getCode(), e.getMessage()
            );

            String json = new com.fasterxml.jackson.databind.ObjectMapper()
                .writeValueAsString(CommonResponse.fail(exceptionResponse));

            response.getWriter().write(json);
            return;
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(jwtUtil.validate(access, userDetails)) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
                log.debug("JWT 인증 성공: {}", username);
            }
        }
        filterChain.doFilter(request, response);
    }
}
