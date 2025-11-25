package jade.product.shortify.feature.crawler.service;

import jade.product.shortify.feature.crawler.dto.ArticleContent;
import jade.product.shortify.feature.crawler.news.kbs.KbsCrawlerService;
import jade.product.shortify.feature.crawler.news.mbc.MbcCrawlerService;
import jade.product.shortify.feature.crawler.news.naver.NaverCrawlerService;
import jade.product.shortify.feature.crawler.news.yna.YnaCrawlerService;
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

    public ArticleContent crawlUrl(String url) {
        if (url.contains("yna.co.kr")) {
            return ynaCrawlerService.crawl(url);
        }
        if (url.contains("news.kbs.co.kr")) {
            return kbsCrawlerService.crawl(url);
        }
        if (url.contains("mbc.co.kr") || url.contains("imnews.imbc.com")) {
            return mbcCrawlerService.crawl(url);
        }
        if (url.contains("n.news.naver.com") || url.contains("news.naver.com")) {
            return naverCrawlerService.crawl(url);
        }

        throw new CustomException(ErrorCode.UNSUPPORTED_NEWS_DOMAIN);
    }
}
