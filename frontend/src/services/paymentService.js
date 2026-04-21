import api from './api';

export const paymentService = {
  createPayPalOrder: async (orderData) => {
    const response = await api.post('/payments/paypal/create-order', orderData);
    return response.data;
  },

  capturePayment: async (paypalOrderId) => {
    const response = await api.post(`/payments/paypal/capture/${paypalOrderId}`);
    return response.data;
  },

  refundPayment: async (transactionId, refundData) => {
    const response = await api.post(`/payments/paypal/refund/${transactionId}`, refundData);
    return response.data;
  },

  getPaymentDetails: async (paypalOrderId) => {
    const response = await api.get(`/payments/paypal/details/${paypalOrderId}`);
    return response.data;
  },
};
