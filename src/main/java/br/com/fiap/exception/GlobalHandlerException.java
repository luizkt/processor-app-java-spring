package br.com.fiap.exception;

import br.com.fiap.entity.ApplicationResponseBody;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Log4j2
public class GlobalHandlerException {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ApplicationResponseBody handleBusinessException(BusinessException exception, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        response.setStatus(exception.getHttpStatus().value());

        log.error(exception.getMessage());
        log.debug("Full stack trace:", exception);

        return new ApplicationResponseBody("An error has occurred", exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApplicationResponseBody handleGenericException(Exception exception, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        log.error(exception.getMessage());
        log.debug("Full stack trace:", exception);

        return new ApplicationResponseBody("An error has occurred", exception.getMessage());
    }
}
