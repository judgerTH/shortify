package jade.product.shortify.test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TistoryTestController {

    private final RestTemplate restTemplate = new RestTemplate();
    @GetMapping("/test/tistory")
    public String postToTistory() {

        String cookie = "TSSESSION=f56ed35e88168b867da4158a2e4d7d459d9d8e9f;";
        String url = "https://dev-work-jade.tistory.com/manage/post.json";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("id", "0"); // 새 글 생성
        body.put("title", "Shortify 자동 포스팅 테스트");
        body.put("content", "<p data-ke-size=\"size16\">Shortify에서 자동 생성된 본문</p>");
        body.put("slogan", "shortify-test");
        body.put("category", 0);
        body.put("published", 1); // 발행
        body.put("visibility", 0); // 공개
        body.put("password", "");
        body.put("attachments", new ArrayList<>());
        body.put("cclCommercial", 0);
        body.put("cclDerive", 0);
        body.put("recaptchaValue", "");
        body.put("type", "post");
        body.put("tag", "shortify,테스트");
        body.put("uselessMarginForEntry", 1);
        body.put("draftSequence", null);

        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        try {
            RestTemplate rest = new RestTemplate();
            ResponseEntity<String> response =
                    rest.exchange(url, HttpMethod.POST, entity, String.class);

            return "응답 상태: " + response.getStatusCodeValue() +
                    "\n응답 바디: " + response.getBody();

        } catch (Exception e) {
            return "예외 발생:\n" + e.getMessage();
        }
    }

}
