package com.my.chargestation.services;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

@Service
public class LanguageService {
	public String getMessageByLanguage(String message){
		ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
		return resourceBundle.getString(message);
	}

}
