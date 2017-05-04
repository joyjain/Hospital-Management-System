/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.login;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.Persistence;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;

import database.entities.AccountDetails;
import database.jpa.AccountDetailsJpaController;
import hospital_management_system.Datastore;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Joy Jain
 */
public class LoginController implements Initializable {

	@FXML
	private JFXTextField username;
	@FXML
	private JFXPasswordField password;
	@FXML
	private JFXSnackbar snackbar;
	@FXML
	private VBox vbox;
	@FXML
	private JFXProgressBar progress;

	// creating a pool for completable future to use
	private static ExecutorService service = Executors.newCachedThreadPool();

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		snackbar = new JFXSnackbar(vbox);
	}

	@FXML
	private void handleLoginAction(ActionEvent event) {
		// run code on a different thread
		Datastore ds = Datastore.getInstance();
		Stage stage = ds.getPrimaryStage();
		progress.setVisible(true);
		CompletableFuture.runAsync(() -> {
			String usertext = username.getText();
			AccountDetailsJpaController admindetails = new AccountDetailsJpaController(
					Persistence.createEntityManagerFactory("Hospital-Management-System"));
			AccountDetails details = admindetails.findAccountDetails(usertext);
			String dbpass = details.getPassword(), pass = password.getText();
			if (!usertext.isEmpty() && usertext.equals(admindetails.findAccountDetails(usertext).getUsername())
					&& dbpass.equals(pass)) {
				// check type of user and go to the corresponding page
				Platform.runLater(() -> {
					showPage(stage, details.getType());
				});
			} else {
				snackbar.enqueue(new SnackbarEvent("Incorrect Username or Password"));
				progress.setVisible(false);
			}
		}, service);
	}

	private void showPage(Stage stage, String type) {
		try {
			Parent root = FXMLLoader
					.load(getClass().getResource(String.format("/view/%s/%s.fxml", type, capsFirst(type))));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private String capsFirst(String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}
}