package jade.product.shortify.application.sample;

import jade.product.shortify.global.dto.ApiResult;
import jade.product.shortify.global.exception.CustomException;
import jade.product.shortify.global.exception.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/sample/success")
    public ApiResult<String> success() {
        return ApiResult.ok("Swagger OK");
    }

    @GetMapping("/sample/error")
    public ApiResult<String> error() {
        throw new CustomException(ErrorCode.CRAWLING_FAILED);
    }
}