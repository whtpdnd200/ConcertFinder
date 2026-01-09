package com.concertfinder.concertfinder.user.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO {

    private Long id;
    private String userId;
    private String nickname;
    private String email;
    private String attentionAreaCode;
    private String attentionAreaName;

}
