package f4.woorimock.domain.account.controller;

import f4.woorimock.domain.account.dto.request.BidCheckRequestDto;
import f4.woorimock.domain.account.dto.request.BidRequestDto;
import f4.woorimock.domain.account.dto.request.CheckBalanceRequestDto;
import f4.woorimock.domain.account.dto.request.CreateRequestDto;
import f4.woorimock.domain.account.dto.request.LinkingRequestDto;
import f4.woorimock.domain.account.dto.request.TransferRequestDto;
import f4.woorimock.domain.account.service.AccountService;
import f4.woorimock.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/woori/account/v1/")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /*
     * @date : 2023.08.27
     * @author : yuki
     * @param : CreateRequestDto(name, password, arteUserId)
     * @description : arte_moㅕderni_id 조회를 통해 해당 유저가 이미 계좌를 가지고 있는지, 없는지 여부를 파악한다.
     */
    @PostMapping("/create")
    public ApiResponse<?> createAccount(
            @Valid @RequestBody CreateRequestDto createRequestDto
    ) {
        log.info("계좌 생성 수행. arteUserId : {}, name : {}", createRequestDto.getArteUserId(), createRequestDto.getName());
        return ApiResponse.success(accountService.createAuctionAccount(createRequestDto));
    }

    /*
     * @date : 2023.08.27
     * @author : yuki
     * @param : LinkingRequestDto(accountNumber, password, arteUserId)
     * @description : arte_moderni 계좌 연동 API
     */
    @PostMapping("/linking")
    public ApiResponse<?> linkingAccount(
            @Valid @RequestBody LinkingRequestDto linkingRequestDto
    ) {
        log.info("계좌 연동 수행. arteUserId : {}, name : {} accountNumber : {}",
                linkingRequestDto.getArteUserId(), linkingRequestDto.getName(), linkingRequestDto.getAccountNumber());
        return ApiResponse.success(accountService.linkingAccount(linkingRequestDto));
    }

    /*
     * @date : 2023.09.05
     * @author : yuki
     * @param : CheckBalanceRequestDto(arteUserId, accountNumber)
     * @description : 계좌 잔액 조회 API
     */
    @PostMapping("/check/balance")
    public ApiResponse<?> checkBalance(
            @Valid @RequestBody CheckBalanceRequestDto checkBalanceRequestDto) {
        log.info("잔액 조회 수행. arteUserId : {}, accountNumber : {}",
                checkBalanceRequestDto.getArteUserId(), checkBalanceRequestDto.getAccountNumber());
        return ApiResponse.success(accountService.checkBalance(checkBalanceRequestDto));
    }

    /*
     * @date : 2023.08.29
     * @author : yuki
     * @param : BidCheckRequestDto(arteUserId, password, bidPrice)
     * @description : 입찰 가능 여부 조회 API
     */
    @PostMapping("/bid/check")
    public ApiResponse<?> bidAvailabilityCheck(
            @Valid @RequestBody BidCheckRequestDto bidCheckRequestDto
    ) {
        log.info("입찰 참여 가능 여부. arteUserId : {}, bidPrice : {}", bidCheckRequestDto.getArteUserId(), bidCheckRequestDto.getBidPrice());
        accountService.bidAvailabilityCheck(bidCheckRequestDto);
        return ApiResponse.successWithNoContent();
    }


    /*
     * @date : 2023.08.29
     * @author : yuki
     * @param : BidRequestDto(option(이전 입찰자가 없는 경우 0 else 1), preUserId, preBidPrice, curUserId,curBidPrice)
     * @description : 입찰자 및 금액 변경 API
     */
    @PutMapping("/bid")
    public ApiResponse<?> bidInfoUpdate(
            @Valid @RequestBody BidRequestDto bidRequestDto
    ) {
        log.info("입찰 수행. option : {}, preUserId : {}, preBidPrice : {}, curUserId : {}, curBidPrice : {}",
                bidRequestDto.getOption(), bidRequestDto.getPreUserId(), bidRequestDto.getPreBidPrice(),
                bidRequestDto.getCurUserId(), bidRequestDto.getCurBidPrice());

        accountService.bidInfoUpdate(bidRequestDto);
        return ApiResponse.successWithNoContent();
    }

    /*
    *
    *
    * */
    @PutMapping("/winning/bid-transfer")
    public ApiResponse<?> winningBidTransfer(@Valid @RequestBody TransferRequestDto transferRequestDto) {
        log.info("낙찰 수행 시작. arteUserId : {}, productName : {}, auctionPrice{}",
                transferRequestDto.getArteUserId(), transferRequestDto.getProductName(), transferRequestDto.getProductName());

        accountService.winningBidTransfer(transferRequestDto);
        return ApiResponse.successWithNoContent();
    }
}
