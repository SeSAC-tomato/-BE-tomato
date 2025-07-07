package com.exam.tomatoback.auth.service;

import com.exam.tomatoback.web.dto.auth.response.KakaoAddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KakaoLocalServiceImpl implements KakaoLocalService {
  private final RestTemplate template = new RestTemplate();
  @Value("${rest-api.kakao}")
  public String kakaoRestKey;

  @Override
  public KakaoAddressResponse searchAddress(String address) {
    String url = UriComponentsBuilder.fromUriString("https://dapi.kakao.com/v2/local/search/address.json")
        .queryParam("query", address)
        .build()
        .toUriString();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "KakaoAK " + kakaoRestKey);

    HttpEntity<?> entity = new HttpEntity<>(headers);

    ResponseEntity<KakaoAddressResponse> response = template.exchange(
        url,
        HttpMethod.GET,
        entity,
        KakaoAddressResponse.class
    );
    return response.getBody();
  }
}
