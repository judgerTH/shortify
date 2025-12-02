package jade.product.shortify.global.config;

import jade.product.shortify.domain.article.repository.OriginalArticleRepository;
import jade.product.shortify.feature.crawler.news.kbs.KbsLatestService;
import jade.product.shortify.feature.crawler.news.mbc.MbcLatestService;
import jade.product.shortify.feature.crawler.news.yna.YnaLatestService;
import jade.product.shortify.feature.crawler.news.naver.NaverPressLatestService;
import jade.product.shortify.feature.crawler.service.PipelineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduleConfig {

    private final KbsLatestService kbsLatestService;
    private final MbcLatestService mbcLatestService;
    private final YnaLatestService ynaLatestService;
    private final NaverPressLatestService naverLatestService;

    private final PipelineService pipelineService;

    private final OriginalArticleRepository originalRepo;

//    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        runBatch();
    }

    public void scheduled() {
        runBatch();
    }

    private void runBatch() {

        // ============================
        // 1) 직접 만든 크롤러 3개씩
        // ============================
        List<String> urls = new ArrayList<>();
        // ============================
        // 2) 네이버 전체 언론사 중 각 언론사 1개씩
        // ============================
        Map<String, String> pressMap = naverLatestService.fetchLatestAllPress();

        List<String> others = pressMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .limit(31) // 너무 많지 않게 제한
                .toList();

        urls.addAll(others);

        log.info("Total collected URLs = {}", urls.size());

        // ============================
        // 2) 최종 파이프라인 실행
        // ============================
        urls.forEach(url -> {
            try {
                if (originalRepo.existsByUrl(url)) {
                    log.info("SKIP (already exists): {}", url);
                    return;
                }
                Thread.sleep(2000); // Rate Limit 보호
                pipelineService.process(url);
            } catch (Exception e) {
                log.error("ERROR processing {}", url, e);
            }
        });
    }
}
