package f4.woorimock.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {

    // Bad Request 400

    // Unathorized 401

    // Forbidden 402

    // Not Found 404

    // Server Error 500
    ;
    private final HttpStatus status;
    private final int code;
    private final String message;
}
