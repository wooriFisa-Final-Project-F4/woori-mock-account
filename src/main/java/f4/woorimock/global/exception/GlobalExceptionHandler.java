package f4.woorimock.global.exception;

import f4.woorimock.global.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@EnableWebMvc
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Not Found Error
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ApiResponse<?> handleNoHandleFoundException(NoHandlerFoundException e) {
        return ApiResponse.error(
                ErrorDetails.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .code(404)
                        .message("잘못된 API URL 요청입니다.")
                        .build());
    }

    // Custom Error Exception
    protected ApiResponse<?> handleCustomErrorException(CustomException e) {
        return ApiResponse.error(
                ErrorDetails.builder()
                        .status(e.getCustomErrorCode().getStatus())
                        .code(e.getCustomErrorCode().getCode())
                        .message(e.getCustomErrorCode().getMessage())
        );
    }

    // Validator Error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                errors.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        }
        return ApiResponse.error(
                errors
        );
    }
}
