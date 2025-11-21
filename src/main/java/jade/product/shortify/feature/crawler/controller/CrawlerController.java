package jade.product.shortify.feature.crawler.controller;

import jade.product.shortify.domain.article.entity.OriginalArticle;
import jade.product.shortify.feature.crawler.service.YnaCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crawler")
public class CrawlerController {

    private final YnaCrawlerService crawlerService;

    @PostMapping
    public OriginalArticle crawl(@RequestParam String url) {
        return crawlerService.crawlUrl(url);
    }
}
