# PayPal Payment Integration Application

A full-stack PayPal payment integration application built with Java Spring Boot (backend) and React.js (frontend), featuring secure payment processing, order management, and transaction tracking.

## Features

- **User Authentication**: Secure JWT-based authentication system
- **Order Management**: Create and manage orders with ease
- **PayPal Checkout**: Seamless PayPal payment integration using PayPal REST API v2
- **Payment History**: View complete transaction history with detailed records
- **Refund Capability**: Process refunds through PayPal
- **Dashboard**: Analytics and statistics for orders and payments
- **Webhook Support**: Handle PayPal webhook events for payment status updates
- **Responsive UI**: Modern, mobile-friendly interface with React Bootstrap

## Tech Stack

### Backend
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with Hibernate
- **MySQL 8+**
- **PayPal Java SDK** (checkout-sdk 2.0.0)
- **Maven** for dependency management

### Frontend
- **React 18**
- **React Router v6** for routing
- **React Bootstrap** for UI components
- **Axios** for HTTP requests
- **@paypal/react-paypal-js** for PayPal integration
- **JWT Decode** for token parsing
- **Vite** for fast development

## Prerequisites

Before running this application, ensure you have the following installed:

- **Java 17 or higher**
- **Maven 3.8+**
- **MySQL 8.0 or higher**
- **Node.js 16+ and npm**
- **PayPal Developer Account** (for sandbox credentials)

## PayPal Sandbox Setup

