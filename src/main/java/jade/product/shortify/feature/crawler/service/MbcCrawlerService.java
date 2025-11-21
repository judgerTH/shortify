package jade.product.shortify.feature.crawler.service;

import jade.product.shortify.domain.article.entity.OriginalArticle;
import jade.product.shortify.domain.article.repository.OriginalArticleRepository;
import jade.product.shortify.feature.crawler.util.MbcCleaningUtils;
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
public class MbcCrawlerService {

    private final OriginalArticleRepository originalArticleRepository;

    public OriginalArticle crawlUrl(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .timeout(10_000)
                    .get();

            // ============================
            // 1) 제목
            // ============================
            String title = doc.select("meta[property=og:title]").attr("content");
            if (title.isBlank()) {
                title = doc.select("#news_title").text();
            }

            if (title.isBlank()) {
                throw new CustomException(ErrorCode.INVALID_ARTICLE_STRUCTURE);
            }

            // ============================
            // 2) 본문
            // ============================
            String content = doc.select("#news_content").text();
            if (content.isBlank()) {
                content = doc.body().text();
            }
            content = MbcCleaningUtils.clean(content);
            if (content.length() < 30) {
                throw new CustomException(ErrorCode.INVALID_ARTICLE_STRUCTURE);
            }

            // ============================
            // 3) 언론사
            // ============================
            String press = "MBC";

            // ============================
            // 4) 발행일
            // ============================
            LocalDateTime publishedAt = null;
            String publishedTime = doc.select("meta[property=article:published_time]").attr("content");

            // case 1: meta ISO format
            if (!publishedTime.isBlank()) {
                publishedAt = LocalDateTime.parse(publishedTime.substring(0, 19));
            } else {
                // case 2: 화면 날짜
                String dateText = doc.select("#news_date").text();
                // 예시: "2025-11-21 15:22"
                if (!dateText.isBlank()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    publishedAt = LocalDateTime.parse(dateText.trim(), formatter);
                }
            }

            // ============================
            // 5) 저장
            // ============================
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
            log.error("MBC 크롤링 실패", e);
            throw new CustomException(ErrorCode.CRAWLING_FAILED);
        }
    }
}
