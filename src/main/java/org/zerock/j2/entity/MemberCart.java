package org.zerock.j2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
// 인덱스 잡아 줌
@Table(name="member_cart", indexes = @Index(columnList = "email,cno"))
public class MemberCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    private String email;

    private Long pno;


    // 시간이 추가 되어야 함

}
