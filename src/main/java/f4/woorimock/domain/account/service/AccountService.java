package f4.woorimock.domain.account.service;

import f4.woorimock.domain.account.dto.request.BidCheckRequestDto;
import f4.woorimock.domain.account.dto.request.BidRequestDto;
import f4.woorimock.domain.account.dto.request.CheckBalanceRequestDto;
import f4.woorimock.domain.account.dto.request.CreateRequestDto;
import f4.woorimock.domain.account.dto.request.LinkingRequestDto;
import f4.woorimock.domain.account.dto.response.CheckBalanceResponseDto;
import f4.woorimock.domain.account.dto.response.CreateResponseDto;
import f4.woorimock.domain.account.dto.response.LinkingResponseDto;

public interface AccountService {

    CreateResponseDto createAuctionAccount(CreateRequestDto createRequestDto);

    LinkingResponseDto linkingAccount(LinkingRequestDto linkingRequestDto);

    CheckBalanceResponseDto checkBalance(CheckBalanceRequestDto checkBalanceRequestDto);

    void bidAvailabilityCheck(BidCheckRequestDto bidCheckRequestDto);

    void bidInfoUpdate(BidRequestDto bidRequestDto);
}
