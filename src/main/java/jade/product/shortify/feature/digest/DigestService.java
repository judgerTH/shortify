package jade.product.shortify.feature.digest;

import jade.product.shortify.domain.article.entity.ArticleSummary;
import jade.product.shortify.domain.article.repository.ArticleSummaryRepository;
import jade.product.shortify.domain.newsInsight.entity.NewsInsight;
import jade.product.shortify.domain.newsInsight.repository.NewsInsightRepository;
import jade.product.shortify.feature.digest.thumbnail.ThumbnailGenerator;
import jade.product.shortify.feature.digest.thumbnail.ThumbnailUploader;
import jade.product.shortify.feature.insight.service.NewsInsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DigestService {

    private final ArticleSummaryRepository summaryRepo;
    private final NewsInsightRepository insightRepo;
    private final RepresentativeNewsSelector selector;
    private final DigestFormatter formatter;
    private final TistoryPublisher tistoryPublisher;
    private final NewsInsightService insightService;
    private final ThumbnailGenerator thumbnailGenerator;
    private final ThumbnailUploader thumbnailUploader;

    public void publishTodayDigest() throws Exception {
        insightService.generateInsight();

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

        // 날짜 및 제목 생성
        String date = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String title = String.format(
                "오늘의 소셜 다이제스트 - %s | 긴장도 %d · 긍정도 %d · 안정도 %d",
                date,
                insight.getTension(),
                insight.getPositivity(),
                insight.getStability()
        );

        // 4) HTML 생성
        // 1) 썸네일 생성
        File thumbnailFile = thumbnailGenerator.generate(
                date,
                insight.getTension(),
                insight.getPositivity(),
                insight.getStability()
        );

        // 2) 썸네일 업로드 → URL
        String thumbnailUrl = thumbnailUploader.upload(thumbnailFile);

        // 3) HTML 생성 (썸네일 포함)
        String html = formatter.buildHtml(insight, top10, thumbnailUrl);

        // 5) 티스토리에 업로드
        tistoryPublisher.post(title, html);
        System.out.println(html);
    }
}
