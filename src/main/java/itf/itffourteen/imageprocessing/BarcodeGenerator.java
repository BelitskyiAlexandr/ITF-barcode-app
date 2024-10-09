package itf.itffourteen.imageprocessing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.springframework.stereotype.Component;

@Component
public class BarcodeGenerator {
    private static final String[] PATTERNS = {
            "00110", "10001", "01001", "11000", "00101",
            "10100", "01100", "00011", "10010", "01010"
    };
    private static final String START_PATTERN = "00";
    private static final String STOP_PATTERN = "10";

    private int xCoordinate = ImageProperties.IMAGE_WIDTH / 84 * 15;
    private int yCoordinate = ImageProperties.BORDER_THICKNESS;

    public BufferedImage generateBarcodeImage(String barcode) {
        BufferedImage image = new BufferedImage(ImageProperties.IMAGE_WIDTH,
                ImageProperties.IMAGE_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0,
                ImageProperties.IMAGE_WIDTH, ImageProperties.IMAGE_HEIGHT);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(ImageProperties.BORDER_THICKNESS));
        g.drawRect(ImageProperties.BORDER_THICKNESS / 2,ImageProperties.BORDER_THICKNESS / 2,
                ImageProperties.IMAGE_WIDTH - ImageProperties.BORDER_THICKNESS,
                ImageProperties.BARCODE_HEIGHT + ImageProperties.BORDER_THICKNESS);

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
                + ImageProperties.INVERTED_STOP_PATTERN;

        for (int i = 0; i < evenPositionPatternedNumbers.length(); i++) {
            g.setColor(Color.BLACK);
            xCoordinate = drawBar(g, evenPositionPatternedNumbers.toCharArray()[i], xCoordinate);
            g.setColor(Color.WHITE);
            xCoordinate = drawBar(g, oddPositionPatternedNumbers.toCharArray()[i], xCoordinate);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font(ImageProperties.FONT,
                ImageProperties.FONT_STYLE,
                ImageProperties.FONT_SIZE));
        g.drawString(barcode,
                ImageProperties.LABEL_X_COORDINATE,
                ImageProperties.LABEL_Y_COORDINATE);

        g.dispose();

        xCoordinate = ImageProperties.IMAGE_WIDTH / 84 * 15;
        yCoordinate = ImageProperties.BORDER_THICKNESS;

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
            g.fillRect(x, yCoordinate,
                    ImageProperties.THICK_BAR_WIDTH,
                    ImageProperties.BARCODE_HEIGHT);
            x += ImageProperties.THICK_BAR_WIDTH;
        } else {
            g.fillRect(x, yCoordinate,
                    ImageProperties.BAR_WIDTH,
                    ImageProperties.BARCODE_HEIGHT);
            x += ImageProperties.BAR_WIDTH;
        }
        return x;
    }
}
