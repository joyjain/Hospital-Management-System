/**
 * 
 */
package hospital_management_system;

import database.entities.AccountDetails;
import database.entities.RoomDetails;
import javafx.stage.Stage;

/**
 * @author Joy Jain
 *
 */
public class Datastore {
	private static Datastore myObj;

	private static Stage primaryStage;

	private static AccountDetails accountdetails;

	private static RoomDetails roomdetails;

	/**
	 * Create private constructor
	 */
	private Datastore() {

	}

	/**
	 * Create a static method to get instance.
	 */
	public static Datastore getInstance() {
		if (myObj == null) {
			myObj = new Datastore();
		}
		return myObj;
	}

	/**
	 * @return the primaryStage
	 */
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * @param primaryStage
	 *            the primaryStage to set
	 */
	public static void setPrimaryStage(Stage primaryStage) {
		Datastore.primaryStage = primaryStage;
	}

	/**
	 * @return the accountdetails
	 */
	public static AccountDetails getAccountdetails() {
		return accountdetails;
	}

	/**
	 * @param accountdetails
	 *            the accountdetails to set
	 */
	public static void setAccountdetails(AccountDetails accountdetails) {
		Datastore.accountdetails = accountdetails;
	}

	/**
	 * @return the roomdetails
	 */
	public static RoomDetails getRoomdetails() {
		return roomdetails;
	}

	/**
	 * @param roomdetails
	 *            the roomdetails to set
	 */
	public static void setRoomdetails(RoomDetails roomdetails) {
		Datastore.roomdetails = roomdetails;
	}
}
