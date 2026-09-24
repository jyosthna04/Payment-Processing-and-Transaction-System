# Payment Processing & Transaction System

A beginner-friendly **Spring Boot** mini project that demonstrates a simple payment lifecycle: **Create → Process → Success/Failed → Refund**.

Built for learning and interview preparation. Not a production payment system.

---

## Project Overview

This is a simple payment management web application where you can:

- Create payments (UPI, CARD, CASH)
- Process pending payments (they become SUCCESS or FAILED)
- Refund successful payments
- View all payments in a table
- See a dashboard with summary statistics

The app runs entirely in-memory using an **H2 database** — no external database installation needed.

---

## Features

### 1. Payment Creation
Create a payment with customer name, amount, and payment method. New payments start with status **PENDING**.

### 2. Payment Processing
Process a pending payment. The status changes to:
- **PENDING → SUCCESS** (if the amount is even)
- **PENDING → FAILED** (if the amount is odd)

### 3. Refund
Refund a successful payment:
- **SUCCESS → REFUNDED**
- Refunds are not allowed for PENDING or FAILED payments.

### 4. Payment History
A table showing all payments with ID, customer, amount, method, status, date, and a link to view details.

### 5. Simple Dashboard
Shows:
- Total Payments
- Successful Payments
- Failed Payments
- Total Amount

### 6. Error Handling
Meaningful error messages for:
- Payment not found
- Invalid amount (zero or negative)
- Processing a non-pending payment
- Refunding a non-successful payment

---

## Payment Lifecycle

```
CREATE → PENDING
              ↓ (Process)
         SUCCESS  ←→  FAILED
              ↓ (Refund)
         REFUNDED
```

- **PENDING**: Initial state after creation.
- **SUCCESS**: Payment processed successfully.
- **FAILED**: Payment processing failed.
- **REFUNDED**: A successful payment was refunded.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot 3.2.5 | Application framework |
| Spring Data JPA | Database access layer |
| H2 Database | In-memory database (no setup needed) |
| Thymeleaf | Server-side HTML templating |
| Maven | Build tool |
| HTML + CSS | Frontend pages |

---

## Project Structure

```
src/main/java/com/example/payment/
├── PaymentProcessingApplication.java   # Main entry point
├── controller/
│   ├── DashboardController.java         # Dashboard page (/)
│   ├── PaymentWebController.java        # Web pages for payments
│   └── PaymentApiController.java        # REST API endpoints
├── entity/
│   ├── Payment.java                     # JPA entity
│   ├── PaymentMethod.java               # Enum: UPI, CARD, CASH
│   └── PaymentStatus.java               # Enum: PENDING, SUCCESS, FAILED, REFUNDED
├── repository/
│   └── PaymentRepository.java           # JPA repository
├── service/
│   └── PaymentService.java              # Business logic
└── exception/
    ├── PaymentNotFoundException.java
    ├── InvalidPaymentException.java
    └── GlobalWebExceptionHandler.java

src/main/resources/
├── application.properties               # H2 + JPA config
├── templates/
│   ├── dashboard.html
│   ├── payments.html
│   ├── create-payment.html
│   ├── payment-detail.html
│   └── error.html
└── static/css/
    └── style.css
```

---

## How to Run

### Prerequisites
- Java 17 (or higher)
- Maven

### Steps

1. **Clone the repository:**
   ```bash
   git clone https://github.com/<your-username>/payment-processing-system.git
   cd payment-processing-system
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Open your browser to:**
   ```
   http://localhost:8080
   ```

4. **H2 Database Console (optional):**
   ```
   http://localhost:8080/h2-console
   ```
   - JDBC URL: `jdbc:h2:mem:paymentdb`
   - Username: `sa`
   - Password: (leave blank)

---

## Web Pages

| URL | Description |
|---|---|
| `/` | Dashboard with summary stats |
| `/payments` | List of all payments |
| `/payments/new` | Create a new payment |
| `/payments/{id}` | View a single payment's details |

---

## REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/payments` | Create a new payment |
| `GET` | `/api/payments` | Get all payments |
| `GET` | `/api/payments/{id}` | Get a payment by ID |
| `PUT` | `/api/payments/{id}/process` | Process a pending payment |
| `PUT` | `/api/payments/{id}/refund` | Refund a successful payment |

### Example: Create a Payment

```bash
curl -X POST http://localhost:8080/api/payments \
  -H "Content-Type: application/json" \
  -d '{"customerName":"John Doe","amount":100.00,"paymentMethod":"UPI"}'
```

### Example Response

```json
{
  "id": 1,
  "customerName": "John Doe",
  "amount": 100.0,
  "paymentMethod": "UPI",
  "status": "PENDING",
  "paymentDate": "2024-01-15T10:30:00"
}
```

### Example: Process a Payment

```bash
curl -X PUT http://localhost:8080/api/payments/1/process
```

### Example: Refund a Payment

```bash
curl -X PUT http://localhost:8080/api/payments/1/refund
```

---

## Screenshots

> After running the project, take screenshots of:
> 1. **Dashboard** (`/`) — summary statistics
> 2. **Payments List** (`/payments`) — payment history table
> 3. **Create Payment** (`/payments/new`) — payment form
> 4. **Payment Details** (`/payments/{id}`) — single payment with action buttons

Add them to a `screenshots/` folder and reference them here:
```
![Dashboard](screenshots/dashboard.png)
![Payments List](screenshots/payments.png)
![Create Payment](screenshots/create-payment.png)
![Payment Details](screenshots/payment-detail.png)
```

---

## Key Concepts for Interview

1. **Spring Boot**: Auto-configuration, embedded server, starter dependencies.
2. **Spring Data JPA**: Repository pattern, entity mapping, Hibernate.
3. **Layered Architecture**: Controller → Service → Repository → Entity.
4. **Thymeleaf**: Server-side templating with dynamic HTML rendering.
5. **Enum usage**: `PaymentStatus` and `PaymentMethod` as Java enums stored as strings in the database.
6. **Exception Handling**: Custom exceptions + `@ControllerAdvice` for web, `@ExceptionHandler` for REST.
7. **H2 Database**: In-memory database for development — no external setup needed.

---

## License

This is a free educational project. Use it for learning and interview prep.
