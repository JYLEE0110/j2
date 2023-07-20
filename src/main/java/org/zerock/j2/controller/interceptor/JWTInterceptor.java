package org.zerock.j2.controller.interceptor;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.zerock.j2.util.JWTUtil;

import java.util.Map;

// 특정 메뉴 접근 시 JWT 토큰이 없으면 API호출이 안되게 Interceptor 설계
// interceptor는 Controller 앞에 존재

// component => bean이다.
@Component
@Log4j2
@RequiredArgsConstructor
public class JWTInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {

        // preFlight일때 => Axios시 cors검증?
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        try {
            
            String headerStr = request.getHeader("Authorization");
            
            // 토큰이 비어있을때 또는 Bearer
            if(headerStr == null || headerStr.length()< 7 ){
                throw new JWTUtil.CustomJWTException("NullToken");
            }
            // Bearer 엑세스 토큰
            String accessToken = headerStr.substring(7);

            Map<String, Object> claims = jwtUtil.validateToken(accessToken);
            log.info("result: " + claims);

        } catch (Exception e) {
            response.setContentType("application/json");

            // 상태 메세지 => 401
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            // Map을 Json으로 변환해서 보냄
            Gson gson = new Gson();
            String str = gson.toJson(Map.of("error", e.getMessage()));

            response.getOutputStream().write(str.getBytes());
            return false;
        }

        log.info("--------------");
        log.info(handler);
        log.info("--------------");
        log.info(jwtUtil);
        log.info("--------------");
        log.info("--------------");

        return true;
    }
}
