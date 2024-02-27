package com.my.chargestation.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ResourceBundle;

@RestController
@AllArgsConstructor
@Slf4j
public class PageController {
	MessageSource messageSource;
	@GetMapping("/international")
	public String getInternationalPage() {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", LocaleContextHolder.getLocale());
		return resourceBundle.getString("greeting");
	}
}