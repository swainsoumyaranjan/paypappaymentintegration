# Quick Setup Guide

## Step-by-Step Instructions to Run the Application

### Step 1: Install Prerequisites
- Install Java 17 or higher: https://www.oracle.com/java/technologies/downloads/
- Install Maven: https://maven.apache.org/download.cgi
- Install MySQL 8+: https://dev.mysql.com/downloads/mysql/
- Install Node.js 16+: https://nodejs.org/

### Step 2: Setup MySQL Database
Open MySQL command line or MySQL Workbench and run:
```sql
CREATE DATABASE paypal_integration;
```

### Step 3: Get PayPal Sandbox Credentials
1. Go to https://developer.paypal.com
2. Sign up or log in
3. Go to Dashboard > Apps & Credentials
4. Create a new app (Sandbox mode)
5. Copy your Client ID and Client Secret

### Step 4: Configure Backend
1. Open `backend/src/main/resources/application.properties`
2. Update these values:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_mysql_password
   
   paypal.client.id=YOUR_PAYPAL_SANDBOX_CLIENT_ID
   paypal.client.secret=YOUR_PAYPAL_SANDBOX_CLIENT_SECRET
   ```

### Step 5: Start Backend
Open terminal in the backend folder:
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
Backend will start on http://localhost:8080

### Step 6: Configure Frontend
1. Open `frontend/src/App.jsx`
2. Replace `YOUR_PAYPAL_SANDBOX_CLIENT_ID` with your PayPal Client ID (line 26)

### Step 7: Start Frontend
Open terminal in the frontend folder:
```bash
cd frontend
npm install
npm run dev
```
Frontend will start on http://localhost:3000

### Step 8: Access the Application
1. Open browser and go to http://localhost:3000
2. Register a new account or use default admin:
   - Email: admin@paypal.com
   - Password: admin123

### Step 9: Test Payment
1. Login to the application
2. Go to "Create Order"
3. Enter amount (e.g., 10.00) and description
4. Click "Create Order"
5. Click PayPal button
6. Login with PayPal sandbox buyer account
7. Complete the payment

## Common Issues

### Backend won't start
- Make sure MySQL is running
- Check database credentials in application.properties
- Verify port 8080 is not in use

### Frontend won't start
- Make sure Node.js is installed
- Run `npm install` first
- Check if port 3000 is available

### PayPal button not showing
- Verify PayPal Client ID is correct in App.jsx
- Check browser console for errors
- Make sure backend is running

### Can't login
- Make sure backend is running
- Check if you registered an account first
- Default admin: admin@paypal.com / admin123

## Next Steps
- Explore the dashboard
- Create multiple orders
- View payment history
- Test different scenarios

## Getting PayPal Sandbox Test Accounts
1. Go to https://developer.paypal.com/dashboard/accounts
2. You'll see pre-created sandbox accounts:
   - Business account (seller)
   - Personal account (buyer)
3. Use the Personal account to test payments

Enjoy testing the application! 🎉
