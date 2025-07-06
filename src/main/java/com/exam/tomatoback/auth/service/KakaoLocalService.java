package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.web.dto.auth.response.KakaoAddressResponse;

public interface KakaoLocalService {
  KakaoAddressResponse searchAddress(String address);
}
