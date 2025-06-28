package com.example.evostyle.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Setter @Getter
@ConfigurationProperties(prefix = "payment.toss")
public class PaymentProperties {
    private String testClientKey;
    private String testSecretKey;
    private String confirmUri;
    private String cancelUri;
}
