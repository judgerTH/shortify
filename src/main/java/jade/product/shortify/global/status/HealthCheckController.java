package jade.product.shortify.global.status;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jade.product.shortify.domain.ping.PingRepository;
import jade.product.shortify.global.dto.ApiResult;
import jade.product.shortify.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final PingRepository pingRepository;

    @GetMapping("/health")
    public ApiResult<String> health() {
        return ApiResult.ok("OK");
    }

    @Operation(summary = "DB Health Check", description = "DB 연결 상태 확인")
    @ApiResponse(responseCode = "200", description = "DB 정상")
    @GetMapping("/health/db")
    public ApiResult<?> healthDB() {
        try {
            pingRepository.count();
            return ApiResult.ok("DB OK");
        } catch (Exception e) {
            return ApiResult.error(ErrorCode.DB_CONNECTION_FAILED);
        }
    }
}
