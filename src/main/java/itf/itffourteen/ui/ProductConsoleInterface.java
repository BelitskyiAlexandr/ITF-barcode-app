package itf.itffourteen.ui;

import itf.itffourteen.imageprocessing.BarcodeDecoder;
import itf.itffourteen.model.Product;
import itf.itffourteen.service.BarcodeService;
import itf.itffourteen.service.ProductService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;
import java.util.Scanner;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductConsoleInterface {

    private final ProductService productService;
    private final BarcodeService barcodeService;
    private final BarcodeDecoder barcodeDecoder;

    @Autowired
    public ProductConsoleInterface(ProductService productService,
                                   BarcodeService barcodeService,
                                   BarcodeDecoder barcodeDecoder) {
        this.productService = productService;
        this.barcodeService = barcodeService;
        this.barcodeDecoder = barcodeDecoder;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n+---------------------------------+");
            System.out.println("Select an action:");
            System.out.println("1. Create a new product");
            System.out.println("2. Decode barcode from image");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createProduct(scanner);
                case 2 -> decodeBarcode(scanner);
                case 3 -> {
                    running = false;
                    System.out.println("Exiting the program...");
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }

        scanner.close();
    }

    private void createProduct(Scanner scanner) {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter product quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Long productId = System.currentTimeMillis();
        String barcode = barcodeService.generateItf14Barcode(productId);

        productService.createProduct(name, quantity, barcode);

        System.out.println("Product successfully created!");
        System.out.println("Name: " + name);
        System.out.println("Quantity: " + quantity);
        System.out.println("Barcode (ITF-14): " + barcode);
    }

    private void decodeBarcode(Scanner scanner) {
        System.out.print("Enter the path to the barcode image: ");
        String filePath = scanner.nextLine();

        try {
            File file = new File(filePath);
            BufferedImage image = ImageIO.read(file);

            String decodedBarcode = barcodeDecoder.decodeBarcode(image);

            System.out.println("Decoded barcode: " + decodedBarcode);
            Optional<Product> product = productService.getProductByBarcode(decodedBarcode);
            if (product.isPresent()) {
                System.out.println("Name: " + product.get().getName());
                System.out.println("Quantity: " + product.get().getQuantity());
            } else {
                System.out.println("Product not found for the given barcode.");
            }
        } catch (Exception e) {
            System.out.println("Error reading the image: " + e.getMessage());
        }
    }
}
