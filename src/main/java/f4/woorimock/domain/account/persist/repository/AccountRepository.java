package f4.woorimock.domain.account.persist.repository;

import f4.woorimock.domain.account.persist.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByArteUserId(Long arteUserId);

    Optional<Account> findByAccountNumber(String accountNumber);
}
