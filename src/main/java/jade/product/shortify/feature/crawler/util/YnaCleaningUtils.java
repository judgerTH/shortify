package jade.product.shortify.feature.crawler.util;

public class YnaCleaningUtils {

    public static String cleanYnaContent(String content) {

        if (content == null) return null;

        String cleaned = content;

        // 1) 기자/구독/유튜브/광고/이미지 등 불필요 요소 제거
        cleaned = cleaned.replaceAll("구독.*?다음", "");
        cleaned = cleaned.replaceAll("youtube.*?보기", "");
        cleaned = cleaned.replaceAll("이미지 확대", "");
        cleaned = cleaned.replaceAll("제보는 카카오톡.*", "");
        cleaned = cleaned.replaceAll("영상:.*", "");

        // 2) 저작권 문구 제거
        cleaned = cleaned.replaceAll("<저작권자.*?>", "");

        // 3) '세 줄 요약' 안내문 제거
        cleaned = cleaned.replaceAll("인공지능이 자동으로 줄인 '세 줄 요약' 기술을 사용합니다\\.?","");
        cleaned = cleaned.replaceAll("전체 내용을 이해하기 위해서는 기사 본문과 함께 읽어야 합니다\\.?","");

        // 4) 송고시간 제거
        cleaned = cleaned.replaceAll("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2} 송고", "");
        cleaned = cleaned.replaceAll("\\d{4}년\\d{1,2}월\\d{1,2}일 \\d{1,2}시\\d{1,2}분 송고", "");

        // 5) 광고 및 추천 콘텐츠 제거
        cleaned = cleaned.replaceAll("함께 보면 좋은 콘텐츠.*", "");
        cleaned = cleaned.replaceAll("Taboola.*", "");
        cleaned = cleaned.replaceAll("광고.*", "");

        // 6) 본문 중복 제거 — 상단 요약 제거
        int idx = cleaned.indexOf("(서울=연합뉴스)");
        if (idx > 0) {
            cleaned = cleaned.substring(idx);  // 실제 본문 시작 부분만 남김
        }

        // 7) 공백 정리
        cleaned = cleaned.replaceAll("\\s{2,}", " ").trim();

        return cleaned;
    }
}
