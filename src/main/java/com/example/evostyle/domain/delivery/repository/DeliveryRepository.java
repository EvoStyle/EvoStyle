package com.example.evostyle.domain.delivery.repository;

import com.example.evostyle.domain.delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>,DeliveryRepositoryCustom {
    List<Delivery> findAllByMemberId(Long memberId);
}
