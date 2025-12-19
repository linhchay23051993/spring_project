package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.ProductCategory;
import vn.hoidanit.jobhunter.domain.Products;
import vn.hoidanit.jobhunter.domain.dto.ProductListDto;
import vn.hoidanit.jobhunter.domain.dto.ProductResponseDto;
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

	public List<ProductListDto> findAllProduct() {
		convertToProductListDto(productRepository.findAll());
		return convertToProductListDto(productRepository.findAll());
	}

	public Products findProductById(long id) {
		return productRepository.findById(id).get();
	}

	public String deleteProductById(long id) {
		productRepository.deleteById(id);
		return "Delete success";
	}

	public ProductResponseDto search(String name, Double min, Double max, String sortDir, int page, int size) {
		// Tạo Sort
		Sort sort = sortDir != null && sortDir.equalsIgnoreCase("desc") ? Sort.by("price").descending()
				: Sort.by("price").ascending();

		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Products> productList = productRepository.findAll(ProductSpecification.filter(name, min, max), pageable);
		ProductResponseDto productResponseDto = new ProductResponseDto();
		productResponseDto.setTotal((int) productList.getTotalElements());
		convertToProductListDto(productList.getContent());
		productResponseDto.setListDto(convertToProductListDto(productList.getContent()));
		return productResponseDto;
	}

//	public ProductResponseDto initProduct() {
//
//		List<Products> productList = productRepository.findAll();
//		ProductResponseDto productResponseDto = new ProductResponseDto();
//		productResponseDto.setTotal(productList.size());
//		productResponseDto.setListDto(convertToProductListDto(productList));
//		return productResponseDto;
//	}
	
	public ProductResponseDto initProduct(int page, int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Products> productList = productRepository.findAll(pageable);
		ProductResponseDto productResponseDto = new ProductResponseDto();
		productResponseDto.setTotal((int) productList.getTotalElements());
		convertToProductListDto(productList.getContent());
		productResponseDto.setListDto(convertToProductListDto(productList.getContent()));
		return productResponseDto;
	}

	public List<ProductListDto> convertToProductListDto(List<Products> list) {
		List<ProductListDto> listDto = new ArrayList<>();
		for (Products item : list) {
			ProductListDto dto = new ProductListDto();
			dto.setId(item.getId());
			dto.setName(item.getName());
			dto.setPrice(item.getPrice());
			dto.setDescription(item.getDescription());
			listDto.add(dto);
		}

		return listDto;
	}
}
