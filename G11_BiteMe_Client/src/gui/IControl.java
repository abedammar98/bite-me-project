package gui;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * Interface for controllers in the application. This interface defines the
 * methods that all controllers should implement, including starting the stage,
 * handling the back button action, and displaying alerts.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public interface IControl {
	/**
	 * Starts the stage for the controller.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	public void start(Stage primaryStage) throws Exception;

	/**
	 * Handles the action of going back to the previous view or page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the back navigation process.
	 */
	public void getBackBtn(ActionEvent event) throws Exception;

	/**
	 * Displays an alert with the specified title and message.
	 * 
	 * @param title   The title of the alert.
	 * @param message The message to be displayed in the alert.
	 */
	public void showAlert(String title, String message);

}
