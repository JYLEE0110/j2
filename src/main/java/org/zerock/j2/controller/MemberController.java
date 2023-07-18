package org.zerock.j2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.j2.dto.MemberDTO;
import org.zerock.j2.service.MemberService;
import org.zerock.j2.service.SocialService;

import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/member/")
@Log4j2
public class MemberController {

    private final MemberService memberService;

    private final SocialService socialService;

    @GetMapping("kakao")
    public MemberDTO getAuthCode(String code){

        log.info("=========================");
        log.info(code);

        String email = socialService.getKakaoEmail(code);

        MemberDTO memberDTO = memberService.getMemberWithEmail(email);
        // DB에 존재하는지 비교 2가지
        // 이미 DB에 존재하는 유저 / 없는 유저(최초 소셜로그인 시도 시)=> redirect 수정페이지 pw 수정안하면 랜덤하게 생성

        return memberDTO;
    }

    @PostMapping("login")
    public MemberDTO login(@RequestBody MemberDTO memberDTO){

        log.info("prameger : "  + memberDTO);

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
        log.info("reutrn" + result);

        System.out.println("result " + result);

        return result;
    }


}
