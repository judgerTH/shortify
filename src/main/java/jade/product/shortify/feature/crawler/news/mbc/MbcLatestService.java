package jade.product.shortify.feature.crawler.news.mbc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MbcLatestService {

    private static final String MBC_LATEST =
            "https://imnews.imbc.com/news/2025/politic/";

    public List<String> fetchLatestArticles() {
        try {
            Document doc = Jsoup.connect(MBC_LATEST)
                    .userAgent("Mozilla/5.0")
                    .timeout(10_000)
                    .get();

            return doc.select("a[href*=article]")
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
