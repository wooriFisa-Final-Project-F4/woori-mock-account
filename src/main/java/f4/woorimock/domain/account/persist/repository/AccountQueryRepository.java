package f4.woorimock.domain.account.persist.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import f4.woorimock.domain.account.persist.entity.QAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QAccount account = QAccount.account;

    public Long getRecentId() {
        return jpaQueryFactory
                .select(account.id)
                .from(account)
                .orderBy(account.id.desc())
                .limit(1)
                .fetchOne();
    }
}
