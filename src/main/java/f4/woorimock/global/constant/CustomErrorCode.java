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
    INVALID_ACCOUNT_NUMBER(HttpStatus.BAD_REQUEST, 400, "입력하신 계좌번호는 존재하지 않는 계좌번호 입니다."),
    INCORRECT_PASSWORD_INPUT(HttpStatus.BAD_REQUEST, 400, "비밀번호가 유효하지 않습니다."),
    LACk_USABLE_BALANCE(HttpStatus.BAD_REQUEST, 400, "입찰 가용 금액이 부족합니다."),
    // Unathorized 401

    // Forbidden 402
    NOT_MATCH_OWNER(HttpStatus.FORBIDDEN, 402, "소유주명과 일치하지 않습니다."),

    // Not Found 404
    NON_REGISTER_USER(HttpStatus.NOT_FOUND, 404, "계좌가 연동되지 않은 사용자입니다."),
    // Server Error 500
    CAN_NOT_TRANSACTIOn(HttpStatus.INTERNAL_SERVER_ERROR, 500, "불가능한 트랜잭션 입니다."),
    ;
    private final HttpStatus status;
    private final int code;
    private final String message;
}
