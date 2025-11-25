package jade.product.shortify.feature.insight.controller;

import jade.product.shortify.feature.insight.dto.NewsInsightResponse;
import jade.product.shortify.feature.insight.service.NewsInsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/insight")
public class NewsInsightController {

    private final NewsInsightService insightService;

    @PostMapping("/generate")
    public NewsInsightResponse generate() throws Exception {
        return insightService.generateInsight();
    }
}
