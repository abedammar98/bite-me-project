package gui;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import logic.BranchReports;
import logic.Message;
import logic.MessageType;
import logic.QuarterReport;

public class ViewCEOQuarterReportsController implements Initializable, IControl {

	private int currentRestaurantIndex1 = 0;
	private int currentRestaurantIndex2 = 0;
	private List<Integer> restaurantIDs1;
	private List<Integer> restaurantIDs2;

	@FXML
	private Button leftArrowBtn1;

	@FXML
	private Button rightArrowBtn1;

	@FXML
	private Label restaurantIdLbl1;

	@FXML
	private Button leftArrowBtn2;

	@FXML
	private Button rightArrowBtn2;

	@FXML
	private Label restaurantIdLbl2;

	/**
	 * Button that navigates the user back to the previous page.
	 */
	@FXML
	private Button btnBack;

	/**
	 * ComboBox for selecting the year of the first quarter report.
	 */
	@FXML
	private ComboBox<String> firstyearCb;

	/**
	 * ComboBox for selecting the quarter of the first report.
	 */
	@FXML
	private ComboBox<String> firstmonthCb;

	/**
	 * ComboBox for selecting the branch ID for the first report.
	 */
	@FXML
	private ComboBox<Integer> firstbranchCb;

	/**
	 * ComboBox for selecting the year of the second quarter report.
	 */
	@FXML
	private ComboBox<String> secondyearCb;

	/**
	 * ComboBox for selecting the quarter of the second report.
	 */
	@FXML
	private ComboBox<String> secondmonthCb;

	/**
	 * ComboBox for selecting the branch ID for the second report.
	 */
	@FXML
	private ComboBox<Integer> secondbranchCb;

	/**
	 * BarChart to display the first quarter report.
	 */
	@FXML
	private BarChart<String, Number> firstBarChart;

	/**
	 * BarChart to display the second quarter report.
	 */
	@FXML
	private BarChart<String, Number> secondBarChart;

	/**
	 * CategoryAxis for the x-axis of the first BarChart.
	 */
	@FXML
	private CategoryAxis firstBarChartXAxis;

	/**
	 * NumberAxis for the y-axis of the first BarChart.
	 */
	@FXML
	private NumberAxis firstBarChartYAxis;

	/**
	 * CategoryAxis for the x-axis of the second BarChart.
	 */
	@FXML
	private CategoryAxis secondBarChartXAxis;

	/**
	 * NumberAxis for the y-axis of the second BarChart.
	 */
	@FXML
	private NumberAxis secondBarChartYAxis;

	/**
	 * Label for displaying the total revenue of the first report.
	 */
	@FXML
	private Label lbltotalRevenue1;

	/**
	 * Label for displaying the total revenue of the second report.
	 */
	@FXML
	private Label lbltotalRevenue2;

	private BranchReports thisBR1; // For the first ComboBox
	private BranchReports thisBR2; // For the second ComboBox

	private ArrayList<QuarterReport> qr1;
	private ArrayList<QuarterReport> qr2;

