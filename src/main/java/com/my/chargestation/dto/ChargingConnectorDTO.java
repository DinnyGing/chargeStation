package com.my.chargestation.dto;

import lombok.Data;

@Data
public class ChargingConnectorDTO {
	Integer id;
	String typeConnector;
	Integer maxPowerKW;
}
