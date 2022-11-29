package com.hfu.project_server.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 自定義的發送給API的異常
 */
@Data
@AllArgsConstructor
public class ApiExceptionFormat {

    private final Long timestamp;
    private final int status;
    private final String message;
}
