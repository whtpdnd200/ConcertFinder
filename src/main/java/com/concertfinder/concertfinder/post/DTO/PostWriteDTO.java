package com.concertfinder.concertfinder.post.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class PostWriteDTO {

    private Character category;

    private String title;

    private String content;
}
