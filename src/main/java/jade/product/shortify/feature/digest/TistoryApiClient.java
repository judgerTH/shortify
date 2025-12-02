package jade.product.shortify.feature.digest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
public class TistoryApiClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String TARGET_URL =
            "https://dev-work-jade.tistory.com/manage/post.json";

    @Value("${shortify.cookie}")
    private String cookie;

    // HTML을 JSON-safe 형태로 escape
    private String escapeHtml(String html) {
        return html
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    public void publish(String html) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Cookie", cookie);
        headers.add("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0 Safari/537.36");
        headers.add("Referer",
                "https://dev-work-jade.tistory.com/manage/entry/post/");

        // escapeHtml 제거 — HTML 그대로 보내야 함
        String safeHtml = html;

        Map<String, Object> body = new HashMap<>();
        body.put("id", "0");
        body.put("title", "오늘의 소셜 다이제스트");
        body.put("content", safeHtml);
        body.put("contentType", "html");
        body.put("slogan", "daily-digest");
        body.put("category", 1286254);
        body.put("published", 1);
        body.put("visibility", 20);

        body.put("password", "");
        body.put("attachments", new ArrayList<>());
        body.put("cclCommercial", 0);
        body.put("cclDerive", 0);
        body.put("recaptchaValue", "");
        body.put("type", "post");
        body.put("tag", "daily,digest,shortify");
        body.put("uselessMarginForEntry", 1);
        body.put("draftSequence", null);

        String json = mapper.writeValueAsString(body);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        TARGET_URL,
                        HttpMethod.POST,
                        new HttpEntity<>(json, headers),
                        String.class
                );

        System.out.println("티스토리 응답: " + response.getBody());
    }
}
