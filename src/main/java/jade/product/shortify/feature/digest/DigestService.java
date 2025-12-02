package jade.product.shortify.feature.digest;

import jade.product.shortify.domain.article.entity.ArticleSummary;
import jade.product.shortify.domain.article.repository.ArticleSummaryRepository;
import jade.product.shortify.domain.newsInsight.entity.NewsInsight;
import jade.product.shortify.domain.newsInsight.repository.NewsInsightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DigestService {

    private final ArticleSummaryRepository summaryRepo;
    private final NewsInsightRepository insightRepo;
    private final RepresentativeNewsSelector selector;
    private final DigestFormatter formatter;
    private final TistoryPublisher tistoryPublisher;

    public void publishTodayDigest() throws Exception {

        LocalDate today = LocalDate.now();

        // 1) 오늘 요약 기사 30개 가져오기
        List<ArticleSummary> summaries = summaryRepo.findTop30WithMeta(
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );


        // 2) 대표 10개 선정
        // 일단 1개만..
        List<ArticleSummary> top10 = selector.selectTop10(summaries).stream()
                .limit(10)
                .toList();


        // 3) 오늘 사회 분위기 마지막 1개
        NewsInsight insight = insightRepo.findFirstByOrderByCreatedAtDesc()
                .orElseThrow();

        // 4) HTML 생성
        String html = formatter.buildHtml(insight, top10);

        // 5) 티스토리에 업로드
        tistoryPublisher.post(html);
    }
}
