package com.concertfinder.concertfinder.post.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostModifyDTO {

    @NotNull(message = "카테고리는 비어 있을 수 없습니다!")
    private Character category;

    @NotBlank(message = "제목은 비어 있을 수 없습니다!")
    private String title;

    @NotBlank(message = "내용은 비어 있을 수 없습니다!")
    private String content;
}
