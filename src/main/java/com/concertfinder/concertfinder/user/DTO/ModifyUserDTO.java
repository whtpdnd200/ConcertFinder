package com.concertfinder.concertfinder.user.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyUserDTO {

    private long id;
    private String nickname;
    private String password;
    private String email;
    private String attentionAreaCode;

}
