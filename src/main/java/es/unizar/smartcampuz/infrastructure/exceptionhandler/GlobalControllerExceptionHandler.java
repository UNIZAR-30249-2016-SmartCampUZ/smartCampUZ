package es.unizar.smartcampuz.infrastructure.exceptionhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.text.ParseException;

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
        return e.getMessage()   ;
    }

    /**
     * Catches ParseException thrown from any Controller.
     * @param e
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParseException.class)
    @ResponseBody
    public String HandleParseException(Exception e) {
        logException(e);
        return e.getMessage();
    }

    /**
     * Catches IllegalArgumentException thrown from any Controller.
     * @param e
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public String HandleIllegalArgumentException(Exception e) {
        logException(e);
        return e.getMessage();
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
