package itf.itf14.imageProcessing;

import org.springframework.stereotype.Component;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

@Component
public class ImageGenerator {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 120;
    private static final int BAR_WIDTH = 2;
    private static final String FONT = "Arial";

    public BufferedImage generateBarcodeImage(String barcode) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Фон білий
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Малюємо штрихкод (чорні смуги)
        g.setColor(Color.BLACK);
        int x = 10;
        for (int i = 0; i < barcode.length(); i++) {
            int digit = Character.getNumericValue(barcode.charAt(i));
            String binary = getITF14BinaryRepresentation(digit);
            for (char bit : binary.toCharArray()) {
                if (bit == '1') {
                    g.fillRect(x, 10, BAR_WIDTH, 100);
                }
                x += BAR_WIDTH;
            }
        }

        // Додаємо текст під штрихкодом
        g.setColor(Color.BLACK);
        g.setFont(new Font(FONT, Font.PLAIN, 16));
        g.drawString(barcode, 10, 135);

        g.dispose();
        return image;
    }

    private String getITF14BinaryRepresentation(int digit) {
        // Кожна цифра має відповідати певному двійковому коду за таблицею
        String[] patterns = {
                "00110", "10001", "01001", "11000", "00101",
                "10100", "01100", "00011", "10010", "01010"
        };
        return patterns[digit];
    }
}