	/**
	 * Initializes the ComboBox for selecting the month for the first report based
	 * on the selected year.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	void initMonthCb(ActionEvent event) {
		leftArrowBtn1.setDisable(true);
		rightArrowBtn1.setDisable(true);
		// Get the selected year
		String selectedYear = firstyearCb.getValue();
		if (selectedYear != null && !selectedYear.isEmpty()) {
			// Retrieve the list of months for the selected year from ChatClient.yearMonth
			ArrayList<String> months = thisBR1.getYearMonth().get(selectedYear);

			// Populate the month ComboBox with the months for the selected year
			ObservableList<String> monthOptions = FXCollections.observableArrayList(months);
			firstmonthCb.setItems(monthOptions);
		}
	}

	/**
	 * Initializes the ComboBox for selecting the month for the second report based
	 * on the selected year.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	void initMonthCb1(ActionEvent event) {
		leftArrowBtn2.setDisable(true);
		rightArrowBtn2.setDisable(true);
		// Get the selected year
		String selectedYear = secondyearCb.getValue();
		if (selectedYear != null && !selectedYear.isEmpty()) {
			// Retrieve the list of months for the selected year from ChatClient.yearMonth
			ArrayList<String> months = thisBR2.getYearMonth().get(selectedYear);

			// Populate the month ComboBox with the months for the selected year
			ObservableList<String> monthOptions = FXCollections.observableArrayList(months);
			secondmonthCb.setItems(monthOptions); // Corrected line
		}
	}

	public void initYearsCb1(ActionEvent event) {
		resetBranchData1();
		if (firstbranchCb.getValue() != null) {
			int selectedBranch = firstbranchCb.getValue();
			thisBR1 = getBranchReports(selectedBranch);
			if (thisBR1 != null) {
				populateYearComboBox(firstyearCb, thisBR1);
			} else {
				showAlert("No Reports Available", "No reports were found for this branch.");
			}
		}
	}

	@FXML
	public void initYearsCb2(ActionEvent event) {
		resetBranchData2();
		if (secondbranchCb.getValue() != null) {
			int selectedBranch = secondbranchCb.getValue();
			thisBR2 = getBranchReports(selectedBranch);
			if (thisBR2 != null) {
				populateYearComboBox(secondyearCb, thisBR2);
			} else {
				showAlert("No Reports Available", "No reports were found for this branch.");
			}
		}
	}

//Method to get the BranchReports object based on the selected branch ID
	private BranchReports getBranchReports(int branchID) {
		for (BranchReports br : ChatClient.branchQuarterReports) {
			if (br.getBranchID().getValue() == branchID) {
				return br;
			}
		}
		return null;
	}

//Method to populate the year ComboBox
	private void populateYearComboBox(ComboBox<String> yearComboBox, BranchReports branchReports) {
		ObservableList<String> years = FXCollections.observableArrayList(branchReports.getYearMonth().keySet());
		yearComboBox.setItems(years);
	}

	private void resetBranchData1() {
		leftArrowBtn1.setDisable(true);
		rightArrowBtn1.setDisable(true);
		currentRestaurantIndex1 = 0;
		firstmonthCb.setValue(null);
		firstmonthCb.getItems().clear();
		firstyearCb.setValue(null);
		firstyearCb.getItems().clear();
		firstBarChart.getData().clear();
		lbltotalRevenue1.setText("");
		restaurantIDs1 = new ArrayList<>(); // Clear and reset the restaurant ID list
		restaurantIdLbl1.setText("Restaurant ID: ");
	}

	private void resetBranchData2() {
		leftArrowBtn2.setDisable(true);
		rightArrowBtn2.setDisable(true);
		currentRestaurantIndex2 = 0;
		secondmonthCb.setValue(null);
		secondmonthCb.getItems().clear();
		secondyearCb.setValue(null);
		secondyearCb.getItems().clear();
		secondBarChart.getData().clear();
		lbltotalRevenue2.setText("");
		restaurantIDs2 = new ArrayList<>(); // Clear and reset the restaurant ID list
		restaurantIdLbl2.setText("Restaurant ID: ");
	}

	/**
	 * Updates the data in the first BarChart and the total revenue label based on
	 * the selected year, quarter, and branch.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	void updateTableValues(ActionEvent event) {
		leftArrowBtn1.setDisable(true);
		rightArrowBtn1.setDisable(true);
		String selectedYear = firstyearCb.getValue();
		String selectedQuarter = firstmonthCb.getValue();

		if (selectedYear != null && selectedQuarter != null && firstbranchCb.getValue() != null) {
			qr1 = thisBR1.getFirstquarterReport();

			// Initialize the restaurant IDs list (you should get this from your data
			// source)
			restaurantIDs1 = new ArrayList<>();
			for (QuarterReport qr : qr1) {
				restaurantIDs1.add(qr.getRestaurantID());
			}
			// Set initial state
			if (!restaurantIDs1.isEmpty()) {
				currentRestaurantIndex1 = 0;
				updateChartAndLabels1();
			}
			updateArrowButtonState1();
		}
	}

	/**
	 * Updates the data in the second BarChart and the total revenue label based on
	 * the selected year, quarter, and branch.
	 * 
	 * @param event The event triggered by the user's action.
	 */
	@FXML
	void updateTableValues1(ActionEvent event) {
		leftArrowBtn2.setDisable(true);
		rightArrowBtn2.setDisable(true);
		String selectedYear = secondyearCb.getValue();
		String selectedQuarter = secondmonthCb.getValue();

		if (selectedYear != null && selectedQuarter != null && secondbranchCb.getValue() != null) {
			qr2 = thisBR2.getSecondquarterReport();

			// Initialize the restaurant IDs list (you should get this from your data
			// source)
			restaurantIDs2 = new ArrayList<>();
			for (QuarterReport qr : qr2) {
				restaurantIDs2.add(qr.getRestaurantID());
			}
			// Set initial state
			if (!restaurantIDs2.isEmpty()) {
				currentRestaurantIndex2 = 0;
				updateChartAndLabels2();
			}
			updateArrowButtonState2();
		}
	}

	// Method to update charts and labels based on the current restaurant for the
	// first chart
	private void updateChartAndLabels1() {

		int restaurantID = restaurantIDs1.get(currentRestaurantIndex1);
		// Populate the first chart and update the first revenue label
		populateBarChart(firstBarChart, qr1, true);
		// Update restaurant label
		restaurantIdLbl1.setText("Restaurant ID: " + restaurantID);
	}

