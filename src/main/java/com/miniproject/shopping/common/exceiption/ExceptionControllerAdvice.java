package com.miniproject.auth.common.exceiption;

import com.miniproject.auth.auth.dto.DefaultRes;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseEntity<DefaultRes> handleInternalException(HttpServletRequest request, Exception e) {
        log.error("exception: {}", e.getMessage());
        DefaultRes response = new DefaultRes(
                false,
                e.getMessage(),
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseEntity<DefaultRes> handleNullPointerException(HttpServletRequest request, NullPointerException e) {
        log.error("exception: {}", e.getMessage());
        DefaultRes response = new DefaultRes(
                false,
                e.getMessage(),
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseEntity<DefaultRes> handleIllegalStateException(HttpServletRequest request, IllegalStateException e) {
        log.error("exception: {}", e.getMessage());
        DefaultRes response = new DefaultRes(
                false,
                e.getMessage(),
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseEntity<DefaultRes> handleCustomException(HttpServletRequest request, CustomException e) {
        log.error("exception: {}", e.getMessage());
        DefaultRes response = new DefaultRes(
                false,
                e.getMessage(),
                String.valueOf(e.getHttpStatus().value())
        );
        return ResponseEntity.status(e.getHttpStatus().value()).body(response);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseEntity<DefaultRes> handleNoResourceFoundException(HttpServletRequest request, NoResourceFoundException e) {
        log.error("exception: {}", e.getMessage());
        DefaultRes response = new DefaultRes(
                false,
                e.getMessage(),
                String.valueOf(HttpStatus.NOT_FOUND)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseEntity<DefaultRes> handleDataAccessException(HttpServletRequest request, DataAccessException e) {
        log.error("exception: {}", e.getMessage());
        DefaultRes response = new DefaultRes(
                false,
                "DB 관련 오류가 발생하였습니다.",
                String.valueOf(HttpStatus.BAD_REQUEST)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
