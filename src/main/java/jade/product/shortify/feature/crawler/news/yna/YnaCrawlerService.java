package jade.product.shortify.feature.crawler.news.yna;

import jade.product.shortify.feature.crawler.dto.ArticleContent;
import jade.product.shortify.feature.crawler.util.YnaCleaningUtils;
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
public class YnaCrawlerService {

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
            if (title.isBlank()) title = doc.select("#articleTitle").text();
            if (title.isBlank()) title = doc.title();

            if (title.isBlank()) {
                throw new CustomException(ErrorCode.INVALID_ARTICLE_STRUCTURE);
            }

            // ============================
            // 2) 본문
            // ============================
            String content = doc.select("article").text();
            if (content.isBlank()) content = doc.body().text();

            content = YnaCleaningUtils.cleanYnaContent(content);

            if (content.isBlank()) {
                throw new CustomException(ErrorCode.INVALID_ARTICLE_STRUCTURE);
            }

            // ============================
            // 3) 언론사
            // ============================
            String press = doc.select("meta[property=og:site_name]").attr("content");
            if (press.isBlank()) press = "연합뉴스";

            // ============================
            // 4) 발행일
            // ============================
            LocalDateTime publishedAt = null;

            try {
                String publishedTime = doc.select("meta[property=article:published_time]").attr("content");

                if (!publishedTime.isBlank()) {
                    publishedAt = LocalDateTime.parse(publishedTime.substring(0, 19));
                } else {
                    String textDate = doc.select(".update-time").text();
                    if (!textDate.isBlank()) {
                        textDate = textDate.split("송고")[0].trim().replace("/", "-");
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        publishedAt = LocalDateTime.parse(textDate, fmt);
                    }
                }
            } catch (Exception e) {
                log.warn("YNA 날짜 파싱 실패(무시): {}", e.getMessage());
            }

            // ============================
            // 5) DB 저장 X → DTO 반환
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
            log.error("YNA crawling failed: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.CRAWLING_FAILED);
        }
    }
}
