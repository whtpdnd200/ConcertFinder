package com.concertfinder.concertfinder.comment.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentModifyDTO {

    @NotBlank(message = "댓글은 비어 있을 수 없습니다!")
    private String comment;
}
