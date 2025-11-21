package jade.product.shortify.feature.crawler.service;

import jade.product.shortify.feature.crawler.dto.ArticleContent;
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

    public ArticleContent crawl(String url) {
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

            // 어느 정도 길이 안 나오면 구조 잘못 파싱된 걸로 간주
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

            try {
                // case 1: meta ISO format (예: 2025-11-21T15:09:21+09:00)
                if (!publishedTime.isBlank()) {
                    publishedAt = LocalDateTime.parse(publishedTime.substring(0, 19));
                } else {
                    // case 2: 화면 상 날짜 (예: "2025-11-21 15:22")
                    String dateText = doc.select("#news_date").text();
                    if (!dateText.isBlank()) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        publishedAt = LocalDateTime.parse(dateText.trim(), formatter);
                    }
                }
            } catch (Exception ignore) {
                // 날짜는 없어도 되니까 무시
            }

            // ============================
            // 5) DTO 반환
            // ============================
            return ArticleContent.builder()
                    .title(title)
                    .content(content)
                    .press(press)
                    .url(url)
                    .publishedAt(publishedAt)
                    .build();

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("MBC 크롤링 실패", e);
            throw new CustomException(ErrorCode.CRAWLING_FAILED);
        }
    }
}
