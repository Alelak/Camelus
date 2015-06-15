package com.devsolutions.camelus.controllers;


import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;







import util.Choice;







import com.devsolutions.camelus.entities.Commission;
import com.devsolutions.camelus.entities.Vendor;
import com.devsolutions.camelus.managers.CommissionManager;


import com.devsolutions.camelus.managers.VendorManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VendorEditSceneController implements Initializable{
	@FXML
	TextField textUsername;
	@FXML
	TextField textPassword;
	@FXML
	TextField textFname;
	@FXML
	TextField textLname;
	@FXML
	DatePicker hireDate;
	@FXML
	TextField textSin;
	@FXML
	ChoiceBox<Choice> commission;
	@FXML
	Button btnEdit;
	@FXML
	Button btnCancel;
	
	List<Commission> commissions;
    ObservableList<Choice> commissionChoices;
    
    VendorTableViewController vendorTVConroller;
    
    private Stage stage;
    private Vendor vendorToUpdate;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		commissions = CommissionManager.getAll();
		
		commissionChoices = FXCollections.observableArrayList();
		commissionChoices.add(new Choice(0, "No selection"));
	    
		for (Commission commission : commissions) {
	    	if(commission.getType() == 0)
	    		commissionChoices.add(new Choice(commission.getId(), commission.getRate() + "%"));
	    	else
	    		commissionChoices.add(new Choice(commission.getId(), "Fixe : " + commission.getRate() + "(>=" + commission.getMcondition() + ")"));
	    }
	    
	    commission.setItems(commissionChoices);
	    commission.getSelectionModel().select(0);
		
	    btnEdit.setOnAction(e->{
			int sin = -1;
			
			if(textSin.getText().matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]"))
				sin = Integer.parseInt(textSin.getText());
			stage = (Stage) btnEdit.getScene().getWindow();
			
			
			if(!textUsername.getText().isEmpty() && !textPassword.getText().isEmpty() &&
				 !textFname.getText().isEmpty() && !textLname.getText().isEmpty() &&
				 !textSin.getText().isEmpty()&& sin>=0 && hireDate.getValue() != null &&
				 commission.getSelectionModel().getSelectedIndex() != 0)
			{
				SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
				
				Date date = new Date();
				try {
					date = ft.parse(hireDate.getValue().toString());
				} catch (ParseException excep) {
					excep.printStackTrace();
				}
				
				Vendor vendor = new Vendor();
				vendor.setId(vendorToUpdate.getId());
				vendor.setLogin(textUsername.getText());
				vendor.setPassword(textPassword.getText());
				vendor.setFname(textFname.getText());
				vendor.setLname(textLname.getText());
				vendor.setSin(sin);
				vendor.setCommission_id(commission.getSelectionModel().getSelectedIndex());
				vendor.setHire_date(date);
				
				VendorManager.update(vendor);
				
				vendorTVConroller.refreshTableView();
				stage.close();
				
			}else
			{
				System.out.println("Tous les champs doivent être remplis");
			}
		});
		
		btnCancel.setOnAction(e->{
			stage = (Stage) btnCancel.getScene().getWindow();
			stage.close();
		});
	}
	
	private void initForm()
	{
		textUsername.setText(vendorToUpdate.getLogin());
		textPassword.setText(vendorToUpdate.getPassword());
		textFname.setText(vendorToUpdate.getFname());
		textLname.setText(vendorToUpdate.getLname());
		
		Calendar cal = Calendar.getInstance();
	    cal.setTime(vendorToUpdate.getHire_date());
	    int year = cal.get(Calendar.YEAR);
	    int month = cal.get(Calendar.MONTH);
	    int day = cal.get(Calendar.DAY_OF_MONTH);
		
		hireDate.setValue(LocalDate.of(year, month+1, day));

		textSin.setText(String.valueOf(vendorToUpdate.getSin()));
		commission.getSelectionModel().select(vendorToUpdate.getCommission_id());
	}
	
	public void initData(VendorTableViewController vendorTVConroller,Vendor vendorToUpdate){
		this.vendorTVConroller = vendorTVConroller;
		this.vendorToUpdate = vendorToUpdate;
		initForm();
	}
	
}
