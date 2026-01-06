package com.concertfinder.concertfinder.common.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponseDTO<T> {

    private String result;

    private String message;

    private T data;

    public static <T>ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>("success", message, data);
    }
}
