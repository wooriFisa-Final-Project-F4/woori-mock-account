package f4.woorimock.domain.account.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkingRequestDto implements Serializable {

    private String name;
    private String accountNumber;
    private String password;
    private Long arteUserId;
}


