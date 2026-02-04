package com.concertfinder.concertfinder.user.service;

import com.concertfinder.concertfinder.sidoCode.service.SidoCodeService;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import com.concertfinder.concertfinder.user.domain.User;
import com.concertfinder.concertfinder.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SidoCodeService sidoCodeService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final String USER_DTO_PREFIX = "user:info:";

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        String key = USER_DTO_PREFIX + userId;

        Object userInfo = redisTemplate.opsForValue().get(key);

        if(userInfo != null) {

            log.info("Redis Cache Hit User Info");

            LoginUserDTO loginUserDTO = objectMapper.convertValue(userInfo, LoginUserDTO.class);

            return new PrincipalDetails(loginUserDTO);
        }

        log.info("Redis Cache Miss User Info");

        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if(!optionalUser.isPresent()) {

            throw new UsernameNotFoundException("아이디가 존재하지 않습니다!");
        }

        LoginUserDTO userDTO = addDTO(optionalUser);

        redisTemplate.opsForValue().set(key, userDTO, java.time.Duration.ofMinutes(30));

        return new PrincipalDetails(userDTO);
    }

    public LoginUserDTO addDTO(Optional<User> oUser) {
        User user = oUser.get();

        LoginUserDTO loginUserDTO = LoginUserDTO.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .attentionAreaCode(user.getAttentionAreaCode())
                .attentionAreaName(sidoCodeService.getSidoName(user.getAttentionAreaCode()))
                .role(user.getRole())
                .isDelete(user.isDelete())
                .build();

        return loginUserDTO;
    }
}
