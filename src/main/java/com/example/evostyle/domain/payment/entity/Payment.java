package com.example.evostyle.domain.payment.entity;

import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.payment.dto.event.PaymentStatus;
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

    @Enumerated(EnumType.STRING)
    public PaymentStatus paymentStatus;

    @ColumnDefault("0")
    private Integer totalAmount = 0;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    private Payment(Order order, String paymentKey, Integer totalAmount) {
        this.order = order;
        this.paymentKey = paymentKey;
        this.totalAmount = totalAmount;
        this.paymentStatus = PaymentStatus.IN_PROGRESS;
    }

    public static Payment of(Order order, String paymentKey, Integer totalAmount) {
        return new Payment(order, paymentKey, totalAmount);
    }

    public void syncState(String paymentKey, PaymentStatus paymentStatus, Integer totalAmount, String method,
                          LocalDateTime approvedAt, LocalDateTime canceledAt
    ){
        this.paymentKey = paymentKey;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.method = method;
        this.approvedAt = approvedAt;
        this.canceledAt = canceledAt;
    }
}
