package jade.product.shortify.feature.crawler.service;

import jade.product.shortify.domain.article.entity.OriginalArticle;
import jade.product.shortify.global.exception.CustomException;
import jade.product.shortify.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final YnaCrawlerService ynaCrawlerService;
    private final KbsCrawlerService kbsCrawlerService;
    private final MbcCrawlerService mbcCrawlerService;
    private final NaverCrawlerService naverCrawlerService;

    public OriginalArticle crawlUrl(String url) {
        if (url.contains("yna.co.kr")) {
            return ynaCrawlerService.crawlUrl(url);
        }
        if (url.contains("news.kbs.co.kr")) {
            return kbsCrawlerService.crawl(url);
        }
        if (url.contains("mbc.co.kr") || url.contains("imnews.imbc.com")) {
            return mbcCrawlerService.crawlUrl(url);
        }
        if (url.contains("n.news.naver.com") || url.contains("news.naver.com")) {
            return naverCrawlerService.crawl(url);
        }

        throw new CustomException(ErrorCode.UNSUPPORTED_NEWS_DOMAIN);
    }
}
