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
import com.jfoenix.controls.JFXTextField;

import database.entities.AccountDetails;
import database.jpa.AccountDetailsJpaController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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

	// creating a pool for completable future to use
	private static ExecutorService service = Executors.newCachedThreadPool();

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

	@FXML
	private void handleLoginAction(ActionEvent event) {
		// run code on a different thread
		CompletableFuture.runAsync(() -> {
			String usertext = username.getText();
			AccountDetailsJpaController admindetails = new AccountDetailsJpaController(
					Persistence.createEntityManagerFactory("Hospital-Management-System"));
			AccountDetails details = admindetails.findAccountDetails(usertext);
			String dbpass = details.getPassword(), pass = password.getText();
			if (!usertext.isEmpty() && usertext.equals(admindetails.findAccountDetails(usertext).getUsername())
					&& dbpass.equals(pass)) {
				// check type of user and go to the corresponding page
				System.out.println(details.getType());
			}
			else {
				System.out.println("Not success");
			}
		}, service);
	}
}