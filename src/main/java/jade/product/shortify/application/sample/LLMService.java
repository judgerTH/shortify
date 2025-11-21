package jade.product.shortify.application.sample;

import jade.product.shortify.global.llm.GeminiClient;
import jade.product.shortify.global.llm.NewsPromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LLMService {

    private final GeminiClient geminiClient;
    private final NewsPromptBuilder promptBuilder;

    public String summarize(String newsContent) throws Exception {
        String prompt = promptBuilder.build(newsContent);
        return geminiClient.generate(prompt);
    }
}
