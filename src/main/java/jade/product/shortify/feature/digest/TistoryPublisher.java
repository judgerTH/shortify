package jade.product.shortify.feature.digest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TistoryPublisher {

    private final TistoryApiClient apiClient;

    public void post(String title, String html) throws Exception {
        apiClient.publish(title, html);
    }

}
