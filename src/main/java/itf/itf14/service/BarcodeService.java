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

    // Обчислення контрольної цифри
    private int calculateCheckDigit(String barcodeWithoutCheckDigit) {
        int sumOdd = 0;
        int sumEven = 0;

        for (int i = 0; i < barcodeWithoutCheckDigit.length(); i++) {
            int digit = Character.getNumericValue(barcodeWithoutCheckDigit.charAt(i));
            if ((i + 1) % 2 == 0) { // Парні цифри
                sumEven += digit;
            } else { // Непарні цифри
                sumOdd += digit;
            }
        }

        int totalSum = sumOdd + sumEven * 3;
        int checkDigit = (10 - (totalSum % 10)) % 10;
        return checkDigit;
    }
}

