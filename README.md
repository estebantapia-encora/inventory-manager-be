# Inventory Manager - Backend

An Inventory Management System built with Java & Spring Boot.
## Table of Contents
- Features
- Tech Stack
- Installation & Setup
- API Endpoints
- Running Tests
- Deployment
- License


## Features
- **Product Management:**
  - Add, edit and delete products.
  - Manage stock levels.
  
- **Stock Control:**
  - Mark products as "Out of Stock".
  - Unchecking the "Out of Stock" box restores stock to **10** by default.
    
- **Filtering & Sorting:**
  - Filter products by **name, category, availability**.
  - Sort products by **name, category, price, expiration, stock**.
    
- **Pagination:**
  - Supports **10 products per page**.
    
- **REST API:**
  - JSON-based API with standard HTTP methods.
    
- **Cross-Origin Resource Sharing (CORS) Configurations:**
  - Allows frontend applications to interact seamlessly with the backend.

## Tech Stack
- **Backend:** Java, Spring Boot.
- **Database:** Uses a Java library for data storage.
- **Build Tool:** Maven.
- **CORS Configuration:** Custom CorsConfig Class.
- **Frontend:** https://github.com/estebantapia-encora/inventory-manager-fe

## Installation
### **1. Clone the Repository**
- git clone https://github.com/estebantapia-encora/inventory-manager-be.git
- cd inventory-manager-be

### **2. Configure Environment**
- Modify application.properties if required.
- Ensure database connection settings align with the intended environment.

### **3. Build & Run the Application**
- mvn clean install
- mvn spring-boot:run
The backend will run at http://localhost:9090/.

## API Endpoints
| METHOD  | ENDPOINT                     | DESCRIPTION                      |
|---------|------------------------------|----------------------------------|
| **GET**  | `/products`                   | Fetch all products               |
| **POST** | `/products`                   | Add a new product                |
| **PUT**  | `/products/{id}`               | Edit a product                   |
| **DELETE** | `/products/{id}`             | Delete a product                 |
| **PUT**  | `/products/{id}/instock`       | Restore product stock            |
| **POST** | `/products/{id}/outofstock`    | Mark a product as "out of stock" |


## Running Tests - To run unit tests for the backend
 - mvn test

## Deplyment - To build and deploy the backend, run:
 - mvn package

 ## License
This project is licensed under the MIT License.
https://github.com/estebantapia-encora/inventory-manager-be/blob/backend-main/LICENSE
For more information on the MIT license, go to: https://opensource.org/license/mit
