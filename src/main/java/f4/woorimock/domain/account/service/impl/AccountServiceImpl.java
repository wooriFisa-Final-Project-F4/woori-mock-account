package f4.woorimock.domain.account.service.impl;

import f4.woorimock.domain.account.constant.BankingProduct;
import f4.woorimock.domain.account.dto.request.BidCheckRequestDto;
import f4.woorimock.domain.account.dto.request.BidRequestDto;
import f4.woorimock.domain.account.dto.request.CheckBalanceRequestDto;
import f4.woorimock.domain.account.dto.request.CreateRequestDto;
import f4.woorimock.domain.account.dto.request.LinkingRequestDto;
import f4.woorimock.domain.account.dto.request.TransferRequestDto;
import f4.woorimock.domain.account.dto.response.CheckBalanceResponseDto;
import f4.woorimock.domain.account.dto.response.CreateResponseDto;
import f4.woorimock.domain.account.dto.response.LinkingResponseDto;
import f4.woorimock.domain.account.persist.entity.Account;
import f4.woorimock.domain.account.persist.repository.AccountRepository;
import f4.woorimock.domain.account.service.AccountService;
import f4.woorimock.global.constant.CustomErrorCode;
import f4.woorimock.global.exception.CustomException;
import f4.woorimock.global.utils.Encryptor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static f4.woorimock.domain.account.constant.BankingProduct.AUCTION_ACCOUNT;
import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final String BANK_PREFIX = "1002";

    private final AccountRepository accountRepository;
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

        Account account = standByAccount(createRequestDto, createAccountNumber());
        return modelMapper.map(account, CreateResponseDto.class);
    }

    private String createAccountNumber() {
        Long recentId = accountRepository.count() + 1;
        return String.join("-", BANK_PREFIX, AUCTION_ACCOUNT.getProductCode(), reverse(String.format("%07d", recentId)));
    }

    private String reverse(String number) {
        return new StringBuilder(number).reverse().toString();
    }

    private Account standByAccount(CreateRequestDto createRequestDto, String accountNumber) {
        return accountRepository.save(
                Account.builder()
                        .name(createRequestDto.getName())
                        .productName(BankingProduct.productCode(AUCTION_ACCOUNT))
                        .accountNumber(accountNumber)
                        .password(encryptor.encrypt(createRequestDto.getPassword()))
                        .balance("0")
                        .auctionUseBalance("0")
                        .arteUserId(createRequestDto.getArteUserId())
                        .createdAt(now())
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
        Account preAccount = null;
        Account curAccount = null;

        if (bidRequestDto.getOption() == 2) {
            preAccount = loadByArteUserId(bidRequestDto.getPreUserId());

            String prePrice = minusBalance(preAccount.getAuctionUseBalance(), bidRequestDto.getPreBidPrice());
            accountRepository.updateAuctionUseBalanceByArteUserId(prePrice, preAccount.getArteUserId(), now());
        }

        curAccount = loadByArteUserId(bidRequestDto.getCurUserId());
        String curPrice = plusBalance(curAccount.getAuctionUseBalance(), bidRequestDto.getCurBidPrice());
        accountRepository.updateAuctionUseBalanceByArteUserId(curPrice, curAccount.getArteUserId(), now());
    }

    @Override
    public CheckBalanceResponseDto checkBalance(CheckBalanceRequestDto checkBalanceRequestDto) {
        Account account = loadByAccountNumber(checkBalanceRequestDto.getAccountNumber());

        if (account.getArteUserId() != checkBalanceRequestDto.getArteUserId()) {
            throw new CustomException(CustomErrorCode.INVALID_ACCOUNT_OWNER);
        }

        return modelMapper.map(account, CheckBalanceResponseDto.class);
    }

    @Override
    @Transactional
    public void winningBidTransfer(TransferRequestDto transferRequestDto) {
        Account account = loadByArteUserId(transferRequestDto.getArteUserId());

        if (!account.getName().equals(transferRequestDto.getUsername())) {
            throw new CustomException(CustomErrorCode.NOT_MATCH_OWNER);
        }

        // update 잔고 - 옥션 가격, 입찰 금액 - 입찰 가격
        if (Long.parseLong(account.getBalance()) - Long.parseLong(transferRequestDto.getAuctionPrice()) < 0) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        String changeBalance = minusBalance(account.getBalance(), transferRequestDto.getAuctionPrice());
        String changeUseBalance = minusBalance(account.getAuctionUseBalance(), transferRequestDto.getAuctionPrice());

        accountRepository.updateBalancesByArteUserId(
                changeBalance, changeUseBalance, transferRequestDto.getArteUserId(), now());
    }

    private String minusBalance(String mainBalance, String subBalance) {
        long returnPrice = Long.parseLong(mainBalance) - Long.parseLong(subBalance);
        return String.valueOf(returnPrice);
    }

    private String plusBalance(String mainBalance, String subBalance) {
        return String.valueOf(Long.parseLong(mainBalance) + Long.parseLong(subBalance));
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
