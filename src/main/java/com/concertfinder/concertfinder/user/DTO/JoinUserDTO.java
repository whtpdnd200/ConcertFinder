package com.concertfinder.concertfinder.user.DTO;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinUserDTO {

    private String userId;
    private String password;
    private String nickname;
    private String email;
    private String attentionAreaCode;
    private String role;
}
