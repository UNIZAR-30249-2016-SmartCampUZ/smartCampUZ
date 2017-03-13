package es.unizar.smartcampuz.application.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * Catches IOExceptions thrown from any Controller.
     * @param e
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public String HandleIOException(Exception e) {
        logException(e);
        return e.getLocalizedMessage();
    }

    /**
     * General log method shared by all ExceptionHandlers
     * @param e
     */
    private void logException(Exception e){
        //Not declarable as static because of Spring restictions with @ControllerAdvice
        Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
        log.error("IOException: " + e.getLocalizedMessage());
    }
}
