/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import database.jpa.AccountDetailsJpaController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javax.persistence.Persistence;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleLoginAction(ActionEvent event) {
        AccountDetailsJpaController admindetails = new AccountDetailsJpaController(
                Persistence.createEntityManagerFactory("Hospital-Management-System")
        );
        System.out.println(admindetails.findAccountDetails(username.getText()).getPassword());
//        String dbpass = admindetails.findAccountDetails(username.getText()).getPassword(),
//                pass = password.getText();
//        if (dbpass.equals(pass)) {
//            System.out.println("Success");
//        } else {
//            System.out.println("Gaand marao");
//        }
    }
}
