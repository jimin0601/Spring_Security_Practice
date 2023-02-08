package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    // @EntityGraph : 쿼리가 수행될 때 Lazy가 아닌 Eager 조회로 authorities 정보를 같이 가져오게 된다.
    @EntityGraph
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
