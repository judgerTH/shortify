package jade.product.shortify.feature.crawler.news.naver;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 언론사 코드 → 최신 기사 URL 한 개씩
    public Map<String, String> fetchLatestAllPress() {
        // { pressId: url }
        Map<String, String> map = new HashMap<>();

        for (String pressId : getAllPressIds()) {
            try {
                String url = fetchLatestArticles(pressId).stream()
                        .findFirst()
                        .orElse(null);

                if (url != null) {
                    map.put(pressId, url);
                }
            } catch (Exception ignore) {}
        }

        return map;
    }

    // 네이버에 등록된 주요 언론사 코드 전체 목록
    public List<String> getAllPressIds() {
        return List.of(
                "056", // KBS
                "214", // MBC
                "001", // YNA
                "005", // 국민일보
                "011", // 서울경제
                "015", // 한국경제
                "020", // 동아일보
                "021", // 문화일보
                "022", // 세계일보
                "023", // 조선일보
                "025", // 중앙일보
                "028", // 한겨레
                "032", // 경향신문
                "081", // 서울신문
                "417", // 머니S
                "421", // 뉴스1
                "003", // 뉴시스
                "448", // SBS
                "088", // 매일신문
                "079", // 노컷뉴스
                "119", // 데일리안
                "310", // 여성신문
                "138", // 디지털데일리
                "293", // 블로터
                "277", // 아시아경제
                "009", // 매일경제
                "014", // 파이낸셜뉴스
                "469", // 한국일보
                "006", // 미디어오늘
                "018", // 이데일리
                "366"  // 조선비즈
                // 필요하면 더 추가 가능
        );
    }

}
