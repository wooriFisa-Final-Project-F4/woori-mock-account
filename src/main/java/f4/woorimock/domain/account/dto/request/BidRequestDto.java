package f4.woorimock.domain.account.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidRequestDto {

    @NotNull
    private int option;
    private Long preUserId;
    private String preBidPrice;
    @NotNull
    private Long curUserId;
    @NotBlank
    private String curBidPrice;
}
