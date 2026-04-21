package com.paypal.config;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.Environment;
import com.paypal.sandbox.SandboxEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public PayPalHttpClient payPalHttpClient() {
        Environment environment;
        
        if ("sandbox".equals(mode)) {
            environment = new SandboxEnvironment(clientId, clientSecret);
        } else {
            environment = new com.paypal.production.ProductionEnvironment(clientId, clientSecret);
        }
        
        return new PayPalHttpClient(environment);
    }
}
