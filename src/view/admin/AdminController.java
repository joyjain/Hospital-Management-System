package view.admin;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.Persistence;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextArea;

import database.entities.AccountDetails;
import database.entities.DoctorDetails;
import database.entities.PatientDetails;
import database.jpa.AccountDetailsJpaController;
import database.jpa.DoctorDetailsJpaController;
import database.jpa.PatientDetailsJpaController;
import database.jpa.exceptions.IllegalOrphanException;
import database.jpa.exceptions.PreexistingEntityException;
import hospital_management_system.Datastore;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.admin.AdminController.Doctor;
import view.login.LoginController;

/**
 * FXML Controller class
 *
 * @author Joy Jain
 */
public class AdminController implements Initializable {

	@FXML
	private StackPane stackpane;

	@FXML
	private JFXDialog addDoctor;

	@FXML
	private JFXTextField name;

	@FXML
	private JFXTextArea address;

	@FXML
	private JFXComboBox<?> department;

	@FXML
	private ToggleGroup gender;

	@FXML
	private JFXTextField username;

	@FXML
	private JFXPasswordField password;

	@FXML
	private JFXPasswordField confirmpassword;

	@FXML
	private JFXDialog addPatient;

	@FXML
	private JFXTextField pname;
	
	@FXML
	private JFXTextField page;
	
	@FXML
	private JFXTextField pweight;
	
	@FXML
	private ToggleGroup pgender;

	@FXML
	private JFXTextArea paddress;
	
	@FXML
	private JFXTextField pcontactno;
	
	@FXML
	private JFXComboBox<Label> pdoctorname;
	
	@FXML
	private JFXTextField pdisease;

	@FXML
	private JFXTextField pusername;

	@FXML
	private JFXPasswordField ppassword;

	@FXML
	private JFXPasswordField pconfirmpassword;

	@FXML
	private JFXTreeTableView<Doctor> doctorTable;

	@FXML
	private JFXTreeTableView<Patient> patientTable;

	@FXML
	private TreeItem<Doctor> doctorItem;

	// run code on a different thread
	private Datastore ds;
	
	// doctor id list
	private List<Integer> doctorids;

