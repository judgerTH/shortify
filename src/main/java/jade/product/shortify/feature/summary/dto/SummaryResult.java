package jade.product.shortify.feature.summary.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;

@Getter
public class SummaryResult {

    private final String title;
    private final String content;
    private final String keywords;

    private SummaryResult(String title, String content, String keywords) {
        this.title = title;
        this.content = content;
        this.keywords = keywords;
    }

    public static SummaryResult fromGeminiResponse(String json) {

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        if (!root.has("candidates")
                || root.getAsJsonArray("candidates").isEmpty()) {
            return new SummaryResult("", "", "");
        }

        JsonObject candidate = root.getAsJsonArray("candidates")
                .get(0).getAsJsonObject();

        // parts → 모든 텍스트 이어 붙이기
        StringBuilder sb = new StringBuilder();
        if (candidate.has("content")) {
            candidate.getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .forEach(p -> {
                        if (p.getAsJsonObject().has("text")) {
                            sb.append(p.getAsJsonObject()
                                    .get("text").getAsString()).append("\n");
                        }
                    });
        }

        String text = sb.toString().trim();
        if (text.isBlank()) return new SummaryResult("", "", "");

        String[] lines = text.split("\n");

        String title = "";
        String summary = "";
        String keywords = "";

        for (String raw : lines) {

            // 1) Markdown 제거 (** ~~ **, * 등)
            String line = raw.replace("*", "").trim();

            // 제목 패턴
            if (line.matches("(?i)^(제목|title)[^:：\\-]*[:：\\-].*")) {
                title = line.replaceAll("(?i)^(제목|title)[^:：\\-]*[:：\\-]", "").trim();
                continue;
            }

            // 요약 패턴
            if (line.matches("(?i)^(요약|summary)[^:：\\-]*[:：\\-].*")) {
                summary = line.replaceAll("(?i)^(요약|summary)[^:：\\-]*[:：\\-]", "").trim();
                continue;
            }

            // 키워드 패턴
            if (line.matches("(?i)^(키워드|keywords)[^:：\\-]*[:：\\-].*")) {
                keywords = line.replaceAll("(?i)^(키워드|keywords)[^:：\\-]*[:：\\-]", "").trim();
                continue;
            }
        }

        // 제목 못 찾았으면 → 첫 번째 라인 fallback
        if (title.isBlank() && lines.length > 0) {
            title = lines[0].trim();
        }

        // 요약이 비었으면 → 두 번째 라인 fallback
        if (summary.isBlank() && lines.length > 1) {
            summary = lines[1].trim();
        }

        return new SummaryResult(title, summary, keywords);
    }
}
