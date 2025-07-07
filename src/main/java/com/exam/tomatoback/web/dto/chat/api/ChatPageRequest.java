package com.exam.tomatoback.web.dto.chat.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatPageRequest {
    @Positive
    @Max(100)
    private int size = 100;
    @Min(0)
    private int page = 0;
}
