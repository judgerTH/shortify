package jade.product.shortify.feature.digest.thumbnail;

import org.springframework.stereotype.Component;

import java.awt.Color;

@Component
public class ThumbnailColorStrategy {

    /**
     * 긴장도 / 긍정도 / 안정도에 따라 배경색 결정
     */
    public Color pickBackgroundColor(int tension, int positivity, int stability) {
        // 가장 큰 값 기준으로 단순 결정 (원하면 더 복잡하게 바꿔도 됨)
        if (tension >= positivity && tension >= stability) {
            // 긴장도 우세 → 붉은 계열
            return new Color(200, 60, 60);
        }
        if (positivity >= tension && positivity >= stability) {
            // 긍정도 우세 → 초록 계열
            return new Color(60, 170, 100);
        }
        // 안정도 우세 → 파란 계열
        return new Color(60, 110, 200);
    }
}
