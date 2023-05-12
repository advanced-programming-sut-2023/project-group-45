package stronghold.view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Data;

@Data
public class CaptchaGenerator {

    private static final int MIN_DIGITS = 4;
    private static final int MAX_DIGITS = 8;
    private static final int IMAGE_WIDTH = 180;
    private static final int IMAGE_HEIGHT = 32;
    private static final int FONT_SIZE = 22;
    private static final int FONT_START = 5;
    private static final double NOISE_PROBABILITY = 0.15;

    private final int answer;
    private final List<String> rows;

    CaptchaGenerator() {
        this(generateAnswer());
    }

    CaptchaGenerator(int answer) {
        this.answer = answer;
        String text = Integer.toString(answer).replace("", " ").trim();
        this.rows = imageToString(generateImage(text));
        addNoise(this.rows);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String row : rows) {
            sb.append(row).append('\n');
        }
        return sb.toString();
    }

    public static int generateAnswer() {
        Random random = new Random();
        int n = random.nextInt(MIN_DIGITS, MAX_DIGITS + 1);
        int answer = 0;
        for (int i = 0; i < n; i++) {
            int digit = random.nextInt((i == 0 ? 1 : 0), 9);
            answer = 10 * answer + digit;
        }
        return answer;
    }

    public static BufferedImage generateImage(String text) {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("Comic Sans MS", Font.ITALIC, FONT_SIZE));
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString(text, FONT_START, FONT_SIZE);
        return image;
    }

    public static List<String> imageToString(BufferedImage image) {
        List<String> rows = new ArrayList<>();
        for (int y = 0; y < image.getHeight(); y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < image.getWidth(); x++) {
                sb.append(image.getRGB(x, y) == -16777216 ? " "
                        : image.getRGB(x, y) == -1 ? "#" : "*");
            }
            String row = sb.toString().replaceAll(" *$", "");
            if (row.isEmpty()) {
                continue;
            }
            rows.add(row);
        }
        return rows;
    }

    public static void addNoise(List<String> rows) {
        Random random = new Random();
        for (int y = 0; y < rows.size(); y++) {
            StringBuilder sb = new StringBuilder(rows.get(y));
            for (int x = 0; x < sb.length(); x++) {
                if (random.nextDouble() < NOISE_PROBABILITY) {
                    sb.setCharAt(x, '&');
                }
            }
            rows.set(y, sb.toString());
        }
    }

}
