package f4.woorimock.domain.account.persist.repository;

import f4.woorimock.domain.account.persist.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByArteUserId(Long arteUserId);

    Optional<Account> findByAccountNumber(String accountNumber);

    @Modifying
    @Query(value = "UPDATE account " +
            "SET auction_use_balance = :price, " +
            "updated_at = :dateTime " +
            "WHERE arte_user_id = :userId", nativeQuery = true)
    void updateAuctionUseBalanceByArteUserId(String price, Long userId, LocalDateTime dateTime);

    @Modifying
    @Query(value = "UPDATE account " +
            "SET balance = :balance, " +
            "auction_use_balance = :auctionUseBalance, " +
            "updated_at = :dateTime " +
            "WHERE arte_user_id = :userId", nativeQuery = true)
    void updateBalancesByArteUserId(String balance, String auctionUseBalance, Long userId, LocalDateTime dateTime);

    @Modifying
    @Query(value = "UPDATE account " +
            "SET arte_user_id = :userId, " +
            "updated_at= :dateTime " +
            "WHERE account_number = :accountNumber", nativeQuery = true)
    void updateAccountByArteUserId(Long userId, LocalDateTime dateTime, String accountNumber);
}