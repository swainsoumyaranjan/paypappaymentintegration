import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { FaShoppingCart, FaHistory, FaLock } from 'react-icons/fa';

const Home = () => {
  const { isAuthenticated } = useAuth();

  return (
    <Container>
      <Row className="justify-content-center my-5">
        <Col md={8}>
          <div className="text-center mb-5">
            <h1 className="display-4">PayPal Payment Integration</h1>
            <p className="lead">
              A complete payment solution with PayPal integration, order management, and transaction tracking.
            </p>
          </div>
        </Col>
      </Row>

      <Row className="mb-4">
        <Col md={4}>
          <Card className="h-100">
            <Card.Body className="text-center">
              <FaShoppingCart size={48} className="text-primary mb-3" />
              <Card.Title>Order Management</Card.Title>
              <Card.Text>
                Create and manage orders with ease. Track order status and payment information in real-time.
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card className="h-100">
            <Card.Body className="text-center">
              <FaLock size={48} className="text-primary mb-3" />
              <Card.Title>Secure Payments</Card.Title>
              <Card.Text>
                Process payments securely through PayPal's trusted payment gateway with buyer and seller protection.
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card className="h-100">
            <Card.Body className="text-center">
              <FaHistory size={48} className="text-primary mb-3" />
              <Card.Title>Transaction History</Card.Title>
              <Card.Text>
                View complete payment history with detailed transaction records and refund capabilities.
              </Card.Text>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="justify-content-center">
        <Col md={6} className="text-center">
          {!isAuthenticated ? (
            <div>
              <h3 className="mb-3">Get Started</h3>
              <Link to="/register" className="btn btn-primary btn-lg me-2">
                Sign Up Now
              </Link>
              <Link to="/login" className="btn btn-outline-primary btn-lg">
                Login
              </Link>
            </div>
          ) : (
            <Link to="/dashboard" className="btn btn-primary btn-lg">
              Go to Dashboard
            </Link>
          )}
        </Col>
      </Row>
    </Container>
  );
};

export default Home;
