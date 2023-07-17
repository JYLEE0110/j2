package org.zerock.j2.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    
    private String email;
    
    // JSON ignore를 걸어서 변환이 안되게 처리 => JSON데이터 수집할 떄도 문제
    private String pw;
    
    private String nickname;
    
    private boolean admin;
    
}
