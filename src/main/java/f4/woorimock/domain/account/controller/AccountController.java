package f4.woorimock.domain.account.controller;

import f4.woorimock.domain.account.dto.request.BidCheckRequestDto;
import f4.woorimock.domain.account.dto.request.BidRequestDto;
import f4.woorimock.domain.account.dto.request.CheckBalanceRequestDto;
import f4.woorimock.domain.account.dto.request.CreateRequestDto;
import f4.woorimock.domain.account.dto.request.LinkingRequestDto;
import f4.woorimock.domain.account.service.AccountService;
import f4.woorimock.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/woori/account/v1/")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /*
     * @date : 2023.08.27
     * @author : yuki
     * @param : name, password, arteUserId(unique)
     * @description : arte_moㅕderni_id 조회를 통해 해당 유저가 이미 계좌를 가지고 있는지, 없는지 여부를 파악한다.
     */
    @PostMapping("/create")
    public ApiResponse<?> createAccount(
            @Valid @RequestBody CreateRequestDto createRequestDto
    ) {
        return ApiResponse.success(accountService.createAuctionAccount(createRequestDto));
    }

    /*
     * @date : 2023.08.27
     * @author : yuki
     * @param : accountNumber, password, arteUserId(unique)
     * @description : arte_moderni 계좌 연동
     */
    @PostMapping("/linking")
    public ApiResponse<?> linkingAccount(
            @Valid @RequestBody LinkingRequestDto linkingRequestDto
    ) {
        return ApiResponse.success(accountService.linkingAccount(linkingRequestDto));
    }

    @PostMapping("/check/balance")
    public ApiResponse<?> checkBalance(
            @Valid @RequestBody CheckBalanceRequestDto checkBalanceRequestDto) {
        return ApiResponse.success(accountService.checkBalance(checkBalanceRequestDto));
    }

    @PostMapping("/bid/check")
    public ApiResponse<?> bidAvailabilityCheck(
            @Valid @RequestBody BidCheckRequestDto bidCheckRequestDto
    ) {
        accountService.bidAvailabilityCheck(bidCheckRequestDto);
        return ApiResponse.successWithNoContent();
    }


    @PutMapping("/bid")
    public ApiResponse<?> bidInfoUpdate(
            @Valid @RequestBody BidRequestDto bidRequestDto
    ) {
        accountService.bidInfoUpdate(bidRequestDto);
        return ApiResponse.successWithNoContent();
    }
}
