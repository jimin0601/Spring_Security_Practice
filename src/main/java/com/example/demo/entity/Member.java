package com.example.demo.entity;

import com.example.demo.dto.SignUpDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    private Long member_index;

    private String id;
    private String password;
    private String name;
    private String email;

    /**
     * 회원가입 DTO -> Entity 변환
     * @param signUpDTO
     * @return
     */
    public Member toEntity(SignUpDTO signUpDTO, String password) {
        this.id = signUpDTO.getId();
        this.password = password;
        this.name = signUpDTO.getName();
        this.email = signUpDTO.getEmail();

        return this;
    }
}
