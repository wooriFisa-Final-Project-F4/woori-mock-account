package f4.woorimock.domain.account.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferRequestDto {

    @NotNull
    private Long arteUserId;
    @NotBlank
    private String productName;
    @NotBlank
    private String username;
    @NotBlank
    private String auctionPrice;
}
