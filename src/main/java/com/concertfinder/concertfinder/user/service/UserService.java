package com.concertfinder.concertfinder.user.service;

import com.concertfinder.concertfinder.SidoCode.service.SidoCodeService;
import com.concertfinder.concertfinder.common.SHA256HashingEncoder;
import com.concertfinder.concertfinder.user.DTO.JoinUserDTO;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.domain.User;
import com.concertfinder.concertfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SidoCodeService sidoCodeService;

    // 회원가입 : 아이디 중복검사 메서드
    public boolean isDuplicate(String userId) {

        return userRepository.existsByUserId(userId);
    }

    // 회원가입 : 유저 정보 DB 저장 메서드
    public boolean insertUser(JoinUserDTO joinUserDTO) {

        String salt = SHA256HashingEncoder.getSalt();
        User user = User.builder()
                .userId(joinUserDTO.getUserId())
                .password(SHA256HashingEncoder.encode(joinUserDTO.getPassword(), salt))
                .salt(salt)
                .nickname(joinUserDTO.getNickname())
                .email(joinUserDTO.getEmail())
                .attentionAreaCode(joinUserDTO.getAttentionAreaCode())
                .build();

        try {
            userRepository.save(user);
        } catch(DataAccessException e) {
            return false;
        }
        return true;
    }

    // 로그인 : 로그인 시도 유저 정보 조회 메서드
    public LoginUserDTO loginUser(String userId, String password) {

        String salt = getSalt(userId);

        if(salt != null) {

            Optional<User> optionalUser = userRepository.findByUserIdAndPassword(userId, SHA256HashingEncoder.encode(password, salt));
            if(optionalUser.isPresent()) {
                User user = optionalUser.get();

                LoginUserDTO loginUserDTO = LoginUserDTO.builder()
                        .id(user.getId())
                        .userId(user.getUserId())
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .attentionAreaCode(user.getAttentionAreaCode())
                        .attentionAreaName(sidoCodeService.getSidoName(user.getAttentionAreaCode()))
                        .build();

                return loginUserDTO;
            }
        }

        return null;
    }

    // 로그인 : id를 통해 salt를 얻어오는 메서드
    public String getSalt(String userId) {

        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            return user.getSalt();
        }

        return null;
    }
}
