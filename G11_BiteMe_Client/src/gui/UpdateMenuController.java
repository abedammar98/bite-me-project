package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import logic.Category;
import logic.CertifiedWorker;
import logic.ConcreteItem;
import logic.Item;
import logic.Message;
import logic.MessageType;
import logic.MainMeal;
import logic.Salad;
import logic.Sweets;
import logic.Drinks;

/**
 * Controller for the Update Menu Page in the application. This class manages
 * the display and modification of menu items, including adding, deleting, and
 * updating items in the menu.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class UpdateMenuController implements Initializable, IControl {
	/**
	 * Button that navigates back to the Supplier Home Page.
	 */
	@FXML
	private Button btnBack;

	/**
	 * Text field for entering the name of the item.
	 */
	@FXML
	private TextField itemNameField;

	/**
	 * Text field for entering the description of the item.
	 */
	@FXML
	private TextField descriptionField;

	/**
	 * Text field for entering the cost of the item.
	 */
	@FXML
	private TextField costField;

	/**
	 * Text field for entering the ingredients of the item.
	 */
	@FXML
	private TextField IngredientsTxtField;

	/**
	 * Text field for entering the cooking method of the item.
	 */
	@FXML
	private TextField CookMethodTxtField;

	/**
	 * Button that adds a new item to the menu.
	 */
	@FXML
	private Button btnAddItem;

	/**
	 * Button that deletes the selected item from the menu.
	 */
	@FXML
	private Button btnDeleteItem;

	/**
	 * Button that clears all input fields.
	 */
	@FXML
	private Button btnClear;

	/**
	 * Table view displaying the menu items.
	 */
	@FXML
	private TableView<Item> itemTableView;

	/**
	 * Table column displaying the name of the item.
	 */
	@FXML
	private TableColumn<Item, String> itemNameColumn;

	/**
	 * Table column displaying the category of the item.
	 */
	@FXML
	private TableColumn<Item, String> categoryNameColumn;

	/**
	 * Table column displaying the description of the item.
	 */
	@FXML
	private TableColumn<Item, String> descriptionColumn;

	/**
	 * Table column displaying the cost of the item.
	 */
	@FXML
	private TableColumn<Item, Float> costColumn;

	/**
	 * Combo box for selecting the category of the item.
	 */
	@FXML
	private ComboBox<String> CategoryCb;

	/**
	 * Initializes the controller after its root element has been completely
	 * processed. Sets up table columns, fetches items from the server, and
	 * initializes the category combo box.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeTableColumns();
		fetchItemsFromServer();
		ChatClient.currController = this;
		ObservableList<String> categories = FXCollections.observableArrayList("Salad", "Sweets", "Drinks", "MainMeal");
		CategoryCb.setItems(categories);

		itemTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				populateFields(newValue);
			}
		});

		// Add listener to ComboBox
		CategoryCb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			updateFieldsEditability(newValue);
		});
	}

	/**
	 * Updates the editability of input fields based on the selected category.
	 * 
	 * @param category The selected category from the ComboBox.
	 */
	private void updateFieldsEditability(String category) {
		if (category == null) {
			return;
		}

		IngredientsTxtField.setEditable(false);
		CookMethodTxtField.setEditable(false);
		IngredientsTxtField.clear();
		CookMethodTxtField.clear();

		switch (Category.valueOf(category)) {
		case Salad:
			IngredientsTxtField.setEditable(true); // Allow editing for Salad ingredients
			break;
		case MainMeal:
			CookMethodTxtField.setEditable(true); // Allow editing for MainMeal cook method
			break;
		case Sweets:
			IngredientsTxtField.setEditable(true); // Allow editing for Sweets ingredients
			break;
		case Drinks:
			// Drinks do not have editable fields
			break;
		default:
			IngredientsTxtField.setEditable(true);
			CookMethodTxtField.setEditable(true);
			break;
		}
	}

	/**
	 * Configures table columns for displaying menu items.
	 */
	private void initializeTableColumns() {
		itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("ItemName"));
		categoryNameColumn.setCellValueFactory(cellData -> {
			Category category = cellData.getValue().getCategoryName();
			return new SimpleStringProperty(category != null ? category.toString() : "");
		});
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
		costColumn.setCellValueFactory(new PropertyValueFactory<>("Cost"));
	}

	/**
	 * Fetches menu items from the server and updates the TableView.
	 */
	private void fetchItemsFromServer() {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				ArrayList<Object> msgList = new ArrayList<>();
				msgList.add(((CertifiedWorker) ChatClient.currUser).getResturantID());
				Message msg = new Message(MessageType.GetAllItems, msgList);
				ClientUI.chat.accept(msg);
				return null;
			}

			@Override
			protected void succeeded() {
				Platform.runLater(() -> {
					if (ChatClient.menuItems != null) {
						ObservableList<Item> items = FXCollections
								.observableArrayList(ChatClient.menuItems.getMenuItems());
						itemTableView.setItems(items);
					} else {
						showAlert("No Items", "Failed to load items or no items available.");
					}
				});
			}

			@Override
			protected void failed() {
				Throwable exception = getException();
				exception.printStackTrace();
				showAlert("Error", "Failed to load items.");
			}
		};

		new Thread(task).start();
	}

	/**
	 * Handles the action of adding a new item to the menu. Validates input fields
	 * and sends the item data to the server.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	private void addItem(ActionEvent event) {
		if (!areFieldsFilled()) {
			showAlert("Missing Fields", "Please ensure all required fields are filled.");
			return;
		}
		try {
			String itemName = itemNameField.getText();
			Category category = Category.valueOf(CategoryCb.getValue());
			String description = descriptionField.getText();
			float cost = Float.parseFloat(costField.getText());

			// Check if item already exists in the restaurant's menu
			boolean itemExists = ChatClient.menuItems.getMenuItems().stream()
					.anyMatch(item -> item.getItemName().equals(itemName) && item.getCategoryName().equals(category)
							&& item.getDescription().equals(description) && item.getCost() == cost);

			if (itemExists) {
				showAlert("Item Already Exists", "This item is already in the restaurant's menu.");
				return;
			}

			Item newItem;
			ArrayList<Object> msgList = new ArrayList<>();

			switch (category) {
			case Salad:
				String[] ingredientsArray = IngredientsTxtField.getText().split(", ");
				ArrayList<String> ingredients = new ArrayList<>(Arrays.asList(ingredientsArray));
				newItem = new Salad(0, itemName, category, description, cost, ingredients, "");
				break;
			case MainMeal:
				String cookMethod = CookMethodTxtField.getText();
				newItem = new MainMeal(0, itemName, category, description, cost, cookMethod, null);
				break;
			case Drinks:
				newItem = new Drinks(0, itemName, category, description, cost, null);
				break;
			case Sweets:
				String[] sweetsIngredientsArray = IngredientsTxtField.getText().split(", ");
				ArrayList<String> sweetsIngredients = new ArrayList<>(Arrays.asList(sweetsIngredientsArray));
				newItem = new Sweets(0, itemName, category, description, cost, sweetsIngredients);
				break;
			default:
				newItem = new ConcreteItem(0, itemName, category, description, cost);
				break;
			}
			msgList.add(newItem);
			msgList.add(ChatClient.menuItems.getMenuID());
			Message msg = new Message(MessageType.AddItemSupplier, msgList);
			ClientUI.chat.accept(msg);
			clearFields();
		} catch (NumberFormatException e) {
			showAlert("Invalid input", "Please ensure all fields are filled correctly.");
		} catch (IllegalArgumentException e) {
			showAlert("Invalid category", "Please ensure the category is valid.");
		}
		fetchItemsFromServer();
		showAlert("Item Addition", "Item added successfully");
	}

	/**
	 * Handles the action of deleting the selected item from the menu. Validates
	 * selection and sends the item ID to the server for deletion.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	private void deleteItem(ActionEvent event) {
		ArrayList<Object> inputList = new ArrayList<>();
		Item selectedItem = itemTableView.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			inputList.add(selectedItem.getItemID());
			inputList.add(ChatClient.menuItems.getMenuID());
			Message msg = new Message(MessageType.DeleteItemSupplier, inputList);
			ClientUI.chat.accept(msg);
			clearFields();
			fetchItemsFromServer();
		} else {
			showAlert("No item selected", "Please select an item to delete.");
		}
		showAlert("Item Removal", "Item removed successfully");

	}
    /**
     * Populates input fields with the details of the selected item.
     * 
     * @param item The selected item to populate fields with.
     */
	private void populateFields(Item item) {
		itemNameField.setText(item.getItemName());
		CategoryCb.setValue(item.getCategoryName().toString());
		descriptionField.setText(item.getDescription());
		costField.setText(String.valueOf(item.getCost()));

		// Reset editability for text fields
		IngredientsTxtField.setEditable(false);
		CookMethodTxtField.setEditable(false);
		IngredientsTxtField.clear();
		CookMethodTxtField.clear();

		if (item instanceof Salad) {
			IngredientsTxtField.setText(String.join(", ", ((Salad) item).getIngredients()));
			IngredientsTxtField.setEditable(true); // Allow editing for Salad ingredients
		} else if (item instanceof MainMeal) {
			CookMethodTxtField.setText(((MainMeal) item).getCookMethod());
			CookMethodTxtField.setEditable(true); // Allow editing for MainMeal cook method
		} else if (item instanceof Sweets) {
			IngredientsTxtField.setText(String.join(", ", ((Sweets) item).getSweetsDescription()));
			IngredientsTxtField.setEditable(true); // Allow editing for Sweets ingredients
		} else if (item instanceof Drinks) {
			// Drinks do not have editable fields
		} else {
			// For other item types, clear the fields
			IngredientsTxtField.clear();
			CookMethodTxtField.clear();
		}
	}
    /**
     * Handles the action of clearing all input fields.
     * 
     * @param event The event triggered by the user's action.
     */
	@FXML
	public void clearBtn(ActionEvent event) {
		clearFields();
	}
    /**
     * Clears all input fields and deselects the selected item in the TableView.
     */
	private void clearFields() {
		itemNameField.clear();
		CategoryCb.setValue(null);
		descriptionField.clear();
		costField.clear();
		IngredientsTxtField.clear();
		CookMethodTxtField.clear();
		IngredientsTxtField.setEditable(true);
		CookMethodTxtField.setEditable(true);
	}
    /**
     * Initializes the primary stage for the Update Menu page.
     * @param primaryStage The primary stage for this application.
     * @throws Exception If an error occurs during loading the FXML file.
     */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/UpdateMenu.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Update Menu Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
    /**
     * Handles the action of navigating back to the Supplier Home Page.
     * 
     * @param event The event triggered by the user's action.
     * @throws Exception If an error occurs while loading the new scene.
     */
	@Override
	public void getBackBtn(ActionEvent event) throws Exception {
		ChatClient.userOrders = null;
		((Node) event.getSource()).getScene().getWindow().hide();
		SupplierHomePageController shpc = new SupplierHomePageController();
		shpc.start(new Stage());
	}
    /**
     * Displays an alert dialog with a given title and message.
     * 
     * @param title   The title of the alert dialog.
     * @param message The message to display in the alert dialog.
     */
	public void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
    /**
     * Checks whether all required fields in the form are filled out.
     * @return true if all required fields are filled, false otherwise.
     */
	private boolean areFieldsFilled() {
		String category = CategoryCb.getValue();
		if (category == null || category.isEmpty()) {
			return false;
		}

		if (itemNameField.getText().isEmpty() || descriptionField.getText().isEmpty()
				|| costField.getText().isEmpty()) {
			return false;
		}

		switch (Category.valueOf(category)) {
		case Salad:
		case Sweets:
			if (IngredientsTxtField.getText().isEmpty()) {
				return false;
			}
			break;
		case MainMeal:
			if (CookMethodTxtField.getText().isEmpty()) {
				return false;
			}
			break;
		case Drinks:
			// No additional fields to check
			break;
		default:
			break;
		}

		return true;
	}

}
