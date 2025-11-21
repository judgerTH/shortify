package jade.product.shortify.application.sample;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/llm")
public class LLMController {

    private final LLMService llmService;

    @PostMapping(value = "/summary")
    public String summarize(@RequestBody String content) throws Exception {
        return llmService.summarize(content);
    }

}
