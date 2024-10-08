package itf.itffourteen.service;

import itf.itffourteen.model.Product;
import itf.itffourteen.repository.ProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(String name, int quantity, String barcode) {
        Product product = new Product(name, quantity, barcode);
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getProductByBarcode(String barcode) {
        return Optional.ofNullable(productRepository.findByBarcode(barcode));
    }
}
