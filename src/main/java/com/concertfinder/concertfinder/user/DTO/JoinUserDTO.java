package com.concertfinder.concertfinder.user.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinUserDTO {

    private String userId;
    private String password;
    private String nickname;
    private String email;
    private byte attentionAreaCode;
}
