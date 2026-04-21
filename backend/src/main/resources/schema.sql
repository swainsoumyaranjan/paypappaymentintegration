-- Create database
CREATE DATABASE IF NOT EXISTS paypal_integration;
USE paypal_integration;

-- Tables will be created automatically by Hibernate
-- This file is for reference only

-- Users table
-- CREATE TABLE users (
--     id VARCHAR(36) PRIMARY KEY,
--     email VARCHAR(255) UNIQUE NOT NULL,
--     password VARCHAR(255) NOT NULL,
--     first_name VARCHAR(255) NOT NULL,
--     last_name VARCHAR(255) NOT NULL,
--     role VARCHAR(20) NOT NULL,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
-- );

-- Orders table
-- CREATE TABLE orders (
--     id VARCHAR(36) PRIMARY KEY,
--     user_id VARCHAR(36) NOT NULL,
--     order_number VARCHAR(255) UNIQUE NOT NULL,
--     amount DECIMAL(19, 2) NOT NULL,
--     currency VARCHAR(10) NOT NULL DEFAULT 'USD',
--     status VARCHAR(20) NOT NULL,
--     paypal_order_id VARCHAR(255),
--     paypal_payment_id VARCHAR(255),
--     description TEXT,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--     FOREIGN KEY (user_id) REFERENCES users(id)
-- );

-- Transactions table
-- CREATE TABLE transactions (
--     id VARCHAR(36) PRIMARY KEY,
--     order_id VARCHAR(36) NOT NULL,
--     transaction_id VARCHAR(255) NOT NULL,
--     type VARCHAR(20) NOT NULL,
--     amount DECIMAL(19, 2) NOT NULL,
--     status VARCHAR(50) NOT NULL,
--     timestamp TIMESTAMP NOT NULL,
--     response_payload TEXT,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     FOREIGN KEY (order_id) REFERENCES orders(id)
-- );

-- Indexes
-- CREATE INDEX idx_user_email ON users(email);
-- CREATE INDEX idx_order_user_id ON orders(user_id);
-- CREATE INDEX idx_order_status ON orders(status);
-- CREATE INDEX idx_transaction_order_id ON transactions(order_id);
