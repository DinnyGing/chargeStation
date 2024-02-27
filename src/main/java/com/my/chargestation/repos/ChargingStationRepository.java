package com.my.chargestation.repos;

import com.my.chargestation.models.ChargingStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargingStationRepository extends JpaRepository<ChargingStation, Integer> {
}