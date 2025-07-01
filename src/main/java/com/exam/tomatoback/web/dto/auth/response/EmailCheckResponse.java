package com.exam.tomatoback.web.dto.auth.response;

import lombok.Builder;

@Builder
public record EmailCheckResponse(
    boolean duplication,
    String message
) {
}
