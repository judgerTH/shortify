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

    public OriginalArticle crawlUrl(String url) {
        if (url.contains("yna.co.kr")) {
            return ynaCrawlerService.crawlUrl(url);
        }

        throw new CustomException(ErrorCode.UNSUPPORTED_NEWS_DOMAIN);
    }
}
