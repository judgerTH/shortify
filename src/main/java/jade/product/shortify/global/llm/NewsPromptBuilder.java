package jade.product.shortify.global.llm;

import org.springframework.stereotype.Component;

@Component
public class NewsPromptBuilder {

    public String build(String content) {
        return """
                아래 뉴스 내용을 쉽고 빠르게 이해하도록 3~4문장으로 요약해줘.
                핵심 사실만 남기고 불필요한 설명은 제거해.

                [NEWS]
                %s
                """.formatted(content);
    }
}
