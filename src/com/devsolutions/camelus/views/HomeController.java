package com.devsolutions.camelus.views;

import java.net.URL;
import java.util.ResourceBundle;

import com.devsolutions.camelus.services.Session;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class HomeController implements Initializable {
	@FXML
	private Label welcomeMessage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (Session.vendor != null) {
			welcomeMessage.setText("Bienvenue " + Session.vendor.getFname()
					+ " " + Session.vendor.getLname());
		} else {
			welcomeMessage.setText("Bienvenue " + Session.admin.getFname()
					+ " " + Session.admin.getLname());
		}

	}
}
