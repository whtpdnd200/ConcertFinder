package com.concertfinder.concertfinder.exception;

import com.concertfinder.concertfinder.common.DTO.ApiResponseDTO;
import com.concertfinder.concertfinder.exception.custom_exception.DuplicateException;
import com.concertfinder.concertfinder.exception.custom_exception.NotFoundException;
import com.concertfinder.concertfinder.exception.custom_exception.UnAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 중복되는 로그인 세션 관련 핸들러 처리 메서드
    public static void loginException(Long userId) {
        if(userId == null) {
            throw new IllegalStateException("로그인 후 이용 가능한 서비스 입니다!");
        }
    }

    // 탈퇴 처리회원 관련 핸들러 처리 메서드
    // 403으로 반환
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponseDTO<Void>> DisabledException(DisabledException e) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseDTO.fail(e.getMessage()));
    }


    // 파라미터 값이 비어있거나 유효하지 않은 형식
    // http 400으로 반환
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<Void>> IllegalArgumentException(IllegalArgumentException e) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.fail(e.getMessage()));
    }

    // 변수 하나에 파라미터 전송시 예외 핸들러
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponseDTO<Void>> HandlerMethodValidationException(HandlerMethodValidationException e) {

        String message = e.getValueResults().get(0).getResolvableErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.fail(message));
    }


    // 객체에 파라미터 전송시 예외 핸들러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Void>> MethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDTO.fail(message));
    }

    // 다른 사람의 글 댓글을 수정 삭제 하려 할 때
    // http 403으로 반환
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ApiResponseDTO<Void>> UnAuthorizedException(UnAuthorizedException e) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseDTO.fail(e.getMessage()));
    }

    // 로그인하지 않은 사용자가 로그인이 필요한 기능을 이용 할 때
    // 401로 반환
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponseDTO<Void>> IllegalStateException(IllegalStateException e) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponseDTO.fail(e.getMessage()));
    }

    // 아이디 중복검사시 중복된 아이디 일때
    // 409
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ApiResponseDTO<Void>> DuplicateException(DuplicateException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseDTO.fail(e.getMessage()));
    }

    // 서버 내부 에러
    // 500으로 반환
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDTO<Void>> RuntimeException(RuntimeException e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDTO.fail(e.getMessage()));
    }

    // 유저의 아이디가 조회 되지 않을때
    // 404로 반환
    // 스프링 시큐리티 예외
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> UsernameNotFoundException(UsernameNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDTO.fail(e.getMessage()));
    }

    // 데이터가 조회 되지 않을때
    // 404로 반환
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponseDTO<Void>> NoSuchElementException(NoSuchElementException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDTO.fail(e.getMessage()));
    }

    // 삭제된 데이터에 접근 할 때
    // 404로 반환
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> NotFoundException(NotFoundException e) {

        String script = "<script>" +
                        "   alert('" + e.getMessage() + "');" +
                        "   location.href = '/concert/list';" +
                        "</script>";

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "text/html; charset=utf-8")
                .body(script);
    }
}
