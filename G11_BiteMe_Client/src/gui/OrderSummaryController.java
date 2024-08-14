package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import logic.Message;
import logic.MessageType;
import logic.Order;
import logic.OrderItem;
import logic.OrderItemDetail;
import client.ChatClient;
import client.ClientUI;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller for the Order Summary page in the application. This class handles
 * the display of order details and item specifics, and manages user
 * interactions for confirming or canceling the order.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class OrderSummaryController implements Initializable, IControl {

	/**
	 * Button that exits the current page and returns to the Customer Home Page.
	 */
	@FXML
	private Button btnExit;

	/**
	 * Button that confirms the order and processes it.
	 */
	@FXML
	private Button btnConfirm;

	/**
	 * ListView that displays the details of the current order.
	 */
	@FXML
	private ListView<String> listViewOrderDetails;

	/**
	 * Initializes the controller after its root element has been completely
	 * processed. It sets up the order details and populates the ListView with the
	 * information.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChatClient.currController = this;

		ArrayList<String> orderDetails = new ArrayList<>();
		String pType;
		// Add order details
		Order order = ChatClient.newOrder;
		if (order != null) {
			orderDetails.add("User ID: " + order.getUserID());
			orderDetails
					.add("Date of Order: " + order.getDateOfOrder().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			orderDetails.add("Time of Order: " + order.getTimeOfOrder());
			orderDetails.add("Restaurant: " + ChatClient.selectedResturant.getResturantName());
			orderDetails.add("Pick Up Type: " + order.getPickUpType());
			if (order.getPickUpType().equals("Delivery")) {
				orderDetails.add("Location: " + order.getLocation());
				pType = "Delivery";
				orderDetails.add("Delivery Cost: $" + String.valueOf(ChatClient.deliveryCost));
			} else
				pType = "TakeAway";

			orderDetails.add("Requested Time of " + pType + ": " + order.getRequestedTimeOfDelivery());
			orderDetails.add("Requested Date of " + pType + ": "
					+ order.getRequestedDateOfDelivery().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			if (ChatClient.discountsApplied.equals(""))
				orderDetails.add("Discounts Applied: None");
			else
				orderDetails.add("Discounts Applied: " + ChatClient.discountsApplied);
			orderDetails.add("Total Cost: $" + String.format("%.2f", order.getCost()));
		}

		// Add order item details
		ArrayList<OrderItemDetail> cart = ChatClient.userCart;
		if (cart != null && !cart.isEmpty()) {
			orderDetails.add("Order Items:");
			for (OrderItemDetail itemDetail : cart) {
				String itemString = String.format("- %s: %d x $%.2f = $%.2f (Notes: %s)",
						itemDetail.getItem().getItemName(), itemDetail.getQuantity(), itemDetail.getCost(),
						itemDetail.getQuantity() * itemDetail.getCost(), itemDetail.getNotes());
				orderDetails.add(itemString);
			}
		}

		// Populate the ListView
		listViewOrderDetails.getItems().addAll(orderDetails);
	}

	/**
	 * Handles the action of clicking the Exit button by hiding the current window
	 * and navigating back to the Customer Home Page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	@FXML
	public void getBackBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		CustomerHomePageController chpc = new CustomerHomePageController();
		chpc.start(new Stage());
	}

	/**
	 * Handles the action of clicking the Confirm button by sending the order to the
	 * database and providing feedback to the user.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the order confirmation process.
	 */
	@FXML
	public void getConfirmBtn(ActionEvent event) throws Exception {
		ArrayList<Object> msgObjList = new ArrayList<>();
		OrderItem om = new OrderItem(ChatClient.newOrder);
		om.setOrderItemDetails(ChatClient.userCart);
		msgObjList.add(om);
		Message msg = new Message(MessageType.AddOrderToDB, msgObjList);
		ClientUI.chat.accept(msg);
		if (ChatClient.lastAction.equals("Order was unsuccessful")) {
			showAlert("Order Unsuccessful", "Your order was not confirmed");
		} else {
			if (ChatClient.currUser.getAmountOfCopunts() > 1)
				ChatClient.currUser.setAmountOfCopunts(ChatClient.currUser.getAmountOfCopunts() - 1);
			showAlert("Order Confirmed", ChatClient.lastAction);
		}

		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		CustomerHomePageController chpc = new CustomerHomePageController();
		chpc.start(new Stage());
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
     * Starts the stage for the Order Summary page and sets up the scene.
     * 
     * @param primaryStage The primary stage for this application.
     * @throws Exception If there is an error during the initialization of the stage.
     */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/OrderSummary.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/OrderSummary.css").toExternalForm());
		primaryStage.setTitle("Login Page");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
