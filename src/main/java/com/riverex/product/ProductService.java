package com.riverex.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riverex.product.dto.CreateProductRequest;
import com.riverex.product.dto.ProductResponse;
import com.riverex.product.dto.UpdateProductRequest;

@Service // @Service tells Spring to create one ProductService and put it in the application context.
@Transactional
public class ProductService {

	private final ProductRepository productRepository;
	// Constructor Injection here --Spring already has a ProductRepository bean,
	// so it passes that in. You never write new ProductRepository() or new ProductService().
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}


	// @Transactional means each public method runs in a database transaction:
	// all of the work commits together, or none of it does.
	@Transactional(readOnly = true)
	public List<ProductResponse> findAll() {
		return productRepository.findAll()
				.stream()
				.map(ProductResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public ProductResponse findById(Long id) {
		Product product = getProduct(id);
		return ProductResponse.from(product);
	}

	//create takes the validated DTO, builds a Product entity,
	// saves it, then copies the saved row into a ProductResponse
	public ProductResponse create(CreateProductRequest request) {
		Product product = new Product(
				request.name(), // record assessor
				request.description(),
				request.price(),
				request.stock()
		);
		Product saved = productRepository.save(product);
		return ProductResponse.from(saved);
	}

	public ProductResponse update(Long id, UpdateProductRequest request) {
		Product product = getProduct(id);
		product.setName(request.name());
		product.setDescription(request.description());
		product.setPrice(request.price());
		product.setStock(request.stock());
		Product saved = productRepository.save(product);
		return ProductResponse.from(saved);
	}

	public void delete(Long id) {
		Product product = getProduct(id);
		productRepository.delete(product);
	}

	private Product getProduct(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException(id));
	}

}

/*
ProductService is the business-logic layer. It sits between the future controller and
ProductRepository. It knows how to create, find, update, and delete products. It does
not know about HTTP status codes or JSON mapping.
 */

