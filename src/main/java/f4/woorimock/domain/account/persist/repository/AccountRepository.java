package f4.woorimock.domain.account.persist.repository;

import f4.woorimock.domain.account.persist.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByArteUserId(Long arteUserId);

    Optional<Account> findByAccountNumber(String accountNumber);

    @Modifying
    @Query(value = "UPDATE Account SET auction_use_balance = :price WHERE arte_user_id = :userId", nativeQuery = true)
    void updateAuctionUseBalanceByArteUserId(String price, Long userId);
}