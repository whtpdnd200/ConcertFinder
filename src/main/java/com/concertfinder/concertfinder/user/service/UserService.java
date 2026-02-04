package com.concertfinder.concertfinder.user.service;

import com.concertfinder.concertfinder.exception.custom_exception.DuplicateException;
import com.concertfinder.concertfinder.exception.custom_exception.UnAuthorizedException;
import com.concertfinder.concertfinder.jwt.CookieUtil;
import com.concertfinder.concertfinder.jwt.JwtProvider;
import com.concertfinder.concertfinder.sidoCode.service.SidoCodeService;
import com.concertfinder.concertfinder.user.DTO.JoinUserDTO;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.ModifyUserDTO;
import com.concertfinder.concertfinder.user.domain.User;
import com.concertfinder.concertfinder.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SidoCodeService sidoCodeService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String NICKNAME_KEY_PREFIX = "user:nickname:";
    private static final String IS_DELETE_PREFIX = "user:isDelete:";
    private final String USER_DTO_PREFIX = "user:info:";

    // LoginUserDTO에 User 정보 담아주는 함수
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

    // 회원가입 : 아이디 중복검사 메서드
    public boolean isDuplicate(String userId) {

        boolean isDuplicate = userRepository.existsByUserId(userId);
        if(isDuplicate) {
            throw new DuplicateException("중복된 아이디 입니다");
        }
        return isDuplicate;
    }

    // 회원가입 : 유저 정보 DB 저장 메서드
    public void insertUser(JoinUserDTO joinUserDTO) {

        User user = User.builder()
                .userId(joinUserDTO.getUserId())
                .password(bCryptPasswordEncoder.encode(joinUserDTO.getPassword()))
                .nickname(joinUserDTO.getNickname())
                .email(joinUserDTO.getEmail())
                .attentionAreaCode(joinUserDTO.getAttentionAreaCode())
                .role("ROLE_USER")
                .isDelete(false)
                .build();

        try {
            userRepository.save(user);
        } catch(DataAccessException e) {
            throw new RuntimeException("서버 에러로 인해 회원가입이 실패 하였습니다 잠시 뒤 다시 시도해주세요!");
        }
    }

    // 로그인 : 로그인 시도 유저 정보 조회 메서드
    @Transactional
    public void loginUser(String userId, String password, HttpServletResponse response) {

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UnAuthorizedException("아이디 혹은 비밀번호가 일치하지 않습니다!"));

        if(user.isDelete()) {

            throw new DisabledException("탈퇴 처리된 회원 입니다!");
        }

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {

            throw new UnAuthorizedException("아이디 혹은 비밀번호가 일치하지 않습니다!");
        }

        String accessToken = jwtProvider.createAccessToken(userId, user.getRole());

        String refreshToken = jwtProvider.createRefreshToken(userId);

        String csrfToken = UUID.randomUUID().toString();

        String key = "refreshToken:" + user.getUserId();

        redisTemplate.opsForValue().set(key, refreshToken, java.time.Duration.ofSeconds(2592000));

        CookieUtil.addSecureCookie(response, "ACCESS_TOKEN", accessToken, 1800);

        CookieUtil.addSecureCookie(response, "REFRESH_TOKEN", refreshToken, 2592000);

        CookieUtil.addCsrfCookie(response, "XSRF-TOKEN", csrfToken, 1800);

    }

