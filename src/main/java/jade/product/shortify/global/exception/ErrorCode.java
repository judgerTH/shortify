package jade.product.shortify.global.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import lombok.Getter;

@Schema(description = "API 에러 코드 목록")
@Getter
public enum ErrorCode {

    @Schema(description = "서버 내부 오류")
    INTERNAL_SERVER_ERROR("E500", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류"),

    @Schema(description = "잘못된 요청 데이터")
    INVALID_INPUT_VALUE("E400", HttpStatus.BAD_REQUEST, "잘못된 요청 데이터"),

    @Schema(description = "리소스 찾을 수 없음")
    NOT_FOUND("E404", HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다"),

    // Crawler
    @Schema(description = "크롤링 실패")
    CRAWLING_FAILED("C001", HttpStatus.BAD_REQUEST, "크롤링에 실패했습니다"),

    @Schema(description = "기사 구조 파싱 실패")
    INVALID_ARTICLE_STRUCTURE("C002", HttpStatus.BAD_REQUEST, "기사 구조 파싱 실패"),

    @Schema(description = "지원하지 않는 언론사 URL")
    UNSUPPORTED_NEWS_DOMAIN("C003", HttpStatus.BAD_REQUEST, "지원하지 않는 언론사 URL"),

    // Summary
    @Schema(description = "요약 생성 실패")
    SUMMARY_FAILED("S001", HttpStatus.BAD_REQUEST, "요약 생성 실패"),

    // Image
    @Schema(description = "이미지 검색 실패")
    IMAGE_SEARCH_FAILED("I001", HttpStatus.BAD_REQUEST, "이미지 검색 실패"),

    // DB
    @Schema(description = "DB 연결 실패")
    DB_CONNECTION_FAILED("DB001", HttpStatus.INTERNAL_SERVER_ERROR, "DB 연결 실패");

    private final String code;
    private final HttpStatus status;
    private final String message;

    ErrorCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

}