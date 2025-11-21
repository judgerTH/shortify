package jade.product.shortify.feature.crawler.service;

import jade.product.shortify.feature.crawler.dto.ArticleContent;
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
public class NaverCrawlerService {

    public ArticleContent crawl(String url) {

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10_000)
                    .get();

            // ========================
            // 1) 제목 (PC / 모바일 커버)
            // ========================
            String title = doc.select("#title_area h2").text();  // PC
            if (title.isBlank()) title = doc.select(".media_end_head_title h2").text(); // Mobile

            if (title.isBlank()) {
                throw new CustomException(ErrorCode.INVALID_ARTICLE_STRUCTURE);
            }

            // ========================
            // 2) 본문
            // ========================
            String content = doc.select("#dic_area").text();
            if (content.isBlank()) {
                throw new CustomException(ErrorCode.INVALID_ARTICLE_STRUCTURE);
            }

            // ========================
            // 3) 언론사
            // ========================
            String press = doc.select(".media_end_head_top_logo img").attr("alt");
            if (press.isBlank()) press = doc.select(".media_end_linked_or_not img").attr("alt");
            if (press.isBlank()) press = "네이버 뉴스";

            // ========================
            // 4) 발행일
            // ========================
            LocalDateTime publishedAt = null;

            String dateText = doc.select("._ARTICLE_DATE_TIME").attr("data-date-time");
            if (!dateText.isBlank()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                publishedAt = LocalDateTime.parse(dateText, formatter);
            }

            // ========================
            // 5) DTO 반환
            // ========================
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
            log.error("Naver crawling error: ", e);
            throw new CustomException(ErrorCode.CRAWLING_FAILED);
        }
    }
}
