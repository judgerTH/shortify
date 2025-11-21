package jade.product.shortify.feature.crawler.service;

import jade.product.shortify.domain.article.entity.OriginalArticle;
import jade.product.shortify.domain.article.repository.OriginalArticleRepository;
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

    private final OriginalArticleRepository originalArticleRepository;

    public OriginalArticle crawlUrl(String url) {

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .timeout(10_000)
                    .get();

            // ============================
            // 1) 제목(title)
            // ============================
            String title = doc.select("meta[property=og:title]").attr("content");
            if (title.isBlank()) {
                title = doc.select("#articleTitle").text();
            }
            if (title.isBlank()) {
                title = doc.title();
            }

            if (title.isBlank()) {
                throw new CustomException(ErrorCode.INVALID_ARTICLE_STRUCTURE);
            }

            // ============================
            // 2) 본문(content)
            // ============================
            String content = doc.select("article").text();
            if (content.isBlank()) {
                content = doc.body().text();
            }

            content = YnaCleaningUtils.cleanYnaContent(content);

            if (content.isBlank()) {
                throw new CustomException(ErrorCode.INVALID_ARTICLE_STRUCTURE);
            }

            // ============================
            // 3) 언론사(press)
            // ============================
            String press = doc.select("meta[property=og:site_name]").attr("content");
            if (press.isBlank()) press = "연합뉴스";

            // ============================
            // 4) 발행일(publishedAt)
            // ============================
            LocalDateTime publishedAt = null;

            String publishedTime = doc.select("meta[property=article:published_time]").attr("content");

            try {
                if (!publishedTime.isBlank()) {
                    publishedAt = LocalDateTime.parse(publishedTime.substring(0, 19));
                } else {
                    String textDate = doc.select(".update-time").text();
                    if (!textDate.isBlank()) {
                        textDate = textDate.split("송고")[0].trim().replace("/", "-");
                        publishedAt = LocalDateTime.parse(textDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    }
                }
            } catch (Exception ignore) {
                // 날짜는 없어도 상관 없음 → publishedAt null 허용
            }

            // ============================
            // 5) DB 저장
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
            throw e; // 그대로 전달
        } catch (Exception e) {
            log.error("YNA crawling failed: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.CRAWLING_FAILED);
        }
    }
}
