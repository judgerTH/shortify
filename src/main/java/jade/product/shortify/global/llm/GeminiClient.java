package jade.product.shortify.global.llm;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final GeminiConfig config;

    // Timeout 1분 버전
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(10))     // 연결
            .writeTimeout(Duration.ofSeconds(20))       // 요청 전송
            .readTimeout(Duration.ofSeconds(60))        // 응답 대기
            .callTimeout(Duration.ofSeconds(60))        // 전체 요청(1분)
            .build();

    public String generate(String prompt) throws Exception {

        String url =
                "https://generativelanguage.googleapis.com/v1beta/"
                        + config.getModel()
                        + ":generateContent?key=" + config.getApiKey();

        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);

        JsonArray parts = new JsonArray();
        parts.add(part);

        JsonObject contentItem = new JsonObject();
        contentItem.addProperty("role", "user");
        contentItem.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(contentItem);

        JsonObject reqBody = new JsonObject();
        reqBody.add("contents", contents);

        RequestBody body = RequestBody.create(
                reqBody.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = httpClient.newCall(request).execute();

        String responseString = response.body().string();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Gemini API 오류(" + response.code() + "): " + responseString);
        }

        return responseString;
    }
}
