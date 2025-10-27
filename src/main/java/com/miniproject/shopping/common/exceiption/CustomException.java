package com.miniproject.auth.common.exceiption;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException {
    public abstract String getMessage();
    public abstract HttpStatus getHttpStatus();
}
