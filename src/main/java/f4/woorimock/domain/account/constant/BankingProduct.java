package f4.woorimock.domain.account.constant;

import f4.woorimock.global.constant.CustomErrorCode;
import f4.woorimock.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BankingProduct {

    AUCTION_ACCOUNT("200", "경매 참여 통장");

    private final String productCode;
    private final String productName;

    public static String productName(BankingProduct product) {
        return Arrays.stream(values())
                .filter(i -> i.equals(product))
                .findFirst()
                .map(BankingProduct::getProductName)
                .orElseThrow(() -> new CustomException(CustomErrorCode.PRODUCT_NOT_EXIST));
    }

    public static String productCode(BankingProduct product) {
        return Arrays.stream(values())
                .filter(i -> i.equals(product))
                .findFirst()
                .map(BankingProduct::getProductCode)
                .orElseThrow(() -> new CustomException(CustomErrorCode.PRODUCT_NOT_EXIST));
    }
}
