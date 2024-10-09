package itf.itffourteen.imageprocessing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Component;

@Component
public class BarcodeGenerator {
    private static final int IMAGE_WIDTH = 300;
    private static final int IMAGE_HEIGHT = 140;
    private static final int BARCODE_HEIGHT = 100;
    private static final int BAR_WIDTH = 2;
    private static final int THICK_BAR_WIDTH = BAR_WIDTH * 2;
    private static final int BORDER_THICKNESS = THICK_BAR_WIDTH * 2;
    private static final String[] PATTERNS = {
            "00110", "10001", "01001", "11000", "00101",
            "10100", "01100", "00011", "10010", "01010"
    };
    private static final String START_PATTERN = "00";
    private static final String STOP_PATTERN = "10";
    private static final String INVERTED_STOP_PATTERN = "00";
    private static final String FONT = "Arial";
    private static final int FONT_STYLE = Font.PLAIN;
    private static final int FONT_SIZE = 16;
    private static final int LABEL_X_COORDINATE = IMAGE_WIDTH / 24 * 7;
    private static final int LABEL_Y_COORDINATE = (IMAGE_HEIGHT + BARCODE_HEIGHT) / 2 + 13;

    private int xCoordinate = IMAGE_WIDTH / 84 * 15;
    private int yCoordinate = BORDER_THICKNESS;

    public BufferedImage generateBarcodeImage(String barcode) {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(BORDER_THICKNESS));
        g.drawRect(BORDER_THICKNESS / 2,BORDER_THICKNESS / 2,
                IMAGE_WIDTH - BORDER_THICKNESS, BARCODE_HEIGHT + BORDER_THICKNESS);

        StringBuilder oddPositionNumbers = new StringBuilder();
        StringBuilder evenPositionNumbers = new StringBuilder();
        for (int i = 0; i < barcode.length(); i++) {
            (i % 2 == 1 ? oddPositionNumbers : evenPositionNumbers)
                    .append(barcode.toCharArray()[i]);
        }

        String evenPositionPatternedNumbers = START_PATTERN
                + convertNumericStringToPatternString(evenPositionNumbers.toString())
                + STOP_PATTERN;
        String oddPositionPatternedNumbers = START_PATTERN
                + convertNumericStringToPatternString(oddPositionNumbers.toString())
                + INVERTED_STOP_PATTERN;

        for (int i = 0; i < evenPositionPatternedNumbers.length(); i++) {
            g.setColor(Color.BLACK);
            xCoordinate = drawBar(g, evenPositionPatternedNumbers.toCharArray()[i], xCoordinate);
            g.setColor(Color.WHITE);
            xCoordinate = drawBar(g, oddPositionPatternedNumbers.toCharArray()[i], xCoordinate);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font(FONT, FONT_STYLE, FONT_SIZE));
        g.drawString(barcode, LABEL_X_COORDINATE, LABEL_Y_COORDINATE);

        g.dispose();

        xCoordinate = IMAGE_WIDTH / 84 * 15;
        yCoordinate = BORDER_THICKNESS;

        return image;
    }

    private String convertNumericStringToPatternString(String numericString) {
        StringBuilder patternString = new StringBuilder();
        for (char number : numericString.toCharArray()) {
            patternString.append(PATTERNS[Character.getNumericValue(number)]);
        }
        return patternString.toString();
    }

    private int drawBar(Graphics2D g, char bit, int x) {
        if (bit == '1') {
            g.fillRect(x, yCoordinate, THICK_BAR_WIDTH, BARCODE_HEIGHT);
            x += THICK_BAR_WIDTH;
        } else {
            g.fillRect(x, yCoordinate, BAR_WIDTH, BARCODE_HEIGHT);
            x += BAR_WIDTH;
        }
        return x;
    }
}
