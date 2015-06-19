package com.devsolutions.camelus.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class HomeControllers {
	@FXML private Label welcomeMessage;

	
	public void setWelcomeMessage(String message){
		welcomeMessage.setText(message);
	}
	
	
	
}
