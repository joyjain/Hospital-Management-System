package view.doctor;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.Persistence;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import database.entities.DoctorDetails;
import database.entities.PatientDetails;
import database.jpa.PatientDetailsJpaController;
import hospital_management_system.Datastore;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Joy Jain
 */
public class DoctorController implements Initializable {

	@FXML
	private Label did;

	@FXML
	private Label dname;

	@FXML
	private Label daddress;

	@FXML
	private Label ddepartment;

	@FXML
	private Label dgender;

	@FXML
	private JFXTreeTableView<Patient> patientTable;

	// run code on a different thread
	private Datastore ds;

	// creating a pool for completable future to use
	private static ExecutorService service = Executors.newCachedThreadPool();

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		doctorInfo();
		 patientDetails();
	}

	private void doctorInfo() {
		CompletableFuture.runAsync(() -> {
			DoctorDetails doc = Datastore.getAccountdetails().getDoctorDetails();
			did.setText(doc.getDoctorId().toString());
			dname.setText(doc.getName());
			dgender.setText(doc.getGender());
			daddress.setText(doc.getAddress());
			ddepartment.setText(doc.getDepartment());
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
	void handleLogoutAction(ActionEvent event) {
		Platform.runLater(() -> {
			Stage stage = ds.getPrimaryStage();
			try {
				Parent root = FXMLLoader.load(getClass().getResource("/view/login/Login.fxml"));
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (Exception e) {
				System.out.println(e);
			}
		});
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
