package com.my.chargestation.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TypeConnector {
	CCS("CCS"), CHADEMO("CHAdeMO"), TYPE1("Type1"), TYPE2("Type2");
	private final String name2;

}
