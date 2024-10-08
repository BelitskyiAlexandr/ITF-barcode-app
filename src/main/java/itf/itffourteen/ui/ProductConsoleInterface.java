package itf.itffourteen.ui;

import itf.itffourteen.service.BarcodeService;
import itf.itffourteen.service.ProductService;
import itf.itffourteen.imageprocessing.BarcodeDecoder;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductConsoleInterface {

    private final ProductService productService;
    private final BarcodeService barcodeService;
    private final BarcodeDecoder barcodeDecoder;

    @Autowired
    public ProductConsoleInterface(ProductService productService, BarcodeService barcodeService, BarcodeDecoder barcodeDecoder) {
        this.productService = productService;
        this.barcodeService = barcodeService;
        this.barcodeDecoder = barcodeDecoder;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Оберіть дію:");
            System.out.println("1. Створити новий товар");
            System.out.println("2. Зчитати штрихкод з зображення");
            System.out.println("3. Вийти");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createProduct(scanner);
                case 2 -> decodeBarcode(scanner);
                case 3 -> {
                    running = false;
                    System.out.println("Завершення програми...");
                }
                default -> System.out.println("Невірний вибір, спробуйте ще раз.");
            }
        }

        scanner.close();
    }

    private void createProduct(Scanner scanner) {
        System.out.print("Введіть назву товару: ");
        String name = scanner.nextLine();

        System.out.print("Введіть кількість товару: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        //1728152125126L
        Long productId = System.currentTimeMillis();
        String barcode = barcodeService.generateItf14Barcode(productId);

        productService.createProduct(name, quantity, barcode);

        System.out.println("Товар успішно створено!");
        System.out.println("Назва: " + name);
        System.out.println("Кількість: " + quantity);
        System.out.println("Штрихкод (ITF-14): " + barcode);
    }

    private void decodeBarcode(Scanner scanner) {
        System.out.print("Введіть шлях до зображення штрихкоду: ");
        String filePath = scanner.nextLine();

        try {
            File file = new File(filePath);
            BufferedImage image = ImageIO.read(file);

            String decodedBarcode = barcodeDecoder.decodeBarcode(image);

            System.out.println("Зчитаний штрихкод: " + decodedBarcode);
        } catch (Exception e) {
            System.out.println("Помилка при зчитуванні зображення: " + e.getMessage());
        }
    }
}
