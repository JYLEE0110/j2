package org.zerock.j2.service;

import jakarta.transaction.Transactional;
import org.zerock.j2.dto.MemberCartDTO;
import org.zerock.j2.entity.MemberCart;

import java.util.List;

@Transactional
public interface MemberCartService {

    List<MemberCartDTO> addCart(MemberCartDTO memberCartDTO);

    // 장바구니에 담긴 상품 수를 새로고침해도 유지
    List<MemberCartDTO> getCart(String email);

    // MemberDTO를 entity로
    default MemberCart dtoToEntity(MemberCartDTO dto){
        MemberCart entity = MemberCart.builder()
                .cno(dto.getCno())
                .email(dto.getEmail())
                .pno(dto.getPno())
                .build();

        return entity;
    }

    default MemberCartDTO entityToDTO(MemberCart entity){

        MemberCartDTO dto = MemberCartDTO.builder()
                .cno(entity.getCno())
                .email(entity.getEmail())
                .pno(entity.getPno())
                .build();

        return dto;
    }
}
