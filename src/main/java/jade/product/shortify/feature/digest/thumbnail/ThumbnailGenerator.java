package jade.product.shortify.feature.digest.thumbnail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@Component
@RequiredArgsConstructor
public class ThumbnailGenerator {

    private final ThumbnailColorStrategy colorStrategy;

    private Font loadFont(String path, float size) throws Exception {
        try (var is = getClass().getResourceAsStream(path)) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(size);
        }
    }

    public File generate(String dateLabel, int tension, int positivity, int stability) throws Exception {

        // Fonts
        Font titleFont = loadFont("/fonts/Pretendard-Bold.ttf", 56f);
        Font subtitleFont = loadFont("/fonts/Pretendard-Medium.ttf", 30f);
        Font labelFont = loadFont("/fonts/Pretendard-Regular.ttf", 28f);
        Font valueFont = loadFont("/fonts/Pretendard-Bold.ttf", 30f);

        int width = 900;
        int height = 500;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        // 고품질 렌더링
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 배경 그라디언트
        Color base = colorStrategy.pickBackgroundColor(tension, positivity, stability);
        GradientPaint gradient = new GradientPaint(0, 0, base.brighter(), width, height, base.darker());
        g.setPaint(gradient);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.WHITE);

        int x = 80;
        int y = 100;

        // ===== 제목 =====
        g.setFont(titleFont);
        g.drawString(dateLabel, x, y);

        // ===== 부제목 =====
        y += 55;
        g.setFont(subtitleFont);
        g.drawString("오늘의 사회 분위기 지표", x, y);

        // ===== 구분선 =====
        y += 25;
        g.setColor(new Color(255, 255, 255, 150));
        g.fillRect(x, y, 520, 2);

        g.setColor(Color.WHITE);
        y += 50;

        // ===== 감정값 3종 출력 =====
        drawRow(g, labelFont, valueFont, "긴장도", tension, x, y);
        y += 70;

        drawRow(g, labelFont, valueFont, "긍정도", positivity, x, y);
        y += 70;

        drawRow(g, labelFont, valueFont, "안정도", stability, x, y);

        g.dispose();

        File output = File.createTempFile("shortify-thumbnail-", ".png");
        ImageIO.write(image, "png", output);
        return output;
    }

    private void drawRow(Graphics2D g, Font labelFont, Font valueFont,
                         String label, int value, int x, int y) {

        // • 라벨
        g.setFont(labelFont);
        g.drawString("• " + label, x, y);

        // 값 숫자 (일관된 오른쪽 정렬: x + 200 위치)
        g.setFont(valueFont);
        g.drawString(String.valueOf(value), x + 200, y);

        // 바 위치
        int barX = x;
        int barY = y + 15;
        int barW = 420;
        int barH = 12;

        // 바 배경
        g.setColor(new Color(255, 255, 255, 50));
        g.fillRoundRect(barX, barY, barW, barH, 10, 10);

        // 실제 값 반영
        int fill = Math.max(10, (int)(barW * (value / 100.0)));
        g.setColor(Color.WHITE);
        g.fillRoundRect(barX, barY, fill, barH, 10, 10);

        g.setColor(Color.WHITE);
    }
}
