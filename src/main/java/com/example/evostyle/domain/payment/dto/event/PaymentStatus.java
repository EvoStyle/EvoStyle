package com.example.evostyle.domain.payment.dto.event;

public enum PaymentStatus {
    IN_PROGRESS, // 결제창 띄워진 상태 (Toss로 넘어간 시점)
    CONFIRMED,  // Toss confirm 성공
    FAILED,     // Toss confirm 실패
    CANCELED    //
}
