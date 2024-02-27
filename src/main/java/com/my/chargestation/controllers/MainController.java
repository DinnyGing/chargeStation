package com.my.chargestation.controllers;

import com.my.chargestation.dto.ChargingStationDTO;
import com.my.chargestation.services.ChargingStationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@Slf4j
public class MainController {
	ChargingStationService chargingStationService;
	@PostMapping("/validate")
	public ResponseEntity<Map<String, String>> validate(@RequestBody ChargingStationDTO chargingStationDTO){

		return ResponseEntity.ok(chargingStationService.validateChargingStation(chargingStationDTO));
	}
}
