package Server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import gui.ServerPortFrameController;
import logic.ClientInfo;
import logic.Message;
import logic.MessageType;
import logic.Order;
import logic.OrderItem;
import logic.User;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * The `EchoServer` class handles server operations, including managing client
 * connections, handling client requests, interacting with the database, and
 * updating the client list.
 * 
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class EchoServer extends AbstractServer {
	/** Database control instance for handling database operations. */
	public DBControl db;

	/** List of all connected clients. */
	public static ArrayList<ClientInfo> ConnectedClientsList = new ArrayList<>();

	/** List of connected customers. */
	public static ArrayList<ClientInfo> ConnectedCustomerList = new ArrayList<>();

	/** List of connected suppliers. */
	public static ArrayList<ClientInfo> ConnectedSupplierList = new ArrayList<>();

	/** List of connected CEOs. */
	public static ArrayList<ClientInfo> ConnectedCeoList = new ArrayList<>();

	/** List of connected managers. */
	public static ArrayList<ClientInfo> ConnectedManagerList = new ArrayList<>();

	/** Controller for the server's UI. */
	private ServerPortFrameController controller;

	/**
	 * Constructs an `EchoServer` object with the specified port.
	 * 
	 * @param port The port number to listen on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	/**
	 * Sets the controller for this server.
	 * 
	 * @param controller The `ServerPortFrameController` to be set.
	 */
	public void setController(ServerPortFrameController controller) {
		this.controller = controller;
	}

	/**
	 * Gets the controller associated with this server.
	 * 
	 * @return The `ServerPortFrameController` of this server.
	 */
	public ServerPortFrameController getController() {
		return this.controller;
	}

	/**
	 * Handles incoming messages from clients.
	 * 
	 * @param msg    The message received from the client.
	 * @param client The connection to the client sending the message.
	 */
	@SuppressWarnings("unchecked")
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Message message = (Message) msg;
		ArrayList<Object> msgObjectList = (ArrayList<Object>) message.getObject();

		switch (message.getType()) {
		case ClientConnected:
			String name = (String) msgObjectList.get(0);
			String address = (String) msgObjectList.get(1);
			ConnectedClientsList.add(new ClientInfo(name, address, "Connected"));
			try {
				client.sendToClient(new Message(null, null));
			} catch (IOException e) {
				e.printStackTrace();
			}
			notifyClientListUpdate();
			break;
		case ClientDisconnected:
			String name2 = (String) msgObjectList.get(0);
			ConnectedClientsList.removeIf(ci -> ci.getName().equals(name2));
			try {
				client.sendToClient(null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			notifyClientListUpdate();
			break;
		case UserConnect:
			Message resultMsg = (Message) db.userValidation(msgObjectList);
			ArrayList<Object> resultList = (ArrayList<Object>) resultMsg.getObject();
			String firstElement = (String) resultList.get(0);
			if (firstElement.equals("User is valid")) {
				User user = (User) resultList.get(1);
				switch (user.getUserType()) {
				case Customer:
					ConnectedCustomerList.add(
							new ClientInfo(user.getUsername(), client.getInetAddress().getHostAddress(), "Connected"));
					break;
				case CEO:
					ConnectedCeoList.add(
							new ClientInfo(user.getUsername(), client.getInetAddress().getHostAddress(), "Connected"));
					break;
				case Manager:
					ConnectedManagerList.add(
							new ClientInfo(user.getUsername(), client.getInetAddress().getHostAddress(), "Connected"));
					break;
				case Supplier:
					ConnectedSupplierList.add(
							new ClientInfo(user.getUsername(), client.getInetAddress().getHostAddress(), "Connected"));
					break;
				default:
					break;
				}
				notifyClientListUpdate();
			}
			try {
				client.sendToClient(resultMsg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case UserDisconnect:
			db.updateUserLogged(msgObjectList);
			User user = (User) msgObjectList.get(0);
			switch (user.getUserType()) {
			case Customer:
				ConnectedCustomerList.removeIf(ci -> ci.getName().equals(user.getUsername()));
				break;
			case CEO:
				ConnectedCeoList.removeIf(ci -> ci.getName().equals(user.getUsername()));
				break;
			case Manager:
				ConnectedManagerList.removeIf(ci -> ci.getName().equals(user.getUsername()));
				break;
			case Supplier:
				ConnectedSupplierList.removeIf(ci -> ci.getName().equals(user.getUsername()));
				break;
			default:
				break;
			}
			notifyClientListUpdate();
			try {
				client.sendToClient(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case GetCustomerOrders:
			Message msgOrder = new Message(MessageType.GetCustomerOrders,
					(ArrayList<Object>) db.getOrderHistory(msgObjectList));
			try {
				client.sendToClient(msgOrder);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case GetResturants:
			try {
				client.sendToClient(db.getResturants(msgObjectList));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case UpdateOrderStatus:
			db.updateOrderStatusToReceived(msgObjectList);
			Message msgUpdateOrder = new Message(MessageType.UpdateOrderStatus, null);
			try {
				client.sendToClient(msgUpdateOrder);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case GetResturantMenuItems:
			try {
				client.sendToClient(db.getResturantMenuItems((int) msgObjectList.get(0)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case AddOrderToDB:
			Message returnedMsgAddOrder = (Message) db.AddOrderToDb(msgObjectList);
			try {
				client.sendToClient(returnedMsgAddOrder);
				ArrayList<Object> arr = (ArrayList<Object>) returnedMsgAddOrder.getObject();
				if ((boolean) arr.get(0)) {
					int orderID = (int) arr.get(1);
					OrderItem orderItem = (OrderItem) msgObjectList.get(0);
					Order order = orderItem.getOrder();
					int restaurantID = (int) order.getResturantID();
					ArrayList<Object> arrToReturn = new ArrayList<>();
					arrToReturn.add(orderID);
					arrToReturn.add(restaurantID);
					this.sendToAllClients(new Message(MessageType.SupplierRecievedNewOrder, arrToReturn));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case UpdateOrderStatusSupplier:
			db.updateOrderStatusAndETA(msgObjectList, msgObjectList);
			Message msgUpdateOrder1 = new Message(MessageType.UpdateOrderStatusSupplier, msgObjectList);
			this.sendToAllClients(msgUpdateOrder1);
			break;
		case GetResturantOrders:
			Message msgOrder1 = new Message(MessageType.GetResturantOrders,
					(ArrayList<Object>) db.getOrderHistorySupplier(msgObjectList));
			try {
				client.sendToClient(msgOrder1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case AddItemSupplier:
			try {
				client.sendToClient(db.addItemToDb(msgObjectList));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case DeleteItemSupplier:
			try {
				Message result = (Message) db.DeleteItemFromDb(msgObjectList);
				client.sendToClient(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case GetAllItems:
			Message msgItem = (Message) db.getAllItemsSupplier(msgObjectList);
			try {
				client.sendToClient(msgItem);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case GetOrderItems:
			try {
				client.sendToClient(db.getOrderItems(msgObjectList));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case AddUserToDB:
			try {
				client.sendToClient(db.addUserToDB(msgObjectList));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case GetBranchManagerReportsYearMonth:
			try {
				client.sendToClient(db.getReportsYearMonth(msgObjectList));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case getBranchReports:
			try {
				client.sendToClient(db.getBranchReports(msgObjectList));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case GetCEOReportsYearQuarter:
			try {
				client.sendToClient(db.getCEOReprotsYearQuarter(msgObjectList));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case getQuarterReports1:
			Message returnedMsg = (Message) db.getQuarterReports(msgObjectList);
			try {
				client.sendToClient(returnedMsg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case getQuarterReports2:
			Message returnedMsg2 = (Message) db.getQuarterReports(msgObjectList);
			returnedMsg2.setType(MessageType.getQuarterReports2);
			try {
				client.sendToClient(returnedMsg2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Generates a report using the database control instance.
	 */
	public void generateReport() {
		try {
			db.generateMonthlyReports(2024, 7);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Notifies the controller to update the client list view.
	 */
	private void notifyClientListUpdate() {
		if (controller != null) {
			controller.updateTableView();
		}
	}

	/**
	 * Invoked when the server starts, indicating that it is listening for
	 * connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * Closes the database connection when the server is stopped.
	 */
	public void closeDB() {
		if (db != null) {
			db.disconnectFromDB();
		} else {
			System.out.println("DB is not connected");
		}
	}

	/**
	 * Starts the database connection with the specified parameters.
	 * 
	 * @param dbName The name of the database.
	 * @param user   The database username.
	 * @param pass   The database password.
	 */
	public void startDB(String dbName, String user, String pass) {
		db = DBControl.getInstance();
		db.connectToDB(dbName, user, pass);
		// Start the report generator
		ReportGenerator reportGenerator = ReportGenerator.getInstance();
		reportGenerator.startGeneratingReports();
		// Ensure to stop the generator when shutting down the server
		Runtime.getRuntime().addShutdownHook(new Thread(reportGenerator::stopGeneratingReports));
		//generateReport();
	}

	/**
	 * Invoked when the server stops, indicating that it has stopped listening for
	 * connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	/**
	 * Handles the disconnection of a client from the server.
	 * 
	 * @param messageArr An array containing details about the disconnected client.
	 */
	synchronized protected void clientDisconnected(String[] messageArr) {
		System.out.println("Client " + messageArr[1] + " Disconnected from IP: " + messageArr[2]);
	}
}
