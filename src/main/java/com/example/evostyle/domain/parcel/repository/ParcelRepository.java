package com.example.evostyle.domain.parcel.repository;

import com.example.evostyle.domain.parcel.entity.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcelRepository extends JpaRepository<Parcel,String> {
}
