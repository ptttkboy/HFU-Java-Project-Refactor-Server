package com.hfu.project_server.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

/**
 *  攔截回傳給RESTful API客戶端的異常並改寫成自定義的格式。
 */
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {EmailAlreadyUsedException.class})
    public ResponseEntity<ApiExceptionFormat> emailAlreadyUsedExceptionResponseEntity(EmailAlreadyUsedException e) {

        ApiExceptionFormat apiExceptionFormat = new ApiExceptionFormat(
                System.currentTimeMillis(),
                CONFLICT.value(),
                e.getMessage()
        );

        return ResponseEntity.status(CONFLICT).body(apiExceptionFormat);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ApiExceptionFormat> accessDeniedExceptionResponseEntity(AccessDeniedException e) {

        ApiExceptionFormat apiExceptionFormat = new ApiExceptionFormat(
                System.currentTimeMillis(),
                FORBIDDEN.value(),
                e.getMessage()
        );

        return ResponseEntity.status(FORBIDDEN).body(apiExceptionFormat);
    }

//    @ExceptionHandler(value = {MemberNotActivate.class})
//    public ResponseEntity<ApiExceptionFormat> memberIsNotActivate(MemberNotActivate e) {
//
//        ApiExceptionFormat apiExceptionFormat = new ApiExceptionFormat(
//                System.currentTimeMillis(),
//                UNAUTHORIZED.value(),
//                e.getMessage()
//        );
//
//        return ResponseEntity.status(UNAUTHORIZED).body(apiExceptionFormat);
//    }
}
