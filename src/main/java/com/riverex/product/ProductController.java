package com.riverex.product;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.riverex.product.dto.CreateProductRequest;
import com.riverex.product.dto.ProductResponse;
import com.riverex.product.dto.UpdateProductRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public List<ProductResponse> findAll() {
		return productService.findAll();
	}

	@GetMapping("/{id}")
	public ProductResponse findById(@PathVariable Long id) {
		return productService.findById(id);
	}

	@PostMapping
	public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
		ProductResponse created = productService.create(request);
		URI location = URI.create("/products/" + created.id());
		return ResponseEntity.created(location).body(created);
	}

	@PutMapping("/{id}")
	public ProductResponse update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
		return productService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		productService.delete(id);
	}

}
// ProductController converts http requests into java code
// for the ProductService class

/*
The URLs
Annotation
HTTP
Java method
@GetMapping
GET /products
findAll()

@GetMapping("/{id}")
GET /products/1
findById(1)

@PostMapping
POST /products
create(...)

@PutMapping("/{id}")
PUT /products/1
update(1, ...)

@DeleteMapping("/{id}")
DELETE /products/1
delete(1)
 */