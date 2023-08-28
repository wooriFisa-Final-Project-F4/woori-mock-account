package f4.woorimock.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {

    // Bad Request 400
    ALREADY_HAS_ACCOUNT(HttpStatus.CONFLICT, 400, "이미 계좌를 소지하고 있습니다."),
    PRODUCT_NOT_EXIST(HttpStatus.BAD_REQUEST, 400, "해당 상품은 존재하지 않습니다"),
    // Unathorized 401

    // Forbidden 402

    // Not Found 404

    // Server Error 500
    ;
    private final HttpStatus status;
    private final int code;
    private final String message;
}