	// Method to update charts and labels based on the current restaurant for the
	// second chart
	private void updateChartAndLabels2() {

		int restaurantID = restaurantIDs2.get(currentRestaurantIndex2);
		// Populate the second chart and update the second revenue label
		populateBarChart(secondBarChart, qr2, false);
		// Update restaurant label
		restaurantIdLbl2.setText("Restaurant ID: " + restaurantID);
	}

	@FXML
	private void showPreviousRestaurant1(ActionEvent event) {
		if (currentRestaurantIndex1 > 0) {
			currentRestaurantIndex1--;
			updateChartAndLabels1();
		}
		updateArrowButtonState1();
	}

	@FXML
	private void showNextRestaurant1(ActionEvent event) {
		if (currentRestaurantIndex1 < restaurantIDs1.size() - 1) {
			currentRestaurantIndex1++;
			updateChartAndLabels1();
		}
		updateArrowButtonState1();
	}

	@FXML
	private void showPreviousRestaurant2(ActionEvent event) {
		if (currentRestaurantIndex2 > 0) {
			currentRestaurantIndex2--;
			updateChartAndLabels2();
		}
		updateArrowButtonState2();
	}

	@FXML
	private void showNextRestaurant2(ActionEvent event) {
		if (currentRestaurantIndex2 < restaurantIDs2.size() - 1) {
			currentRestaurantIndex2++;
			updateChartAndLabels2();
		}
		updateArrowButtonState2();
	}

	private void updateArrowButtonState1() {
		leftArrowBtn1.setDisable(currentRestaurantIndex1 == 0);
		rightArrowBtn1.setDisable(currentRestaurantIndex1 == restaurantIDs1.size() - 1);
	}

	private void updateArrowButtonState2() {
		leftArrowBtn2.setDisable(currentRestaurantIndex2 == 0);
		rightArrowBtn2.setDisable(currentRestaurantIndex2 == restaurantIDs2.size() - 1);
	}

	/**
	 * Populates the specified BarChart with data from the provided quarter reports.
	 * Also updates the corresponding label for total revenue.
	 * 
	 * @param barChart       The BarChart to be populated.
	 * @param quarterReports The list of QuarterReport objects containing the data.
	 * @param isFirstChart   A flag indicating whether this is the first BarChart.
	 */
	private void populateBarChart(BarChart<String, Number> barChart, List<QuarterReport> quarterReports,
			boolean isFirstChart) {
		if (quarterReports == null || quarterReports.isEmpty()) {
			// Handle the case where there's no data to display
			showAlert("No Data", "No data available for the selected quarter.");
			barChart.getData().clear();
			if (isFirstChart) {
				lbltotalRevenue1.setText("N/A");
			} else {
				lbltotalRevenue2.setText("N/A");
			}
			return;
		}

		// Get the current restaurant ID based on the chart (first or second)
		int currentRestaurantID = isFirstChart ? restaurantIDs1.get(currentRestaurantIndex1)
				: restaurantIDs2.get(currentRestaurantIndex2);

		// Filter the reports for the current restaurant ID
		List<QuarterReport> filteredReports = new ArrayList<>();
		for (QuarterReport report : quarterReports) {
			if (report.getRestaurantID() == currentRestaurantID) {
				filteredReports.add(report);
			}
		}

		if (filteredReports.isEmpty()) {
			// Handle the case where there's no data to display for the selected restaurant
			showAlert("No Data", "No data available for the selected restaurant.");
			barChart.getData().clear();
			if (isFirstChart) {
				lbltotalRevenue1.setText("N/A");
			} else {
				lbltotalRevenue2.setText("N/A");
			}
			return;
		}

		// Clear previous data and categories
		barChart.getData().clear();
		CategoryAxis xAxis = (CategoryAxis) barChart.getXAxis();
		xAxis.getCategories().clear(); // Clear existing categories

		Map<String, Integer> dayToOrderMap = new TreeMap<>(); // Using TreeMap to keep the dates ordered

		// Aggregate the orders by day for the filtered reports
		for (QuarterReport report : filteredReports) {
			if (report.getDayToOrder() != null) {
				report.getDayToOrder().forEach((day, orders) -> {
					dayToOrderMap.merge(day, orders, Integer::sum);
				});
			}
		}

		// Populate the bar chart
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		dayToOrderMap.forEach((day, orders) -> {
			series.getData().add(new XYChart.Data<>(day, orders));
		});
		barChart.getData().add(series);

		// Set new categories based on the current data
		ObservableList<String> categories = FXCollections.observableArrayList(dayToOrderMap.keySet());
		xAxis.setCategories(categories);

		// Update the corresponding label for total revenue
		double totalRevenue = filteredReports.stream().mapToDouble(QuarterReport::getTotalRevenue).sum();
		if (isFirstChart) {
			lbltotalRevenue1.setText(String.format("%.2f", totalRevenue));
		} else {
			lbltotalRevenue2.setText(String.format("%.2f", totalRevenue));
		}

		// Adjust the axis properties dynamically
		xAxis.setAutoRanging(false);
		xAxis.setTickLabelRotation(45); // Rotate labels for better readability

		NumberAxis yAxis = (NumberAxis) barChart.getYAxis();
		yAxis.setAutoRanging(true);
		yAxis.setForceZeroInRange(true); // Ensure the y-axis starts from zero
	}

