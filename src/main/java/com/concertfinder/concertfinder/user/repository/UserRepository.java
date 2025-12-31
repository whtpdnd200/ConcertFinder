package com.concertfinder.concertfinder.user.repository;

import com.concertfinder.concertfinder.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 중복된 아이디가 있는지 확인하는 메서드
    public boolean existsByUserId(String userId);

    // 로그인 메서드 : 아이디로 salt값 확인
    public Optional<User> findByUserId(String userId);

    // 로그인 메서드 : 아이디와 비밀번호로 회원 조회
    public Optional<User> findByUserIdAndPassword(String userId, String password);

}
