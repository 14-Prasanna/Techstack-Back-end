# 🚀 TechStack – Backend (Spring Boot + MySQL)

This is the **backend API** for the **TechStack E-Commerce Web App**, built using **Spring Boot**, **Hibernate**, **Spring Security (JWT)**, and **MySQL**. It handles product management, cart, wishlist, orders, authentication, and secure data flow for the frontend React client.

---

## 📁 Backend Project Structure

```
src/
├── main/
│ ├── java/
│ │ └── com/
│ │ └── techstack/
│ │ └── techstack/
│ │ ├── TechstackApplication.java
│ │ ├── entity/
│ │ │ ├── Cart.java
│ │ │ ├── CartItem.java
│ │ │ ├── Order.java
│ │ │ ├── OrderItem.java
│ │ │ ├── Otp.java
│ │ │ ├── Product.java
│ │ │ ├── ProductReview.java
│ │ │ ├── Specification.java
│ │ │ ├── User.java
│ │ │ ├── Wishlist.java
│ │ │ └── WishlistItem.java
│ │ ├── repository/
│ │ │ ├── CartRepository.java
│ │ │ ├── OrderRepository.java
│ │ │ ├── ProductRepository.java
│ │ │ ├── ProductReviewRepository.java
│ │ │ ├── SpecificationRepository.java
│ │ │ ├── UserRepository.java
│ │ │ ├── WishlistRepository.java
│ │ │ └── WishlistItemRepository.java
│ │ ├── service/
│ │ │ ├── AuthService.java
│ │ │ ├── CartService.java
│ │ │ ├── CheckoutService.java
│ │ │ ├── ProductService.java
│ │ │ ├── ProductReviewService.java
│ │ │ ├── UserProfileService.java
│ │ │ └── WishlistService.java
│ │ └── config/
│ │ └── SecurityConfig.java
│ └── resources/
│ ├── application.properties
│ ├── static/
│ └── templates/
├── test/
│ └── java/
│ └── com/techstack/techstack/
│ └── TechstackApplicationTests.java
```

---

## 🔐 Authentication with JWT

- User login returns a **JWT token**.
- Token must be sent with every protected API call via the `Authorization: Bearer <token>` header.
- Token is stored in **localStorage** on the frontend.
- APIs are protected via **Spring Security**.

---

## 💡 Key Features

✔️ Full JWT Authentication flow (login/signup)  
✔️ Product listing, filtering, and search  
✔️ Wishlist & Cart operations (only for authenticated users)  
✔️ Order placement & history  
✔️ Product reviews & rating  
✔️ Category + Brand filters (e.g., Laptops, Dell, HP, etc.)  
✔️ All APIs tested in Postman  
✔️ MySQL database schema with constraints  
✔️ Structured in layers: `entity`, `repository`, `service`, `config`

---

## 🗃️ Database

- **MySQL** is used as the relational database.
- Entity relationships:
  - One-to-Many: User → Orders, Wishlist → Items, Cart → Items
  - Many-to-One: Item → Product
- Proper indexing and cascading (e.g., `ON DELETE CASCADE`) are used.

---

## 📦 API Overview

| API Endpoint                 | Method | Access      | Description                         |
|-----------------------------|--------|-------------|-------------------------------------|
| `/api/auth/signup`          | POST   | Public      | Register new users                  |
| `/api/auth/login`           | POST   | Public      | Login and receive JWT token         |
| `/api/products`             | GET    | Public      | Get all products with filters       |
| `/api/products/{id}`        | GET    | Public      | Get product details by ID           |
| `/api/products/search`      | GET    | Public      | Search products by keyword          |
| `/api/cart`                 | GET    | Private     | Get current user's cart             |
| `/api/cart/add`             | POST   | Private     | Add item to cart                    |
| `/api/cart/remove/{id}`     | DELETE | Private     | Remove item from cart               |
| `/api/wishlist`             | GET    | Private     | View wishlist                       |
| `/api/wishlist/add`         | POST   | Private     | Add product to wishlist             |
| `/api/wishlist/remove/{id}` | DELETE | Private     | Remove item from wishlist           |
| `/api/orders/checkout`      | POST   | Private     | Place an order                      |
| `/api/orders/history`       | GET    | Private     | Get user's order history            |
| `/api/reviews/{productId}`  | GET    | Public      | View reviews for a product          |
| `/api/reviews/{productId}`  | POST   | Private     | Add a review for a product          |

---

## 🧪 API Testing with Postman

All backend endpoints were tested with **Postman** for:

- Authentication & token management  
- CRUD operations  
- Error handling & validations  
- Header-based access control (`Authorization`)

You can import the collection via the attached `.postman_collection.json` (if applicable).

---

## 🛠️ How to Run Backend

```bash
# Clone the repo
git clone https://github.com/14-Prasanna/Techstack-Back-end.git

# Open in IDE (e.g., IntelliJ, VS Code with Spring Extension)

# Configure MySQL credentials in:
src/main/resources/application.properties

# Run the application
./mvnw spring-boot:run

```
---
Make sure MySQL is running and the schema is set before starting the app. You can use the DDLs from the project documentation.

🙏 Acknowledgement
Special thanks to Fetcho Company for providing me the opportunity to build this real-world project and explore Spring Boot deeply in just 12 days.

🏷️ Tags
Spring Boot Java JWT MySQL Postman Hibernate JPA REST API Ecommerce Secure Auth Full Stack
