package jade.product.shortify.feature.crawler.service;

import jade.product.shortify.domain.article.entity.OriginalArticle;
import jade.product.shortify.domain.article.repository.OriginalArticleRepository;
import jade.product.shortify.global.exception.CustomException;
import jade.product.shortify.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class KbsCrawlerService {

    private final OriginalArticleRepository originalArticleRepository;

    public OriginalArticle crawl(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10_000)
                    .get();

            // 제목
            String title = doc.select("meta[property=og:title]").attr("content");
            if (title.isBlank()) {
                title = doc.select(".headline-title").text();
            }
            if (title.isBlank()) throw new CustomException(ErrorCode.INVALID_ARTICLE_STRUCTURE);

            // 본문
            String content = doc.select(".detail-body").text();
            if (content.isBlank()) content = doc.body().text();
            if (content.isBlank()) throw new CustomException(ErrorCode.INVALID_ARTICLE_STRUCTURE);

            // 언론사
            String press = "KBS 뉴스";

            // 발행일
            LocalDateTime publishedAt = parseKbsPublishedAt(doc);

            OriginalArticle article = OriginalArticle.builder()
                    .title(title)
                    .content(content)
                    .press(press)
                    .url(url)
                    .publishedAt(publishedAt)
                    .build();

            return originalArticleRepository.save(article);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("KBS 크롤링 실패: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.CRAWLING_FAILED);
        }
    }

    private LocalDateTime parseKbsPublishedAt(Document doc) {
        try {
            String dateText = doc.select(".input-date").text();
            if (dateText.isBlank()) return null;

            dateText = dateText.replace("입력", "").trim();
            dateText = dateText.replace("(", "").replace(")", "").trim();
            dateText = dateText.replace(".", "-");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            return LocalDateTime.parse(dateText, formatter);

        } catch (Exception e) {
            log.warn("KBS 날짜 파싱 실패");
            return null;
        }
    }
}
