package com.concertfinder.concertfinder.user.DTO;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO implements Serializable {

    private Long id;
    private String userId;
    private String nickname;
    private String email;
    private String attentionAreaCode;
    private String attentionAreaName;
    private String role;
    private boolean isDelete;
}
