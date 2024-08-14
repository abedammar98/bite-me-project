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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import logic.Message;
import logic.MessageType;

/**
 * Controller for the Customer Home Page in the application. This class handles
 * navigation to different functionalities available to the customer, such as
 * ordering food and receiving orders, and manages user logout and session
 * cleanup.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class CustomerHomePageController implements IControl, Initializable {
	/**
	 * ImageView displaying the user's profile image.
	 */
	@FXML
	private ImageView imageUserimg;

	/**
	 * Label displaying a greeting message with the user's first name.
	 */
	@FXML
	private Label lblUsername;

	/**
	 * Button that logs out the user and navigates back to the login page.
	 */
	@FXML
	private Button btnLogout;

	/**
	 * Button that navigates to the page for ordering food.
	 */
	@FXML
	private Button btnOrderFood;

	/**
	 * Button that navigates to the page for receiving orders.
	 */
	@FXML
	private Button btnReceiveOrder;

	/**
	 * Handles the action of navigating to the food ordering page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	public void getOrderFoodBtn(ActionEvent event) throws Exception {
		PickRestuarntController ofc = new PickRestuarntController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ofc.start(new Stage());
	}

	/**
	 * Handles the action of navigating to the order receiving page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	public void getRecieveOrderBtn(ActionEvent event) throws Exception {
		ReceiveOrderController roc = new ReceiveOrderController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		roc.start(new Stage());
	}

	/**
	 * Starts the Customer Home Page and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/CustomerHomePage.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/CustomerHomePage.css").toExternalForm());
		primaryStage.setTitle("Customer Home Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the action of logging out the user and navigating back to the login
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
		ChatClient.discountsApplied = "";
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

		lblUsername.setText("Hello " + ChatClient.currUser.getFirstName());
		ChatClient.menuItems = null;
		ChatClient.newOrder = null;
		ChatClient.selectedResturant = null;
		ChatClient.userCart = null;
		ChatClient.userOrders = null;
		ChatClient.userResturants = null;
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
}
