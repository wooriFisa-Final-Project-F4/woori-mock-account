package f4.woorimock.domain.account.service;

import f4.woorimock.domain.account.dto.request.CreateRequestDto;
import f4.woorimock.domain.account.dto.response.CreateResponseDto;

public interface AccountService {

    CreateResponseDto createAuctionAccount(CreateRequestDto createRequestDto);
}
