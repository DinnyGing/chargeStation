package com.my.chargestation.repos;

import com.my.chargestation.models.ChargingConnector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargingConnectorRepository extends JpaRepository<ChargingConnector, Integer> {
}