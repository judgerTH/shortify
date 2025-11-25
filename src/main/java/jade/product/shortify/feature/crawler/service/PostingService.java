package jade.product.shortify.feature.crawler.service;

import jade.product.shortify.domain.article.entity.ArticleMeta;
import jade.product.shortify.domain.article.entity.ArticleSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostingService {

    public void post(ArticleMeta meta, ArticleSummary summary) {

        // ❗ 여기에 Naver Blog / Tistory / YouTube Shorts 연동 넣을 예정
        // 지금은 로그만
        log.info("[POSTING] {} | {}", meta.getTitle(), summary.getSummaryTitle());

    }
}
