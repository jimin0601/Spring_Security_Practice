package com.example.demo.controller;

import com.example.demo.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/api/member")
@RestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final CustomUserDetailsService customUserDetailsService;
}
