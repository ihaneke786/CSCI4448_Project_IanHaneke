package com.riverex.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.riverex.product.dto.CreateProductRequest;
import com.riverex.product.dto.ProductResponse;
import com.riverex.product.dto.UpdateProductRequest;

// Tests ProductService without HTTP or PostgreSQL.
// Mockito fakes ProductRepository; the service itself is real.
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock // fake repository so these tests never touch the database
	private ProductRepository productRepository;

	@InjectMocks // builds a real ProductService and injects the fake repository
	private ProductService productService;

	// create() should save the entity and return a response that includes the generated id.
	@Test
	void createSavesProductAndReturnsResponse() {
		CreateProductRequest request = new CreateProductRequest(
				"Mechanical Keyboard",
				"Tactile switches",
				new BigDecimal("89.99"),
				14
		);
		// PostgreSQL would assign id=1; the fake repository does that here.
		when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
			Product product = invocation.getArgument(0);
			ReflectionTestUtils.setField(product, "id", 1L);
			return product;
		});

		ProductResponse response = productService.create(request);

		assertEquals(1L, response.id());
		assertEquals("Mechanical Keyboard", response.name());
		assertEquals(new BigDecimal("89.99"), response.price());
		assertEquals(14, response.stock());
		verify(productRepository).save(any(Product.class));
	}

	// findById() should map an existing Product entity into a ProductResponse.
	@Test
	void findByIdReturnsProduct() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(product(1L)));

		ProductResponse response = productService.findById(1L);

		assertEquals(1L, response.id());
		assertEquals("Mechanical Keyboard", response.name());
	}

	// findById() should throw when the repository has no row for that id.
	@Test
	void findByIdThrowsWhenMissing() {
		when(productRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(ProductNotFoundException.class, () -> productService.findById(99L));
	}

	// findAll() should convert each entity from the repository into a response DTO.
	@Test
	void findAllReturnsMappedProducts() {
		when(productRepository.findAll()).thenReturn(List.of(product(1L)));

		List<ProductResponse> products = productService.findAll();

		assertEquals(1, products.size());
		assertEquals("Mechanical Keyboard", products.getFirst().name());
	}

	// update() should load the product, apply the request fields, and return the new values.
	@Test
	void updateChangesFields() {
		Product existing = product(1L);
		when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(productRepository.save(existing)).thenReturn(existing);
		UpdateProductRequest request = new UpdateProductRequest(
				"Wireless Mouse",
				"Updated",
				new BigDecimal("39.99"),
				32
		);

		ProductResponse response = productService.update(1L, request);

		assertEquals("Wireless Mouse", response.name());
		assertEquals(new BigDecimal("39.99"), response.price());
		assertEquals(32, response.stock());
	}

	// delete() should look up the product, then ask the repository to remove it.
	@Test
	void deleteRemovesProduct() {
		Product existing = product(1L);
		when(productRepository.findById(1L)).thenReturn(Optional.of(existing));

		productService.delete(1L);

		verify(productRepository).delete(existing);
	}

	// delete() should throw if the product is missing, and must not call repository.delete().
	@Test
	void deleteThrowsWhenMissing() {
		when(productRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(ProductNotFoundException.class, () -> productService.delete(99L));
		verify(productRepository, never()).delete(any());
	}

	// Helper: builds a Product and sets id as Hibernate would after a database insert.
	private Product product(Long id) {
		Product product = new Product(
				"Mechanical Keyboard",
				"Tactile switches",
				new BigDecimal("89.99"),
				14
		);
		ReflectionTestUtils.setField(product, "id", id);
		return product;
	}

}
