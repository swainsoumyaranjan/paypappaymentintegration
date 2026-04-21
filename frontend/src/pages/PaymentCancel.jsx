import React from 'react';
import { Container, Card, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { FaTimesCircle } from 'react-icons/fa';

const PaymentCancel = () => {
  return (
    <Container>
      <Card className="text-center my-5">
        <Card.Body>
          <FaTimesCircle size={64} className="text-danger mb-3" />
          <Card.Title className="text-danger">Payment Cancelled</Card.Title>
          <Card.Text>
            Your payment has been cancelled. You can try again or contact support.
          </Card.Text>
          <div className="d-flex justify-content-center gap-2">
            <Link to="/orders/create" className="btn btn-primary">
              Try Again
            </Link>
            <Link to="/dashboard" className="btn btn-outline-secondary">
              Go to Dashboard
            </Link>
          </div>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default PaymentCancel;
