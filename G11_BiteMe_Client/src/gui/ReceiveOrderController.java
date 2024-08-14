package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.Message;
import logic.MessageType;
import logic.Order;

/**
 * Controller for the Receive Order page in the application. This class manages
 * the display and handling of user orders, including updating the status of
 * selected orders, refreshing the order list, and navigating back to the
 * customer home page.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class ReceiveOrderController implements Initializable, IControl {
	/**
	 * Button that navigates back to the previous screen.
	 */
	@FXML
	private Button btnBack;

	/**
	 * TableView that displays the list of orders.
	 */
	@FXML
	private TableView<Order> orderTableView;

	/**
	 * TableColumn displaying the order ID.
	 */
	@FXML
	private TableColumn<Order, Integer> orderIdCol;

	/**
	 * TableColumn displaying the date of the order.
	 */
	@FXML
	private TableColumn<Order, String> dateOfOrderCol;

	/**
	 * TableColumn displaying the time of the order.
	 */
	@FXML
	private TableColumn<Order, String> timeOfOrderCol;

	/**
	 * TableColumn displaying the restaurant ID associated with the order.
	 */
	@FXML
	private TableColumn<Order, Integer> restaurantIdCol;

	/**
	 * TableColumn displaying the pick-up type for the order.
	 */
	@FXML
	private TableColumn<Order, String> pickUpTypeCol;

	/**
	 * TableColumn displaying the location for the order.
	 */
	@FXML
	private TableColumn<Order, String> locationCol;

	/**
	 * TableColumn displaying the current status of the order.
	 */
	@FXML
	private TableColumn<Order, String> statusCol;

	/**
	 * TableColumn displaying the requested time of delivery.
	 */
	@FXML
	private TableColumn<Order, String> requestedTimeCol;

	/**
	 * TableColumn displaying the requested date of delivery.
	 */
	@FXML
	private TableColumn<Order, String> requestedDateCol;

	/**
	 * TableColumn displaying the estimated time of arrival (ETA).
	 */
	@FXML
	private TableColumn<Order, String> etaCol;

	/**
	 * TableColumn displaying the duration of the order.
	 */
	@FXML
	private TableColumn<Order, String> durationCol;

	/**
	 * TableColumn displaying the cost of the order.
	 */
	@FXML
	private TableColumn<Order, Double> costCol;

	/**
	 * Button that confirms the receipt of an order.
	 */
	@FXML
	private Button receiveButton;

	/**
	 * Handles the action of updating the status of a selected order. If the order
	 * status is "Ready", it updates the order status to "Received" and awards a
	 * coupon if the customer is eligible.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	void updateStatus(ActionEvent event) {
		Order selectedOrder = orderTableView.getSelectionModel().getSelectedItem();
		if (selectedOrder != null) {
			if (selectedOrder.getStatus().equals("Received")) {
				showAlert("Order Received", "You already received this order.");
				return;
			}
			// Duration duration =
			// Duration.between(selectedOrder.getRequestedDeliveryDateTime(),
			// LocalDateTime.now());
			// if (duration.isNegative()) {
			// showAlert("Order Requested Time",
			// "The current time is earlier than the requested delivery time. Please try
			// again later.");
			// return;
			// }
			if (!selectedOrder.getStatus().equals("Ready")) {
				showAlert("Order Not Ready", "Order not ready yet.");
				return;
			}
			selectedOrder.calculateDurationFromNow();
			if (selectedOrder.customerEligibleForCoupon()) {
				ChatClient.currUser.setAmountOfCopunts(ChatClient.currUser.getAmountOfCopunts() + 1);
				showAlert("Eligible For Coupon", "You were awareded 1 coupon for late delivery\nYou now have: "
						+ ChatClient.currUser.getAmountOfCopunts() + " Coupons");
			}
			ArrayList<Object> msgList = new ArrayList<>();
			msgList.add(selectedOrder);
			Message msg = new Message(MessageType.UpdateOrderStatus, msgList);
			ClientUI.chat.accept(msg);
			updateTableView();
		} else {
			showAlert("No Order Selected", "Please select an order.");
		}
	}

	/**
	 * Initializes the controller after its root element has been completely
	 * processed. Sets up the table columns and initializes data for the table view.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeTableColumns();
		init();
		ChatClient.currController = this;

	}

	/**
	 * Configures the table columns to match the properties of the Order class.
	 */
	private void initializeTableColumns() {
		try {
			orderIdCol.setCellValueFactory(new PropertyValueFactory<>("OrderID"));
			dateOfOrderCol.setCellValueFactory(new PropertyValueFactory<>("DateOfOrder"));
			timeOfOrderCol.setCellValueFactory(new PropertyValueFactory<>("TimeOfOrder"));
			restaurantIdCol.setCellValueFactory(new PropertyValueFactory<>("ResturantID"));
			pickUpTypeCol.setCellValueFactory(new PropertyValueFactory<>("PickUpType"));
			locationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
			statusCol.setCellValueFactory(new PropertyValueFactory<>("Status"));
			requestedTimeCol.setCellValueFactory(new PropertyValueFactory<>("RequestedTimeOfDelivery"));
			requestedDateCol.setCellValueFactory(new PropertyValueFactory<>("RequestedDateOfDelivery"));
			etaCol.setCellValueFactory(new PropertyValueFactory<>("ETA"));
			durationCol.setCellValueFactory(new PropertyValueFactory<>("Duration"));
			costCol.setCellValueFactory(new PropertyValueFactory<>("Cost"));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the table view by loading the current user's orders and setting
	 * them to the table view.
	 */
	private void init() {
		try {
			ArrayList<Object> msgList = new ArrayList<>();
			msgList.add(ChatClient.currUser.getUserID());

			Message msg = new Message(MessageType.GetCustomerOrders, msgList);
			ClientUI.chat.accept(msg);

			if (ChatClient.userOrders != null) {
				ObservableList<Order> orderList = FXCollections.observableArrayList(ChatClient.userOrders);
				orderTableView.setItems(orderList);
			} else {
				System.out.println("ChatClient.userOrders is null.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates the table view with the latest orders for the current user. This
	 * method ensures the update runs on the JavaFX Application Thread.
	 */
	public void updateTableView() {
		// Ensure this method runs on the JavaFX Application Thread
		Platform.runLater(() -> {
			try {
				ArrayList<Object> msgList = new ArrayList<>();
				msgList.add(ChatClient.currUser.getUserID());
				Message msg = new Message(MessageType.GetCustomerOrders, msgList);
				ClientUI.chat.accept(msg);

				// Assuming ClientUI.chat.accept(msg) is blocking until response
				if (ChatClient.userOrders != null) {
					orderTableView.getItems().clear();
					ObservableList<Order> orderList = FXCollections.observableArrayList(ChatClient.userOrders);
					orderTableView.setItems(orderList);
				} else {
					System.out.println("ChatClient.userOrders is null.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Starts the ReceiveOrderController and sets up the user interface with the
	 * FXML file and CSS styles.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ReceiveOrder.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ReceiveOrder.css").toExternalForm());
		primaryStage.setTitle("Order Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the action of navigating back to the Customer Home Page, clearing the
	 * current user’s orders and closing the current window.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	@Override
	public void getBackBtn(ActionEvent event) throws Exception {
		ChatClient.userOrders = null;
		((Node) event.getSource()).getScene().getWindow().hide();
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
}
