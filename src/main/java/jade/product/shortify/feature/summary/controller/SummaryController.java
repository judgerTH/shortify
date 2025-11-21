package jade.product.shortify.feature.summary.controller;

import jade.product.shortify.feature.summary.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/llm")
public class SummaryController {

    private final SummaryService llmService;

    @PostMapping(value = "/summary")
    public String summarize(@RequestBody String content) throws Exception {
        return llmService.summarize(content);
    }

}
