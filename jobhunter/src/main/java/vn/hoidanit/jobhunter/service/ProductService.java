package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.ProductCategory;
import vn.hoidanit.jobhunter.domain.Products;
import vn.hoidanit.jobhunter.repository.ProductCategoryRepository;
import vn.hoidanit.jobhunter.repository.ProductRepository;
import vn.hoidanit.jobhunter.util.ProductSpecification;

@Service
public class ProductService {

	private ProductRepository productRepository;
	private ProductCategoryRepository categoryRepository;

	public ProductService(ProductRepository productRepository, ProductCategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	public Products createProduct(Products products) {
		ProductCategory category = this.categoryRepository.findById(products.getCategoryId()).get();
		products.setProductCategory(category);
		return this.productRepository.save(products);
	}

	public Products updateProduct(Products products) {
		Products productsDB = this.productRepository.findById(products.getId()).get();
		productsDB.setName(products.getName());
		productsDB.setPrice(products.getPrice());
		productsDB.setDescription(products.getDescription());
		return this.productRepository.save(productsDB);
	}

	public void createListProduct(List<Products> list) {
		this.productRepository.saveAll(list);
	}

	public List<Products> findAllProduct() {
		return productRepository.findAll();
	}

	public Products findProductById(long id) {
		return productRepository.findById(id).get();
	}

	public String deleteProductById(long id) {
		productRepository.deleteById(id);
		return "Delete success";
	}

	@SuppressWarnings("null")
	public Page<Products> search(String name, Double min, Double max, String sortDir, int page, int size) {
		// Tạo Sort
		Sort sort = sortDir != null && sortDir.equalsIgnoreCase("desc") 
			? Sort.by("price").descending() 
			: Sort.by("price").ascending();

		// Tạo Pageable với Sort
		Pageable pageable = PageRequest.of(page, size, sort);

		// Thực hiện query với phân trang
		return productRepository.findAll(ProductSpecification.filter(name, min, max), pageable);
	}
}
