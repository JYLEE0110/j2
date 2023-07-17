package org.zerock.j2.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.j2.dto.MemberCartDTO;
import org.zerock.j2.service.MemberCartService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Log4j2
@RequestMapping("/api/cart/")
public class MemberCartController {

    // 상품 데이터를 넣어놓으면 데이터가 많아 질 수 도있고, sale / 가격변동이 있을 수 도있어서
    // 상품번호와 카트번호만 배열로 담아서
    private final MemberCartService memberCartService;
    @PostMapping("add")
    public List<MemberCartDTO> add(@RequestBody MemberCartDTO memberCartDTO){

        log.info("param" + memberCartDTO);

        return memberCartService.addCart(memberCartDTO);

    }

    @GetMapping("{email}")
    public List<MemberCartDTO> get(@PathVariable("email") String email){
        return memberCartService.getCart(email);
    }

}
