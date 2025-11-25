package jade.product.shortify.global.llm;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jade.product.shortify.feature.insight.dto.NewsInsightResponse;
public class InsightParser {

    public static NewsInsightResponse parse(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        JsonObject candidate = root.getAsJsonArray("candidates")
                .get(0).getAsJsonObject();

        String text = candidate
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();

        // 1) 코드블록 제거
        text = text.replace("```json", "")
                .replace("```", "")
                .trim();

        // 2) JSON만 파싱
        JsonObject obj = JsonParser.parseString(text).getAsJsonObject();

        return new NewsInsightResponse(
                obj.get("tension").getAsInt(),
                obj.get("positivity").getAsInt(),
                obj.get("stability").getAsInt(),
                obj.get("summary").getAsString()
        );
    }
}
