package jade.product.shortify.global.llm;

import org.springframework.stereotype.Component;

@Component
public class NewsPromptBuilder {

    public String build(String content) {
        return """
            아래 뉴스 내용을 기반으로 정확한 텍스트-only 형식으로 출력해줘.
            Markdown(**, ## 등) 절대 사용하지 마.
            반드시 아래와 같은 형태의 순수 텍스트로만 출력해.

            제목: 한 문장
            요약: 3~4문장
            키워드: 콤마로 구분한 핵심 단어 3~6개

            [NEWS]
            %s
            """.formatted(content);
    }
}
