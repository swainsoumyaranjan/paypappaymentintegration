import React, { useState, useEffect } from 'react';
import { Container, Card, Spinner, Alert, Row, Col, Button } from 'react-bootstrap';
import { useParams, Link } from 'react-router-dom';
import { orderService } from '../services/orderService';

const OrderDetails = () => {
  const { id } = useParams();
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchOrder();
  }, [id]);

  const fetchOrder = async () => {
    try {
      const response = await orderService.getOrder(id);
      if (response.success) {
        setOrder(response.data);
      }
    } catch (err) {
      setError('Failed to load order details');
      console.error('Error fetching order:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <Container className="text-center my-5">
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
      </Container>
    );
  }

  if (error) {
    return (
      <Container>
        <Alert variant="danger" className="my-5">
          {error}
        </Alert>
      </Container>
    );
  }

  if (!order) {
    return (
      <Container>
        <Alert variant="warning" className="my-5">
          Order not found
        </Alert>
      </Container>
    );
  }

  return (
    <Container>
      <h2 className="mb-4">Order Details</h2>

      <Card>
        <Card.Header>
          <h4>Order #{order.orderNumber}</h4>
        </Card.Header>
        <Card.Body>
          <Row>
            <Col md={6}>
              <h5>Order Information</h5>
              <table className="table">
                <tbody>
                  <tr>
                    <th>Order Number:</th>
                    <td>{order.orderNumber}</td>
                  </tr>
                  <tr>
                    <th>Amount:</th>
                    <td>${parseFloat(order.amount).toFixed(2)}</td>
                  </tr>
                  <tr>
                    <th>Currency:</th>
                    <td>{order.currency}</td>
                  </tr>
                  <tr>
                    <th>Status:</th>
                    <td>
                      <span className={`status-badge status-${order.status.toLowerCase()}`}>
                        {order.status}
                      </span>
                    </td>
                  </tr>
                  <tr>
                    <th>Description:</th>
                    <td>{order.description}</td>
                  </tr>
                </tbody>
              </table>
            </Col>
            <Col md={6}>
              <h5>Payment Details</h5>
              <table className="table">
                <tbody>
                  <tr>
                    <th>PayPal Order ID:</th>
                    <td>{order.paypalOrderId || 'N/A'}</td>
                  </tr>
                  <tr>
                    <th>PayPal Payment ID:</th>
                    <td>{order.paypalPaymentId || 'N/A'}</td>
                  </tr>
                  <tr>
                    <th>Created At:</th>
                    <td>{new Date(order.createdAt).toLocaleString()}</td>
                  </tr>
                  <tr>
                    <th>Updated At:</th>
                    <td>{new Date(order.updatedAt).toLocaleString()}</td>
                  </tr>
                </tbody>
              </table>
            </Col>
          </Row>

          <div className="mt-4">
            <Link to="/dashboard" className="btn btn-secondary me-2">
              Back to Dashboard
            </Link>
            {order.status === 'PENDING' && (
              <Link to="/orders/create" className="btn btn-primary">
                Complete Payment
              </Link>
            )}
          </div>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default OrderDetails;
