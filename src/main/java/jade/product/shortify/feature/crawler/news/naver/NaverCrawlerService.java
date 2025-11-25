package jade.product.shortify.feature.crawler.news.naver;

import jade.product.shortify.feature.crawler.dto.ArticleContent;
import jade.product.shortify.global.exception.CustomException;
import jade.product.shortify.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class NaverCrawlerService {

    // 제목 후보 리스트
    private static final List<String> TITLE_SELECTORS = List.of(
            "h2.media_end_head_headline",
            "#title_area h2",
            "meta[property=og:title]",
            "#news_title",
            "h3#articleTitle",
            "div.media_end_head_title"
    );

    // 본문 후보 리스트
    private static final List<String> CONTENT_SELECTORS = List.of(
            "#dic_area",
            "div.newsct_article._article_body",
            "div#newsct_article",
            "div.newsct_article",
            "div#newsEndContents",
            "article#dic_area",
            "div#articleBodyContents",
            "div#content",
            "meta[property=og:description]"
    );

    public ArticleContent crawl(String url) {

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Connection", "keep-alive")
                    .referrer("https://www.google.com")
                    .ignoreHttpErrors(true)
                    .timeout(20000)
                    .get();

            // -------------------
            // 1) 제목 탐색
            // -------------------
            String title = extractFirstMatched(doc, TITLE_SELECTORS);
            if (title.isBlank()) {
                throw new CustomException(ErrorCode.INVALID_ARTICLE_STRUCTURE);
            }

            // -------------------
            // 2) 본문 탐색
            // -------------------
            String content = extractFirstMatched(doc, CONTENT_SELECTORS);
            if (content.isBlank()) {
                throw new CustomException(ErrorCode.INVALID_ARTICLE_STRUCTURE);
            }

            // 너무 짧으면 단신 → 요약 불가한 경우 skip
            if (content.length() < 20) {
                log.warn("본문이 너무 짧음 → Skip: {}", url);
                content += " "; // 최소 길이 확보
            }

            // -------------------
            // 3) 언론사
            // -------------------
            String press = doc.select("img.media_end_head_top_logo_img").attr("alt");
            if (press.isBlank()) {
                press = doc.select("meta[property=og:article:author]").attr("content");
            }
            if (press.isBlank()) {
                press = "네이버 뉴스";
            }

            // -------------------
            // 4) 날짜
            // -------------------
            LocalDateTime publishedAt = null;
            String dateText = doc.select("span.media_end_head_info_datestamp_time")
                    .attr("data-date-time");

            if (!dateText.isBlank()) {
                publishedAt = LocalDateTime.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }

            // -------------------
            // 5) DTO 반환
            // -------------------
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
            log.error("Naver crawling error:", e);
            throw new CustomException(ErrorCode.CRAWLING_FAILED);
        }
    }

    // 공통 fallback selector 처리
    private String extractFirstMatched(Document doc, List<String> selectors) {
        for (String selector : selectors) {
            String text = doc.select(selector).text();
            if (!text.isBlank()) return text;
        }
        return "";
    }
}
