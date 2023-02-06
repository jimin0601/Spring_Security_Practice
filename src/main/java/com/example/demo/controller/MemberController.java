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
        System.out.println(signUpDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
