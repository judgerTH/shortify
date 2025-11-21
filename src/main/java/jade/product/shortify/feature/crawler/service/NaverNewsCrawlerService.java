package jade.product.shortify.feature.crawler.service;

import jade.product.shortify.domain.article.entity.OriginalArticle;
import jade.product.shortify.domain.article.repository.OriginalArticleRepository;
import jade.product.shortify.global.exception.CustomException;
import jade.product.shortify.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// n.news 기사 페이지 크롤링
@Service
@RequiredArgsConstructor
public class NaverNewsCrawlerService {

    private final OriginalArticleRepository originalArticleRepository;

    public OriginalArticle crawl(String url) {

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            // 제목
            String title = doc.select("h2.media_end_head_headline").text();

            // 본문
            String content = doc.select("#dic_area").text();

            // 언론사
            String press = doc.select(".media_end_head_top_logo img").attr("alt");
            if (press.isBlank()) press = "네이버뉴스";

            // 발행일
            String publishedStr = doc.select("span.media_end_head_info_datestamp_time")
                    .attr("data-date-time");
            LocalDateTime publishedAt = null;

            if (!publishedStr.isBlank()) {
                publishedAt = LocalDateTime.parse(
                        publishedStr.substring(0, 19),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                );
            }

            OriginalArticle article = OriginalArticle.builder()
                    .title(title)
                    .content(content)
                    .press(press)
                    .url(url)
                    .publishedAt(publishedAt)
                    .build();

            return originalArticleRepository.save(article);

        } catch (Exception e) {
            throw new CustomException(ErrorCode.CRAWLING_FAILED);
        }
    }
}
