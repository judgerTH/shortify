package jade.product.shortify.global.llm;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jade.product.shortify.global.config.GeminiConfig;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final GeminiConfig config;
    private final OkHttpClient httpClient = new OkHttpClient();

    public String generate(String prompt) throws Exception {

        // Google 공식 엔드포인트
        String url =
                "https://generativelanguage.googleapis.com/v1beta/"
                        + config.getModel()
                        + ":generateContent?key=" + config.getApiKey();

        // ===== parts 배열 =====
        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);

        JsonArray parts = new JsonArray();
        parts.add(part);

        // ===== contents 배열(role 추가 필수) =====
        JsonObject contentItem = new JsonObject();
        contentItem.addProperty("role", "user");
        contentItem.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(contentItem);

        // ===== 전체 바디 =====
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

        System.out.println("URL = " + url);
        System.out.println("REQUEST = " + reqBody.toString());

        Response response = httpClient.newCall(request).execute();

        System.out.println("RESPONSE CODE = " + response.code());

        String responseString = response.body().string();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Gemini API 오류(" + response.code() + "): " + responseString);
        }

        return responseString;
    }
}
