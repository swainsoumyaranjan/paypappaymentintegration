package com.paypal.service;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import com.paypal.payments.Capture;
import com.paypal.payments.Refund;
import com.paypal.payments.RefundRequest;
import com.paypal.exception.PaymentException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {
    
    private final PayPalHttpClient payPalHttpClient;
    
    public PayPalService(PayPalHttpClient payPalHttpClient) {
        this.payPalHttpClient = payPalHttpClient;
    }
    
    public Order createOrder(BigDecimal amount, String currency, String description, String returnUrl, String cancelUrl) {
        try {
            OrdersCreateRequest request = new OrdersCreateRequest();
            request.prefer("return=representation");
            
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.checkoutPaymentIntent("CAPTURE");
            
            List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
            purchaseUnits.add(new PurchaseUnitRequest()
                    .amountWithBreakdown(new AmountWithBreakdown()
                            .currencyCode(currency)
                            .value(amount.toString()))
                    .description(description));
            
            orderRequest.purchaseUnits(purchaseUnits);
            
            ApplicationContext applicationContext = new ApplicationContext()
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .brandName("PayPal Integration")
                    .landingPage("BILLING")
                    .userAction("PAY_NOW");
            
            orderRequest.applicationContext(applicationContext);
            request.requestBody(orderRequest);
            
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            return response.result();
            
        } catch (IOException e) {
            throw new PaymentException("Failed to create PayPal order: " + e.getMessage());
        }
    }
    
    public Order captureOrder(String orderId) {
        try {
            OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
            request.prefer("return=representation");
            
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            return response.result();
            
        } catch (IOException e) {
            throw new PaymentException("Failed to capture PayPal order: " + e.getMessage());
        }
    }
    
    public Refund refundPayment(String captureId, BigDecimal amount, String currency) {
        try {
            RefundRequest refundRequest = new RefundRequest()
                    .amount(new com.paypal.payments.Money()
                            .currencyCode(currency)
                            .value(amount.toString()));
            
            CapturesRefundRequest request = new CapturesRefundRequest(captureId);
            request.requestBody(refundRequest);
            
            HttpResponse<Refund> response = payPalHttpClient.execute(request);
            return response.result();
            
        } catch (IOException e) {
            throw new PaymentException("Failed to process refund: " + e.getMessage());
        }
    }
    
    public Order getOrderDetails(String orderId) {
        try {
            OrdersGetRequest request = new OrdersGetRequest(orderId);
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            return response.result();
            
        } catch (IOException e) {
            throw new PaymentException("Failed to get order details: " + e.getMessage());
        }
    }
}
