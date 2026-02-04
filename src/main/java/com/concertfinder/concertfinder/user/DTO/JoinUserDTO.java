package com.concertfinder.concertfinder.user.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinUserDTO {

    @NotBlank(message = "아이디는 비어 있을 수 없습니다!")
    private String userId;

    @NotBlank(message = "비밀번호는 비어 있을 수 없습니다!")
    private String password;

    @NotBlank(message = "닉네임은 비어 있을 수 없습니다!")
    @Size(max = 16, message = "닉네임은 16글자 이하로만 작성 가능 합니다!")
    private String nickname;

    @Email(message = "이메일 형식이 잘못 되었습니다!")
    private String email;

    @NotBlank(message = "선호 지역은 비어 있을 수 없습니다!")
    private String attentionAreaCode;
    private String role;
}
