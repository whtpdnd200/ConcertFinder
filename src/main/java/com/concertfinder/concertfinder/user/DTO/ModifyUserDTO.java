package com.concertfinder.concertfinder.user.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDTO {


    @NotBlank(message = "닉네임은 비어 있을 수 없습니다!")
    @Size(max = 16, message = "닉네임은 16글자 이하로만 작성 가능 합니다!")
    private String nickname;

    private String password;

    @Email(message = "이메일 형식이 잘못 되었습니다!")
    private String email;

    @NotBlank(message = "선호 지역은 비어 있을 수 없습니다!")
    private String attentionAreaCode;

}
