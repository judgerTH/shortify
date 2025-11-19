package jade.product.shortify.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 요청 데이터"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다"),

    // Crawler
    CRAWLING_FAILED(HttpStatus.BAD_REQUEST, "크롤링에 실패했습니다"),
    INVALID_ARTICLE_STRUCTURE(HttpStatus.BAD_REQUEST, "기사 구조 파싱 실패"),

    // Summary
    SUMMARY_FAILED(HttpStatus.BAD_REQUEST, "요약 생성 실패"),

    // Image
    IMAGE_SEARCH_FAILED(HttpStatus.BAD_REQUEST, "이미지 검색 실패");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
}