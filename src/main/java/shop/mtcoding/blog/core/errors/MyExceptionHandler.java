package shop.mtcoding.blog.core.errors;

import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.mtcoding.blog.core.errors.exception.*;
import shop.mtcoding.blog.core.errors.exception.api.*;
import shop.mtcoding.blog.core.utils.ApiUtil;
import shop.mtcoding.blog.core.utils.Script;

// RuntimeException이 터지면 해당 파일로 오류가 모인다
@Slf4j
@RestControllerAdvice // 데이터 응답
public class MyExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(StudentCheckException.class)
    public @ResponseBody String stcheck(StudentCheckException e) {
        return Script.href("/student/check-form", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception400.class)
    public @ResponseBody String ex400(Exception400 e) {
        return Script.back(e.getMessage()); // http body, http header
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(Exception401.class)
    public @ResponseBody String ex401(Exception401 e) {
        System.out.println(e.getMessage());
        return Script.href("/login-form");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(Exception403.class)
    public @ResponseBody String ex403(Exception403 e) {
        return Script.back(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception404.class)
    public @ResponseBody String ex404(Exception404 e) {
        return Script.back(e.getMessage());
    }

    @ExceptionHandler(ApiException400.class)
    public ResponseEntity<?> badRequest(ApiException400 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(ApiException401.class)
    public ResponseEntity<?> unAuthorized(ApiException401 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(ApiException403.class)
    public ResponseEntity<?> forbidden(ApiException403 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(ApiException404.class)
    public ResponseEntity<?> notFound(ApiException404 e) {
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(ApiException500.class)
    public ResponseEntity<?> serverError(ApiException500 e) {
        log.error(e.getMessage());
        Sentry.captureException(e);
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception500.class)
    public @ResponseBody String ex500(Exception500 e) {
        Sentry.captureException(e);
        return Script.back(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknown(Exception e) {
        log.error(e.getMessage());
        Sentry.captureException(e);
        e.printStackTrace();
        return new ResponseEntity<>(new ApiUtil<>(500, "오류 : 관리자에게 문의하세요"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
