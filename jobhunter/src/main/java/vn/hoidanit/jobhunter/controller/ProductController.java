package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Products;
import vn.hoidanit.jobhunter.domain.dto.ProductResponseDto;
import vn.hoidanit.jobhunter.service.ProductService;

@RestController
public class ProductController {

	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/product")
	public ResponseEntity<Products> createProductCategory(@RequestBody Products product) {
		return ResponseEntity.ok(productService.createProduct(product));
	}

	@PostMapping("/product/list")
	public ResponseEntity<String> createProductList(@RequestBody List<Products> product) {
		productService.createListProduct(product);
		return ResponseEntity.ok("Tao thanh cong");
	}

	@GetMapping("/product/search")
	public ResponseEntity<ProductResponseDto> search(@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "minPrice") Double minPrice,
			@RequestParam(required = false, name = "maxPrice") Double maxPrice,
			@RequestParam(defaultValue = "asc", name = "sort") String sort,
			@RequestParam(defaultValue = "0", name = "page") int page,
			@RequestParam(defaultValue = "4", name = "size") int size) {
		ProductResponseDto result = productService.search(name, minPrice, maxPrice, sort, page, size);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/product")
	public ResponseEntity<ProductResponseDto> initProduct() {
		ProductResponseDto result = productService.initProduct();
		return ResponseEntity.ok(result);
	}

	@GetMapping("/product/{id}")
	public ResponseEntity<Products> findProductCategoryById(@PathVariable("id") long id) {
		return ResponseEntity.ok(productService.findProductById(id));
	}

	@PutMapping("/product")
	public ResponseEntity<Products> findProductCategoryById(@RequestBody Products product) {
		return ResponseEntity.ok(productService.updateProduct(product));
	}

	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProductCategoryById(@PathVariable("id") long id) {
		return ResponseEntity.ok(productService.deleteProductById(id));
	}
}
