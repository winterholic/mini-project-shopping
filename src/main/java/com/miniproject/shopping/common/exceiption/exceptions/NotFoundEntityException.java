package com.miniproject.auth.common.exceiption.exceptions;

import com.miniproject.auth.common.exceiption.CustomException;
import org.springframework.http.HttpStatus;

public class NotFoundEntityException extends CustomException {
    private static final String MESSAGE = "해당 정보를 찾을 수 없습니다.";
    private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

    @Override
    public String getMessage() {
        return MESSAGE;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HTTP_STATUS;
    }
}
