package com.riverex.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
		@NotBlank @Size(max = 255) String name, // NotBlank means there must be a name
		@Size(max = 2000) String description, // max is just max string size
		@NotNull @DecimalMin("0.00") @Digits(integer = 8, fraction = 2) BigDecimal price, // no negative prices, digits makes money format ex: 12.99
		@NotNull @Min(0) Integer stock // no negative amounts of products
) {
}

// A DTO is a data transfer object: a Java type that matches JSON going in
// or out of the API. It is not a database table.

// Product is a PostgreSQL row. These records are the HTTP shapes.

/*
Json converted to java
{
  "name": "Mechanical Keyboard",
  "description": "Tactile switches",
  "price": 89.99,
  "stock": 14
}

java
CreateProductRequest(
  name = "Mechanical Keyboard",
  description = "Tactile switches",
  price = 89.99,
  stock = 14
)

field names need to match the CreateProductRequest
 */