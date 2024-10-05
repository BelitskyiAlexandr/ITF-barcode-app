package itf.itf14.service;

import itf.itf14.imageProcessing.ImageGenerator;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarcodeService {
    private final ImageGenerator imageGenerator;

    public String generateITF14Barcode(Long productId) {
        String barcodeWithoutCheckDigit = String.format("%013d", productId);
        int checkDigit = calculateCheckDigit(barcodeWithoutCheckDigit);
        String fullBarcode = barcodeWithoutCheckDigit + checkDigit;

        try {
            BufferedImage image = imageGenerator.generateBarcodeImage(fullBarcode);
            File outputfile = new File(fullBarcode + ".png");
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fullBarcode;
    }

        /* 14  13  12  11 10  9  8  7  6  5  4  3  2 1
         * S13 S12 S11 S10 S9 S8 S7 S6 S5 S4 S3 S2 S1 C
         *  3   1   3   1  3  1  3  1  3  1  3  1  3
         *
         * when i = 0:
         *  12  11  10  9  8  7  6  5  4  3  2  1  0
         * */
        private int calculateCheckDigit(String barcodeWithoutCheckDigit) {
        int sumOdd = 0;
        int sumEven = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(barcodeWithoutCheckDigit);
        sb.reverse();

        for (int i = 0; i < barcodeWithoutCheckDigit.length(); i++) {
            int digit = Character.getNumericValue(sb.charAt(i));
            if (i % 2 == 0) {
                sumEven += digit;
            } else {
                sumOdd += digit;
            }
        }

        int totalSum = sumOdd + sumEven * 3;
        int checkDigit = (10 - (totalSum % 10)) % 10;
        return checkDigit;
    }
}

