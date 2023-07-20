package org.zerock.j2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.j2.dto.MemberDTO;
import org.zerock.j2.service.MemberService;
import org.zerock.j2.service.SocialService;
import org.zerock.j2.util.JWTUtil;

import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/member/")
@Log4j2
public class MemberController {

    private final MemberService memberService;

    private final SocialService socialService;

    private final JWTUtil jwtUtil;

    @GetMapping("kakao")
    public MemberDTO getAuthCode(String code) {

        log.info("=========================");
        log.info(code);

        String email = socialService.getKakaoEmail(code);

        MemberDTO memberDTO = memberService.getMemberWithEmail(email);
        // DB에 존재하는지 비교 2가지
        // 이미 DB에 존재하는 유저 / 없는 유저(최초 소셜로그인 시도 시)=> redirect 수정페이지 pw 수정안하면 랜덤하게 생성

        return memberDTO;
    }

    @PostMapping("login")
    public MemberDTO login(@RequestBody MemberDTO memberDTO) {

        log.info("prameger : " + memberDTO);

        // 로그인 2초뒤에 실행 pending 테스트
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        MemberDTO result = memberService.login(
                memberDTO.getEmail(),
                memberDTO.getPw()
        );

        // 토큰 생성 후 반환
        result.setAccessToken(jwtUtil.generate(Map.of("email", result.getEmail()), 1));
        result.setRefreshToken(jwtUtil.generate(Map.of("email", result.getEmail()), 60 * 24));

        log.info("reutrn" + result);

        System.out.println("result " + result);

        return result;
    }

    // JWT Interceptor 거치면 안된다. => config
    // 쿼릿 스트링으로 refreshToken이 날아간다.
    @RequestMapping("refresh")
    public Map<String, String> refresh(@RequestHeader("Authorization") String accessToken,
                                       String refreshToken) {


        log.info("Refresh....access" + accessToken);
        log.info("Refresh....refresh" + refreshToken);

        // accessToken은 만료되었는지 확인


        // refreshToken은 만료되지 않았는지 확인
        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);

        return Map.of("accessToken", jwtUtil.generate(claims, 1),
                "refreshToken", jwtUtil.generate(claims, 60 * 24));

    }

}