//    // 로그인 : id를 통해 salt를 얻어오는 메서드
//    public String getSalt(String userId) {
//
//         Optional<User> optionalUser = userRepository.findByUserId(userId);
//
//        if(optionalUser.isPresent()) {
//            User user = optionalUser.get();
//
//            return user.getSalt();
//        }
//
//        return null;
//    }

    // 회원 정보 수정 메서드
    @Transactional
    public void userModify(Long id, ModifyUserDTO modifyUserDTO) {


        Optional<User> optionalUser = userRepository.findById(id);

        String password = null;


        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            password = user.getPassword();

            if(modifyUserDTO.getPassword() != null && !modifyUserDTO.getPassword().equals("")) {

                password = bCryptPasswordEncoder.encode(modifyUserDTO.getPassword());

            }
            user = user.toBuilder()
                    .nickname(modifyUserDTO.getNickname())
                    .email(modifyUserDTO.getEmail())
                    .attentionAreaCode(modifyUserDTO.getAttentionAreaCode())
                    .password(password)
                    .role("ROLE_USER")
                    .build();

            try {
                userRepository.save(user);
                redisTemplate.delete(NICKNAME_KEY_PREFIX + id);
                redisTemplate.delete(USER_DTO_PREFIX + user.getUserId());

            } catch(DataAccessException e) {
                throw new RuntimeException("서버에러로 회원 정보 수정이 실패 했습니다 잠시후 다시 시도해주세요!");
            }
        }
    }

    // 회원 정보를 얻어오는 메서드
    public LoginUserDTO getUser(long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()) {

            return addDTO(optionalUser);
        }
        return null;
    }

    public boolean getIsDelete(long id) {

        String key = IS_DELETE_PREFIX + id;

        Boolean isDelete = (Boolean)redisTemplate.opsForValue().get(key);

        if(isDelete != null) {

            log.info("redis Cache Hit : isDelete : {} ", isDelete);
            return isDelete;
        }

        log.info("redis Cache Miss : userId : {} ", id);

        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()) {

            throw new NoSuchElementException("유저 정보를 찾을 수 없습니다!");
        }
        User user = optionalUser.get();

        isDelete = user.isDelete();

        redisTemplate.opsForValue().set(key, isDelete, java.time.Duration.ofMinutes(30));

        return user.isDelete();
    }

    // 유저 이름 얻어오는 메서드
    public String getNickname(long id) {

        String key = NICKNAME_KEY_PREFIX + id;

        // redis에서 조회
        String nickname = (String)redisTemplate.opsForValue().get(key);

        if(nickname != null) {

            log.info("redis Cache Hit : userId : {}, nickname : {}", id, nickname);
            return nickname;
        }

        log.info("Redis Cache Miss: userId : {}", id);

        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent() || optionalUser.get().isDelete()) {

            return "탈퇴한 유저";
        }

        User user = optionalUser.get();
        nickname = user.isDelete() ? "탈퇴한 유저" : user.getNickname();

        redisTemplate.opsForValue().set(key, nickname, java.time.Duration.ofMinutes(30));

        return nickname;
    }

    public boolean isExistsUser(long id) {

        return userRepository.existsById(id);
    }

    // 회원 탈퇴 처리 메서드
    @Transactional
    public void updateDeleteUser(long userId) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if(!optionalUser.isPresent()) {

            throw new NoSuchElementException("사용자 정보를 찾을 수 없습니다!");
        }

        User user = optionalUser.get();

        user = user.toBuilder()
                .isDelete(true)
                .build();

        try {
            userRepository.save(user);
            redisTemplate.delete(NICKNAME_KEY_PREFIX + userId);
            redisTemplate.delete(IS_DELETE_PREFIX + userId);
        } catch(DataAccessException e) {

            throw new RuntimeException("서버 에러로 인해 탈퇴를 진행 하지 못했습니다 잠시 후 다시 시도해주세요!");
        }
    }


    public List<Long> getDeleteUserIdList() {

        return userRepository.findAllByIsDelete(true).stream().map(User::getId).toList();
    }

    public void deleteUsers(List<Long> userIdList) {

        userRepository.deleteAllByIdIn(userIdList);
    }

    // 회원 탈퇴 메서드
    public void deleteUser(long userId) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if(!optionalUser.isPresent()) {

            log.warn("사용자 정보가 없음!");
        }

        try {

            User user = optionalUser.get();
            userRepository.delete(user);
        } catch(DataAccessException e) {

            log.warn("탈퇴 처리 에러!");
        }
    }
}
