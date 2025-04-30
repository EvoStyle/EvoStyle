package com.example.evostyle.domain.parcel.repository;

import com.example.evostyle.domain.parcel.entity.Sender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SenderRepository extends JpaRepository<Sender,Long> {
}
