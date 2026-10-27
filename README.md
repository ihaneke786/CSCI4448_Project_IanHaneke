What I am building:
RiverEx is a REST-based e-commerce order and inventory 
management system built using Java, Spring Boot, and 
PostgreSQL. It simulates the backend of an online retailer 
like Amazon, allowing users to manage products, place orders,
process simulated payments, track order statuses, and manage 
inventory.


How It Works
1. Product Management: Users can create, update, delete, and browse products.
2. Order Processing: Customers place orders containing multiple products.
3. Inventory Management: The system checks product availability and updates inventory when orders are placed or canceled.
4. Payment Processing: The system simulates different payment methods.
5. Order Tracking: Orders transition through states such as Pending, Paid, Processing, Shipped, and Delivered.
6. Data Persistence: Customer, product, inventory, and order information is stored in PostgreSQL.


System Architecture
1. Controllers: Handle HTTP requests and responses.
2. Services: Implement business logic, including order processing, payments, and inventory management.
3. Repositories: Handle database operations through Spring Data JPA.
4. Database: Persists application data.


Mid-Project-Review

Design Patterns:
State Pattern: Not implemented in the code yet, will work with the orders,
Orders will have an interface like cancel, process and ship so they can have 
states like Pending, Paid, Processing, Shipped, Delivered and Cancelled.

Strategy Pattern: Not implemented in code yet, will help with payments
like there will be CreditCardPaymentStrategy, PayPalPaymentStrategy and
GiftCardPaymentStrategy.

Factory Pattern: Not implemented yet, will also work with Payments,
there will be a PaymentFactory that handles Credit Cards, Paypal and Gift cards.

Observer Pattern: Not implemented yet, it will let users know when the state of their 
Orders changes like if something is processed, shipped and/or delivered.

Core OO principles:
Encapsulation: In the Product class, fields are private, it uses getters/setters.
ProductController does not access the database directly, ProductService does not know
the HTTP status and ProductRepository does not access JSON.

Inheritance: 
ProductRepository is a JpaRepository<Product, Long> so it inherits methods
like save, findById, findAll and delete.

ProductNotFoundException extends RuntimeException

Dependency Injection: The ProductService class does not construct its own repository.
Same with ProductController.