	/**
	 * Starts the ViewCEOQuarterReportsController and sets up the user interface.
	 * 
	 * @param primaryStage The primary stage for this application.
	 * @throws Exception If there is an error during the initialization of the
	 *                   stage.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ViewCEOQuarterReports.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ViewCEOQuarterReports.css").toExternalForm());
		primaryStage.setTitle("Ceo Quarter Report page");
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

	/**
	 * Initializes the controller after its root element has been completely
	 * processed.
	 * 
	 * @param location  The location used to resolve relative paths for the root
	 *                  object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if
	 *                  the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChatClient.currController=this;
		leftArrowBtn1.setDisable(true);
		rightArrowBtn1.setDisable(true);
		leftArrowBtn2.setDisable(true);
		rightArrowBtn2.setDisable(true);
		if (ChatClient.branchQuarterReports == null
				|| ChatClient.lastDataFetchMonthQuarter != (LocalDateTime.now().getMonthValue())) {
			ChatClient.lastDataFetchMonthQuarter = LocalDateTime.now().getMonthValue();
			ArrayList<Object> msgListYearMonth = new ArrayList<>();
			ArrayList<Object> msgListReports = new ArrayList<>();
			Message msgYearMonth;
			Message msgReports;
			msgListYearMonth.add(1);
			msgYearMonth = new Message(MessageType.GetCEOReportsYearQuarter, msgListYearMonth);
			ClientUI.chat.accept(msgYearMonth);
			for (String year : ChatClient.currBranchReports.getYearMonth().keySet()) {
				msgListReports.clear();
				msgListReports.add(1);
				msgListReports.add(year);
				msgListReports.add(ChatClient.currBranchReports.getYearMonth().get(year));
				msgReports = new Message(MessageType.getQuarterReports1, msgListReports);
				ClientUI.chat.accept(msgReports);
				msgReports = new Message(MessageType.getQuarterReports2, msgListReports);
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
				msgReports = new Message(MessageType.getQuarterReports1, msgListReports);
				ClientUI.chat.accept(msgReports);
				msgReports = new Message(MessageType.getQuarterReports2, msgListReports);
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
				msgReports = new Message(MessageType.getQuarterReports1, msgListReports);
				ClientUI.chat.accept(msgReports);
				msgReports = new Message(MessageType.getQuarterReports2, msgListReports);
				ClientUI.chat.accept(msgReports);
			}
		}

		// Populate the month ComboBox with the branch id's
		ObservableList<Integer> branchIDs = FXCollections.observableArrayList(Arrays.asList(1, 2, 3));
		firstbranchCb.setItems(branchIDs);
		secondbranchCb.setItems(branchIDs);

		firstBarChart.setTitle("First Quarter Report");
		secondBarChart.setTitle("Second Quarter Report");

		firstBarChartXAxis.setLabel("Day/Month");
		firstBarChartYAxis.setLabel("Number of Orders");

		secondBarChartXAxis.setLabel("Day/Month");
		secondBarChartYAxis.setLabel("Number of Orders");

		firstBarChart.setLegendVisible(false);
		secondBarChart.setLegendVisible(false);

		// Adjust the axis properties for better alignment
		configureAxisProperties(firstBarChartXAxis, firstBarChartYAxis);
		configureAxisProperties(secondBarChartXAxis, secondBarChartYAxis);
	}

	/**
	 * Configures the properties of the specified axes to improve the display of the
	 * BarChart.
	 * 
	 * @param xAxis The CategoryAxis to be configured.
	 * @param yAxis The NumberAxis to be configured.
	 */
	// Method to configure axis properties
	private void configureAxisProperties(CategoryAxis xAxis, NumberAxis yAxis) {
		xAxis.setAutoRanging(true);
		xAxis.setTickMarkVisible(true);
		xAxis.setTickLabelRotation(45); // Rotate labels for better readability
		yAxis.setAutoRanging(true);
		yAxis.setForceZeroInRange(true); // Ensure the y-axis starts from zero
	}
}
