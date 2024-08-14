package gui;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
 * Controller for the CEO Reports Page in the application. This class manages
 * the display and interaction with various reports including order reports,
 * income reports, and performance charts.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class ViewCEOReportsController implements Initializable, IControl {

	/**
	 * Button that navigates back to the CEO Home Page.
	 */
	@FXML
	private Button btnBack;

	/**
	 * Tab displaying order reports.
	 */
	@FXML
	private Tab Orderstab;

	/**
	 * Table view displaying order reports.
	 */
	@FXML
	private TableView<OrderReport> ordertblview;

	/**
	 * Table column displaying the restaurant ID for order reports.
	 */
	@FXML
	private TableColumn<OrderReport, Integer> ResturantIDCol;

	/**
	 * Table column displaying the total number of orders for order reports.
	 */
	@FXML
	private TableColumn<OrderReport, Integer> TotalOrdersCol;

	/**
	 * Table column displaying the number of salad orders for order reports.
	 */
	@FXML
	private TableColumn<OrderReport, Integer> SaladOrdersCol;

	/**
	 * Table column displaying the number of sweets orders for order reports.
	 */
	@FXML
	private TableColumn<OrderReport, Integer> SweetsOrdersCol;

	/**
	 * Table column displaying the number of drinks orders for order reports.
	 */
	@FXML
	private TableColumn<OrderReport, Integer> DrinksOrdersCol;

	/**
	 * Table column displaying the number of main meal orders for order reports.
	 */
	@FXML
	private TableColumn<OrderReport, Integer> MainMealOrdersCol;

	/**
	 * Tab displaying income reports.
	 */
	@FXML
	private Tab Incometab;

	/**
	 * Table view displaying income reports.
	 */
	@FXML
	private TableView<IncomeReport> incometblview;

	/**
	 * Table column displaying the restaurant ID for income reports.
	 */
	@FXML
	private TableColumn<IncomeReport, Integer> ResturantIDCol1;

	/**
	 * Table column displaying the total revenue for income reports.
	 */
	@FXML
	private TableColumn<IncomeReport, Float> TotalRevenueCol;

	/**
	 * Table column displaying the revenue from salads for income reports.
	 */
	@FXML
	private TableColumn<IncomeReport, Float> SaladRevenueCol;

	/**
	 * Table column displaying the revenue from sweets for income reports.
	 */
	@FXML
	private TableColumn<IncomeReport, Float> SweetsRevenueCol;

	/**
	 * Table column displaying the revenue from drinks for income reports.
	 */
	@FXML
	private TableColumn<IncomeReport, Float> DrinksRevenueCol;

	/**
	 * Table column displaying the revenue from main meals for income reports.
	 */
	@FXML
	private TableColumn<IncomeReport, Float> MainMealRevenueCol;

	/**
	 * Tab displaying performance reports.
	 */
	@FXML
	private Tab Performancetab;

	/**
	 * Pie chart displaying performance metrics.
	 */
	@FXML
	private PieChart piechartperformance;

	/**
	 * Label displaying the average delay time.
	 */
	@FXML
	private Label avgdelatTimelbl;

	/**
	 * Combo box for selecting the year for reports.
	 */
	@FXML
	private ComboBox<String> yearCb;

	/**
	 * Combo box for selecting the month for reports.
	 */
	@FXML
	private ComboBox<String> monthCb;

	/**
	 * Button to navigate to the previous restaurant's performance report.
	 */
	@FXML
	private Button leftArrowBtn;

	/**
	 * Button to navigate to the next restaurant's performance report.
	 */
	@FXML
	private Button rightArrowBtn;

	/**
	 * Label displaying the restaurant ID for performance reports.
	 */
	@FXML
	private Label restaurantIdLbl;

	/**
	 * Combo box for selecting the branch ID.
	 */
	@FXML
	private ComboBox<Integer> branchCb;

	/**
	 * Index of the currently displayed restaurant's performance report.
	 */
	private int currentRestaurantIndex = 0;
	private BranchReports thisBR;
	private ArrayList<IncomeReport> ir;
	private ArrayList<PerformanceReport> pr;
	private ArrayList<OrderReport> or;

	/**
	 * Initializes the controller after its root element has been completely
	 * processed. Sets up table columns, populates the year combo box, and requests
	 * initial data.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChatClient.currController=this;
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
		if (ChatClient.branchReports == null
				|| ChatClient.lastDataFetchMonth != (LocalDateTime.now().getMonthValue())) {
			ChatClient.lastDataFetchMonth = LocalDateTime.now().getMonthValue();
			ArrayList<Object> msgListYearMonth = new ArrayList<>();
			ArrayList<Object> msgListReports = new ArrayList<>();
			Message msgYearMonth;
			Message msgReports;
			msgListYearMonth.add(1);
			msgYearMonth = new Message(MessageType.GetBranchManagerReportsYearMonth, msgListYearMonth);
			ClientUI.chat.accept(msgYearMonth);
			for (String year : ChatClient.currBranchReports.getYearMonth().keySet()) {
				msgListReports.clear();
				msgListReports.add(1);
				msgListReports.add(year);
				msgListReports.add(ChatClient.currBranchReports.getYearMonth().get(year));
				msgReports = new Message(MessageType.getBranchReports, msgListReports);
				ClientUI.chat.accept(msgReports);
			}
			msgListYearMonth.clear();
			msgListYearMonth.add(2);
			ClientUI.chat.accept(msgYearMonth);
			for (String year : ChatClient.currBranchReports.getYearMonth().keySet()) {
				msgListReports.clear();
				msgListReports.add(2);
				msgListReports.add(year);
				msgListReports.add(ChatClient.currBranchReports.getYearMonth().get(year));
				msgReports = new Message(MessageType.getBranchReports, msgListReports);
				ClientUI.chat.accept(msgReports);
			}
			msgListYearMonth.clear();
			msgListYearMonth.add(3);
			ClientUI.chat.accept(msgYearMonth);
			for (String year : ChatClient.currBranchReports.getYearMonth().keySet()) {
				msgListReports.clear();
				msgListReports.add(3);
				msgListReports.add(year);
				msgListReports.add(ChatClient.currBranchReports.getYearMonth().get(year));
				msgReports = new Message(MessageType.getBranchReports, msgListReports);
				ClientUI.chat.accept(msgReports);
			}
		}
		leftArrowBtn.setDisable(true);
		rightArrowBtn.setDisable(true);
		// Populate the month ComboBox with the branch id's
		ObservableList<Integer> branchIDs = FXCollections.observableArrayList(Arrays.asList(1, 2, 3));
		branchCb.setItems(branchIDs);
	}

	/**
	 * Initializes the month combo box with months available for the selected year.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	public void initMonthCb(ActionEvent event) {
		leftArrowBtn.setDisable(true);
		rightArrowBtn.setDisable(true);
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

	@FXML
	public void initYearsCb(ActionEvent event) {
		leftArrowBtn.setDisable(true);
		rightArrowBtn.setDisable(true);
		currentRestaurantIndex = 0;
		monthCb.setValue(null);
		monthCb.getItems().clear();
		yearCb.setValue(null);
		yearCb.getItems().clear();
		ordertblview.getItems().clear();
		incometblview.getItems().clear();
		piechartperformance.getData().clear();
		avgdelatTimelbl.setText("");
		restaurantIdLbl.setText("Restaurant ID: ");
		if (branchCb.getValue() != null) {
			int selectedBranch = branchCb.getValue();
			// Populate the year ComboBox with available years from ChatClient.yearMonth
			thisBR = null;
			for (BranchReports br : ChatClient.branchReports) {
				if (br.getBranchID().getValue() == (selectedBranch)) {
					thisBR = br;
				}
			}
			if (thisBR == null) {
				showAlert("No Reports Available", "No reports were found for this year and month and branch");
				return;
			}

			ObservableList<String> years = FXCollections.observableArrayList(thisBR.getYearMonth().keySet());
			yearCb.setItems(years);
		}
	}

	/**
	 * Updates the table views with reports based on the selected year, month, and
	 * branch.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	public void updateTableValues(ActionEvent event) {
		leftArrowBtn.setDisable(true);
		rightArrowBtn.setDisable(true);
		String selectedYear = yearCb.getValue();
		String selectedMonth = monthCb.getValue();

		if (selectedYear != null && selectedMonth != null && branchCb.getValue() != null) {
			ir = thisBR.getIncomeReport();
			pr = thisBR.getPerformanceReport();
			or = thisBR.getOrderReport();

			if (pr != null && !pr.isEmpty()) {
				updateChartAndLabels();
				updateArrowButtonState(); // Enable/disable buttons based on the report size
			} else {
				leftArrowBtn.setDisable(true);
				rightArrowBtn.setDisable(true);
			}

			if (ir != null && !ir.isEmpty()) {
				ObservableList<IncomeReport> incomeReportList = FXCollections.observableArrayList(ir);
				incometblview.setItems(incomeReportList);
			}

			if (or != null && !or.isEmpty()) {
				ObservableList<OrderReport> orderReportList = FXCollections.observableArrayList(or);
				ordertblview.setItems(orderReportList);
			}
		}
	}

	/**
	 * Updates the performance chart and labels based on the current restaurant's
	 * performance report.
	 */
	private void updateChartAndLabels() {
		String selectedYear = yearCb.getValue();
		String selectedMonth = monthCb.getValue();
		if (selectedYear != null && selectedMonth != null && branchCb.getValue() != null) {

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
	}

	/**
	 * Displays the performance report for the previous restaurant.
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
	 * Displays the performance report for the next restaurant.
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
	    if (pr == null || pr.isEmpty()) {
	        leftArrowBtn.setDisable(true);
	        rightArrowBtn.setDisable(true);
	    } else {
	        leftArrowBtn.setDisable(currentRestaurantIndex == 0);
	        rightArrowBtn.setDisable(currentRestaurantIndex == pr.size() - 1);
	    }
	}

	/**
	 * Starts the ViewCEOReportsController and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ViewCEOReports.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ViewCEOReports.css").toExternalForm());
		primaryStage.setTitle("Ceo Report page");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Handles the action of navigating back to the CEO Home Page.
	 * 
	 * @param event The event triggered by the user's action.
	 * @throws Exception If there is an error during the navigation process.
	 */
	@Override
	public void getBackBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		CEOHomePageController chpc = new CEOHomePageController();
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
