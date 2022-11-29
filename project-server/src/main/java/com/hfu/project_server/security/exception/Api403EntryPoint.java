package com.hfu.project_server.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfu.project_server.exception.ApiExceptionFormat;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 自定義403的Response給API響應
 *
 * 當客戶端通過RESTful API進行驗證時，如尚未登入則發送自定義的錯誤訊息給使用端。
 */
public class Api403EntryPoint extends Http403ForbiddenEntryPoint {

    public Api403EntryPoint() {
        super();
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {

        response.setStatus(FORBIDDEN.value());
        response.setContentType(APPLICATION_JSON_VALUE);

        ApiExceptionFormat apiExceptionFormat = new ApiExceptionFormat(System.currentTimeMillis(), FORBIDDEN.value(), e.getMessage());
        new ObjectMapper().writeValue(response.getOutputStream(), apiExceptionFormat);
    }
}
