package com.hfu.project_server.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtInvalidException extends AuthenticationException {

    public JwtInvalidException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
