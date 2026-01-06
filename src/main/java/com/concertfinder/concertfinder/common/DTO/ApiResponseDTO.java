package com.concertfinder.concertfinder.common.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponseDTO<T> {

    private String result;

    private String message;

    private T data;

    // 게시글 댓글 목록 등 데이터가 존재하는 API 리턴시 사용
    public static <T>ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>("success", message, data);
    }

    // 글 수정 삭제 등 데이터가 존재하지 않는 API 리턴시 사용
    public static <T>ApiResponseDTO<T> success(String message) {
        return new ApiResponseDTO<>("success", message,null);
    }

    // 에러가 발생 했을때 사용
    public static <T>ApiResponseDTO<T> fail(String message) {
        return new ApiResponseDTO<>("fail", message, null);
    }
}
