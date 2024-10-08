package itf.itffourteen.imageprocessing;

import java.awt.image.BufferedImage;
import java.awt.Color;
import org.springframework.stereotype.Component;

@Component
public class BarcodeDecoder {

    private static final String[] PATTERNS = {
            "00110", "10001", "01001", "11000", "00101",
            "10100", "01100", "00011", "10010", "01010"
    };

    public String decodeBarcode(BufferedImage barcodeImage) {
        int width = barcodeImage.getWidth();
        int height = barcodeImage.getHeight();

        int xCoordinate = findStartingPoint(barcodeImage);
        int yCoordinate = height / 3;

        StringBuilder decodedBarcode = new StringBuilder();

        while (xCoordinate < width) {
            String blackPattern = readPattern(barcodeImage, xCoordinate, yCoordinate, Color.BLACK);
            String whitePattern = readPattern(barcodeImage, xCoordinate + blackPattern.length(), yCoordinate, Color.WHITE);

            if (blackPattern.isEmpty() || whitePattern.isEmpty()) {
                break;
            }

            String fullPattern = blackPattern + whitePattern;
            String decodedDigit = decodePatternToDigit(fullPattern);

            if (decodedDigit != null) {
                decodedBarcode.append(decodedDigit);
            }

            xCoordinate += blackPattern.length() + whitePattern.length();
        }

        return decodedBarcode.toString();
    }

    private int findStartingPoint(BufferedImage image) {
        int height = image.getHeight();
        int borderThickness = 8;
        for (int x = borderThickness; x < image.getWidth(); x++) {
            for (int y = 0; y < height; y++) {
                if (image.getRGB(x, y) == Color.BLACK.getRGB()) {
                    return x;
                }
            }
        }
        return 0;
    }

    private String readPattern(BufferedImage image, int x, int y, Color expectedColor) {
        StringBuilder pattern = new StringBuilder();
        int currentWidth = 0;
        boolean isPatternContinuing = true;

        while (isPatternContinuing && x < image.getWidth()) {
            int pixelColor = image.getRGB(x, y);
            if ((expectedColor == Color.BLACK && pixelColor == Color.BLACK.getRGB()) ||
                    (expectedColor == Color.WHITE && pixelColor == Color.WHITE.getRGB())) {
                currentWidth++;
            } else {
                isPatternContinuing = false;
            }
            x++;
        }

        if (currentWidth >= 2) {
            pattern.append('1');
        } else if (currentWidth == 1) {
            pattern.append('0');
        }

        return pattern.toString();
    }

    private String decodePatternToDigit(String pattern) {
        for (int i = 0; i < PATTERNS.length; i++) {
            if (PATTERNS[i].equals(pattern)) {
                return String.valueOf(i);
            }
        }
        return null;
    }
}