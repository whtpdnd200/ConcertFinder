package com.concertfinder.concertfinder.post.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostWriteDTO {

    @NotNull(message = "카테고리는 비어 있을 수 없습니다!")
    private Character category;

    @NotBlank(message = "제목은 비어 있을 수 없습니다!")
    private String title;

    @NotBlank(message = "내용은 비어 있을 수 없습니다!")
    private String content;

    // 동행 모집 정보
    private Byte headCount;

    private String place;

    @DateTimeFormat(pattern = "yyyy년 MM월 dd일 HH시 mm분")
    private LocalDateTime sDateTime;
}
