import React, { useState } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { PayPalButtons, usePayPalScriptReducer } from '@paypal/react-paypal-js';
import { orderService } from '../services/orderService';
import { paymentService } from '../services/paymentService';
import { toast } from 'react-toastify';

const CreateOrder = () => {
  const [formData, setFormData] = useState({
    amount: '',
    description: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [orderId, setOrderId] = useState(null);
  const [paypalOrderId, setPaypalOrderId] = useState(null);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await orderService.createOrder(formData);
      if (response.success) {
        setOrderId(response.data.id);
        toast.success('Order created successfully!');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create order');
      toast.error('Failed to create order');
    } finally {
      setLoading(false);
    }
  };

  const createPayPalOrder = async () => {
    try {
      const response = await paymentService.createPayPalOrder(formData);
      if (response.success) {
        setPaypalOrderId(response.data.paypalOrderId);
        return response.data.paypalOrderId;
      }
    } catch (err) {
      console.error('Error creating PayPal order:', err);
      throw err;
    }
  };

  const onApprove = async (data) => {
    try {
      await paymentService.capturePayment(data.orderID);
      toast.success('Payment completed successfully!');
      navigate('/payment-history');
    } catch (err) {
      console.error('Error capturing payment:', err);
      toast.error('Payment failed');
    }
  };

  const onError = (err) => {
    console.error('PayPal error:', err);
    toast.error('Payment error occurred');
  };

  return (
    <Container>
      <Row className="justify-content-center my-4">
        <Col md={8}>
          <Card>
            <Card.Header>
              <h3>Create Order</h3>
            </Card.Header>
            <Card.Body>
              {error && <Alert variant="danger">{error}</Alert>}

              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label>Amount (USD)</Form.Label>
                  <Form.Control
                    type="number"
                    name="amount"
                    value={formData.amount}
                    onChange={handleChange}
                    placeholder="Enter amount"
                    min="0.01"
                    step="0.01"
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Description</Form.Label>
                  <Form.Control
                    type="text"
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    placeholder="Enter order description"
                    required
                  />
                </Form.Group>

                <Button
                  type="submit"
                  variant="primary"
                  className="w-100 mb-3"
                  disabled={loading}
                >
                  {loading ? <Spinner animation="border" size="sm" /> : 'Create Order'}
                </Button>
              </Form>

              {orderId && (
                <div className="mt-4">
                  <h5>Pay with PayPal</h5>
                  <PayPalButtons
                    createOrder={createPayPalOrder}
                    onApprove={onApprove}
                    onError={onError}
                    style={{ layout: 'vertical' }}
                  />
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default CreateOrder;
