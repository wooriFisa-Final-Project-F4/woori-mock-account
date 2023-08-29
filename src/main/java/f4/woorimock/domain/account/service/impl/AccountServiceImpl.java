package f4.woorimock.domain.account.service.impl;

import f4.woorimock.domain.account.constant.BankingProduct;
import f4.woorimock.domain.account.dto.request.BidCheckRequestDto;
import f4.woorimock.domain.account.dto.request.BidRequestDto;
import f4.woorimock.domain.account.dto.request.CreateRequestDto;
import f4.woorimock.domain.account.dto.request.LinkingRequestDto;
import f4.woorimock.domain.account.dto.response.CreateResponseDto;
import f4.woorimock.domain.account.dto.response.LinkingResponseDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static f4.woorimock.domain.account.constant.BankingProduct.AUCTION_ACCOUNT;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final String BANK_PREFIX = "1002";

    private final AccountRepository accountRepository;
    private final AccountQueryRepository accountQueryRepository;
    private final Encryptor encryptor;
    private final ModelMapper modelMapper;

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
        recentId = (recentId == null) ? 0 : recentId;
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

    @Override
    public LinkingResponseDto linkingAccount(LinkingRequestDto accountRequestDto) {
        Account account = loadByAccountNumber(accountRequestDto.getAccountNumber());
        ownerValidate(account, accountRequestDto.getName());
        passwordValidate(account, accountRequestDto.getPassword());

        return modelMapper.map(account, LinkingResponseDto.class);
    }

    @Override
    public void bidAvailabilityCheck(BidCheckRequestDto bidCheckRequestDto) {
        Account account = loadByArteUserId(bidCheckRequestDto.getArteUserId());
        passwordValidate(account, bidCheckRequestDto.getPassword());
        bidAvailabilityValidator(account.getBalance(), account.getAuctionUseBalance(), bidCheckRequestDto.getBidPrice());
    }

    private Account loadByArteUserId(Long arteUserId) {
        return accountRepository.findByArteUserId(arteUserId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.NON_REGISTER_USER));
    }

    @Override
    @Transactional
    public void bidInfoUpdate(BidRequestDto bidRequestDto) {
        Account preAccount;
        Account curAccount;

        if (bidRequestDto.getOption() == 2) {
            preAccount = loadByArteUserId(bidRequestDto.getPreUserId());

            String prePrice = bidFail(bidRequestDto.getPreBidPrice(), preAccount.getAuctionUseBalance());
            accountRepository.updateAuctionUseBalanceByArteUserId(prePrice, preAccount.getArteUserId());
        }

        curAccount = loadByArteUserId(bidRequestDto.getCurUserId());
        String curPrice = bidSuccess(bidRequestDto.getCurBidPrice(), curAccount.getAuctionUseBalance());
        accountRepository.updateAuctionUseBalanceByArteUserId(curPrice, curAccount.getArteUserId());
    }


    private String bidFail(String preBidPrice, String auctionUseBalance) {
        long returnPrice = Long.parseLong(auctionUseBalance) - Long.parseLong(preBidPrice);
        return String.valueOf(returnPrice);
    }

    private String bidSuccess(String preBidPrice, String auctionUseBalance) {
        return String.valueOf(Long.parseLong(auctionUseBalance) + Long.parseLong(preBidPrice));
    }

    // 가용 금액
    private void bidAvailabilityValidator(String balance, String auctionUseBalance, String bidPrice) {
        if (!((Long.parseLong(balance) - Long.parseLong(auctionUseBalance)) >= Long.parseLong(bidPrice))) {
            throw new CustomException(CustomErrorCode.LACk_USABLE_BALANCE);
        }
    }

    private Account loadByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new CustomException(CustomErrorCode.INVALID_ACCOUNT_NUMBER));
    }

    private void ownerValidate(Account account, String name) {
        if (!account.getName().equals(name)) {
            throw new CustomException(CustomErrorCode.NOT_MATCH_OWNER);
        }
    }

    private void passwordValidate(Account account, String password) {
        if (!account.getPassword().equals(password)) {
            throw new CustomException(CustomErrorCode.INCORRECT_PASSWORD_INPUT);
        }
    }
}
