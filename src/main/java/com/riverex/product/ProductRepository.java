package com.riverex.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

/*
JpaRepository<Product, Long> means:

Product is the entity it saves and loads
Long is the type of Product’s @Id
Spring sees this interface at startup, builds an implementation,
        and registers it as a bean. Later ProductService will receive it through
        constructor injection, the same way your proposal injected services. You never
        write new ProductRepository().

 */

// converts java to sql, has methods
/*
Method	& What it does in PostgreSQL

save(product)
INSERT a new row, or UPDATE an existing one

findById(id)
SELECT one row by id

findAll()
SELECT every product

delete(product)
DELETE that row

existsById(id)
check whether that id exists
 */