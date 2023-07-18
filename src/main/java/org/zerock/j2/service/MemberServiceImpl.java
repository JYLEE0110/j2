package org.zerock.j2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.j2.dto.MemberDTO;
import org.zerock.j2.entity.Member;
import org.zerock.j2.repository.MemberRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements  MemberService{

    private final MemberRepository memberRepository;

    // 로그인 ID / pW 틀릴시 예외처리 RuntimeException은
    public static final class MemberLoginException extends  RuntimeException{

        public MemberLoginException(String msg){
            super(msg);
        }

    }

    @Override
    public MemberDTO login(String email, String pw) {

        MemberDTO memberDTO = null;

        try {
            Optional<Member> result = memberRepository.findById(email);

            Member member = result.orElseThrow();

            // email 이존재해서 가져왔지만 입력한 비밀번호가 다를때
            // 리액트 에서 비밀번호 5번 제한 구현 본인이 구현
            if(member.getPw().equals(pw) == false){
                throw new MemberLoginException("Password Incorrect");
            }

            memberDTO = MemberDTO.builder()
                    .email(member.getEmail())
                    .pw("")
                    .nickname(member.getNickname())
                    .admin(member.isAdmin())
                    .build();

        }catch(Exception e){
            // email이 없을 시
            throw new MemberLoginException(e.getMessage());
        }

        return memberDTO;
    }

    @Override
    public MemberDTO getMemberWithEmail(String email) {

        Optional<Member> result = memberRepository.findById(email);

        // 소셜 로그인시 이미 DB에 데이터가 존재한다면
        if(result.isPresent()){
            Member member = result.get();

            MemberDTO dto = MemberDTO.builder()
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .admin(member.isAdmin())
                    .build();
            return dto;
        }
        // 데이터베이스에 존재하지 않는 이메일이라면
        Member socialMember = Member.builder()
                .email(email)
                .pw(UUID.randomUUID().toString())
                .nickname("SOCIAL_MEMBER")
                .build();
        memberRepository.save(socialMember);

        MemberDTO dto = MemberDTO.builder()
                .email(socialMember.getEmail())
                .nickname(socialMember.getNickname())
                .admin(socialMember.isAdmin())
                .build();

        return dto;
    }
}
