package jade.product.shortify.feature.insight.service;

import jade.product.shortify.domain.article.entity.ArticleSummary;
import jade.product.shortify.domain.article.repository.ArticleSummaryRepository;
import jade.product.shortify.domain.newsInsight.entity.NewsInsight;
import jade.product.shortify.domain.newsInsight.repository.NewsInsightRepository;
import jade.product.shortify.feature.insight.dto.NewsInsightResponse;
import jade.product.shortify.global.llm.GeminiClient;
import jade.product.shortify.global.llm.InsightParser;
import jade.product.shortify.global.llm.InsightPromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsInsightService {

    private final ArticleSummaryRepository summaryRepo;
    private final GeminiClient gemini;
    private final InsightPromptBuilder promptBuilder;
    private final NewsInsightRepository newsInsightRepo;
    public NewsInsightResponse generateInsight() throws Exception {

        // 1) 오늘 summary 30개 가져오기
        LocalDate today = LocalDate.now();
        Pageable pageable = (Pageable) PageRequest.of(0, 30, Sort.by("createdAt").descending());
        List<ArticleSummary> list = summaryRepo.findByCreatedAtBetween(
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay(),
                pageable
        ).getContent();


        // 2) 텍스트 합치기
        String combined = list.stream()
                .map(ArticleSummary::getSummaryContent)
                .collect(Collectors.joining("\n"));

        // 3) LLM 호출
        String prompt = promptBuilder.build(combined);
        String raw = gemini.generate(prompt);
        System.out.println("RAW RESPONSE =====");
        System.out.println(raw);
        System.out.println("=== Combined Summaries ===");
        System.out.println(combined);
        System.out.println("=== END ===");

        NewsInsightResponse insight = InsightParser.parse(raw);

        // 저장
        newsInsightRepo.save(
                NewsInsight.create(
                        insight.getTension(),
                        insight.getPositivity(),
                        insight.getStability(),
                        insight.getSummary()
                )
        );

        return insight;
    }
}
