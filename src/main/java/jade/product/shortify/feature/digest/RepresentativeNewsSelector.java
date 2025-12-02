package jade.product.shortify.feature.digest;

import jade.product.shortify.domain.article.entity.ArticleSummary;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RepresentativeNewsSelector {

    public List<ArticleSummary> selectTop10(List<ArticleSummary> summaries) {

        // 1) 키워드 분리
        Map<ArticleSummary, List<String>> map = summaries.stream()
                .collect(Collectors.toMap(
                        s -> s,
                        s -> Arrays.stream(s.getKeywords().split(","))
                                .map(String::trim)
                                .map(String::toLowerCase)
                                .toList()
                ));

        // 2) 중복 키워드가 많은 기사끼리 묶기
        List<ArticleSummary> result = new ArrayList<>();
        Set<ArticleSummary> used = new HashSet<>();

        for (ArticleSummary cur : summaries) {
            if (used.contains(cur)) continue;

            List<String> kw1 = map.get(cur);

            // 대표 기사로 선정
            result.add(cur);
            used.add(cur);

            // 중복 제거
            for (ArticleSummary other : summaries) {
                if (used.contains(other)) continue;
                List<String> kw2 = map.get(other);

                long overlap = kw1.stream().filter(kw2::contains).count();
                if (overlap >= 2) { // 키워드 2개 이상 같으면 중복 기사로 간주
                    used.add(other);
                }
            }

            if (result.size() == 10) break;
        }

        return result;
    }
}
