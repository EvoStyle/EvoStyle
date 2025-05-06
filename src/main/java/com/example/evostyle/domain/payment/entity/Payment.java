package com.example.evostyle.domain.payment.entity;

import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.payment.dto.response.TossPaymentResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "method")
    private String method;

    @Column(name = "order_name")
    private String orderName;

    @ColumnDefault("0")
    private Integer totalAmount = 0;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    private Payment(Order order, TossPaymentResponse tossResponse) {
        this.order = order;
        this.paymentKey = tossResponse.paymentKey();
        this.method = tossResponse.method();
        this.orderName = tossResponse.orderName();
        this.totalAmount = tossResponse.totalAmount();
        this.approvedAt = LocalDateTime.now();
    }

    public static Payment of(Order order, TossPaymentResponse tossResponse) {
        return new Payment(order, tossResponse);
    }
}