	// creating a pool for completable future to use
	private static ExecutorService service = Executors.newCachedThreadPool();

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ds = Datastore.getInstance();
		patientDetails();
		doctorDetails();
	}

	private void doctorDetails() {
		JFXTreeTableColumn<Doctor, Integer> idColumn = new JFXTreeTableColumn<>("Id");
		idColumn.setPrefWidth(20);
		idColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Doctor, Integer> param) -> {
			if (idColumn.validateValue(param))
				return param.getValue().getValue().idProperty().asObject();
			else
				return idColumn.getComputedValue(param);
		});
		JFXTreeTableColumn<Doctor, String> nameColumn = new JFXTreeTableColumn<>("Name");
		nameColumn.setPrefWidth(40);
		nameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Doctor, String> param) -> {
			if (nameColumn.validateValue(param))
				return param.getValue().getValue().nameProperty();
			else
				return nameColumn.getComputedValue(param);
		});
		JFXTreeTableColumn<Doctor, String> addressColumn = new JFXTreeTableColumn<>("Address");
		addressColumn.setPrefWidth(40);
		addressColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Doctor, String> param) -> {
			if (addressColumn.validateValue(param))
				return param.getValue().getValue().addressProperty();
			else
				return addressColumn.getComputedValue(param);
		});
		JFXTreeTableColumn<Doctor, String> departmentColumn = new JFXTreeTableColumn<>("Department");
		departmentColumn.setPrefWidth(40);
		departmentColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Doctor, String> param) -> {
			if (departmentColumn.validateValue(param))
				return param.getValue().getValue().departmentProperty();
			else
				return departmentColumn.getComputedValue(param);
		});
		JFXTreeTableColumn<Doctor, String> genderColumn = new JFXTreeTableColumn<>("Gender");
		genderColumn.setPrefWidth(40);
		genderColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Doctor, String> param) -> {
			if (genderColumn.validateValue(param))
				return param.getValue().getValue().genderProperty();
			else
				return genderColumn.getComputedValue(param);
		});
		CompletableFuture.runAsync(() -> {
			doctorids = new ArrayList();
			DoctorDetailsJpaController doctordetails = new DoctorDetailsJpaController(
					Persistence.createEntityManagerFactory("Hospital-Management-System"));
			// convert doctor details to doctor class style
			ObservableList<Doctor> doctors = FXCollections.observableArrayList();
			for (DoctorDetails d : doctordetails.findDoctorDetailsEntities()) {
				doctors.add(new Doctor(d.getDoctorId(), d.getName(), d.getAddress(), d.getDepartment(), d.getGender()));
				doctorids.add(d.getDoctorId());
				pdoctorname.getItems().add(new Label(d.getName()));
			}
			// build tree
			doctorItem = new RecursiveTreeItem<Doctor>(doctors, RecursiveTreeObject::getChildren);
			doctorTable.setRoot(doctorItem);
			doctorTable.setShowRoot(false);
			doctorTable.getColumns().setAll(idColumn, nameColumn, addressColumn, departmentColumn, genderColumn);
		}, service);
	}

	private void patientDetails() {
		JFXTreeTableColumn<Patient, Integer> idColumn = new JFXTreeTableColumn<>("Id");
		idColumn.setPrefWidth(20);
		idColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Patient, Integer> param) -> {
			if (idColumn.validateValue(param))
				return param.getValue().getValue().idProperty().asObject();
			else
				return idColumn.getComputedValue(param);
		});
		JFXTreeTableColumn<Patient, String> nameColumn = new JFXTreeTableColumn<>("Name");
		nameColumn.setPrefWidth(40);
		nameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Patient, String> param) -> {
			if (nameColumn.validateValue(param))
				return param.getValue().getValue().nameProperty();
			else
				return nameColumn.getComputedValue(param);
		});
		JFXTreeTableColumn<Patient, Integer> ageColumn = new JFXTreeTableColumn<>("Age");
		ageColumn.setPrefWidth(20);
		ageColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Patient, Integer> param) -> {
			if (ageColumn.validateValue(param))
				return param.getValue().getValue().ageProperty().asObject();
			else
				return ageColumn.getComputedValue(param);
		});
		JFXTreeTableColumn<Patient, Integer> weightColumn = new JFXTreeTableColumn<>("Weight");
		weightColumn.setPrefWidth(20);
		weightColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Patient, Integer> param) -> {
			if (weightColumn.validateValue(param))
				return param.getValue().getValue().weightProperty().asObject();
			else
				return weightColumn.getComputedValue(param);
		});
		JFXTreeTableColumn<Patient, String> genderColumn = new JFXTreeTableColumn<>("Gender");
		genderColumn.setPrefWidth(20);
		genderColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Patient, String> param) -> {
			if (genderColumn.validateValue(param))
				return param.getValue().getValue().genderProperty();
			else
				return genderColumn.getComputedValue(param);
		});
		JFXTreeTableColumn<Patient, String> addressColumn = new JFXTreeTableColumn<>("Address");
		addressColumn.setPrefWidth(40);
		addressColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Patient, String> param) -> {
			if (addressColumn.validateValue(param))
				return param.getValue().getValue().addressProperty();
			else
				return addressColumn.getComputedValue(param);
		});
		JFXTreeTableColumn<Patient, Integer> contactNoColumn = new JFXTreeTableColumn<>("Contact No");
		contactNoColumn.setPrefWidth(20);
		contactNoColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Patient, Integer> param) -> {
			if (contactNoColumn.validateValue(param))
				return param.getValue().getValue().contactNoProperty().asObject();
			else
				return contactNoColumn.getComputedValue(param);
		});
		JFXTreeTableColumn<Patient, String> diseaseColumn = new JFXTreeTableColumn<>("Disease");
		diseaseColumn.setPrefWidth(40);
		diseaseColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Patient, String> param) -> {
			if (diseaseColumn.validateValue(param))
				return param.getValue().getValue().diseaseProperty();
			else
				return diseaseColumn.getComputedValue(param);
		});
		CompletableFuture.runAsync(() -> {
			PatientDetailsJpaController patientdetails = new PatientDetailsJpaController(
					Persistence.createEntityManagerFactory("Hospital-Management-System"));
			// convert patient details to patient class style
			ObservableList<Patient> patients = FXCollections.observableArrayList();
			for (PatientDetails p : patientdetails.findPatientDetailsEntities()) {
				patients.add(new Patient(p.getPatientId(), p.getName(), p.getAge(), p.getWeight(), p.getGender(),
						p.getAddress(), p.getContactNo(), p.getDisease()));
			}
			patients.add(new Patient(1, "yolo", 25, 67, "female", "ysds", 123456789, "typhoid"));
			// build tree
			patientTable.setRoot(new RecursiveTreeItem<Patient>(patients, RecursiveTreeObject::getChildren));
			patientTable.setShowRoot(false);
			patientTable.getColumns().setAll(idColumn, nameColumn, ageColumn, weightColumn, genderColumn, addressColumn,
					contactNoColumn, diseaseColumn);
		}, service);
	}

	@FXML
	private void handleOkDoctorAction(ActionEvent event) {
		CompletableFuture.runAsync(() -> {
			// create a new user
			AccountDetailsJpaController accountdetails = new AccountDetailsJpaController(
					Persistence.createEntityManagerFactory("Hospital-Management-System"));
			DoctorDetailsJpaController doctordetails = new DoctorDetailsJpaController(
					Persistence.createEntityManagerFactory("Hospital-Management-System"));
			AccountDetails acc = new AccountDetails();
			DoctorDetails doc = new DoctorDetails();
			acc.setUsername(username.getText());
			acc.setPassword(password.getText());
			acc.setType("doctor");
			accountdetails.create(acc);
			acc = accountdetails.findAccountDetails(username.getText());
			doc.setAccountDetails(acc);
			doc.setDoctorId(acc.getId());
			doc.setName(name.getText());
			doc.setAddress(address.getText());
			Label lb = (Label) department.getSelectionModel().getSelectedItem();
			doc.setDepartment(lb.getText());
			JFXRadioButton chk = (JFXRadioButton) gender.getSelectedToggle();
			doc.setGender(chk.getText());
			doc.setUsername(username.getText());
			doc.setPassword(password.getText());
			try {
				doctordetails.create(doc);
			} catch (IllegalOrphanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PreexistingEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}, service);
	}

	@FXML
	private void handleCancelDoctorAction(ActionEvent event) {

	}

	@FXML
	private void handleAddDoctorAction(ActionEvent event) {
		addDoctor.show(stackpane);
	}

	@FXML
	private void handleOkPatientAction(ActionEvent event) {
		CompletableFuture.runAsync(() -> {
			// create a new user
			AccountDetailsJpaController accountdetails = new AccountDetailsJpaController(
					Persistence.createEntityManagerFactory("Hospital-Management-System"));
			PatientDetailsJpaController patientdetails = new PatientDetailsJpaController(
					Persistence.createEntityManagerFactory("Hospital-Management-System"));
			AccountDetails acc = new AccountDetails();
			PatientDetails pat = new PatientDetails();
			acc.setUsername(pusername.getText());
			acc.setPassword(ppassword.getText());
			acc.setType("patient");
			accountdetails.create(acc);
			acc = accountdetails.findAccountDetails(pusername.getText());
			pat.setAccountDetails(acc);
			pat.setPatientId(acc.getId());
			pat.setName(pname.getText());
			pat.setAge(Integer.parseInt(page.getText()));
			pat.setWeight(Integer.parseInt(pweight.getText()));
			pat.setAddress(paddress.getText());
			int docid = doctorids.get(pdoctorname.getSelectionModel().getSelectedIndex());
			pat.setDoctorId(docid);
			JFXRadioButton chk = (JFXRadioButton) pgender.getSelectedToggle();
			pat.setGender(chk.getText());
			pat.setRoomNo("0");
			pat.setUsername(pusername.getText());
			pat.setPassword(ppassword.getText());
			pat.setDisease(pdisease.getText());
			try {
				patientdetails.create(pat);
			} catch (IllegalOrphanException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PreexistingEntityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}, service);
	}

	@FXML
	private void handleCancelPatientAction(ActionEvent event) {

	}

	@FXML
	private void handleAddPatientAction(ActionEvent event) {
		addPatient.show(stackpane);
	}

	@FXML
	private void handleLogoutAction(ActionEvent event) {
		Datastore ds = Datastore.getInstance();
		Stage stage = ds.getPrimaryStage();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/login/Login.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/*
	 * data class
	 */
	class Doctor extends RecursiveTreeObject<Doctor> {
		final SimpleIntegerProperty id;
		final StringProperty name;
		final StringProperty address;
		final StringProperty department;
		final StringProperty gender;

		Doctor(int id, String name, String address, String department, String gender) {
			this.id = new SimpleIntegerProperty(id);
			this.name = new SimpleStringProperty(name);
			this.address = new SimpleStringProperty(address);
			this.department = new SimpleStringProperty(department);
			this.gender = new SimpleStringProperty(gender);
		}

		SimpleIntegerProperty idProperty() {
			return id;
		}

		StringProperty nameProperty() {
			return name;
		}

		StringProperty addressProperty() {
			return address;
		}

		StringProperty departmentProperty() {
			return department;
		}

		StringProperty genderProperty() {
			return gender;
		}
	}

	class Patient extends RecursiveTreeObject<Patient> {
		final SimpleIntegerProperty id;
		final StringProperty name;
		final SimpleIntegerProperty age;
		final SimpleIntegerProperty weight;
		final StringProperty gender;
		final StringProperty address;
		final SimpleIntegerProperty contactNo;
		final StringProperty disease;

		Patient(int id, String name, int age, int weight, String gender, String address, int contactNo,
				String disease) {
			this.id = new SimpleIntegerProperty(id);
			this.name = new SimpleStringProperty(name);
			this.age = new SimpleIntegerProperty(age);
			this.weight = new SimpleIntegerProperty(weight);
			this.gender = new SimpleStringProperty(gender);
			this.address = new SimpleStringProperty(address);
			this.contactNo = new SimpleIntegerProperty(contactNo);
			this.disease = new SimpleStringProperty(disease);
		}

		SimpleIntegerProperty idProperty() {
			return id;
		}

		StringProperty nameProperty() {
			return name;
		}

		SimpleIntegerProperty ageProperty() {
			return age;
		}

		SimpleIntegerProperty weightProperty() {
			return weight;
		}

		StringProperty genderProperty() {
			return gender;
		}

		StringProperty addressProperty() {
			return address;
		}

		SimpleIntegerProperty contactNoProperty() {
			return contactNo;
		}

		StringProperty diseaseProperty() {
			return disease;
		}
	}
}