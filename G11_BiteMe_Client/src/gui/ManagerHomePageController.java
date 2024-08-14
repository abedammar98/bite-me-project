package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logic.Message;
import logic.MessageType;

/**
 * Controller for the Manager Home Page in the application. This class handles
 * navigation to different manager-related functionalities such as viewing
 * reports and creating accounts. It also handles user logout and display of
 * user-specific information.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class ManagerHomePageController implements IControl, Initializable {

	/**
	 * Button that opens the View Reports page when clicked.
	 */
	@FXML
	private Button btnViewReports;

	/**
	 * Button that opens the Create Account page when clicked.
	 */
	@FXML
	private Button btnCreateAccount;

	/**
	 * Label that displays a personalized greeting message to the user.
	 */
	@FXML
	private Label idHelloLbl;

	/**
	 * Button that logs the user out of the application when clicked.
	 */
	@FXML
	private Button btnLogout;

	/**
	 * Starts the stage for the Manager Home Page and sets up the scene.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ManagerHomePage.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ManagerHomePage.css").toExternalForm());
		primaryStage.setTitle("Manager Home Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the action of clicking the View Reports button by navigating to the
	 * View Branch Reports page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	public void getViewReportsBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ViewBranchReportsController vbrc = new ViewBranchReportsController();
		vbrc.start(new Stage());
	}

	/**
	 * Handles the action of clicking the Create Account button by navigating to the
	 * Create Account page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	public void getCreateAccountBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		CreateAccountController cac = new CreateAccountController();
		cac.start(new Stage());
	}

	/**
	 * Handles the action of logging out the user by disconnecting the user and
	 * navigating back to the Login page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the logout process.
	 */
	@Override
	public void getBackBtn(ActionEvent event) throws Exception {
		ArrayList<Object> msgObjectList = new ArrayList<>();
		msgObjectList.add(ChatClient.currUser);
		msgObjectList.add(0);
		Message msg = new Message(MessageType.UserDisconnect, msgObjectList);
		ClientUI.chat.accept(msg);
		ChatClient.currUser = null;
		ChatClient.userOrders = null;
		ChatClient.lastAction = "";
		ChatClient.userResturants = null;
		ChatClient.selectedResturant = null;
		ChatClient.menuItems = null;
		ChatClient.userCart = null;
		ChatClient.newOrder = null;
		ChatClient.items = null;
		ChatClient.currController = null;
		ChatClient.currentOrderItem = null;
		ChatClient.orderMeals = null;
		ChatClient.discountsApplied = null;
		ChatClient.deliveryCost = 0;
		ChatClient.branchReports = null;
		ChatClient.incomeReport = null;
		ChatClient.firstquarterReport = null;
		ChatClient.secondquarterReport = null;
		ChatClient.performanceReport = null;
		ChatClient.orderReport = null;
		ChatClient.yearMonth = null;
		ChatClient.awaitResponse = false;
		ChatClient.currBranchReports = null;
		ChatClient.lastDataFetchMonth = 0;
		ChatClient.lastDataFetchMonthQuarter=0;
		ChatClient.branchQuarterReports=null;
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		LoginController lc = new LoginController();
		lc.start(new Stage());
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
		idHelloLbl.setText("Hello " + ChatClient.currUser.getFirstName());
	}

}
