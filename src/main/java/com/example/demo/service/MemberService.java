package com.example.demo.service;

import com.example.demo.dto.SignUpDTO;
import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    /**
     * 이메일 중복 확인
     */
    @Transactional
    public void duplicateEmail(String email) {

        Optional<Member> findEmail = memberRepository.findByEmail(email);

        if (findEmail.isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
    }

    /**
     * 아이디 중복 확인
     */
    @Transactional
    public void duplicateId(String id) {
        
        Optional<Member> findId = memberRepository.findById(id);
        
        if(findId.isPresent()) {
            throw new IllegalStateException("이미 가입된 아이디입니다.");
        }
    }

    /**
     * 회원가입
     */
    @Transactional
    public void signMember(SignUpDTO signUpDTO) {

        // 회원가입 전 아이디 중복 체크
        duplicateId(signUpDTO.getId());
        
        // 회원가입 전 이메일 중복 체크
        duplicateEmail(signUpDTO.getEmail());

        // 비밀번호 암호화
        String password = passwordEncoder.encode(signUpDTO.getPassword());

        Member signUpMember = new Member().toEntity(signUpDTO, password);

        memberRepository.save(signUpMember);
    }

    /**
     * 로그인
     */
    public boolean loginMember(SignUpDTO signUpDTO) {

        // 사용자가 입력한 아이디의 정보를 가져옴
        Optional<Member> findMember = memberRepository.findById(signUpDTO.getId());

        // 사용자의 입력한 정보의 비밀번호와, DB에 암호화된 패스워드랑 같은지 확인
        boolean passwordTrue = passwordEncoder.matches(signUpDTO.getPassword(), findMember.get().getPassword());

        // 틀리면 로그인 실패
        if (!passwordTrue) return false;

        // 맞으면 로그인 성공
        else return true;
    }
}
