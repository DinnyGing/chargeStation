package com.my.chargestation.dto;

import lombok.Data;
import org.springframework.data.geo.Point;

import java.util.List;

@Data
public class ChargingStationDTO {
	Integer id;
	String title;
	String description;
	String address;
	Point geoPoint;
	Boolean isPublic;
	List<ChargingConnectorDTO> chargeConnectors;

}
