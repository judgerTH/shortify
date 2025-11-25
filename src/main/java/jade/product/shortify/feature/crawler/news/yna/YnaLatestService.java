package jade.product.shortify.feature.crawler.news.yna;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YnaLatestService {

    private static final String YNA_LATEST =
            "https://www.yna.co.kr/news?site=navi_latest";

    public List<String> fetchLatestArticles() {
        try {
            Document doc = Jsoup.connect(YNA_LATEST)
                    .userAgent("Mozilla/5.0")
                    .timeout(10_000)
                    .get();

            return doc.select("a[href^=/view]")
                    .eachAttr("abs:href")
                    .stream()
                    .distinct()
                    .limit(5)
                    .toList();

        } catch (Exception e) {
            return List.of();
        }
    }
}
