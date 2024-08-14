package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import logic.Message;
import logic.MessageType;
import logic.User;
import logic.UserType;

/**
 * Controller for the Create Account page in the application. This class handles
 * the creation of a new user account by validating the input fields, sending
 * user details to the database, and managing the transition back to the Manager
 * Home Page upon successful registration.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class CreateAccountController implements IControl, Initializable {
	/**
	 * TextField where the user inputs their first name.
	 */
	@FXML
	private TextField FNtxtfield;

	/**
	 * TextField where the user inputs their last name.
	 */
	@FXML
	private TextField LNtxtfield;

	/**
	 * TextField where the user inputs their username.
	 */
	@FXML
	private TextField UNtxtfield;

	/**
	 * TextField where the user inputs their password.
	 */
	@FXML
	private TextField Passtxtfield;

	/**
	 * TextField where the user inputs their user type.
	 */
	@FXML
	private TextField UserTypetxtfield;

	/**
	 * TextField where the user inputs their home branch ID.
	 */
	@FXML
	private TextField HomeBranchtxtfield;

	/**
	 * TextField where the user inputs their phone number.
	 */
	@FXML
	private TextField Phonetxtfield;

	/**
	 * TextField where the user inputs their email address.
	 */
	@FXML
	private TextField Emailtxtfield;

	/**
	 * ComboBox where the user selects the account type (Private or Business).
	 */
	@FXML
	private ComboBox<String> AccountTypeCB;

	/**
	 * TextField where the user inputs their credit card number (if applicable).
	 */
	@FXML
	private TextField CreditCardtxtfield;

	/**
	 * Button that creates a new account when clicked.
	 */
	@FXML
	private Button btnCreateAccount;

	/**
	 * Button that navigates back to the Manager Home Page.
	 */
	@FXML
	private Button backBtn;

	/**
	 * Handles the action of creating a new account by validating input fields and
	 * sending user details to the database.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the account creation process.
	 */
	@FXML
	void getCreateAccountBtn(ActionEvent event) throws Exception {
		if (!areAllFieldsFilled()) {
			showAlert("Validation Error", "Please fill in all fields.");
		} else {
			User customer = new User(0, FNtxtfield.getText(), LNtxtfield.getText(), UNtxtfield.getText(),
					Passtxtfield.getText(), UserType.Customer, ChatClient.currUser.getBranchID(),
					Phonetxtfield.getText(), Emailtxtfield.getText(), CreditCardtxtfield.getText(), 0, 0,
					AccountTypeCB.getValue());
			ArrayList<Object> msgList = new ArrayList<>();
			msgList.add(customer);
			Message msg = new Message(MessageType.AddUserToDB, msgList);
			ClientUI.chat.accept(msg);
			if (ChatClient.lastAction.equals("User added successfully.")) {
				showAlert("Register Successfull", "User added successfully.");
				getBackBtn(event);
			} else
				showAlert("Register Failed", "Username already exists.");

		}
	}

	/**
	 * Checks if all input fields are filled and valid.
	 * 
	 * @return True if all fields are filled; false otherwise.
	 */
	private boolean areAllFieldsFilled() {
		// Check if any TextField is empty
		if (FNtxtfield.getText().isEmpty() || LNtxtfield.getText().isEmpty() || UNtxtfield.getText().isEmpty()
				|| Passtxtfield.getText().isEmpty() || UserTypetxtfield.getText().isEmpty()
				|| HomeBranchtxtfield.getText().isEmpty() || Phonetxtfield.getText().isEmpty()
				|| Emailtxtfield.getText().isEmpty()) {
			return false;
		}

		// Check if the ComboBox has a selected value
		if (AccountTypeCB.getValue() == null || AccountTypeCB.getValue().isEmpty()) {
			return false;
		}
		if (AccountTypeCB.getValue().equals("Private") && CreditCardtxtfield.getText().isEmpty())
			return false;
		return true;

	}

	/**
	 * Starts the CreateAccountController and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/CreateAccount.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Create Account Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the action of navigating back to the Manager Home Page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	@Override
	public void getBackBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ManagerHomePageController mhpc = new ManagerHomePageController();
		mhpc.start(new Stage());
	}

	/**
	 * Displays an alert with the specified title and message.
	 * 
	 * @param title   The title of the alert.
	 * @param message The message to be displayed in the alert.
	 */
	public void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	/**
	 * Initializes the controller after its root element has been completely
	 * processed.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChatClient.currController = this;
		HomeBranchtxtfield.setText(String.valueOf(ChatClient.currUser.getBranchID()));
		AccountTypeCB.setItems(FXCollections.observableArrayList("Private", "Business"));
		CreditCardtxtfield.setVisible(false);
	}

	/**
	 * Handles the action when the account type ComboBox value changes, showing or
	 * hiding the credit card field based on the selected account type.
	 * 
	 * @param event The event triggered by the ComboBox value change.
	 */
	@FXML
	void AccountTypeCBChanged(ActionEvent event) {
		Parent root = ((Node) event.getSource()).getScene().getRoot();
		String newValue = AccountTypeCB.getValue();
		if ("Private".equals(newValue)) {
			root.getStyleClass().add("private");
			CreditCardtxtfield.setVisible(true);
		} else if ("Business".equals(newValue)) {
			root.getStyleClass().remove("private");
			CreditCardtxtfield.clear();
			CreditCardtxtfield.setVisible(false);
		}
	}
}