1. Create a PayPal Developer account at [https://developer.paypal.com](https://developer.paypal.com)
2. Log in to the PayPal Developer Dashboard
3. Navigate to **Accounts** under **Sandbox** section
4. Create a **Business** sandbox account (if not exists)
5. Navigate to **Apps & Credentials**
6. Create a new app (or use default sandbox app)
7. Copy your **Client ID** and **Client Secret**

## Installation & Setup

### 1. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE paypal_integration;
```

### 2. Backend Setup

Navigate to the backend directory:

```bash
cd backend
```

Configure the application properties in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/paypal_integration?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

# PayPal Configuration
paypal.client.id=YOUR_PAYPAL_SANDBOX_CLIENT_ID
paypal.client.secret=YOUR_PAYPAL_SANDBOX_CLIENT_SECRET
paypal.mode=sandbox

# JWT Configuration
jwt.secret=YOUR_JWT_SECRET_KEY_HERE_MAKE_IT_LONG_AND_SECURE
jwt.expiration-ms=86400000
```

Build and run the backend:

```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

Navigate to the frontend directory:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Configure PayPal Client ID in `src/App.jsx`:

```javascript
const initialOptions = {
  "client-id": "YOUR_PAYPAL_SANDBOX_CLIENT_ID", // Replace with your PayPal Client ID
  currency: "USD",
  intent: "capture",
};
```

Start the development server:

```bash
npm run dev
```

The frontend will start on `http://localhost:3000`

## Usage

### 1. Register a New Account

- Navigate to `http://localhost:3000`
- Click on **Register**
- Fill in your details and submit the form

### 2. Login

- Use your registered email and password to login
- Default admin credentials:
  - Email: `admin@paypal.com`
  - Password: `admin123`

### 3. Create an Order

- After logging in, navigate to **Create Order**
- Enter the amount and description
- Click **Create Order**

### 4. Complete Payment

- After creating an order, the PayPal button will appear
- Click the PayPal button to proceed with payment
- You'll be redirected to PayPal's sandbox environment
- Complete the payment using a sandbox buyer account
- You'll be redirected back to the application upon successful payment

### 5. View Payment History

- Navigate to **Payment History** to see all your transactions
- View order details, status, and payment information

### 6. Dashboard

- Access the dashboard to view:
  - Total orders
  - Total amount processed
  - Completed orders count
  - Recent orders list

## Project Structure

```
paypal-payment-integration/
├── backend/                          # Spring Boot Application
│   ├── src/main/java/com/paypal/
│   │   ├── config/                   # Configuration classes
│   │   ├── controller/               # REST API endpoints
│   │   ├── service/                  # Business logic
│   │   ├── repository/               # JPA repositories
│   │   ├── model/                    # Entity classes
│   │   ├── dto/                      # Data Transfer Objects
│   │   ├── security/                 # Security configuration
│   │   └── exception/                # Custom exceptions
│   ├── src/main/resources/
│   │   └── application.properties    # Configuration
│   └── pom.xml
├── frontend/                         # React Application
│   ├── src/
│   │   ├── components/               # React components
│   │   ├── pages/                    # Page components
│   │   ├── services/                 # API calls
│   │   ├── context/                  # React context
│   │   └── utils/                    # Helper functions
│   └── package.json
└── README.md
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - User login

### Orders
- `POST /api/orders/create` - Create a new order
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/user` - Get user's orders
- `GET /api/orders/all` - Get all orders (authenticated)

### Payments
- `POST /api/payments/paypal/create-order` - Create PayPal order
- `POST /api/payments/paypal/capture/{orderId}` - Capture PayPal payment
- `POST /api/payments/paypal/refund/{transactionId}` - Process refund
- `GET /api/payments/paypal/details/{orderId}` - Get payment details

### Webhooks
- `POST /api/webhooks/paypal` - Handle PayPal webhook events

## Security

- **JWT Authentication**: All protected endpoints require a valid JWT token
- **Password Encryption**: Passwords are encrypted using BCrypt
- **CORS Configuration**: Configured to allow requests from React frontend
- **Input Validation**: Request validation using Spring Boot Validation

## Testing with PayPal Sandbox

### Sandbox Accounts

1. Log in to [PayPal Developer Dashboard](https://developer.paypal.com)
2. Navigate to **Accounts** under Sandbox
3. Use the sandbox buyer account credentials to test payments

### Testing Flow

1. Create an order in the application
2. Click PayPal checkout button
3. Log in with sandbox buyer account
4. Complete the payment
5. Verify payment status in the application

## Troubleshooting

### Backend Issues

**Database Connection Error:**
- Verify MySQL is running
- Check database credentials in `application.properties`
- Ensure database `paypal_integration` exists

**PayPal SDK Error:**
- Verify PayPal Client ID and Secret are correct
- Check internet connection
- Review PayPal sandbox account status

### Frontend Issues

**PayPal Button Not Loading:**
- Verify PayPal Client ID in `App.jsx`
- Check browser console for errors
- Ensure backend is running

**Authentication Issues:**
- Clear browser localStorage
- Login again
- Verify backend is running and accessible

## Production Deployment

### Backend

1. Update `application.properties` with production database credentials
2. Change PayPal mode to `production`
3. Add production PayPal credentials
4. Use a strong, secure JWT secret
5. Build the application: `mvn clean package`
6. Run: `java -jar target/paypal-payment-integration-1.0.0.jar`

### Frontend

1. Update PayPal Client ID to production credentials
2. Build the application: `npm run build`
3. Deploy the `dist` folder to your web server or hosting service

## Environment Variables (Production)

Create `.env` files for sensitive data:

**Backend:**
```properties
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/paypal_integration
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
PAYPAL_CLIENT_ID=your_production_client_id
PAYPAL_CLIENT_SECRET=your_production_client_secret
JWT_SECRET=your_secure_jwt_secret
```

**Frontend:**
```env
VITE_PAYPAL_CLIENT_ID=your_production_client_id
VITE_API_URL=https://your-api-domain.com/api
```

## License

This project is open source and available under the MIT License.

## Support

For issues, questions, or contributions, please open an issue on GitHub.

## Acknowledgments

- [PayPal Developer Documentation](https://developer.paypal.com/docs/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev/)
"# paypappaymentintegration" 
