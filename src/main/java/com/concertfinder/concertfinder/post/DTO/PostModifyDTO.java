package com.concertfinder.concertfinder.post.DTO;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostModifyDTO {

    private Character category;

    private String title;

    private String content;
}
