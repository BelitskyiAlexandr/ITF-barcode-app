package itf.itffourteen.imageprocessing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BarcodeDecoder {
    private static final String[] PATTERNS = {
            "00110", "10001", "01001", "11000", "00101",
            "10100", "01100", "00011", "10010", "01010"
    };

    public String decodeBarcode(BufferedImage image) {
        int width = image.getWidth();

        int xcoordinate = ImageProperties.IMAGE_WIDTH / 84 * 15;
        int ycoordinate = ImageProperties.BORDER_THICKNESS;

        List<Integer> barsAndSpaces = new ArrayList<>();

        int currentBarWidth = 0;
        boolean isBlack = isBlackColumn(image, xcoordinate, ycoordinate);

        for (int x = xcoordinate; x < width - xcoordinate; x++) {
            boolean currentColumnIsBlack = isBlackColumn(image, x, ycoordinate);
            if (currentColumnIsBlack == isBlack) {
                currentBarWidth++;
            } else {
                barsAndSpaces.add(currentBarWidth);
                isBlack = currentColumnIsBlack;
                currentBarWidth = 1;
            }
        }

        StringBuilder binarySequence = new StringBuilder();
        for (int widthValue : barsAndSpaces) {
            binarySequence.append(widthValue >= ImageProperties.THICK_BAR_WIDTH ? "1" : "0");
        }

        String trimmedBinary = binarySequence.substring(4, binarySequence.length() - 1);

        StringBuilder evenIndexBits = new StringBuilder();
        StringBuilder oddIndexBits = new StringBuilder();

        for (int i = 0; i < trimmedBinary.length(); i++) {
            if (i % 2 == 0) {
                evenIndexBits.append(trimmedBinary.charAt(i));
            } else {
                oddIndexBits.append(trimmedBinary.charAt(i));
            }
        }

        String evenDecoded = decodePatternSequence(evenIndexBits.toString());
        String oddDecoded = decodePatternSequence(oddIndexBits.toString());

        StringBuilder finalBarcode = new StringBuilder();
        for (int i = 0; i < evenDecoded.length(); i++) {
            finalBarcode.append(evenDecoded.charAt(i));
            if (i < oddDecoded.length()) {
                finalBarcode.append(oddDecoded.charAt(i));
            }
        }

        return finalBarcode.toString();
    }

    private boolean isBlackColumn(BufferedImage image, int x, int y) {
        return image.getRGB(x, y) != -1;
    }

    private String decodePatternSequence(String binarySequence) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < binarySequence.length(); i += 5) {
            String pattern = binarySequence.substring(i, i + 5);

            for (int j = 0; j < PATTERNS.length; j++) {
                if (PATTERNS[j].equals(pattern)) {
                    result.append(j);
                    break;
                }
            }
        }

        return result.toString();
    }
}
