package org.zerock.j2.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;

@Service
@Log4j2
public class SocialServiceImpl implements SocialService {

    // application.properties에 저장한 값을 불러옴
    // => 이렇게 안하면 빌드 후 수정하면 다시 빌드해야한다.
    // properties는 메모장이라 아래 방식으로 처리
    @Value("${org.zerock.kakao.token_url}")
    private String tokenURL;

    @Value("${org.zerock.kakao.rest_key}")
    private String clientId;

    @Value("${org.zerock.kakao.redirect_url}")
    private String redirectURI;

    @Value("${org.zerock.kakao.get_user}")
    private String getUser;

    @Override
    public String getKakaoEmail(String authCode) {

        log.info("===============");
        log.info("authCode: " + authCode);
        log.info("tokenURL: " + tokenURL);
        log.info("clientId: " + clientId);
        log.info("redirectURI: " + redirectURI);
        log.info("getUser: " + getUser);

        String accessToken = getAccessToken(authCode);

        String email = getEmailFromAccessToken(accessToken);

        return email;
    }

    private String getAccessToken(String authCode) {

        String accessToken = null;
        // 크롤링할때도 사용
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        // 리액트에서 header 추가 부분이랑 동일
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(tokenURL)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectURI)
                .queryParam("code", authCode)
                .build(true);

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(
                        uriComponents.toString(), HttpMethod.POST, entity, LinkedHashMap.class);

        log.info("-----------------------------------");
        log.info(response);

        // response는 해쉬 맵  안에 accessToken을 뽑아줘야한다.
        LinkedHashMap<String, String> bodyMap = response.getBody();

        accessToken = bodyMap.get("access_token");

        log.info("access Token: " + accessToken);

        return accessToken;
    }

    private String getEmailFromAccessToken(String accessToken){

        if(accessToken == null){
            throw new RuntimeException("Access Token is null");
        }
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type","application/x-www-form-urlencoded");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(getUser).build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(
                        uriBuilder.toString(),
                        HttpMethod.GET,
                        entity,
                        LinkedHashMap.class);

        log.info(response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();

        log.info("------------------------------------");
        log.info(bodyMap);

        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("kakao_account");

        log.info("kakaoAccount: " + kakaoAccount);

        return kakaoAccount.get("email");

    }
}
