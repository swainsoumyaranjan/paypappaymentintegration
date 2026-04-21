import React, { useEffect, useState } from 'react';
import { Container, Card, Button, Spinner, Alert } from 'react-bootstrap';
import { useSearchParams, useNavigate, Link } from 'react-router-dom';
import { paymentService } from '../services/paymentService';
import { FaCheckCircle } from 'react-icons/fa';

const PaymentSuccess = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const paypalOrderId = searchParams.get('token') || searchParams.get('PayerID');
    
    if (paypalOrderId) {
      capturePayment(paypalOrderId);
    } else {
      setLoading(false);
    }
  }, [searchParams]);

  const capturePayment = async (paypalOrderId) => {
    try {
      await paymentService.capturePayment(paypalOrderId);
    } catch (err) {
      setError('Payment capture failed. Please contact support.');
      console.error('Error capturing payment:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container>
      <Card className="text-center my-5">
        <Card.Body>
          {loading ? (
            <>
              <Spinner animation="border" role="status" className="mb-3">
                <span className="visually-hidden">Processing...</span>
              </Spinner>
              <Card.Title>Processing Payment...</Card.Title>
              <Card.Text>Please wait while we confirm your payment.</Card.Text>
            </>
          ) : error ? (
            <>
              <Alert variant="danger">{error}</Alert>
              <Link to="/dashboard" className="btn btn-primary">
                Go to Dashboard
              </Link>
            </>
          ) : (
            <>
              <FaCheckCircle size={64} className="text-success mb-3" />
              <Card.Title className="text-success">Payment Successful!</Card.Title>
              <Card.Text>
                Your payment has been processed successfully.
              </Card.Text>
              <div className="d-flex justify-content-center gap-2">
                <Link to="/payment-history" className="btn btn-primary">
                  View Payment History
                </Link>
                <Link to="/orders/create" className="btn btn-outline-primary">
                  Create New Order
                </Link>
              </div>
            </>
          )}
        </Card.Body>
      </Card>
    </Container>
  );
};

export default PaymentSuccess;
