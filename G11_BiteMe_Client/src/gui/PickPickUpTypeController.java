package gui;

import javafx.application.Platform;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import logic.Order;
import logic.OrderItemDetail;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import client.ChatClient;

/**
 * Controller for the Pick Up Type page in the application. This class manages
 * the selection of pick-up types (TakeAway or Delivery), including input
 * validation, styling updates, and transitioning to the order summary page.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class PickPickUpTypeController implements Initializable, IControl {
	/**
	 * Button that exits the current page and navigates to the next page.
	 */
	@FXML
	private Button btnExit;

	/**
	 * Button that navigates to the next page after selecting the pick-up type and
	 * filling in required details.
	 */
	@FXML
	private Button btnNext;

	/**
	 * Toggle button for selecting the TakeAway pick-up type.
	 */
	@FXML
	private ToggleButton ToggleBtnTakeAway;

	/**
	 * Image view displaying an image related to TakeAway option.
	 */
	@FXML
	private ImageView imageViewTakeAway;

	/**
	 * Toggle group containing the pick-up type toggle buttons.
	 */
	@FXML
	private ToggleGroup photoToggleGroup;

	/**
	 * Toggle button for selecting the Delivery pick-up type.
	 */
	@FXML
	private ToggleButton ToggleBtnDelivery;

	/**
	 * Image view displaying an image related to Delivery option.
	 */
	@FXML
	private ImageView ImageViewDelivery;

	/**
	 * Text field for inputting the time of the order.
	 */
	@FXML
	private TextField idTimetxtField;

	/**
	 * Date picker for selecting the date of the order.
	 */
	@FXML
	private DatePicker idDatePicker;

	/**
	 * Text field for inputting the delivery location.
	 */
	@FXML
	private TextField idLocation;

	/**
	 * Combo box for selecting the number of orderers for shared delivery.
	 */
	@FXML
	private ComboBox<String> SharedDeliveryCb;

	/**
	 * Initializes the controller after its root element has been completely
	 * processed. Sets up the date picker, toggle buttons, and initializes the
	 * shared delivery combo box.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChatClient.currController = this;
		Platform.runLater(() -> {
			Parent root = idTimetxtField.getScene().getRoot();
			root.getStyleClass().add("takeaway"); // or "delivery" depending on the default state
		});
		idLocation.setVisible(false);
		SharedDeliveryCb.setVisible(false);
		idDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(DatePicker picker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate date, boolean empty) {
						super.updateItem(date, empty);
						LocalDate today = LocalDate.now();
						setDisable(empty || date.compareTo(today) < 0 || date.compareTo(today.plusDays(3)) > 0);
					}
				};
			}
		});
		photoToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
			if (newToggle == null) {
				Platform.runLater(() -> photoToggleGroup.selectToggle(oldToggle));
			}
		});

		// Initialize the shared delivery combo box with values
		SharedDeliveryCb.setItems(FXCollections.observableArrayList("1", "2", "3+"));
		SharedDeliveryCb.setVisible(false);
	}

	/**
	 * Starts the PickPickUpTypeController and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/PickUpType.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/PickUpType.css").toExternalForm());
		primaryStage.setTitle("PickUpType Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the action of navigating back to the previous page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	@FXML
	public void getBackBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		PickItemsController pic = new PickItemsController();
		pic.start(new Stage());
	}

	/**
	 * Handles the action of navigating to the order summary page after validation
	 * of input fields.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	@FXML
	public void getNextBtn(ActionEvent event) throws Exception {
		if (!ToggleBtnTakeAway.isSelected() && !ToggleBtnDelivery.isSelected()) {
			showAlert("Validation Error", "Please select a pickup type (TakeAway or Delivery).");
			return;
		}
		if (idTimetxtField.getText().isEmpty() || idDatePicker.getValue() == null
				|| (ToggleBtnDelivery.isSelected() && idLocation.getText().isEmpty())) {
			showAlert("Validation Error", "All fields must be filled.");
			return;
		}

		if (!isValidTime(idTimetxtField.getText(), idDatePicker.getValue())) {
			showAlert("Validation Error", "Invalid time. Ensure the time is in the future and in HH:MM format.");
			return;
		}
		if (ToggleBtnDelivery.isSelected() && ChatClient.currUser.getAccountType().equals("Business")
				&& SharedDeliveryCb.getValue() == null) {
			showAlert("Validation Error", "Please pick number of orderers.");
			return;
		}
		String time = idTimetxtField.getText();
		LocalDate date = idDatePicker.getValue();
		String pType = ToggleBtnDelivery.isSelected() ? "Delivery" : "TakeAway";
		String location = ToggleBtnDelivery.isSelected() ? idLocation.getText() : "";

		Order order = new Order(0, ChatClient.currUser.getUserID(), LocalDate.now(),
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
				ChatClient.selectedResturant.getResturantID(), pType, location, "Pending", time, date, "", "",
				OrderItemDetail.totalCost(ChatClient.userCart));
		if (pType.equals("Delivery") && ChatClient.currUser.getAccountType().equals("Business")) {
			String val = SharedDeliveryCb.getValue();
			if (val.equals("2")) {
				order.setCost(order.getCost() + 20);
				ChatClient.deliveryCost = 20;
			} else if (val.equals("3+")) {
				order.setCost(order.getCost() + 15);
				ChatClient.deliveryCost = 15;
			} else {
				order.setCost(order.getCost() + 25);
				ChatClient.deliveryCost = 25;
			}
		} else if (pType.equals("Delivery")) {
			order.setCost(order.getCost() + 25);
			ChatClient.deliveryCost = 25;
		}
		ChatClient.discountsApplied = "";
		try {
			// Apply 10% if early order
			if (!order.isOrderWithinTwoHours()) {
				System.out.println("aa");
				order.setCost(order.getCost() * (float) 0.9);
				ChatClient.discountsApplied += "10% for early order";
			}
		} catch (Exception e) {
			showAlert("Validation Error", "Invalid time. Ensure the time is in the future and in HH:MM format.");
			return;
		}
		// Apply 50% if customer has copun
		if (ChatClient.currUser.getAmountOfCopunts() > 0) {
			if (!ChatClient.discountsApplied.equals(""))
				ChatClient.discountsApplied += ", ";
			ChatClient.discountsApplied += "50% for coupon";
			order.setCost(order.getCost() * (float) 0.5);
		}
		ChatClient.newOrder = order;
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		OrderSummaryController osc = new OrderSummaryController();
		osc.start(new Stage());
	}

	/**
	 * Handles the action of updating the UI based on the selected pick-up type
	 * (TakeAway or Delivery).
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	private void handleToggleButtonAction(ActionEvent event) {
		Parent root = ((Node) event.getSource()).getScene().getRoot();

		if (ToggleBtnDelivery.isSelected()) {
			root.getStyleClass().remove("takeaway");
			root.getStyleClass().remove("delivery1");// Remove any existing classes
			root.getStyleClass().remove("delivery");
			idLocation.setVisible(true);
			if (ChatClient.currUser.getAccountType().equals("Business")) {
				SharedDeliveryCb.setVisible(true);
				root.getStyleClass().add("delivery"); // Add the delivery class
			} else {
				root.getStyleClass().add("delivery1");
			}

		} else if (ToggleBtnTakeAway.isSelected()) {
			root.getStyleClass().remove("delivery"); // Remove any existing classes
			root.getStyleClass().remove("delivery1");
			root.getStyleClass().add("takeaway"); // Add the takeaway class (if needed)
			idLocation.setVisible(false);
			SharedDeliveryCb.setVisible(false);
		}
	}

	/**
	 * Validates the time input to ensure it is in the correct format and represents
	 * a future time.
	 * 
	 * @param timeStr The time input as a string.
	 * @param date    The selected date.
	 * @return true if the time is valid and in the future, false otherwise.
	 */
	private boolean isValidTime(String timeStr, LocalDate date) {
		try {
			String[] parts = timeStr.split(":");
			if (parts.length != 2) {
				return false;
			}

			int hour = Integer.parseInt(parts[0]);
			int minute = Integer.parseInt(parts[1]);

			if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
				return false;
			}

			LocalTime time = LocalTime.of(hour, minute);
			LocalDateTime dateTime = LocalDateTime.of(date, time);

			return !dateTime.isBefore(LocalDateTime.now());
		} catch (NumberFormatException e) {
			return false;
		}
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
