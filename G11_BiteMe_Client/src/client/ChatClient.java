// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.ChatIF;
import gui.IControl;
import gui.ReceiveOrderController;
import gui.SupplierOrdersPageController;
import javafx.application.Platform;
import logic.Branch;
import logic.BranchReports;
import logic.CertifiedWorker;
import logic.IncomeReport;
import logic.Item;
import logic.MenuItem;
import logic.Message;
import logic.Order;
import logic.OrderItem;
import logic.OrderItemDetail;
import logic.OrderReport;
import logic.PerformanceReport;
import logic.Quarter;
import logic.QuarterReport;
import logic.Report;
import logic.ReportType;
import logic.Resturant;
import logic.User;
import java.io.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {

	/** The current logged-in user. */
	public static User currUser = null;

	/** The last action performed by the client. */
	public static String lastAction = "";

	/** The list of orders associated with the user. */
	public static ArrayList<Order> userOrders;

	/** The list of restaurants available to the user. */
	public static ArrayList<Resturant> userResturants;

	/** The restaurant selected by the user. */
	public static Resturant selectedResturant;

	/** The menu items available in the selected restaurant. */
	public static MenuItem menuItems;

	/** The user's cart containing order details. */
	public static ArrayList<OrderItemDetail> userCart;

	/** The new order being placed by the user. */
	public static Order newOrder;

	/** The list of items available in the system. */
	public static ArrayList<Item> items;

	/** The current controller handling the client's requests. */
	public static IControl currController;

	/** The current order item being processed. */
	public static OrderItem currentOrderItem;

	/** The list of meal details in the user's order. */
	public static ArrayList<OrderItemDetail> orderMeals;

	/** The discounts applied to the order. */
	public static String discountsApplied = "";

	/** The cost associated with delivery. */
	public static int deliveryCost;

	/** An Array of BranchReports to save the reports for each branch */
	public static ArrayList<BranchReports> branchReports;

	/** An Array of BranchReports to save the quarter reports for each branch */
	public static ArrayList<BranchReports> branchQuarterReports;

	/** The list of income reports available to the user. */
	public static ArrayList<IncomeReport> incomeReport;

	/** The list of first quarter reports available to the user. */
	public static ArrayList<QuarterReport> firstquarterReport;

	/** The list of second quarter reports available to the user. */
	public static ArrayList<QuarterReport> secondquarterReport;

	/** The list of performance reports available to the user. */
	public static ArrayList<PerformanceReport> performanceReport;

	/** The list of order reports available to the user. */
	public static ArrayList<OrderReport> orderReport;

	/** A map of available years and months for report generation. */
	public static Map<String, ArrayList<String>> yearMonth;

	/** Indicates whether the client is awaiting a response from the server. */
	public static boolean awaitResponse = false;

	public static BranchReports currBranchReports;

	public static int lastDataFetchMonth;
	public static int lastDataFetchMonthQuarter;

	/** The interface type variable for client UI communication. */
	ChatIF clientUI;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		// openConnection();
	}

	// Instance methods ************************************************

	/**
	 * Handles all data that comes in from the server.
	 *
	 * @param msg The message received from the server.
	 */
	@SuppressWarnings("unchecked")
	public void handleMessageFromServer(Object msg) {
		awaitResponse = false;
		System.out.println("--> handleMessageFromServer");
		Message message = (Message) msg;
		ArrayList<Object> arr = (ArrayList<Object>) message.getObject();
		switch (message.getType()) {
		case UserConnect:
			if (((String) arr.get(0)).equals("User not found")) {
				lastAction = "User not found";
			} else if (((String) arr.get(0)).equals("User is already logged in")) {
				lastAction = "User is already logged in";
			} else if (((String) arr.get(0)).equals("User is valid")) {
				lastAction = "User is valid";
				currUser = (User) (arr.get(1));
				currUser.setIsLogged(1);
			}
			break;
		case UserDisconnect:
			currUser = null;
			lastAction = "Disconnected";
			break;
		case GetCustomerOrders:
			userOrders = new ArrayList<>();
			lastAction = "Order is found";
			for (Object obj : arr) {
				if (obj instanceof Order) {
					userOrders.add((Order) obj);
				}
			}
			break;
		case UpdateOrderStatus:
			lastAction = "Order Status has been changed";
			break;
		case GetResturants:
			lastAction = "Got Resturants";
			userResturants = new ArrayList<>();
			for (Object obj : arr) {
				if (obj instanceof Resturant) {
					userResturants.add((Resturant) obj);
				}
			}
			break;
		case GetResturantMenuItems:
			menuItems = (MenuItem) arr.get(0);
			lastAction = "Returned menu items";
			break;
		case AddOrderToDB:
			if (((boolean) arr.get(0)) == true)
				lastAction = "Order Confirmed\nOrder number is: " + (int) arr.get(1) + ".";
			else
				lastAction = "Order was unsuccessful";
			break;
		case GetResturantOrders:
			userOrders = new ArrayList<>();
			lastAction = "Order is found";
			for (Object obj : arr) {
				if (obj instanceof Order) {
					userOrders.add((Order) obj);
				}
			}
			break;
		case GetAllItems:
			menuItems = (MenuItem) arr.get(0);
			lastAction = "Got restaurant menu";
			break;
		case UpdateOrderStatusSupplier:
			Order tempOrder = (Order) arr.get(0);
			String newStatus = (String) arr.get(1);
			if (currController != null && tempOrder.getUserID() == currUser.getUserID()) {
				if (currController instanceof ReceiveOrderController) {
					((ReceiveOrderController) currController).updateTableView();
				}
				Platform.runLater(() -> {
					currController.showAlert("Simulation",
							"Email: " + currUser.getEmail() + "\nPhone: " + currUser.getPhone()
									+ "\nYour order with ID: " + tempOrder.getOrderID() + " is now " + newStatus);
				});
			}
			break;
		case AddItemSupplier:
			if (((String) arr.get(1)).equals("Item added successfully."))
				lastAction = "Item added successfully.";
			else if (((String) arr.get(1)).equals("Failed to add item."))
				lastAction = "Failed to add item.";
			else
				lastAction = "Error!";
			break;
		case GetOrderItems:
			orderMeals = (ArrayList<OrderItemDetail>) arr.get(0);
			lastAction = "Fetched order meals";
			break;
		case AddUserToDB:
			String returnedMsg = (String) arr.get(0);
			if (returnedMsg.equals("User added successfully."))
				lastAction = "User added successfully.";
			else
				lastAction = "Username already exists.";
			break;
		case GetBranchManagerReportsYearMonth:
			lastAction = "Got year months for reports";
			yearMonth = (HashMap<String, ArrayList<String>>) arr.get(0);
			if (branchReports == null)
				branchReports = new ArrayList<>();
			currBranchReports = new BranchReports(Branch.fromValue((int) arr.get(1)), null, null, null, null, null,
					yearMonth);
			break;
		case getBranchReports:
			lastAction = "Got Branch Reports";
			incomeReport = new ArrayList<>();
			orderReport = new ArrayList<>();
			performanceReport = new ArrayList<>();
			for (Object obj : arr) {
				if (obj instanceof Report) {
					switch (((Report) obj).getReportType()) {
					case PerformanceReport:
						performanceReport.add((PerformanceReport) obj);
						break;
					case IncomeReport:
						incomeReport.add((IncomeReport) obj);
						break;
					case OrderReport:
						orderReport.add((OrderReport) obj);
						break;
					default:
						break;
					}
				}
			}
			currBranchReports.setIncomeReport(incomeReport);
			currBranchReports.setOrderReport(orderReport);
			currBranchReports.setPerformanceReport(performanceReport);
			branchReports.add(currBranchReports);
			break;
		case GetCEOReportsYearQuarter:
			lastAction = "Got year quarter for reports";
			yearMonth = (HashMap<String, ArrayList<String>>) arr.get(0);
			if (branchQuarterReports == null)
				branchQuarterReports = new ArrayList<>();
			currBranchReports = new BranchReports(Branch.fromValue((int) arr.get(1)), null, null, null, null, null,
					yearMonth);
			break;
		case getQuarterReports1:
			lastAction = "Got quarter reports";
			firstquarterReport = new ArrayList<>();
			for (Object obj : arr) {
				if (obj instanceof QuarterReport) {
					firstquarterReport.add((QuarterReport) obj);
				}
			}
			currBranchReports.setFirstquarterReport(firstquarterReport);
			break;
		case getQuarterReports2:
			lastAction = "Got quarter reports";
			secondquarterReport = new ArrayList<>();
			for (Object obj : arr) {
				if (obj instanceof QuarterReport) {
					secondquarterReport.add((QuarterReport) obj);
				}
			}
			currBranchReports.setSecondquarterReport(secondquarterReport);
			branchQuarterReports.add(currBranchReports);

			break;
		case SupplierRecievedNewOrder:
			int newRecievedOrderID = (int) arr.get(0);
			int newRestaurantOrderID = (int) arr.get(1);
			if (currController != null && currUser instanceof CertifiedWorker
					&& newRestaurantOrderID == ((CertifiedWorker) currUser).getResturantID()) {
				if (currController instanceof SupplierOrdersPageController) {
					((SupplierOrdersPageController) currController).reloadOrders();
				}
				Platform.runLater(() -> {
					currController.showAlert("Simulation", "New order received\nOrder ID is: " + newRecievedOrderID);
				});
			}
			break;
		default:
			break;
		}

	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param msg The message from the UI.
	 */

	public void handleMessageFromClientUI(Object msg) {
		try {
			openConnection();// in order to send more than one message
			awaitResponse = true;
			sendToServer(msg);
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client." + e);
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
//End of ChatClient class
