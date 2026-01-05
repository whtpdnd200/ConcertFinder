package com.concertfinder.concertfinder.post.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostModifyDTO {

    private Character category;

    private String title;

    private String content;
}
