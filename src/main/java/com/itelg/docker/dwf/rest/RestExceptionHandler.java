package com.itelg.docker.dwf.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.itelg.docker.dwf.rest.domain.AccessDeniedException;

@ControllerAdvice
public class RestExceptionHandler
{
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "invalid token")
    public String accessDeniedHandler()
    {
        return "invalid token";
    }
}