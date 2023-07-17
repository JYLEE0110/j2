package org.zerock.j2.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.zerock.j2.service.MemberServiceImpl;

import java.util.Map;

@RestControllerAdvice
@Log4j2
public class CustomControllerAdvice {

    // MemverServiceImpl에 로그인 exception이 발생 했을 시 status가 200이 뜨게 설정
    @ExceptionHandler(MemberServiceImpl.MemberLoginException.class)
    public ResponseEntity<Map<String,String>> handlerException(MemberServiceImpl.MemberLoginException e){

        log.info("----------");
        log.info(e.getMessage());

        // e.getMessage => 비밀번호가 틀렸는지 ID가 틀렸는지 확인이 가능해서 LoginFail로 응답 메세지를 보내준다.
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("errorMsg", "LoginFail"));

    }

}
