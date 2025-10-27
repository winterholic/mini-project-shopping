package com.miniproject.auth.common.exceiption.exceptions;

import com.miniproject.auth.common.exceiption.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidAuthException extends CustomException {
    private final String message;
    private static final HttpStatus HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public InvalidAuthException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HTTP_STATUS;
    }
}
