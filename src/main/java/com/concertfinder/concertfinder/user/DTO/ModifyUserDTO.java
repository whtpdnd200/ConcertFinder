package com.concertfinder.concertfinder.user.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ModifyUserDTO {

    private long id;
    private String nickname;
    private String password;
    private String email;
    private byte attentionAreaCode;

}
