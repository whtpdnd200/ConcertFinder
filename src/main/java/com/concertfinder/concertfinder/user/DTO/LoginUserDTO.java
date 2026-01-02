package com.concertfinder.concertfinder.user.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginUserDTO {

    private long id;
    private String userId;
    private String nickname;
    private String email;
    private String attentionAreaCode;
    private String attentionAreaName;

}
