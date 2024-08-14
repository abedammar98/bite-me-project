package gui;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.CertifiedWorker;
import logic.Message;
import logic.MessageType;
import logic.Order;
import logic.OrderItemDetail;

/**
 * Controller for the Supplier Orders Page in the application. This class
 * manages the display and updating of orders, including viewing order details
 * and updating their status and ETA.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class SupplierOrdersPageController implements Initializable, IControl {
	/**
	 * Button that navigates back to the Supplier Home Page.
	 */
	@FXML
	private Button btnBack;

	/**
	 * Button that updates the selected order's status and ETA.
	 */
	@FXML
	private Button btnUpdateOrder;

	/**
	 * Text field for entering the ETA (Estimated Time of Arrival) for an order.
	 */
	@FXML
	private TextField ETAtxtField;

	/**
	 * Table view displaying details of meals for the selected order.
	 */
	@FXML
	private TableView<OrderItemDetail> mealsTableView;

	/**
	 * Table column displaying the name of the meal.
	 */
	@FXML
	private TableColumn<OrderItemDetail, String> NameMealCol;

	/**
	 * Table column displaying notes for the meal.
	 */
	@FXML
	private TableColumn<OrderItemDetail, String> MealNotesCol;

	/**
	 * Table column displaying the quantity of the meal.
	 */
	@FXML
	private TableColumn<OrderItemDetail, Integer> MealQuantityCol;

	/**
	 * Table view displaying orders.
	 */
	@FXML
	private TableView<Order> orderTableView;

	/**
	 * Table column displaying the order ID.
	 */
	@FXML
	private TableColumn<Order, Integer> orderIdCol;

	/**
	 * Table column displaying the date of the order.
	 */
	@FXML
	private TableColumn<Order, String> dateOfOrderCol;

	/**
	 * Table column displaying the time of the order.
	 */
	@FXML
	private TableColumn<Order, String> timeOfOrderCol;

	/**
	 * Table column displaying the restaurant ID associated with the order.
	 */
	@FXML
	private TableColumn<Order, Integer> restaurantIdCol;

	/**
	 * Table column displaying the pickup type (e.g., Delivery or TakeAway).
	 */
	@FXML
	private TableColumn<Order, String> pickUpTypeCol;

	/**
	 * Table column displaying the location for delivery orders.
	 */
	@FXML
	private TableColumn<Order, String> locationCol;

	/**
	 * Table column displaying the status of the order.
	 */
	@FXML
	private TableColumn<Order, String> statusCol;

	/**
	 * Table column displaying the requested time of delivery.
	 */
	@FXML
	private TableColumn<Order, String> requestedTimeCol;

	/**
	 * Table column displaying the requested date of delivery.
	 */
	@FXML
	private TableColumn<Order, String> requestedDateCol;

	/**
	 * Table column displaying the estimated time of arrival (ETA) for the order.
	 */
	@FXML
	private TableColumn<Order, String> etaCol;

	/**
	 * Table column displaying the duration of the order.
	 */
	@FXML
	private TableColumn<Order, String> durationCol;

	/**
	 * Table column displaying the cost of the order.
	 */
	@FXML
	private TableColumn<Order, Double> costCol;

	/**
	 * Combo box for selecting the status of an order.
	 */
	@FXML
	private ComboBox<String> statusComboBox;

	/**
	 * Observable list of possible order statuses.
	 */
	@FXML
	private ObservableList<String> statuses = FXCollections.observableArrayList("Pending", "Processing", "Ready",
			"Cancelled");

	/**
	 * The currently selected order.
	 */
	private Order selectedOrder;

	/**
	 * Starts the SupplierOrdersPageController and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/SupplierOrdersPage.fxml"));
		Scene scene = new Scene(root);

		primaryStage.setTitle("Supplier Orders Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the action of updating the selected order's status and ETA. Validates
	 * the ETA format and ensures it is not in the past.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the update process.
	 */
	@FXML
	public void btnUpdateOrder(ActionEvent event) throws Exception {
		selectedOrder = orderTableView.getSelectionModel().getSelectedItem();
		if (selectedOrder == null) {
			showAlert("No Order Selected", "Please select an order to update.");
			return;
		}

		String eta = ETAtxtField.getText();
		if (!isValidETA(eta)) {
			showAlert("Invalid ETA", "Please enter a valid ETA in HH:MM format.");
			return;
		}

		LocalTime etaTime = LocalTime.parse(eta, DateTimeFormatter.ofPattern("HH:mm"));
		if (etaTime.isBefore(LocalTime.now())) {
			showAlert("Invalid ETA", "ETA cannot be in the past.");
			return;
		}

		String status = statusComboBox.getValue();
		if (status == null || status.isEmpty()) {
			showAlert("No Status Selected", "Please select a status.");
			return;
		}

		// Run network operation in a background thread
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				ArrayList<Object> msgList = new ArrayList<>();
				msgList.add(selectedOrder);
				msgList.add(status);
				msgList.add(eta);
				Message msg = new Message(MessageType.UpdateOrderStatusSupplier, msgList);

				ClientUI.chat.accept(msg);
				return null;
			}

			@Override
			protected void succeeded() {
				Platform.runLater(() -> {
					reloadOrders(); // Reload the orders and refresh the TableView on the JavaFX Application Thread
				});
			}

			@Override
			protected void failed() {
				Throwable exception = getException();
				exception.printStackTrace();
				// Handle the error (e.g., show an alert)
			}
		};

		new Thread(task).start();
	}

	/**
	 * Checks if the provided ETA string matches the required format of HH:MM.
	 * 
	 * @param eta The ETA string to validate.
	 * @return True if the ETA string is in the correct format, otherwise false.
	 */
	private boolean isValidETA(String eta) {
		String pattern = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
		return eta.matches(pattern);
	}

	/**
	 * Initializes the controller after its root element has been completely
	 * processed. Sets up table columns, status combo box, and initializes order
	 * data.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChatClient.currController = this;
		initializeTableColumns();
		initializeStatusComboBox();
		init();
	}

	/**
	 * Configures table columns for displaying orders and order items.
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

			NameMealCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
			MealNotesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
			MealQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

			orderTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
				selectedOrder = newSelection;
				if (selectedOrder != null) {
					statusComboBox.setValue(selectedOrder.getStatus());
					if (selectedOrder.getETA() != null)
						ETAtxtField.setText(selectedOrder.getETA());
					fetchMealsForOrder(selectedOrder);
				}
			});

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the status combo box with possible order statuses.
	 */
	private void initializeStatusComboBox() {
		statusComboBox.setItems(statuses);
	}

	/**
	 * Loads the orders for the restaurant associated with the current user and
	 * updates the TableView.
	 */
	public void init() {
		try {
			ArrayList<Object> msgList = new ArrayList<>();
			msgList.add(((CertifiedWorker) (ChatClient.currUser)).getResturantID());

			Message msg = new Message(MessageType.GetResturantOrders, msgList);
			ClientUI.chat.accept(msg);

			if (ChatClient.userOrders != null) {
				ObservableList<Order> orderList = FXCollections.observableArrayList(ChatClient.userOrders);
				orderTableView.setItems(orderList);
			} else {
				System.out.println("userOrders is null.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		reloadOrders();
	}

	/**
	 * Reloads the orders and updates the TableView.
	 */
	public void reloadOrders() {

		// Ensure this method runs on the JavaFX Application Thread
		Platform.runLater(() -> {
			try {
				ArrayList<Object> msgList = new ArrayList<>();
				msgList.add(((CertifiedWorker) (ChatClient.currUser)).getResturantID());
				Message msg = new Message(MessageType.GetResturantOrders, msgList);
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
	 * Fetches meal details for the specified order and updates the mealsTableView.
	 * 
	 * @param order The order for which meal details are to be fetched.
	 */
	private void fetchMealsForOrder(Order order) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				ArrayList<Object> msgList = new ArrayList<>();
				msgList.add(order.getOrderID());
				Message msg = new Message(MessageType.GetOrderItems, msgList);
				ClientUI.chat.accept(msg);
				return null;
			}

			@Override
			protected void succeeded() {
				Platform.runLater(() -> {
					ArrayList<OrderItemDetail> orderItemDetails = (ArrayList<OrderItemDetail>) ChatClient.orderMeals;
					if (orderItemDetails != null) {
						ObservableList<OrderItemDetail> meals = FXCollections.observableArrayList(orderItemDetails);
						mealsTableView.setItems(meals);
					} else {
						showAlert("No Meals", "No meals found for the selected order.");
					}
				});
			}

			@Override
			protected void failed() {
				Throwable exception = getException();
				exception.printStackTrace();
				showAlert("Error", "Failed to load meals for the order.");
			}
		};

		new Thread(task).start();
	}

	/**
	 * Handles the action of navigating back to the Supplier Home Page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	@Override
	public void getBackBtn(ActionEvent event) throws Exception {
		ChatClient.userOrders = null;
		((Node) event.getSource()).getScene().getWindow().hide();
		SupplierHomePageController shpc = new SupplierHomePageController();
		shpc.start(new Stage());
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
