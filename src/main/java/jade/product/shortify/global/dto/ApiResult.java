package jade.product.shortify.global.dto;

import jade.product.shortify.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResult<T> {

    private boolean success;
    private T data;
    private String errorCode;
    private String message;

    public static <T> ApiResult<T> ok(T data) {
        return ApiResult.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static ApiResult<?> error(ErrorCode code) {
        return ApiResult.builder()
                .success(false)
                .errorCode(code.getCode())
                .message(code.getMessage())
                .build();
    }
}
