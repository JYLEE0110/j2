package org.zerock.j2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.j2.entity.MemberCart;

import java.util.List;

public interface MemberCartRepository extends JpaRepository<MemberCart, Long> {

    // 인덱스를 태웠으므로 where 조건 => order by도
    @Query("select mc from MemberCart mc where mc.email = :email order by mc.cno asc")
    List<MemberCart> selectCart(@Param("email") String email);

}
