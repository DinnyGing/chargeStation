package com.my.chargestation.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.geo.Point;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "charge_station")
public class ChargingStation {
	@Id
	Integer id;
	String title;
	String description;
	String address;
	Point geoPoint;
	@Column(name = "public")
	boolean isPublic;

	@Size(min = 1, max = 8)
	@NotNull
	@OneToMany(cascade = CascadeType.ALL)
	List<ChargingConnector> chargeConnectors;
}
