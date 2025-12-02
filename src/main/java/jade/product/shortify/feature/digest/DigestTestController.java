package jade.product.shortify.feature.digest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DigestTestController {

    private final DigestService digestService;

    @GetMapping("/test/digest")
    public String testDigest() {
        try {
            digestService.publishTodayDigest();
            return "OK - Digest Published";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
}
