package jade.product.shortify.feature.crawler.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.List;

// 언론사별 최신 기사 URL 수집
@Service
public class NaverPressLatestService {

    private static final String NAVER_PRESS_BASE = "https://media.naver.com/press/";

    public List<String> fetchLatestArticles(String pressCode) {
        try {
            String url = NAVER_PRESS_BASE + pressCode;
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .get();

            return doc.select("a[href*='n.news.naver.com/article']")
                    .eachAttr("href");

        } catch (Exception e) {
            return List.of();
        }
    }
}
