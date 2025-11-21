package jade.product.shortify.feature.crawler.util;

public class MbcCleaningUtils {

    public static String clean(String raw) {
        if (raw == null) return "";

        String text = raw;

        // ============================
        // 1) UI / 메뉴 문구 제거
        // ============================
        text = text.replaceAll("본문 바로가기", "");
        text = text.replaceAll("메뉴 바로가기", "");
        text = text.replaceAll("상세 기사보기", "");

        // 북마크, 공유
        text = text.replaceAll("해당 기사를 북마크했습니다\\.", "");
        text = text.replaceAll("내 북마크 보기", "");
        text = text.replaceAll("페이스북|트위터|카카오 스토리|카카오톡|밴드|링크 복사", "");

        // ============================
        // 2) 방송 뉴스 공통 문구 제거
        // ============================
        text = text.replaceAll("◀ *앵커 *▶", "");
        text = text.replaceAll("◀ *리포트 *▶", "");
        text = text.replaceAll("Previous", "");
        text = text.replaceAll("Next", "");
        text = text.replaceAll("레이어", "");

        // ============================
        // 3) 날짜 / 입력 / 수정 정보 제거
        // ============================
        text = text.replaceAll("입력 \\d{4}\\.\\d{2}\\.\\d{2}.*?\\)", "");
        text = text.replaceAll("수정 \\d{4}\\.\\d{2}\\.\\d{2}.*?\\)", "");

        // ============================
        // 4) 제보 관련 문구 제거
        // ============================
        text = text.replaceAll("MBC뉴스.*?\\.", "");
        text = text.replaceAll("MBC 뉴스는.*?기다립니다\\.", "");
        text = text.replaceAll("전화 \\d{2}-\\d{3,4}-\\d{4}", "");
        text = text.replaceAll("이메일 mbcjebo@mbc.co.kr", "");
        text = text.replaceAll("카카오톡 @mbc제보", "");

        // ============================
        // 5) 댓글/의견 입력 UI 제거
        // ============================
        text = text.replaceAll("이 기사 어땠나요\\?", "");
        text = text.replaceAll("좋아요|훌륭해요|슬퍼요|화나요|후속요청", "");
        text = text.replaceAll("당신의 의견을 남겨주세요.*?등록", "");
        text = text.replaceAll("0/300", "");
        text = text.replaceAll("가 가 가", "");

        // ============================
        // 6) 기타 불필요한 정보 제거
        // ============================
        text = text.replaceAll("이시각 주요뉴스", "");
        text = text.replaceAll("많이 본 뉴스", "");
        text = text.replaceAll("관련 뉴스", "");
        text = text.replaceAll("분야별 추천 뉴스", "");
        text = text.replaceAll("취재플러스", "");
        text = text.replaceAll("엠빅뉴스", "");

        // ============================
        // 7) 반복 공백 정리
        // ============================
        text = text.replaceAll("\\s{2,}", " ").trim();

        return text;
    }
}
