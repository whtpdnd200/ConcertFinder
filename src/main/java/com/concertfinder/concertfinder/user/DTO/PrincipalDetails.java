package com.concertfinder.concertfinder.user.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class PrincipalDetails implements UserDetails {

    private final LoginUserDTO loginUserDTO;
    private final String password;


    public PrincipalDetails(LoginUserDTO loginUserDTO, String password) {
        this.loginUserDTO = loginUserDTO;
        this.password = password;
    }


    public PrincipalDetails(LoginUserDTO loginUserDTO) {
        this.loginUserDTO = loginUserDTO;
        this.password = null;
    }

    // 유저의 권한 정보를 담는 리스트를 리턴
    @Override
    public Collection <? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(loginUserDTO.getRole()));
    }


    @Override
    public String getPassword() {

        return this.password;
    }

    @Override
    public String getUsername() {

        return loginUserDTO.getUserId();
    }

    // 계정이 만료 되었는지 확인하는 메서드
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    // 계정이 잠겼는지 확인하는 메서드
    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    // 비밀번호가 만료 되었는지 확인하는 메서드
    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    // 계정이 활성화 상태인지 확인하는 메서드
    @Override
    public boolean isEnabled() {

        return !loginUserDTO.isDelete();
    }
}
