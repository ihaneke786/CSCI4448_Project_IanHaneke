package com.riverex.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.riverex.common.RestExceptionHandler;
import com.riverex.product.dto.ProductResponse;
import com.riverex.product.dto.UpdateProductRequest;

// Tests ProductController HTTP behavior without starting the full app or using PostgreSQL.
// MockMvc is a fake browser; ProductService is faked so we only test URLs, JSON, and status codes.
@WebMvcTest(controllers = ProductController.class)
@Import(RestExceptionHandler.class) // include the 404/400 handler in this web-only test
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc; // fake HTTP client used to call controller methods

	@MockitoBean
	private ProductService productService; // fake service; the controller is the real object under test

	// GET /products should return 200 and a JSON array of products from the service.
	@Test
	void listProductsReturnsOk() throws Exception {
		when(productService.findAll()).thenReturn(List.of(keyboard()));

		mockMvc.perform(get("/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value("Mechanical Keyboard"));
	}

	// GET /products/1 should return 200 and the matching product JSON.
	@Test
	void getProductReturnsOk() throws Exception {
		when(productService.findById(1L)).thenReturn(keyboard());

		mockMvc.perform(get("/products/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.stock").value(14));
	}

	// GET /products/99 should turn ProductNotFoundException into 404 JSON.
	@Test
	void getMissingProductReturnsNotFound() throws Exception {
		when(productService.findById(99L)).thenThrow(new ProductNotFoundException(99L));

		mockMvc.perform(get("/products/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value("Product not found: 99"));
	}

	// POST /products with valid JSON should return 201, a Location header, and the created product.
	@Test
	void createProductReturnsCreated() throws Exception {
		when(productService.create(any())).thenReturn(keyboard());

		mockMvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Mechanical Keyboard",
								  "description": "Tactile switches",
								  "price": 89.99,
								  "stock": 14
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/products/1"))
				.andExpect(jsonPath("$.id").value(1));
	}

	// POST /products with invalid JSON should return 400 and never call ProductService.create().
	@Test
	void createInvalidProductReturnsBadRequest() throws Exception {
		mockMvc.perform(post("/products")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "",
								  "price": -1,
								  "stock": -3
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message").value("Validation failed"));

		verify(productService, never()).create(any());
	}

	// PUT /products/1 should return 200 and the updated product JSON.
	@Test
	void updateProductReturnsOk() throws Exception {
		ProductResponse updated = new ProductResponse(
				1L,
				"Wireless Mouse",
				"Updated",
				new BigDecimal("39.99"),
				32
		);
		when(productService.update(eq(1L), any(UpdateProductRequest.class))).thenReturn(updated);

		mockMvc.perform(put("/products/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Wireless Mouse",
								  "description": "Updated",
								  "price": 39.99,
								  "stock": 32
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Wireless Mouse"));
	}

	// DELETE /products/1 should return 204 and call productService.delete(1).
	@Test
	void deleteProductReturnsNoContent() throws Exception {
		mockMvc.perform(delete("/products/1"))
				.andExpect(status().isNoContent());

		verify(productService).delete(1L);
	}

	// DELETE /products/99 should return 404 when the service says the product does not exist.
	@Test
	void deleteMissingProductReturnsNotFound() throws Exception {
		doThrow(new ProductNotFoundException(99L)).when(productService).delete(99L);

		mockMvc.perform(delete("/products/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	// Helper: canned ProductResponse used as the fake service return value.
	private ProductResponse keyboard() {
		return new ProductResponse(
				1L,
				"Mechanical Keyboard",
				"Tactile switches",
				new BigDecimal("89.99"),
				14
		);
	}

}
