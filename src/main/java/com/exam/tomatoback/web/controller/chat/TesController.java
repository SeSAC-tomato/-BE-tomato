package com.exam.tomatoback.web.controller.chat;

import com.exam.tomatoback.infrastructure.util.Constants;
import com.exam.tomatoback.user.model.User;
import com.exam.tomatoback.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tes")
public class TesController {

    private final UserService userService;

    // 자기정보 가져오기
    @GetMapping("/login/userInfo")
    public Map<String, String> getUserInfo() {
        Map<String, String> map = new HashMap<>();
        User currentUser = userService.getCurrentUser();
        map.put("userId", String.valueOf(currentUser.getId()));
        map.put("email", currentUser.getEmail());
        map.put("nickname", currentUser.getNickname());
        return map;
    }

}
