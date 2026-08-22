# Creator Store API

<p align="center">
  <strong>A production-minded REST API for managing creator products, inventory, and customer orders.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.7-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/PostgreSQL-Database-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/API-REST-0A66C2?style=for-the-badge" alt="REST API">
</p>

## Overview

Creator Store is a backend service for creators who sell physical or digital products. It provides a clean API for maintaining a product catalog, tracking inventory, and creating customer orders with a captured purchase price.

The project is intentionally structured around the service-layer pattern: controllers expose HTTP endpoints, services own business rules, and Spring Data JPA repositories persist domain entities. This keeps the application easy to reason about and extend as requirements grow.

## Highlights

- Full product catalog management: create, read, update, and delete products.
- Inventory-aware ordering that rejects requests exceeding available stock.
- Transactional order creation: inventory updates and order persistence succeed or fail together.
- Snapshot pricing through `priceAtPurchase`, preserving the price paid even if a product changes later.
- Request validation for product details, customer information, and order quantities.
- PostgreSQL persistence with JPA entity relationships for products, orders, and order items.
- Interactive API documentation through OpenAPI / Swagger UI.
- Ready-to-use Postman request collection in [`postman/collections/CreatorStore`](postman/collections/CreatorStore).

## Architecture

```text
HTTP client / Postman
        │
        ▼
REST controllers
        │
        ▼
Service layer ── product and inventory rules
        │
        ▼
Spring Data JPA repositories
        │
        ▼
PostgreSQL
```

## Tech stack

| Area | Technology |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 4.0.7 |
| Web layer | Spring Web MVC |
| Persistence | Spring Data JPA / Hibernate |
| Database | PostgreSQL |
| Validation | Jakarta Bean Validation |
| API documentation | Springdoc OpenAPI / Swagger UI |
| Build tool | Maven Wrapper |
| Boilerplate reduction | Lombok |

## Getting started

### Prerequisites

- Java 17 or later
- PostgreSQL

### Configure the database

Create a PostgreSQL database, then create a `.env` file in the project root with your connection details:

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/creator_store
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_secure_password
```

The application reads these environment variables from `src/main/resources/application.yaml`. Do not commit real credentials.

### Run locally

```bash
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

The API starts at `http://localhost:8080`. Once running, Swagger UI is available at `http://localhost:8080/swagger-ui/index.html`.

## API reference

### Products

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/products` | Create a product |
| `GET` | `/api/products` | List all products |
| `GET` | `/api/products/{id}` | Retrieve one product |
| `PUT` | `/api/products/{id}` | Update a product |
| `DELETE` | `/api/products/{id}` | Delete a product |

Create a product:

```json
{
  "name": "Creator Sticker Pack",
  "description": "A limited-edition set of branded stickers.",
  "category": "Accessories",
  "price": 99.00,
  "stockQuantity": 200
}
```

### Orders

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/orders` | Create an order and reduce inventory atomically |
| `GET` | `/api/orders` | List all orders |
| `GET` | `/api/orders/{id}` | Retrieve one order |

Create an order:

```json
{
  "customerName": "Alex Johnson",
  "customerEmail": "alex@example.com",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

## Domain model

```text
Product 1 ─── * OrderItem * ─── 1 Order
```

- A **Product** stores catalog details, price, category, and available stock.
- An **Order** stores customer details, status, total price, creation time, and its line items.
- An **OrderItem** records the selected product, requested quantity, and the price at the moment of purchase.

## Key business rule: safe order placement

When an order is created, the service validates each requested product, checks that enough stock exists, calculates the total, stores the purchase-time price, and decrements inventory. The workflow runs within a transaction so incomplete orders do not leave inventory in an inconsistent state.

## Project structure

```text
src/main/java/com/example/creatorstore/
├── controllers/    # REST endpoints
├── dto/            # Validated request models
├── entities/       # JPA domain entities
├── repositories/   # Database access
└── services/       # Business logic and transactions
```

---

Built by [Anuj Yadav](https://github.com/anujyadav11) as a practical Spring Boot backend project.
