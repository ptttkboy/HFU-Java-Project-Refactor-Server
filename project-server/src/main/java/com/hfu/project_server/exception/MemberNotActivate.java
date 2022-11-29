package com.hfu.project_server.exception;

public class MemberNotActivate extends RuntimeException{

    public MemberNotActivate() {
        super();
    }

    public MemberNotActivate(String message) {
        super(message);
    }

    public MemberNotActivate(String message, Throwable cause) {
        super(message, cause);
    }
}
