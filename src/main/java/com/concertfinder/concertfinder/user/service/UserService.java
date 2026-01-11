package com.concertfinder.concertfinder.user.service;

import com.concertfinder.concertfinder.sidoCode.service.SidoCodeService;
import com.concertfinder.concertfinder.concert.DTO.ConcertFavoritesListDTO;
import com.concertfinder.concertfinder.concert.service.ConcertService;
import com.concertfinder.concertfinder.user.DTO.JoinUserDTO;
import com.concertfinder.concertfinder.user.DTO.LoginUserDTO;
import com.concertfinder.concertfinder.user.DTO.ModifyUserDTO;
import com.concertfinder.concertfinder.user.domain.User;
import com.concertfinder.concertfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SidoCodeService sidoCodeService;
    private final ConcertService concertService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
                .build();

        return loginUserDTO;
    }

    // 회원가입 : 아이디 중복검사 메서드
    public boolean isDuplicate(String userId) {

        return userRepository.existsByUserId(userId);
    }

    // 회원가입 : 유저 정보 DB 저장 메서드
    public void insertUser(JoinUserDTO joinUserDTO) {

        if(joinUserDTO.getNickname().length() > 16) {
            throw new IllegalArgumentException("닉네임은 16글자 이하로 작성해야 합니다!");
        }

        User user = User.builder()
                .userId(joinUserDTO.getUserId())
                .password(bCryptPasswordEncoder.encode(joinUserDTO.getPassword()))
                .nickname(joinUserDTO.getNickname())
                .email(joinUserDTO.getEmail())
                .attentionAreaCode(joinUserDTO.getAttentionAreaCode())
                .role("ROLE_USER")
                .build();

        try {
            userRepository.save(user);
        } catch(DataAccessException e) {
            throw new RuntimeException("서버 에러로 인해 회원가입이 실패 하였습니다 잠시 뒤 다시 시도해주세요!");
        }
    }

    // 로그인 : 로그인 시도 유저 정보 조회 메서드
//    public LoginUserDTO loginUser(String userId, String password) {
//
//        String salt = getSalt(userId);
//
//        if(salt == null) {
//            throw new NoSuchElementException("일치하는 아이디가 존재하지 않습니다!");
//        }
//
//        Optional<User> optionalUser = userRepository.findByUserIdAndPassword(userId, SHA256HashingEncoder.encode(password, salt));
//        if(!optionalUser.isPresent()) {
//
//            throw new NoSuchElementException("비밀번호가 일치하지 않습니다!");
//        }
//
//        return addDTO(optionalUser);
//    }

    // 로그인 : id를 통해 salt를 얻어오는 메서드
    public String getSalt(String userId) {

        // Optional<User> optionalUser = userRepository.findByUserId(userId);

//        if(optionalUser.isPresent()) {
//            User user = optionalUser.get();
//
//            return user.getSalt();
//        }

        return null;
    }

    // 회원 정보 수정 메서드
    public void userModify(long id, ModifyUserDTO modifyUserDTO) {

        if(modifyUserDTO.getNickname().length() > 16) {
            throw new IllegalArgumentException("닉네임은 16글자 이하로 작성해야 합니다!");
        }

        Optional<User> optionalUser = userRepository.findById(id);

        String password = null;


        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            password = user.getPassword();
            // salt = user.getSalt();

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

    // 유저 이름 얻어오는 메서드
    public String getNickname(long id) {

        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {

            return optionalUser.get().getNickname();
        }

        return null;
    }

    public List<ConcertFavoritesListDTO> getLists(long userId) {

        return concertService.getConcertList(userId);
    }

    public List<ConcertFavoritesListDTO> getConcertListTop3(long userId) {

        return concertService.getConcertListTop3(userId);
    }
}
