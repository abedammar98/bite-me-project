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
 * Controller for the CEO's home page in the application. This class manages the
 * navigation between different views accessible to the CEO, such as viewing
 * general reports and quarterly reports, as well as handling logout
 * functionality.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class CEOHomePageController implements IControl, Initializable {

	/**
	 * Button that navigates the user to the general reports view.
	 */
	@FXML
	private Button btnViewReports;

	/**
	 * Button that navigates the user to the quarterly reports view.
	 */
	@FXML
	private Button btnQuarterReports;

	/**
	 * Label that displays a greeting message with the CEO's first name.
	 */
	@FXML
	private Label idHelloLbl;

	/**
	 * Button that logs out the current user and returns to the login page.
	 */
	@FXML
	private Button btnLogout;

	/**
	 * Starts the CEOHomePageController and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/CEOHomePage.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/CEOHomePage.css").toExternalForm());
		primaryStage.setTitle("Manager Home Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the action of navigating to the general reports view.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	public void getViewReportsBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ViewCEOReportsController vcrc = new ViewCEOReportsController();
		vcrc.start(new Stage());
	}

	/**
	 * Handles the action of navigating to the quarterly reports view.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	public void getViewQuarterReportsBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ViewCEOQuarterReportsController vcqrc = new ViewCEOQuarterReportsController();
		vcqrc.start(new Stage());
	}

	/**
	 * Handles the action of logging out the current user and returning to the login
	 * page.
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
