package f4.woorimock.global.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static f4.woorimock.global.constant.ApiStatus.ERROR;
import static f4.woorimock.global.constant.ApiStatus.SUCCESS;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private T data;
    private T error;

    public static ApiResponse<?> successWithNoContent() {
        return new ApiResponse<>(SUCCESS.getApiStatus(), null, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS.getApiStatus(), data, null);
    }

    // 예외 발생으로 API 호출 실패시 반환
    public static <T> ApiResponse<?> error(T errors) {
        return new ApiResponse<>(ERROR.getApiStatus(), null, errors);
    }
}
