package jade.product.shortify.global.llm;

import org.springframework.stereotype.Component;

@Component
public class InsightPromptBuilder {
    public String build(String combinedSummaries) {
        return """
        아래는 오늘의 뉴스 요약들이다. 이 내용을 종합하여 한국 사회 분위기를 분석하라.
        JSON 형식으로만 답변하라.

        {
          "tension": 0~100 숫자,
          "positivity": 0~100 숫자,
          "stability": 0~100 숫자,
          "summary": "오늘의 분위기를 요약한 한 문장"
        }

        뉴스 요약:
        %s
        """.formatted(combinedSummaries);
    }
}
