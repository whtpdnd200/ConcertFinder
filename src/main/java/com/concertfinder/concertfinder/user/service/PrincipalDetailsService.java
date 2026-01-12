package com.concertfinder.concertfinder.user.service;

import com.concertfinder.concertfinder.user.DTO.PrincipalDetails;
import com.concertfinder.concertfinder.user.domain.User;
import com.concertfinder.concertfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if(!optionalUser.isPresent()) {

            throw new UsernameNotFoundException("아이디가 존재하지 않습니다!");
        }

        return new PrincipalDetails(userService.addDTO(optionalUser), optionalUser.get().getPassword());
    }
}
