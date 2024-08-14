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
 * Controller for the Supplier Home Page in the application. This class manages
 * the interactions on the supplier's home page, including updating the menu,
 * managing orders, and logging out.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class SupplierHomePageController implements IControl, Initializable {

	/**
	 * Button that allows the supplier to update the menu.
	 */
	@FXML
	private Button btnUpdateMenu;

	/**
	 * Button that allows the supplier to view and manage orders.
	 */
	@FXML
	private Button btnUpdateOrders;

	/**
	 * Button that logs out the supplier and returns to the login page.
	 */
	@FXML
	private Button btnLogout;

	/**
	 * Label that displays a greeting message to the supplier.
	 */
	@FXML
	private Label lblHello;

	/**
	 * Image view displaying the supplier's profile image.
	 */
	@FXML
	private ImageView imageUserimg;
	/**
	 * Handles the action of navigating to the Supplier Orders Page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	public void UpdateOrdersBtn(ActionEvent event) throws Exception {
		SupplierOrdersPageController soc = new SupplierOrdersPageController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		soc.start(new Stage());
	}
	/**
	 * Starts the SupplierHomePageController and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the stage.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/SupplierHomePage.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/SupplierHome.css").toExternalForm());
		primaryStage.setTitle("Supplier Home Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * Handles the action of logging out and navigating back to the login page.
	 * Sends a user disconnect message to the server.
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
	 * Handles the action of navigating to the Update Menu page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	public void UpdateMenuBtn(ActionEvent event) throws Exception {
		UpdateMenuController umc = new UpdateMenuController();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		umc.start(new Stage());
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
	 * Initializes the controller after its root element has been completely processed.
	 * Sets the greeting label with the current user's first name.
	 * 
	 * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChatClient.currController = this;
		lblHello.setText("Hello " + ChatClient.currUser.getFirstName());
	}

}
