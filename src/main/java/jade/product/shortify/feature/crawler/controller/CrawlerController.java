package jade.product.shortify.feature.crawler.controller;

import jade.product.shortify.feature.crawler.dto.ArticleContent;
import jade.product.shortify.feature.crawler.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crawler")
public class CrawlerController {

    private final CrawlerService crawlerService;

    @PostMapping
    public ArticleContent crawl(@RequestParam String url) {
        return crawlerService.crawlUrl(url);
    }

}
