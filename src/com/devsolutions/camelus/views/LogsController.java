package com.devsolutions.camelus.views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.devsolutions.camelus.auditing.GenerateAudits;
import com.devsolutions.camelus.utils.Choice;
import com.devsolutions.camelus.utils.FXUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LogsController implements Initializable {

	@FXML
	private GridPane titleBar;
	@FXML
	private Label lblClose;
	@FXML
	private Button browseBtn;
	@FXML
	private TextField filepathTxt;
	@FXML
	private Label msgTxt;
	@FXML
	private Button exportBtn;
	@FXML
	private ChoiceBox<Choice> durationChoiceBox;
	private ObservableList<Choice> durationOb = FXCollections
			.observableArrayList();

	private Service<Void> generateAuditService;
	private String filename;
	private int duration;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initChoiceBox();
		FXUtils.addDraggableNode(titleBar);
		lblClose.setOnMouseClicked(e -> {
			((Stage) lblClose.getScene().getWindow()).close();
		});
		generateAuditService = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						try {
							GenerateAudits.generate(filename, duration);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return null;
					}

				};
			}

		};
		generateAuditService.setOnRunning(e -> {
			msgTxt.setText("En Cours...");
		});
		generateAuditService.setOnSucceeded(e -> {
			msgTxt.setText("Vos logs on a était sauvegarder avec succès");
			generateAuditService.reset();
			filepathTxt.clear();
			exportBtn.setDisable(true);
		});
		generateAuditService
				.setOnFailed(e -> {
					msgTxt.setText("Une erreur s'est produite. S'il vous plaît réessayer plus tard.");
					generateAuditService.reset();
				});

		browseBtn
				.setOnAction(e -> {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"yyyyMMddhhmmss");
					filename = "Camelus" + simpleDateFormat.format(new Date());
					FileChooser fileChooser = new FileChooser();
					File defaultDirectory = new File(System
							.getProperty("user.home") + "/Documents");
					fileChooser.setInitialDirectory(defaultDirectory);
					fileChooser.setTitle("Exporter les Logs");
					fileChooser.setInitialFileName(filename);
					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
							"Log Files", "*.log");
					fileChooser.getExtensionFilters().add(extFilter);

					File savedFile = fileChooser.showSaveDialog(browseBtn
							.getScene().getWindow());
					if (savedFile != null) {
						msgTxt.setText("");
						filename = savedFile.getAbsolutePath();
						filepathTxt.setText(filename);
						exportBtn.setDisable(false);
					}
				});
		exportBtn.setOnAction(e -> {
			duration = durationChoiceBox.getSelectionModel().getSelectedItem()
					.getId();
			generateAuditService.start();

		});
	}

	private void initChoiceBox() {
		durationOb.addAll(new Choice(1, "Dernière heure"), new Choice(24,
				"24 dernières heures"), new Choice(72, "Trois derniers jours"),
				new Choice(168, "La semaine dernière"), new Choice(730,
						"Le mois dernier"));
		durationChoiceBox.setItems(durationOb);
		durationChoiceBox.getSelectionModel().select(0);
	}
}
