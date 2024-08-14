package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.Message;
import logic.MessageType;
import logic.Resturant;
import javafx.scene.control.TableView;

/**
 * Controller for the Pick Restaurant page in the application. This class
 * handles the display and selection of restaurants, including navigating to the
 * next page where menu items of the selected restaurant can be viewed, and
 * navigating back to the customer home page.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class PickRestuarntController implements Initializable, IControl {
	/**
	 * Button that exits the current page and navigates back to the customer home
	 * page.
	 */
	@FXML
	private Button btnExit;

	/**
	 * TableView displaying the list of restaurants.
	 */
	@FXML
	private TableView<Resturant> idResturantTableView;

	/**
	 * TableColumn displaying the restaurant names in the TableView.
	 */
	@FXML
	private TableColumn<Resturant, String> resturantNameCol;

	/**
	 * TableColumn displaying the restaurant branches in the TableView.
	 */
	@FXML
	private TableColumn<Resturant, String> resturantBranchCol;

	/**
	 * Button that navigates to the next page to view menu items of the selected
	 * restaurant.
	 */
	@FXML
	private Button btnNext;

	/**
	 * The currently selected restaurant.
	 */
	private Resturant selectedResturant;

	/**
	 * Initializes the controller after its root element has been completely
	 * processed. This method sets up the TableView with data and adds a listener
	 * for selection changes.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChatClient.currController = this;
		ChatClient.userCart = null;
		Message msg = new Message(MessageType.GetResturants, null);
		ClientUI.chat.accept(msg);
		resturantNameCol.setCellValueFactory(new PropertyValueFactory<>("resturantName"));
		resturantBranchCol.setCellValueFactory(new PropertyValueFactory<>("branch"));

		// Populate the table with data from ChatClient.userResturants
		ObservableList<Resturant> resturants = FXCollections.observableArrayList(ChatClient.userResturants);
		idResturantTableView.setItems(resturants);

		// Add listener to table selection
		idResturantTableView.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						selectedResturant = newSelection;
					}
				});
	}

	/**
	 * Starts the PickRestaurantController and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/PickResturant.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/PickResturant.css").toExternalForm());
		primaryStage.setTitle("OrderFood Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the action of navigating back to the customer home page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	public void getBackBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		CustomerHomePageController chpc = new CustomerHomePageController();
		chpc.start(new Stage());
	}

	/**
	 * Handles the action of navigating to the next page to view menu items of the
	 * selected restaurant.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	public void getNextBtn(ActionEvent event) throws Exception {
		if (selectedResturant != null) {
			ArrayList<Object> msgObjList = new ArrayList<>();
			msgObjList.add(selectedResturant.getMenuID());
			Message msg = new Message(MessageType.GetResturantMenuItems, msgObjList);
			ClientUI.chat.accept(msg);
			ChatClient.selectedResturant = this.selectedResturant;
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			PickItemsController pic = new PickItemsController();
			pic.start(new Stage());

		} else {
			showAlert("No Resturant Selecetd", "Please select a resturant.");
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
