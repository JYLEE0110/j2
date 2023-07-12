package org.zerock.j2.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 자동 으로 FK 생성
@Embeddable
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImage {

    private String fname;
    
    private int ord;
}
