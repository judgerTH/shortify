package jade.product.shortify.feature.digest.thumbnail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ThumbnailUploader {

    private final S3Client r2Client;

    @Value("${r2.bucket}")
    private String bucket;

    @Value("${shortify.cdn-base-url}")
    private String cdnBaseUrl; // cdn.welcome-jade.site

    /**
     * R2로 업로드 후 CDN URL 반환
     */
    public String upload(File file) throws Exception {

        // 파일명 유니크하게 생성
        String fileName = "thumbnail/" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                + ".png";

        byte[] fileBytes = Files.readAllBytes(file.toPath());

        // 업로드 요청
        r2Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileName)
                        .contentType("image/png")
                        .build(),
                RequestBody.fromBytes(fileBytes)
        );

        // 최종 접근 URL
        return cdnBaseUrl + "/" + fileName;
    }
}
