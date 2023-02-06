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

    private final MemberRepository memberRepository;

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
}
