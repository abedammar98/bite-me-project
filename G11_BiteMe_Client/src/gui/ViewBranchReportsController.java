package gui;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientUI;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import logic.BranchReports;
import logic.IncomeReport;
import logic.Message;
import logic.MessageType;
import logic.OrderReport;
import logic.PerformanceReport;

/**
 * Controller for the ViewBranchReports page in the application. This class
 * handles the display and management of branch reports, including income
 * reports, order reports, and performance reports.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class ViewBranchReportsController implements Initializable, IControl {

	/** Button to navigate back to the previous page. */
	@FXML
	private Button btnBack;

	/** Tab for viewing order reports. */
	@FXML
	private Tab Orderstab;

	/** TableView for displaying order reports. */
	@FXML
	private TableView<OrderReport> ordertblview;

	/** TableColumn for displaying restaurant IDs in the order reports table. */
	@FXML
	private TableColumn<OrderReport, Integer> ResturantIDCol;

	/** TableColumn for displaying total orders in the order reports table. */
	@FXML
	private TableColumn<OrderReport, Integer> TotalOrdersCol;

	/** TableColumn for displaying salad orders in the order reports table. */
	@FXML
	private TableColumn<OrderReport, Integer> SaladOrdersCol;

	/** TableColumn for displaying sweets orders in the order reports table. */
	@FXML
	private TableColumn<OrderReport, Integer> SweetsOrdersCol;

	/** TableColumn for displaying drinks orders in the order reports table. */
	@FXML
	private TableColumn<OrderReport, Integer> DrinksOrdersCol;

	/** TableColumn for displaying main meal orders in the order reports table. */
	@FXML
	private TableColumn<OrderReport, Integer> MainMealOrdersCol;

	/** Tab for viewing income reports. */
	@FXML
	private Tab Incometab;

	/** TableView for displaying income reports. */
	@FXML
	private TableView<IncomeReport> incometblview;

	/** TableColumn for displaying restaurant IDs in the income reports table. */
	@FXML
	private TableColumn<IncomeReport, Integer> ResturantIDCol1;

	/** TableColumn for displaying total revenue in the income reports table. */
	@FXML
	private TableColumn<IncomeReport, Float> TotalRevenueCol;

	/** TableColumn for displaying salad revenue in the income reports table. */
	@FXML
	private TableColumn<IncomeReport, Float> SaladRevenueCol;

	/** TableColumn for displaying sweets revenue in the income reports table. */
	@FXML
	private TableColumn<IncomeReport, Float> SweetsRevenueCol;

	/** TableColumn for displaying drinks revenue in the income reports table. */
	@FXML
	private TableColumn<IncomeReport, Float> DrinksRevenueCol;

	/** TableColumn for displaying main meal revenue in the income reports table. */
	@FXML
	private TableColumn<IncomeReport, Float> MainMealRevenueCol;

	/** Tab for viewing performance reports. */
	@FXML
	private Tab Performancetab;

	/** PieChart for displaying performance report data. */
	@FXML
	private PieChart piechartperformance;

	/** Label for displaying the average delay time in performance reports. */
	@FXML
	private Label avgdelatTimelbl;

	/** ComboBox for selecting the year of the report. */
	@FXML
	private ComboBox<String> yearCb;

	/** ComboBox for selecting the month of the report. */
	@FXML
	private ComboBox<String> monthCb;

	/**
	 * Button for navigating to the previous restaurant in the performance report.
	 */
	@FXML
	private Button leftArrowBtn;

	/** Button for navigating to the next restaurant in the performance report. */
	@FXML
	private Button rightArrowBtn;

	/** Label for displaying the restaurant ID in performance reports. */
	@FXML
	private Label restaurantIdLbl;

	/** Index for the currently selected restaurant in the performance report. */
	private int currentRestaurantIndex = 0;

	private ArrayList<IncomeReport> ir;
	private ArrayList<PerformanceReport> pr;
	private ArrayList<OrderReport> or;
	private BranchReports thisBR;

	/**
	 * Initializes the controller after its root element has been completely
	 * processed. Sets up the TableView columns and populates the year ComboBox with
	 * available years.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChatClient.currController=this;

		rightArrowBtn.setDisable(true);
		leftArrowBtn.setDisable(true);

		// Initialize the income table
		ResturantIDCol.setCellValueFactory(new PropertyValueFactory<>("restaurantID"));
		TotalRevenueCol.setCellValueFactory(new PropertyValueFactory<>("TotalRevenue"));
		SaladRevenueCol.setCellValueFactory(new PropertyValueFactory<>("SaladRevenue"));
		SweetsRevenueCol.setCellValueFactory(new PropertyValueFactory<>("SweetsRevenue"));
		DrinksRevenueCol.setCellValueFactory(new PropertyValueFactory<>("DrinksRevenue"));
		MainMealRevenueCol.setCellValueFactory(new PropertyValueFactory<>("MainMealRevenue"));

		// Initialize the order table
		ResturantIDCol1.setCellValueFactory(new PropertyValueFactory<>("restaurantID"));
		TotalOrdersCol.setCellValueFactory(new PropertyValueFactory<>("TotalOrders"));
		SaladOrdersCol.setCellValueFactory(new PropertyValueFactory<>("SaladOrders"));
		SweetsOrdersCol.setCellValueFactory(new PropertyValueFactory<>("SweetsOrders"));
		DrinksOrdersCol.setCellValueFactory(new PropertyValueFactory<>("DrinksOrders"));
		MainMealOrdersCol.setCellValueFactory(new PropertyValueFactory<>("MainMealOrders"));
		if (ChatClient.branchReports == null || ChatClient.lastDataFetchMonth != (LocalDateTime.now().getMonthValue())) {
			ChatClient.lastDataFetchMonth = LocalDateTime.now().getMonthValue();
			ArrayList<Object> msgList = new ArrayList<>();
			msgList.add(ChatClient.currUser.getBranchID());
			Message msg = new Message(MessageType.GetBranchManagerReportsYearMonth, msgList);
			ClientUI.chat.accept(msg);
			for (String year : ChatClient.currBranchReports.getYearMonth().keySet()) {
				msgList.clear();
				msgList.add(ChatClient.currUser.getBranchID());
				msgList.add(year);
				msgList.add(ChatClient.currBranchReports.getYearMonth().get(year));
				msg = new Message(MessageType.getBranchReports, msgList);
				ClientUI.chat.accept(msg);
			}
		}
		thisBR = null;
		for (BranchReports br : ChatClient.branchReports) {
			if (br.getBranchID().getValue() == (ChatClient.currUser.getBranchID())) {
				thisBR = br;
			}

		}
		if (thisBR == null) {
			showAlert("No Reports Available", "No reports were found for this year and month and branch");
			return;
		}
		// Populate the year ComboBox with available years from ChatClient.yearMonth
		ObservableList<String> years = FXCollections.observableArrayList(thisBR.getYearMonth().keySet());
		yearCb.setItems(years);
	}

	/**
	 * Initializes the month ComboBox based on the selected year.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	public void initMonthCb(ActionEvent event) {
		rightArrowBtn.setDisable(true);
		leftArrowBtn.setDisable(true);
		// Get the selected year
		String selectedYear = yearCb.getValue();

		if (selectedYear != null && !selectedYear.isEmpty()) {
			// Retrieve the list of months for the selected year from ChatClient.yearMonth
			ArrayList<String> months = thisBR.getYearMonth().get(selectedYear);
			// Populate the month ComboBox with the months for the selected year
			ObservableList<String> monthOptions = FXCollections.observableArrayList(months);
			monthCb.setItems(monthOptions);
		}
	}

	/**
	 * Updates the TableView and PieChart based on the selected year and month.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	public void updateTableValues(ActionEvent event) {
		rightArrowBtn.setDisable(true);
		leftArrowBtn.setDisable(true);
		// Get the selected year and month
		String selectedYear = yearCb.getValue();
		String selectedMonth = monthCb.getValue();

		if (selectedYear != null && selectedMonth != null) {
			currentRestaurantIndex=0;
			ir = thisBR.getIncomeReport();
			pr = thisBR.getPerformanceReport();
			or = thisBR.getOrderReport();

			if (ir.size() > 0) {
				ObservableList<IncomeReport> incomeReportList = FXCollections.observableArrayList(ir);
				incometblview.setItems(incomeReportList);
			}
			if (or.size() > 0) {
				ObservableList<OrderReport> orderReportList = FXCollections.observableArrayList(or);
				ordertblview.setItems(orderReportList);
			}
			if (pr.size() > 0) {
				updateChartAndLabels();
			}
			updateArrowButtonState();

		} else {
			showAlert("Selection Error", "Please select both year and month.");
		}
	}

	/**
	 * Updates the PieChart and labels with the data for the currently selected
	 * restaurant.
	 */
	private void updateChartAndLabels() {
		PerformanceReport report = pr.get(currentRestaurantIndex);

		int onTimeOrders = report.getOnTimeOrders();
		int lateOrders = report.getLateOrders();

		// Create PieChart Data
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("On Time Orders", onTimeOrders), new PieChart.Data("Late Orders", lateOrders));
		piechartperformance.setData(pieChartData);

		// Add the labels to the PieChart
		for (PieChart.Data data : piechartperformance.getData()) {
			data.nameProperty().bind(Bindings.concat(data.getName(), " (", (int) data.getPieValue(), ")"));
		}

		avgdelatTimelbl.setText("Average Delay Time:\n" + report.getAverageDelayTime().toString());
		restaurantIdLbl.setText("Restaurant ID: " + report.getRestaurantID());
	}

	/**
	 * Handles the action of navigating to the previous restaurant in the
	 * performance report.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	private void showPreviousRestaurant(ActionEvent event) {
		if (currentRestaurantIndex > 0) {
			currentRestaurantIndex--;
			updateChartAndLabels();
		}
		updateArrowButtonState();
	}

	/**
	 * Handles the action of navigating to the next restaurant in the performance
	 * report.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	private void showNextRestaurant(ActionEvent event) {
		if (currentRestaurantIndex < pr.size() - 1) {
			currentRestaurantIndex++;
			updateChartAndLabels();
		}
		updateArrowButtonState();
	}

	/**
	 * Updates the state of the arrow buttons based on the current restaurant index.
	 */
	private void updateArrowButtonState() {
		leftArrowBtn.setDisable(currentRestaurantIndex == 0);
		rightArrowBtn.setDisable(currentRestaurantIndex == pr.size() - 1);
	}

	/**
	 * Starts the ViewBranchReportsController and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ViewBranchReports.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ViewBranchReports.css").toExternalForm());
		primaryStage.setTitle("Order Page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the action of navigating back to the previous page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	@Override
	public void getBackBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		ManagerHomePageController mhpc = new ManagerHomePageController();
		mhpc.start(new Stage());
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
