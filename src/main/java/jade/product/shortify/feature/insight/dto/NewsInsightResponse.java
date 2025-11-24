package jade.product.shortify.feature.insight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewsInsightResponse {
    private int tension;    // 긴장도 (0~100)
    private int positivity; // 긍정도 (0~100)
    private int stability;  // 사회 안정 지수 (0~100)
    private String summary; // 오늘의 한줄 요약
}
