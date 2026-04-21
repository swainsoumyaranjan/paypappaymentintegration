import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Spinner } from 'react-bootstrap';
import { orderService } from '../services/orderService';
import { FaShoppingCart, FaDollarSign, FaCheckCircle } from 'react-icons/fa';

const Dashboard = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    totalOrders: 0,
    totalAmount: 0,
    completedOrders: 0,
  });

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await orderService.getUserOrders();
      if (response.success) {
        setOrders(response.data);
        
        // Calculate stats
        const totalAmount = response.data.reduce(
          (sum, order) => sum + parseFloat(order.amount),
          0
        );
        const completedOrders = response.data.filter(
          (order) => order.status === 'COMPLETED'
        ).length;
        
        setStats({
          totalOrders: response.data.length,
          totalAmount: totalAmount,
          completedOrders: completedOrders,
        });
      }
    } catch (error) {
      console.error('Error fetching orders:', error);
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

  return (
    <Container>
      <h2 className="mb-4">Dashboard</h2>

      <Row className="mb-4">
        <Col md={4}>
          <Card className="text-center">
            <Card.Body>
              <FaShoppingCart size={32} className="text-primary mb-2" />
              <Card.Title>Total Orders</Card.Title>
              <h3>{stats.totalOrders}</h3>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card className="text-center">
            <Card.Body>
              <FaDollarSign size={32} className="text-success mb-2" />
              <Card.Title>Total Amount</Card.Title>
              <h3>${stats.totalAmount.toFixed(2)}</h3>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card className="text-center">
            <Card.Body>
              <FaCheckCircle size={32} className="text-info mb-2" />
              <Card.Title>Completed</Card.Title>
              <h3>{stats.completedOrders}</h3>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Card>
        <Card.Header>Recent Orders</Card.Header>
        <Card.Body>
          {orders.length === 0 ? (
            <p className="text-center text-muted">No orders yet. Create your first order!</p>
          ) : (
            <div className="table-responsive">
              <table className="table table-hover">
                <thead>
                  <tr>
                    <th>Order Number</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Date</th>
                  </tr>
                </thead>
                <tbody>
                  {orders.slice(0, 10).map((order) => (
                    <tr key={order.id}>
                      <td>{order.orderNumber}</td>
                      <td>${parseFloat(order.amount).toFixed(2)}</td>
                      <td>
                        <span className={`status-badge status-${order.status.toLowerCase()}`}>
                          {order.status}
                        </span>
                      </td>
                      <td>{new Date(order.createdAt).toLocaleDateString()}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </Card.Body>
      </Card>
    </Container>
  );
};

export default Dashboard;
