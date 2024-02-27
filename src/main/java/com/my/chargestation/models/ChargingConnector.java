package com.my.chargestation.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "charge_connector")
public class ChargeConnector {
	@Id
	Integer id;
	@NotEmpty
	@Enumerated(EnumType.STRING)
	TypeConnector typeConnector;
	@NotNull
	@Min(1)
	@Column(name = "max_power_kW")
	Integer maxPowerKW;

}
