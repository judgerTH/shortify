package jade.product.shortify.feature.digest;

import jade.product.shortify.domain.article.entity.ArticleSummary;
import jade.product.shortify.domain.newsInsight.entity.NewsInsight;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DigestFormatter {

    public String buildHtml(NewsInsight insight, List<ArticleSummary> news, String thumbnailUrl) {

        StringBuilder sb = new StringBuilder();

        if (thumbnailUrl != null && !thumbnailUrl.isBlank()) {
            sb.append("<div style='margin-bottom:20px;text-align:center;'>")
                    .append("<img src=\"")
                    .append(thumbnailUrl)
                    .append("\" style=\"max-width:100%;height:auto;\" />")
                    .append("</div>")
                    .append("<hr/>");
        }

        // --- 사회 분위기 분석 ---
        sb.append("<h2>오늘의 사회 분위기 분석</h2>");
        sb.append("<p>긴장도: ").append(insight.getTension()).append("</p>");
        sb.append("<p>긍정도: ").append(insight.getPositivity()).append("</p>");
        sb.append("<p>안정도: ").append(insight.getStability()).append("</p>");
        sb.append("<p><b>요약:</b> ").append(insight.getSummary()).append("</p>");
        sb.append("<hr/>");

        // --- 뉴스 목록 ---
        sb.append("<h2>오늘의 주요 뉴스 10선</h2>");

        int i = 1;
        for (ArticleSummary s : news) {

            sb.append("<div style='margin-bottom:20px;'>");

            // 제목 + URL 링크
            sb.append("<h3>")
                    .append(i++)
                    .append(") <a href=\"")
                    .append(s.getArticleMeta().getUrl())
                    .append("\" target=\"_blank\">")
                    .append(s.getSummaryTitle())
                    .append("</a></h3>");

            // 요약 내용
            sb.append("<p>")
                    .append(s.getSummaryContent())
                    .append("</p>");

            sb.append("</div>");
        }

        return sb.toString();
    }
}
