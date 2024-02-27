package com.my.chargestation.services;

import com.my.chargestation.dto.ChargingConnectorDTO;
import com.my.chargestation.dto.ChargingStationDTO;
import com.my.chargestation.models.ChargingConnector;
import com.my.chargestation.models.ChargingStation;
import com.my.chargestation.models.TypeConnector;
import com.my.chargestation.repos.ChargingConnectorRepository;
import com.my.chargestation.repos.ChargingStationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class ChargingStationService {
	LanguageService languageService;
	ChargingStationRepository chargingStationRepository;
	ChargingConnectorRepository chargingConnectorRepository;
	public Map<String, String> validateChargingStation(ChargingStationDTO chargingStationDTO){
		Map<String, String> errors = new HashMap<>();
		checkId(chargingStationDTO, errors);
		checkForPublicStation(chargingStationDTO, errors);
		checkConnectors(chargingStationDTO, errors);
		if(errors.isEmpty()) {
			List<ChargingConnector> connectors = chargingStationDTO.getChargeConnectors().stream().map(cc -> ChargingConnector.builder().id(cc.getId())
					.typeConnector(getType(cc.getTypeConnector())).maxPowerKW(cc.getMaxPowerKW()).build()).toList();
			chargingConnectorRepository.saveAll(connectors);
			ChargingStation chargingStation = ChargingStation.builder().id(chargingStationDTO.getId())
					.title(chargingStationDTO.getTitle())
					.description(chargingStationDTO.getDescription())
					.address(chargingStationDTO.getAddress())
					.geoPoint(chargingStationDTO.getGeoPoint())
					.isPublic(chargingStationDTO.getIsPublic())
					.chargeConnectors(connectors).build();
			chargingStationRepository.save(chargingStation);
		}
		return errors;
	}
	private void checkConnectors(ChargingStationDTO chargingStationDTO, Map<String, String> errors){
		if(checkCountOfConnectors(chargingStationDTO.getChargeConnectors())){
			List<Integer> ids =  chargingStationDTO.getChargeConnectors().stream().map(ChargingConnectorDTO::getId).toList();
			if(!isValidAllIds(ids))
				errors.put("ChargingConnectorId", languageService.getMessageByLanguage("id.wrongId"));
			else if(!isUniqueIds(ids) && !isAllUniqueId(chargingStationDTO.getChargeConnectors()))
				errors.put("ChargingConnectorId", languageService.getMessageByLanguage("id.repeatValue"));
		}
		else
			errors.put("CountOfConnectors", languageService.getMessageByLanguage("chargingConnectors.wrongCount"));
		if(!isValidAllTypes(chargingStationDTO.getChargeConnectors()))
			errors.put("TypesOfConnectors", languageService.getMessageByLanguage("connectorType.doesNotExist"));
		if(!isValidAllPowers(chargingStationDTO.getChargeConnectors()))
			errors.put("MaxPowersOfConnectors", languageService.getMessageByLanguage("maxPowerKw.wrongValue"));
	}
	private void checkForPublicStation(ChargingStationDTO chargingStationDTO, Map<String, String> errors){
		if(isValidPublic(chargingStationDTO) &&Boolean.TRUE.equals(chargingStationDTO.getIsPublic())){
			if(!isValid(chargingStationDTO.getTitle()))
				errors.put("ChargingStationTitle", languageService.getMessageByLanguage("wrongTitle"));
			if(!isValid(chargingStationDTO.getDescription()))
				errors.put("ChargingStationDescription", languageService.getMessageByLanguage("wrongDescription"));
			if(!isValid(chargingStationDTO.getAddress()))
				errors.put("ChargingStationAddress", languageService.getMessageByLanguage("wrongAddress"));
			if(chargingStationDTO.getGeoPoint() == null)
				errors.put("ChargingStationGeoPoint", languageService.getMessageByLanguage("wrongGeoCoordination"));
		}
		else
			errors.put("ChargingStationPublic", languageService.getMessageByLanguage("publicIsNotBool"));
	}
	private void checkId(ChargingStationDTO chargingStationDTO, Map<String, String> errors){
		if(!isValidValue(chargingStationDTO.getId()))
			errors.put("ChargingStationId", languageService.getMessageByLanguage("id.wrongId"));
		else if(isNotUniqueValue(chargingStationDTO))
			errors.put("ChargingStationId", languageService.getMessageByLanguage("id.repeatValue"));
	}
	private boolean isValidAllTypes(List<ChargingConnectorDTO> connectors){
		boolean valid = true;
		for(ChargingConnectorDTO connector : connectors){
			if (!isValidTypeConnector(connector.getTypeConnector()))
				valid = false;
		}
		return valid;
	}
	private boolean isValidAllPowers(List<ChargingConnectorDTO> connectors){
		boolean valid = true;
		for(ChargingConnectorDTO connector : connectors){
			if (!isValidValue(connector.getMaxPowerKW())) {
				valid = false;
				break;
			}
		}
		return valid;
	}
	private boolean checkCountOfConnectors(List<ChargingConnectorDTO> connectors){
		return !connectors.isEmpty() && connectors.size() < 9;
	}
	private boolean isValid(String text){
		return text != null && !text.isEmpty();
	}
	private boolean isValidPublic(ChargingStationDTO chargingStation){
		return chargingStation.getIsPublic() != null;
	}
	private boolean isValidValue(Integer id){
		return id != null && id > 0;
	}
	private boolean isNotUniqueValue(ChargingStationDTO chargingStationDTO){
		return chargingStationRepository.existsById(chargingStationDTO.getId());
	}
	private boolean isNotUniqueValue(ChargingConnectorDTO chargingConnectorDTO){
		return chargingConnectorRepository.existsById(chargingConnectorDTO.getId());
	}
	private boolean isValidAllIds(List<Integer> ids){
		boolean valid = true;
		for(Integer id : ids){
			if (!isValidValue(id)) {
				valid = false;
				break;
			}
		}
		return valid;
	}
	private boolean isAllUniqueId(List<ChargingConnectorDTO> connectors){
		boolean valid = true;
		for(ChargingConnectorDTO connector : connectors){
			if (isNotUniqueValue(connector))
				valid = false;
		}
		return valid;
	}
	private boolean isUniqueIds(List<Integer> ids){
		return Set.of(ids).size() == ids.size();
	}
	private boolean isValidTypeConnector(String type){
		return isCssType(type) || isChademoType(type) || isType1Type(type) || isType2Type(type);
	}
	private TypeConnector getType(String type){
		if(isCssType(type))
			return TypeConnector.CCS;
		else if (isChademoType(type))
			return TypeConnector.CHADEMO;
		else if(isType1Type(type))
			return TypeConnector.TYPE1;
		return TypeConnector.TYPE2;
	}
	private boolean isCssType(String type){
		return type.equals(languageService.getMessageByLanguage("CCS"));
	}
	private boolean isChademoType(String type){
		return type.equals(languageService.getMessageByLanguage("CHADEMO"));
	}
	private boolean isType1Type(String type){
		return type.equals(languageService.getMessageByLanguage("TYPE1"));
	}
	private boolean isType2Type(String type){
		return type.equals(languageService.getMessageByLanguage("TYPE2"));
	}
}
