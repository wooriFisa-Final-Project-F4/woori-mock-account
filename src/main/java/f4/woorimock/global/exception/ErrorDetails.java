package f4.woorimock.global.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ErrorDetails {

    private HttpStatus status;
    private int code;
    private String message;
}
