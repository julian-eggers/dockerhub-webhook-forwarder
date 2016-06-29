package com.itelg.docker.dwf.rest.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "invalid token")
public class AccessDeniedException extends RuntimeException
{
    private static final long serialVersionUID = -784991291496352686L;
}