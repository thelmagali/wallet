package com.playtomic.tests.wallet.api.exception;

import com.playtomic.tests.wallet.service.exception.BalanceNotMatchingException;
import com.playtomic.tests.wallet.service.exception.WalletNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleAllExceptions(final Exception ex) {
        logger.error("ERROR: ", ex);
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleWalletNotFoundException(final WalletNotFoundException ex) {
        logger.error("Wallet not found: ", ex);
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(BalanceNotMatchingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleBalanceNotMatching(final BalanceNotMatchingException ex) {
        logger.error("Balance does not match: ", ex);
        return new ErrorResponse(ex.getMessage());
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return this.handleExceptionInternal(ex, fieldErrors, headers, status, request);
    }
}
