package f4.woorimock.domain.account.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkingRequestDto implements Serializable {

    @NotNull
    private Long arteUserId;
    @NotBlank
    private String name;
    @NotBlank
    private String accountNumber;
    @NotBlank
    private String password;
}


