package jade.product.shortify.feature.crawler.news.kbs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KbsLatestService {

    private static final String KBS_LATEST_URL =
            "https://news.kbs.co.kr/news/list.do?ref=pMenu";

    public List<String> fetchLatestArticles() {
        try {
            Document doc = Jsoup.connect(KBS_LATEST_URL)
                    .userAgent("Mozilla/5.0")
                    .timeout(10_000)
                    .get();

            return doc.select("a[href*=view.do]")
                    .eachAttr("abs:href")        // 절대 URL 변환
                    .stream()
                    .distinct()
                    .limit(5)
                    .toList();

        } catch (Exception e) {
            return List.of();
        }
    }
}
