package jade.product.shortify.feature.crawler.service;

import jade.product.shortify.domain.article.entity.*;
import jade.product.shortify.domain.article.repository.*;
import jade.product.shortify.feature.crawler.dto.ArticleContent;
import jade.product.shortify.feature.summary.dto.SummaryResult;
import jade.product.shortify.feature.summary.service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PipelineService {

    private final CrawlerService crawlerService;
    private final OriginalArticleRepository originalRepo;
    private final ArticleMetaRepository metaRepo;
    private final ArticleSummaryRepository summaryRepo;

    private final SummaryService summaryService;
    private final PostingService postingService;
    private final ArticleFailLogRepository failLogRepo;

    public void process(String url) throws Exception {

        log.info("=== PIPELINE START {}", url);

        // 1) 크롤링
        ArticleContent article;
        try {
            article = crawlerService.crawlUrl(url);
        } catch (Exception e) {
            logFail(url, "CRAWL", e.getMessage(), null);
            return;
        }

        // 2) 원문 저장
        var original = originalRepo.save(OriginalArticle.fromCrawler(
                article.getUrl(),
                article.getTitle(),
                article.getContent(),
                article.getPress(),
                article.getPublishedAt()
        ));

        // 3) meta 저장
        var meta = metaRepo.save(ArticleMeta.fromOriginal(original));

        // 4) LLM 요약
        String llmResponse;
        try {
            llmResponse = summaryService.summarize(article.getContent());
        } catch (Exception e) {
            logFail(url, "LLM_CALL", e.getMessage(), null);
            return;
        }

        Thread.sleep(1000);

        SummaryResult parsed = SummaryResult.fromGeminiResponse(llmResponse);

        if (parsed.getTitle() == null || parsed.getTitle().isBlank()) {
            logFail(url, "LLM_PARSE", "EMPTY_TITLE", llmResponse);
            return;
        }

        if (parsed.getContent() == null || parsed.getContent().isBlank()) {
            logFail(url, "LLM_PARSE", "EMPTY_CONTENT", llmResponse);
            return;
        }

        // 5) 요약 저장
        ArticleSummary summary = summaryRepo.save(
                ArticleSummary.create(parsed.getTitle(), parsed.getContent(), parsed.getKeywords(), "gemini-2.5-flash", meta)
        );

        // 6) 포스팅
        postingService.post(meta, summary);

        log.info("=== PIPELINE END {} ===", url);
    }

    private void logFail(String url, String step, String reason, String detail) {
        ArticleFailLog log = new ArticleFailLog();
        log.setUrl(url);
        log.setStep(step);
        log.setReason(reason);
        log.setDetail(detail);
        failLogRepo.save(log);
    }

}
