package f4.woorimock.domain.account.service.impl;

import f4.woorimock.domain.account.constant.BankingProduct;
import f4.woorimock.domain.account.dto.request.CreateRequestDto;
import f4.woorimock.domain.account.dto.response.CreateResponseDto;
import f4.woorimock.domain.account.persist.entity.Account;
import f4.woorimock.domain.account.persist.repository.AccountQueryRepository;
import f4.woorimock.domain.account.persist.repository.AccountRepository;
import f4.woorimock.domain.account.service.AccountService;
import f4.woorimock.global.constant.CustomErrorCode;
import f4.woorimock.global.exception.CustomException;
import f4.woorimock.global.utils.Encryptor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static f4.woorimock.domain.account.constant.BankingProduct.AUCTION_ACCOUNT;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountQueryRepository accountQueryRepository;
    private final Encryptor encryptor;
    private final ModelMapper modelMapper;

    private static final String BANK_PREFIX = "1002";

    @Override
    @Transactional
    public CreateResponseDto createAuctionAccount(CreateRequestDto createRequestDto) {
        accountRepository.findByArteUserId(createRequestDto.getArteUserId())
                .ifPresent(
                        data -> {
                            throw new CustomException(CustomErrorCode.ALREADY_HAS_ACCOUNT);
                        }
                );

        Account account = accountBuilder(createRequestDto, createAccountNumber());
        return modelMapper.map(account, CreateResponseDto.class);
    }

    private String createAccountNumber() {
        Long recentId = accountQueryRepository.getRecentId();
        recentId = recentId == null ? 0 : recentId;
        return String.join("-", BANK_PREFIX, AUCTION_ACCOUNT.getProductCode(), String.format("%07d", recentId));
    }

    private Account accountBuilder(CreateRequestDto createRequestDto, String accountNumber) {
        return accountRepository.save(
                Account.builder()
                        .name(createRequestDto.getName())
                        .productName(BankingProduct.productCode(AUCTION_ACCOUNT))
                        .accountNumber(accountNumber)
                        .password(encryptor.encrypt(createRequestDto.getPassword()))
                        .balance("0")
                        .auctionUseBalance("0")
                        .arteUserId(createRequestDto.getArteUserId())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }
}
