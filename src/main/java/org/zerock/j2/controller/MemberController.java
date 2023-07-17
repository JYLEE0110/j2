package org.zerock.j2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.j2.dto.MemberDTO;
import org.zerock.j2.service.MemberService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/member/")
@Log4j2
public class MemberController {

    private final MemberService memberService;

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
