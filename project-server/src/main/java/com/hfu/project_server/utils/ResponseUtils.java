package com.hfu.project_server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfu.project_server.exception.ApiExceptionFormat;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtils {

    public static void exceptionWriter(HttpServletResponse response, int status, String msg) throws IOException {

        ApiExceptionFormat apiExceptionFormat = new ApiExceptionFormat(
                System.currentTimeMillis(),
                status,
                msg
        );
        response.setStatus(status);
        new ObjectMapper().writeValue(response.getOutputStream(), apiExceptionFormat);
    };
}
