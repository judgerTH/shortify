package jade.product.shortify.feature.crawler.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ArticleContent {

    // 원문 제목
    private final String title;

    // 크롤링으로 가져온 원문 본문 (메모리에서만 사용, DB에는 저장 안 함)
    private final String content;

    // 언론사
    private final String press;

    // 원문 URL
    private final String url;

    // 발행일
    private final LocalDateTime publishedAt;
}
