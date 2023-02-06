package com.example.demo.controller;

import com.example.demo.dto.SignUpDTO;
import com.example.demo.entity.Member;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/api/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;

    /**
     * 회원가입 컨트롤러
     */
    @PostMapping("/sign")
    public ResponseEntity<Member> memberSign(@RequestBody SignUpDTO signUpDTO) {

        memberService.signMember(signUpDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 로그인 컨트롤러
     */
    @PostMapping("/login")
    public ResponseEntity<Member> memberLogin(@RequestBody SignUpDTO signUpDTO) {

        boolean login = memberService.loginMember(signUpDTO);

        if(login) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
