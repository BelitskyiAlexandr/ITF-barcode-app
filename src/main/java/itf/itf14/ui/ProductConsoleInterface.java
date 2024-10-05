package itf.itf14.ui;

import itf.itf14.service.BarcodeService;
import itf.itf14.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ProductConsoleInterface {

    private final ProductService productService;
    private final BarcodeService barcodeService;

    @Autowired
    public ProductConsoleInterface(ProductService productService, BarcodeService barcodeService) {
        this.productService = productService;
        this.barcodeService = barcodeService;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Оберіть дію:");
            System.out.println("1. Створити новий товар");
            System.out.println("2. Вийти");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createProduct(scanner);
                case 2 -> {
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

        Long productId = System.currentTimeMillis(); // Тимчасове використання часу як ID (можна використовувати більш надійний спосіб)
        String barcode = barcodeService.generateITF14Barcode(productId);

        productService.createProduct(name, quantity, barcode);

        System.out.println("Товар успішно створено!");
        System.out.println("Назва: " + name);
        System.out.println("Кількість: " + quantity);
        System.out.println("Штрихкод (ITF-14): " + barcode);
    }
}
