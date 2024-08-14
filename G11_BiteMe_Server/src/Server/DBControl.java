package Server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logic.UserType;
import logic.User;
import logic.Category;
import logic.CertifiedWorker;
import logic.ConcreteItem;
import logic.Doneness;
import logic.Drinks;
import logic.IncomeReport;
import logic.Item;
import logic.MainMeal;
import logic.MenuItem;
import logic.Message;
import logic.MessageType;
import logic.Order;
import logic.OrderItem;
import logic.OrderItemDetail;
import logic.OrderReport;
import logic.PerformanceReport;
import logic.Quarter;
import logic.QuarterReport;
import logic.ReportType;
import logic.Resturant;
import logic.Salad;
import logic.Size;
import logic.Sweets;

/**
 * The `DBControl` class is responsible for managing database operations related
 * to the BiteMe application. This class uses a singleton pattern to ensure a
 * single instance is used throughout the application.
 *
 * @version 5.0
 * @authors Amal, Adam, Abed, Heba, Bashar, Mohammed
 */
public class DBControl {
	// Singleton instance
	private static DBControl instance;

	// Database connection
	private Connection conn;

	// Private constructor to prevent instantiation
	private DBControl() {
	}

	// Public method to provide access to the instance
	public static DBControl getInstance() {
		if (instance == null) {
			instance = new DBControl();
		}
		return instance;
	}
	// Methods

	/**
	 * Connects to the database using the specified database name, user, and
	 * password.
	 *
	 * @param dbName The name of the database.
	 * @param user   The database username.
	 * @param pass   The database password.
	 */
	public void connectToDB(String dbName, String user, String pass) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			conn = DriverManager.getConnection(dbName + "?serverTimezone=IST", user, pass);
			System.out.println("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Disconnects from the database if the connection is active.
	 */
	public void disconnectFromDB() {
		if (conn != null) {
			try {
				conn.close();
				System.out.println("SQL connection closed successfully");
			} catch (SQLException ex) {
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
		} else {
			System.out.println("Connection is already closed or was never opened");
		}
	}

	/**
	 * Imports data from an external database to the BiteMe database.
	 *
	 * @return An object indicating the success or failure of the operation.
	 */
	public Object importExternalDB() {
		PreparedStatement ps, ps1;
		ResultSet res;
		try {
			// Import users from externalusermanagement.user to biteme.users
			ps = conn.prepareStatement("SELECT * FROM externalusermanagement.user");
			ps.execute();
			res = ps.getResultSet();
			while (res.next()) {
				ps1 = conn.prepareStatement(
						"INSERT INTO biteme.user (UserID, FirstName, LastName, Username, Password, UserType, BranchID, Phone, Email, CreditCard, isLogged, amountOfCopuns, AccountType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				ps1.setInt(1, res.getInt("UserID"));
				ps1.setString(2, res.getString("FirstName"));
				ps1.setString(3, res.getString("LastName"));
				ps1.setString(4, res.getString("Username"));
				ps1.setString(5, res.getString("Password"));
				ps1.setString(6, res.getString("UserType"));
				ps1.setInt(7, res.getInt("BranchID"));
				ps1.setString(8, res.getString("Phone"));
				ps1.setString(9, res.getString("Email"));
				ps1.setString(10, res.getString("CreditCard"));
				ps1.setInt(11, res.getInt("isLogged"));
				ps1.setInt(12, res.getInt("amountOfCopuns"));
				ps1.setString(13, res.getString("AccountType"));
				ps1.execute();
			}
			// Import certified workers from externalusermanagement.certifiedworker
			ps = conn.prepareStatement("SELECT * FROM externalusermanagement.certifiedworker");
			ps.execute();
			res = ps.getResultSet();
			while (res.next()) {
				ps1 = conn.prepareStatement("INSERT INTO biteme.certifiedworker(UserID, RestaurantID) VALUES (?, ?)");
				ps1.setInt(1, res.getInt("UserID"));
				ps1.setInt(2, res.getInt("RestaurantID"));
				ps1.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Validates a user's credentials and returns a corresponding message.
	 *
	 * @param msgObjectList A list containing the username, password, and user type.
	 * @return A message object indicating the result of the validation.
	 */
	public Object userValidation(ArrayList<Object> msgObjectList) {
		String username = (String) msgObjectList.get(0);
		String password = (String) msgObjectList.get(1);
		String userType = (String) msgObjectList.get(2);
		String query = "SELECT * FROM user WHERE Username = ? AND Password = ? AND UserType = ?";
		ArrayList<Object> returnList = new ArrayList<>();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, userType);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					User user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), logic.UserType.getUserType(rs.getString(6)), rs.getInt(7), rs.getString(8),
							rs.getString(9), rs.getString(10), rs.getInt(11), rs.getInt(12), rs.getString(13));

					if (user.getIsLogged() == 1) {
						returnList.add("User is already logged in");
						return new Message(MessageType.UserConnect, returnList);
					}

					// If the user is a supplier, create a CertifiedWorker instance
					if (user.getUserType() == UserType.Supplier) {
						int userID = user.getUserID();
						String resturantQuery = "SELECT RestaurantID FROM certifiedworker WHERE UserID = ?";
						try (PreparedStatement resturantPs = conn.prepareStatement(resturantQuery)) {
							resturantPs.setInt(1, userID);
							try (ResultSet resturantRs = resturantPs.executeQuery()) {
								if (resturantRs.next()) {
									int resturantID = resturantRs.getInt("RestaurantID");
									CertifiedWorker certifiedWorker = new CertifiedWorker(user.getUserID(),
											user.getFirstName(), user.getLastName(), user.getUsername(),
											user.getPassword(), user.getUserType(), user.getBranchID(), user.getPhone(),
											user.getEmail(), user.getCreditCard(), user.getIsLogged(),
											user.getAmountOfCopunts(), resturantID, user.getAccountType());
									ArrayList<Object> userIsLoggedList = new ArrayList<>();
									userIsLoggedList.add(certifiedWorker);
									userIsLoggedList.add(1);
									updateUserLogged(userIsLoggedList);
									returnList.add("User is valid");
									returnList.add(certifiedWorker);
									return new Message(MessageType.UserConnect, returnList);
								}
							}
						}
					} else {
						ArrayList<Object> userIsLoggedList = new ArrayList<>();
						userIsLoggedList.add(user);
						userIsLoggedList.add(1);
						updateUserLogged(userIsLoggedList);

						returnList.add("User is valid");
						returnList.add(user);
						return new Message(MessageType.UserConnect, returnList);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		returnList.add("User not found");
		return new Message(MessageType.UserConnect, returnList);
	}

	/**
	 * Updates the logged-in status of a user in the database.
	 *
	 * @param msgObjectList A list containing the user and their logged-in status.
	 */
	public void updateUserLogged(ArrayList<Object> msgObjectList) {
		String query = "UPDATE user SET isLogged = ? WHERE UserID = ?";
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, (Integer) msgObjectList.get(1));
			ps.setInt(2, ((User) msgObjectList.get(0)).getUserID());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the order history for a specific user.
	 *
	 * @param msgObjectList A list containing the user ID.
	 * @return A list of orders associated with the user.
	 */
	public Object getOrderHistory(ArrayList<Object> msgObjectList) {
		ArrayList<Object> resultList = new ArrayList<>();
		int userid = (int) msgObjectList.get(0);
		String str = "SELECT * FROM `Order` WHERE UserID=?";
		try {
			PreparedStatement ps = conn.prepareStatement(str);
			ps.setInt(1, userid);
			try {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					// Fetch dates correctly from the ResultSet
					Order order = new Order(rs.getInt("OrderID"), rs.getInt("UserID"),
							rs.getDate("DateOfOrder").toLocalDate().plusDays(1), rs.getString("TimeOfOrder"),
							rs.getInt("RestaurantID"), rs.getString("PickUpType"), rs.getString("Location"),
							rs.getString("Status"), rs.getString("RequestedTimeOfDelivery"),
							rs.getDate("RequestedDateOfDelivery").toLocalDate().plusDays(1), rs.getString("ETA"),
							rs.getString("Duration"), rs.getFloat("Cost"));
					resultList.add(order);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * Retrieves a list of all restaurants in the database.
	 *
	 * @param msgObjectList An empty list.
	 * @return A message containing a list of restaurants.
	 */
	public Object getResturants(ArrayList<Object> msgObjectList) {
		String query = "SELECT * FROM restaurant";
		ArrayList<Object> resultList = new ArrayList<>();
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Resturant res = new Resturant(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
					resultList.add(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Message(MessageType.GetResturants, resultList);
	}

	/**
	 * Updates the status of an order to 'Received' and records its duration.
	 *
	 * @param inputList A list containing the order details.
	 */
	public void updateOrderStatusToReceived(ArrayList<Object> inputList) {
		// Retrieve the order ID from the input list
		int orderID = ((Order) inputList.get(0)).getOrderID();
		String duration = ((Order) inputList.get(0)).getDuration();

		// SQL query to update the status of the order
		String updateQuery = "UPDATE `Order` SET Status = 'Received', Duration = ? WHERE OrderID = ?";

		try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
			ps.setString(1, duration);
			ps.setInt(2, orderID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		boolean eligilbeForCoupon = ((Order) inputList.get(0)).customerEligibleForCoupon();
		if (eligilbeForCoupon)
			updateCustomerCoupons(((Order) inputList.get(0)).getUserID());
	}

	/**
	 * Updates the number of coupons for a customer.
	 *
	 * @param customerID The ID of the customer.
	 */
	public void updateCustomerCoupons(int customerID) {
		// SQL query to update the customer's coupons
		String updateCouponsQuery = "UPDATE `user` SET amountOfCopuns = amountOfCopuns + 1 WHERE UserID = ?";

		try (PreparedStatement ps = conn.prepareStatement(updateCouponsQuery)) {
			ps.setInt(1, customerID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the menu items for a specific restaurant.
	 *
	 * @param menuID The ID of the menu.
	 * @return A message containing the menu items.
	 */
	public Object getResturantMenuItems(int menuID) {
		String query = "SELECT i.ItemID, i.ItemName, i.Description, i.Cost, i.CategoryName, "
				+ "s.Ingredients AS saladIngredients, s.Size AS saladSize, "
				+ "m.CookMethod AS mainMealCookMethod, m.Doneness AS mainMealDoneness, "
				+ "sw.Ingredients AS sweetsIngredients, d.Size AS drinkSize " + "FROM item i "
				+ "JOIN itemmenu im ON i.ItemID = im.ItemID " + "LEFT JOIN salad s ON i.ItemID = s.ItemID "
				+ "LEFT JOIN mainmeal m ON i.ItemID = m.ItemID " + "LEFT JOIN sweets sw ON i.ItemID = sw.ItemID "
				+ "LEFT JOIN drinks d ON i.ItemID = d.ItemID " + "WHERE im.MenuID = ?";

		ArrayList<Item> menuItems = new ArrayList<>();
		Set<Integer> seenItemIDs = new HashSet<>();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, menuID);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int itemID = rs.getInt("ItemID");
					if (seenItemIDs.contains(itemID)) {
						continue; // Skip this item as it has already been processed
					}
					seenItemIDs.add(itemID);

					String itemName = rs.getString("ItemName");
					String itemDescription = rs.getString("Description");
					float itemCost = rs.getFloat("Cost");
					Category category = Category.valueOf(rs.getString("CategoryName"));

					Item item = null;

					switch (category) {
					case Salad:
						ArrayList<String> saladIngredientsList = new ArrayList<>();
						String[] saladIngredientsArray = rs.getString("saladIngredients").split(", ");
						for (String ingredient : saladIngredientsArray) {
							saladIngredientsList.add(ingredient);
						}
						String saladSize = rs.getString("saladSize");
						item = new Salad(itemID, itemName, category, itemDescription, itemCost, saladIngredientsList,
								saladSize);
						break;
					case MainMeal:
						String mainMealCookMethod = rs.getString("mainMealCookMethod");
						Doneness mainMealDoneness = Doneness.valueOf(rs.getString("mainMealDoneness"));
						item = new MainMeal(itemID, itemName, category, itemDescription, itemCost, mainMealCookMethod,
								mainMealDoneness);
						break;
					case Sweets:
						ArrayList<String> sweetsIngredientsList = new ArrayList<>();
						String[] sweetsIngredientsArray = rs.getString("sweetsIngredients").split(", ");
						for (String ingredient : sweetsIngredientsArray) {
							sweetsIngredientsList.add(ingredient);
						}
						item = new Sweets(itemID, itemName, category, itemDescription, itemCost, sweetsIngredientsList);
						break;
					case Drinks:
						Size drinkSize = Size.valueOf(rs.getString("drinkSize"));
						item = new Drinks(itemID, itemName, category, itemDescription, itemCost, drinkSize);
						break;
					}

					if (item != null) {
						menuItems.add(item);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		MenuItem menuItem = new MenuItem(menuID, menuItems);
		ArrayList<Object> resultList = new ArrayList<>();
		resultList.add(menuItem);
		return new Message(MessageType.GetResturantMenuItems, resultList);
	}

	/**
	 * Adds a new order to the database.
	 *
	 * @param inputList A list containing the order details.
	 * @return A message indicating the success or failure of the operation.
	 */
	public Object AddOrderToDb(ArrayList<Object> inputList) {
		ArrayList<Object> resultList = new ArrayList<>();
		// Assuming inputList contains an OrderItem object
		if (inputList == null || inputList.isEmpty() || !(inputList.get(0) instanceof OrderItem)) {
			resultList.add(false);
			return new Message(MessageType.AddOrderToDB, resultList);
		}

		OrderItem orderItem = (OrderItem) inputList.get(0);
		Order order = orderItem.getOrder();
		ArrayList<OrderItemDetail> orderItemDetails = (ArrayList<OrderItemDetail>) orderItem.getOrderItemDetails();

		String orderQuery = "INSERT INTO `order` (UserID, DateOfOrder, TimeOfOrder, RestaurantID, PickUpType, Location, Status, RequestedTimeOfDelivery, RequestedDateOfDelivery, ETA, Duration, Cost) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement orderStmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
			orderStmt.setInt(1, order.getUserID());
			orderStmt.setDate(2, java.sql.Date.valueOf(order.getDateOfOrder()));
			orderStmt.setString(3, order.getTimeOfOrder());
			orderStmt.setInt(4, order.getResturantID());
			orderStmt.setString(5, order.getPickUpType());
			orderStmt.setString(6, order.getLocation());
			orderStmt.setString(7, order.getStatus());
			orderStmt.setString(8, order.getRequestedTimeOfDelivery());
			orderStmt.setDate(9, java.sql.Date.valueOf(order.getRequestedDateOfDelivery()));
			if (order.getETA() != null && !order.getETA().isEmpty()) {
				orderStmt.setString(10, order.getETA());
			} else {
				orderStmt.setNull(10, java.sql.Types.VARCHAR);
			}

			if (order.getDuration() != null && !order.getDuration().isEmpty()) {
				orderStmt.setString(11, order.getDuration());
			} else {
				orderStmt.setNull(11, java.sql.Types.VARCHAR);
			}

			orderStmt.setFloat(12, order.getCost());

			orderStmt.executeUpdate();

			// Retrieve the generated OrderID
			try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					order.setOrderID(generatedKeys.getInt(1));
				} else {
					resultList.add(false);
					return new Message(MessageType.AddOrderToDB, resultList);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			resultList.add(false);
			return new Message(MessageType.AddOrderToDB, resultList);
		}

		// Insert order items into orderitem table
		String orderItemQuery = "INSERT INTO orderitem (OrderID, ItemID, Quantity, Notes) VALUES (?, ?, ?, ?)";

		try (PreparedStatement orderItemStmt = conn.prepareStatement(orderItemQuery)) {
			for (OrderItemDetail oid : orderItemDetails) {
				orderItemStmt.setInt(1, order.getOrderID());
				orderItemStmt.setInt(2, oid.getItem().getItemID());
				orderItemStmt.setInt(3, oid.getQuantity());
				orderItemStmt.setString(4, oid.getNotes());

				orderItemStmt.addBatch();
			}
			orderItemStmt.executeBatch();

		} catch (SQLException e) {
			e.printStackTrace();
			resultList.add(false);
			return new Message(MessageType.AddOrderToDB, resultList);
		}

		// Check and update the user's coupons if they have more than 1
		String checkCouponQuery = "SELECT amountOfCopuns FROM `user` WHERE UserID = ?";
		String updateCouponQuery = "UPDATE `user` SET amountOfCopuns = amountOfCopuns - 1 WHERE UserID = ?";

		try (PreparedStatement checkCouponStmt = conn.prepareStatement(checkCouponQuery)) {
			checkCouponStmt.setInt(1, order.getUserID());
			try (ResultSet rs = checkCouponStmt.executeQuery()) {
				if (rs.next()) {
					int amountOfCoupons = rs.getInt("amountOfCopuns");
					if (amountOfCoupons > 0) {
						try (PreparedStatement updateCouponStmt = conn.prepareStatement(updateCouponQuery)) {
							updateCouponStmt.setInt(1, order.getUserID());
							updateCouponStmt.executeUpdate();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			resultList.add(false);
			return new Message(MessageType.AddOrderToDB, resultList);
		}

		resultList.add(true);
		resultList.add(order.getOrderID());
		return new Message(MessageType.AddOrderToDB, resultList);
	}

	/**
	 * Retrieves all items associated with a specific supplier.
	 *
	 * @param msgObjectList A list containing the restaurant ID.
	 * @return A message containing the menu items.
	 */
	public Object getAllItemsSupplier(ArrayList<Object> msgObjectList) {
		String query = "SELECT i.ItemID, i.ItemName, i.Description, i.Cost, i.CategoryName, "
				+ "s.Ingredients AS saladIngredients, s.Size AS saladSize, "
				+ "m.CookMethod AS mainMealCookMethod, m.Doneness AS mainMealDoneness, "
				+ "sw.Ingredients AS sweetsIngredients, d.Size AS drinkSize " + "FROM item i "
				+ "JOIN itemmenu im ON i.ItemID = im.ItemID " + "LEFT JOIN salad s ON i.ItemID = s.ItemID "
				+ "LEFT JOIN mainmeal m ON i.ItemID = m.ItemID " + "LEFT JOIN sweets sw ON i.ItemID = sw.ItemID "
				+ "LEFT JOIN drinks d ON i.ItemID = d.ItemID "
				+ "WHERE im.MenuID = (SELECT MenuID FROM restaurant WHERE RestaurantID = ?)";

		int restaurantID = (int) msgObjectList.get(0);
		ArrayList<Item> menuItems = new ArrayList<>();
		Set<Integer> seenItemIDs = new HashSet<>();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, restaurantID);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int itemID = rs.getInt("ItemID");
					if (seenItemIDs.contains(itemID)) {
						continue; // Skip this item as it has already been processed
					}
					seenItemIDs.add(itemID);

					String itemName = rs.getString("ItemName");
					String itemDescription = rs.getString("Description");
					float itemCost = rs.getFloat("Cost");
					Category category = Category.valueOf(rs.getString("CategoryName"));

					Item item = null;

					switch (category) {
					case Salad:
						ArrayList<String> saladIngredientsList = new ArrayList<>();
						String[] saladIngredientsArray = rs.getString("saladIngredients").split(", ");
						for (String ingredient : saladIngredientsArray) {
							saladIngredientsList.add(ingredient);
						}
						String saladSize = rs.getString("saladSize");
						item = new Salad(itemID, itemName, category, itemDescription, itemCost, saladIngredientsList,
								saladSize);
						break;
					case MainMeal:
						String mainMealCookMethod = rs.getString("mainMealCookMethod");
						Doneness mainMealDoneness = Doneness.valueOf(rs.getString("mainMealDoneness"));
						item = new MainMeal(itemID, itemName, category, itemDescription, itemCost, mainMealCookMethod,
								mainMealDoneness);
						break;
					case Sweets:
						ArrayList<String> sweetsIngredientsList = new ArrayList<>();
						String[] sweetsIngredientsArray = rs.getString("sweetsIngredients").split(", ");
						for (String ingredient : sweetsIngredientsArray) {
							sweetsIngredientsList.add(ingredient);
						}
						item = new Sweets(itemID, itemName, category, itemDescription, itemCost, sweetsIngredientsList);
						break;
					case Drinks:
						Size drinkSize = Size.valueOf(rs.getString("drinkSize"));
						item = new Drinks(itemID, itemName, category, itemDescription, itemCost, drinkSize);
						break;
					}

					if (item != null) {
						menuItems.add(item);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		MenuItem menuItem = new MenuItem(restaurantID, menuItems);
		ArrayList<Object> resultList = new ArrayList<>();
		resultList.add(menuItem);
		return new Message(MessageType.GetAllItems, resultList);
	}

	/**
	 * Deletes an item from the database.
	 *
	 * @param msgObjectList A list containing the item ID and menu ID.
	 * @return A message indicating the success or failure of the deletion.
	 */
	public Object DeleteItemFromDb(ArrayList<Object> msgObjectList) {
		ArrayList<Object> resultList = new ArrayList<>();
		int itemID = (int) msgObjectList.get(0);
		int menuID = (int) msgObjectList.get(1);
		String deleteQuery = "DELETE FROM Itemmenu WHERE ItemID = ? AND MenuID = ?";

		try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
			ps.setInt(1, itemID);
			ps.setInt(2, menuID);

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				resultList.add("Item successfully deleted.");
				return new Message(MessageType.DeleteItemSupplier, resultList);
			} else {
				resultList.add("Failed to delete item. Item not found in Itemmenu.");
				return new Message(MessageType.DeleteItemSupplier, resultList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			resultList.add("SQL Exception Error!.");
			return new Message(MessageType.DeleteItemSupplier, resultList);
		}
	}

	/**
	 * Adds a new item to the database.
	 *
	 * @param msgObjectList A list containing the item details and menu ID.
	 * @return A message indicating the success or failure of the addition.
	 */
	public Object addItemToDb(ArrayList<Object> msgObjectList) {
		ArrayList<Object> returnList = new ArrayList<>();
		Item item = (Item) msgObjectList.get(0);
		int menuID = (int) msgObjectList.get(1);

		String selectItemQuery = "SELECT ItemID FROM Item WHERE ItemName = ? AND CategoryName = ? AND Description = ? AND Cost = ?";
		String insertItemQuery = "INSERT INTO Item (ItemName, CategoryName, Description, Cost) VALUES (?, ?, ?, ?)";
		String insertItemMenuQuery = "INSERT INTO Itemmenu (ItemID, MenuID) VALUES (?, ?)";

		try (PreparedStatement pstmtSelectItem = conn.prepareStatement(selectItemQuery);
				PreparedStatement pstmtInsertItem = conn.prepareStatement(insertItemQuery,
						Statement.RETURN_GENERATED_KEYS);
				PreparedStatement pstmtInsertItemMenu = conn.prepareStatement(insertItemMenuQuery);
				Statement stmt = conn.createStatement()) {

			// Check if item already exists
			pstmtSelectItem.setString(1, item.getItemName());
			pstmtSelectItem.setString(2, item.getCategoryName().name());
			pstmtSelectItem.setString(3, item.getDescription());
			pstmtSelectItem.setDouble(4, item.getCost());
			ResultSet rs = pstmtSelectItem.executeQuery();

			int itemID;
			if (rs.next()) {
				// Item exists, get the ItemID
				itemID = rs.getInt("ItemID");
			} else {
				// Item does not exist, insert it
				pstmtInsertItem.setString(1, item.getItemName());
				pstmtInsertItem.setString(2, item.getCategoryName().name());
				pstmtInsertItem.setString(3, item.getDescription());
				pstmtInsertItem.setDouble(4, item.getCost());
				int rowsAffectedItem = pstmtInsertItem.executeUpdate();

				if (rowsAffectedItem > 0) {
					ResultSet generatedKeys = pstmtInsertItem.getGeneratedKeys();
					if (generatedKeys.next()) {
						itemID = generatedKeys.getInt(1);
					} else {
						throw new SQLException("Failed to retrieve ItemID.");
					}
				} else {
					returnList.add("Failed to add item.");
					return new Message(MessageType.AddItemSupplier, returnList);
				}

				// Insert into category-specific tables
				switch (item.getCategoryName()) {
				case Salad:
					String insertSaladQuery = "INSERT INTO Salad (ItemID, Ingredients, Size) VALUES (?, ?, ?)";
					try (PreparedStatement pstmtInsertSalad = conn.prepareStatement(insertSaladQuery)) {
						for (String size : new String[] { "Small", "Medium", "Large" }) {
							pstmtInsertSalad.setInt(1, itemID);
							pstmtInsertSalad.setString(2, String.join(", ", ((Salad) item).getIngredients()));
							pstmtInsertSalad.setString(3, size);
							pstmtInsertSalad.executeUpdate();
						}
					}
					break;
				case Drinks:
					String insertDrinksQuery = "INSERT INTO Drinks (ItemID, Size) VALUES (?, ?)";
					try (PreparedStatement pstmtInsertDrinks = conn.prepareStatement(insertDrinksQuery)) {
						for (String size : new String[] { "Small", "Medium", "Large" }) {
							pstmtInsertDrinks.setInt(1, itemID);
							pstmtInsertDrinks.setString(2, size);
							pstmtInsertDrinks.executeUpdate();
						}
					}
					break;
				case Sweets:
					String insertSweetsQuery = "INSERT INTO Sweets (ItemID, Ingredients) VALUES (?, ?)";
					try (PreparedStatement pstmtInsertSweets = conn.prepareStatement(insertSweetsQuery)) {
						pstmtInsertSweets.setInt(1, itemID);
						pstmtInsertSweets.setString(2, String.join(", ", ((Sweets) item).getSweetsDescription()));
						pstmtInsertSweets.executeUpdate();
					}
					break;
				case MainMeal:
					String insertMainMealQuery = "INSERT INTO MainMeal (ItemID, CookMethod, Doneness) VALUES (?, ?, ?)";
					try (PreparedStatement pstmtInsertMainMeal = conn.prepareStatement(insertMainMealQuery)) {
						for (String doneness : new String[] { "Rare", "MediumRare", "Medium", "MediumWell",
								"WellDone" }) {
							pstmtInsertMainMeal.setInt(1, itemID);
							pstmtInsertMainMeal.setString(2, ((MainMeal) item).getCookMethod());
							pstmtInsertMainMeal.setString(3, doneness);
							pstmtInsertMainMeal.executeUpdate();
						}
					}
					break;
				default:
					throw new IllegalArgumentException("Unknown category: " + item.getCategoryName());
				}
			}

			// Insert into ItemMenu table
			pstmtInsertItemMenu.setInt(1, itemID);
			pstmtInsertItemMenu.setInt(2, menuID);
			pstmtInsertItemMenu.executeUpdate();
			returnList.add("Item added successfully.");
			return new Message(MessageType.AddItemSupplier, returnList);
		} catch (SQLException e) {
			e.printStackTrace();
			returnList.add("Error occurred during item insertion: " + e.getMessage());
			return new Message(MessageType.AddItemSupplier, returnList);
		}
	}

	/**
	 * Retrieves the order history for a specific supplier.
	 *
	 * @param msgObjectList A list containing the restaurant ID.
	 * @return A list of orders associated with the supplier.
	 */
	public Object getOrderHistorySupplier(ArrayList<Object> msgObjectList) {
		int resturantID = (int) msgObjectList.get(0);
		ArrayList<Object> resultList = new ArrayList<>();

		String query = "SELECT * FROM `Order` WHERE RestaurantID = ?";
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, resturantID);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					// Fetch dates correctly from the ResultSet
					Order order = new Order(rs.getInt("OrderID"), rs.getInt("UserID"),
							rs.getDate("DateOfOrder").toLocalDate().plusDays(1), rs.getString("TimeOfOrder"),
							rs.getInt("RestaurantID"), rs.getString("PickUpType"), rs.getString("Location"),
							rs.getString("Status"), rs.getString("RequestedTimeOfDelivery"),
							rs.getDate("RequestedDateOfDelivery").toLocalDate().plusDays(1), rs.getString("ETA"),
							rs.getString("Duration"), rs.getFloat("Cost"));
					resultList.add(order);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * Updates the status and estimated time of arrival (ETA) of an order.
	 *
	 * @param inputList     A list containing the order details.
	 * @param msgObjectList A list containing the order status and ETA.
	 */
	public void updateOrderStatusAndETA(ArrayList<Object> inputList, ArrayList<Object> msgObjectList) {
		// Retrieve the order ID and other details from the input list
		int orderID = ((Order) msgObjectList.get(0)).getOrderID();
		String newStatus = (String) msgObjectList.get(1);
		String ETA = (String) msgObjectList.get(2);

		// SQL query to update the status of the order
		String updateQuery = "UPDATE `Order` SET Status = ?, ETA = ? WHERE OrderID = ?";

		try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
			ps.setString(1, newStatus); // Set the new status
			ps.setString(2, ETA);
			ps.setInt(3, orderID);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the items associated with a specific order.
	 *
	 * @param msgObjectList A list containing the order ID.
	 * @return A message containing the order items.
	 */
	public Object getOrderItems(ArrayList<Object> msgObjectList) {
		int orderId = (int) msgObjectList.get(0);
		ArrayList<OrderItemDetail> orderItemDetails = new ArrayList<>();

		String queryOrderItems = "SELECT oi.OrderID, oi.ItemID, oi.Quantity, oi.Notes, i.ItemName, i.Description, i.Cost, i.CategoryName "
				+ "FROM orderitem oi " + "JOIN item i ON oi.ItemID = i.ItemID " + "WHERE oi.OrderID = ?";

		try (PreparedStatement psOrderItems = conn.prepareStatement(queryOrderItems)) {
			psOrderItems.setInt(1, orderId);

			try (ResultSet rsOrderItems = psOrderItems.executeQuery()) {
				while (rsOrderItems.next()) {
					int itemId = rsOrderItems.getInt("ItemID");
					int quantity = rsOrderItems.getInt("Quantity");
					String notes = rsOrderItems.getString("Notes");
					String itemName = rsOrderItems.getString("ItemName");
					String description = rsOrderItems.getString("Description");
					float cost = rsOrderItems.getFloat("Cost");
					Category category = Category.valueOf(rsOrderItems.getString("CategoryName"));

					Item item = new ConcreteItem(itemId, itemName, category, description, cost);
					OrderItemDetail orderItemDetail = new OrderItemDetail(item, notes, quantity, cost);
					orderItemDetails.add(orderItemDetail);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ArrayList<Object> resultList = new ArrayList<>();
		resultList.add(orderItemDetails);
		return new Message(MessageType.GetOrderItems, resultList);
	}

	// Report Generator

	/**
	 * /** Generates monthly reports for a specified year and month.
	 *
	 * @param year  The year for which the reports are generated.
	 * @param month The month for which the reports are generated.
	 * @throws SQLException If a database access error occurs.
	 */
	public void generateMonthlyReports(int year, int month) throws SQLException {
		String quarter = getQuarter(month);
		int[] monthsInQuarter = getMonthsInQuarter(month);

		for (int restaurantID : getRestaurantIDs()) {
			int dailyOrders = 0;
			float quarterTotalRevenue = 0;
			Map<LocalDate, Integer> dailyOrderMap = new HashMap<>();

			// Loop through each month in the quarter to aggregate data for the quarter
			// report
			for (int m : monthsInQuarter) {
				dailyOrders += calculateDailyOrders(restaurantID, year, m);
				quarterTotalRevenue += calculateTotalRevenue(restaurantID, year, m);
				Map<LocalDate, Integer> dailyOrdersForMonth = calculateDailyOrdersForMonth(restaurantID, year, m);
				dailyOrderMap.putAll(dailyOrdersForMonth);
			}

			// Check if the report for the quarter already exists
			int reportID = getQuarterReportID(restaurantID, year, quarter);
			if (reportID == -1) { // If no report exists, create a new one
				reportID = saveReport(restaurantID, year, month, "QuarterReport");
				saveQuarterReport(reportID, quarter, dailyOrders, quarterTotalRevenue);
			} else { // Update the existing report
				updateQuarterReport(reportID, dailyOrders, quarterTotalRevenue);
			}

			// Save or update the daily orders to quarterreportdailyorders
			saveQuarterReportDailyOrders(reportID, dailyOrderMap);

			// Calculate total revenue for the specific month
			float monthTotalRevenue = calculateTotalRevenue(restaurantID, year, month);

			// Generate other reports for the month
			int totalOrders = calculateTotalOrders(restaurantID, year, month);
			int mainMealOrders = calculateMainMealOrders(restaurantID, year, month);
			int saladOrders = calculateSaladOrders(restaurantID, year, month);
			int sweetsOrders = calculateSweetsOrders(restaurantID, year, month);
			int drinksOrders = calculateDrinksOrders(restaurantID, year, month);
			int onTimeOrders = calculateOnTimeOrders(restaurantID, year, month);
			int lateOrders = calculateLateOrders(restaurantID, year, month);
			String averageDelayTime = calculateAverageDelayTime(restaurantID, year, month);

			float saladRevenue = calculateCategoryRevenue(restaurantID, year, month, "Salad");
			float sweetsRevenue = calculateCategoryRevenue(restaurantID, year, month, "Sweets");
			float drinksRevenue = calculateCategoryRevenue(restaurantID, year, month, "Drinks");
			float mainMealRevenue = calculateCategoryRevenue(restaurantID, year, month, "MainMeal");

			// Save Order Report
			int orderReportID = saveReport(restaurantID, year, month, "OrderReport");
			saveOrderReport(orderReportID, totalOrders, mainMealOrders, saladOrders, sweetsOrders, drinksOrders);

			// Save Performance Report
			int performanceReportID = saveReport(restaurantID, year, month, "PerformanceReport");
			savePerformanceReport(performanceReportID, onTimeOrders, lateOrders, averageDelayTime);

			// Save Income Report (specific to the given month)
			int incomeReportID = saveReport(restaurantID, year, month, "IncomeReport");
			saveIncomeReport(incomeReportID, monthTotalRevenue, saladRevenue, sweetsRevenue, drinksRevenue,
					mainMealRevenue);
		}
	}

	/**
	 * Checks if a quarterly report already exists for a specific restaurant, year,
	 * and quarter.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year of the report.
	 * @param quarter      The quarter (e.g., "Q1", "Q2", etc.).
	 * @return The report ID if a report exists, or -1 if no report exists.
	 * @throws SQLException If a database access error occurs.
	 */
	private int getQuarterReportID(int restaurantID, int year, String quarter) throws SQLException {
		String query = "SELECT r.ReportID FROM report r " + "JOIN quarterreport qr ON r.ReportID = qr.ReportID "
				+ "WHERE r.RestaurantID = ? AND r.Year = ? AND qr.Quarter = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setString(3, quarter);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("ReportID");
				} else {
					return -1;
				}
			}
		}
	}

	/**
	 * Updates the existing quarterly report with new daily orders and total
	 * revenue.
	 *
	 * @param reportID     The ID of the existing report.
	 * @param dailyOrders  The new number of daily orders to add to the report.
	 * @param totalRevenue The new total revenue to add to the report.
	 * @throws SQLException If a database access error occurs.
	 */
	private void updateQuarterReport(int reportID, int dailyOrders, float totalRevenue) throws SQLException {
		String query = "UPDATE quarterreport SET DailyOrders = DailyOrders + ?, TotalRevenue = TotalRevenue + ? WHERE ReportID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, dailyOrders);
			pstmt.setFloat(2, totalRevenue);
			pstmt.setInt(3, reportID);
			pstmt.executeUpdate();
		}
	}

	/**
	 * Determines the months that belong to the quarter of the given month.
	 *
	 * @param month The month for which the quarter is determined.
	 * @return An array of integers representing the months in the quarter.
	 */
	private int[] getMonthsInQuarter(int month) {
		if (month <= 3)
			return new int[] { 1, 2, 3 };
		else if (month <= 6)
			return new int[] { 4, 5, 6 };
		else if (month <= 9)
			return new int[] { 7, 8, 9 };
		else
			return new int[] { 10, 11, 12 };
	}

	/**
	 * Calculates the daily orders for a specific restaurant in a given year and
	 * month.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the orders are calculated.
	 * @param month        The month for which the orders are calculated.
	 * @return A map of LocalDate to the number of daily orders.
	 * @throws SQLException If a database access error occurs.
	 */
	private Map<LocalDate, Integer> calculateDailyOrdersForMonth(int restaurantID, int year, int month)
			throws SQLException {
		String query = "SELECT DateOfOrder, COUNT(*) AS dailyOrders FROM `order` "
				+ "WHERE RestaurantID = ? AND YEAR(DateOfOrder) = ? AND MONTH(DateOfOrder) = ? "
				+ "GROUP BY DateOfOrder";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			try (ResultSet rs = pstmt.executeQuery()) {
				Map<LocalDate, Integer> dailyOrdersMap = new HashMap<>();
				while (rs.next()) {
					LocalDate date = rs.getDate("DateOfOrder").toLocalDate().plusDays(1);
					int dailyOrders = rs.getInt("dailyOrders");
					dailyOrdersMap.put(date, dailyOrders);
				}
				return dailyOrdersMap;
			}
		}
	}

	/**
	 * Saves the daily orders for a specific report.
	 *
	 * @param reportID      The ID of the report.
	 * @param dailyOrderMap A map of LocalDate to the number of daily orders.
	 * @throws SQLException If a database access error occurs.
	 */
	private void saveQuarterReportDailyOrders(int reportID, Map<LocalDate, Integer> dailyOrderMap) throws SQLException {
		String query = "INSERT INTO quarterreportdailyorders (ReportID, Date, NumberOfOrders) VALUES (?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE NumberOfOrders = NumberOfOrders + VALUES(NumberOfOrders)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			for (Map.Entry<LocalDate, Integer> entry : dailyOrderMap.entrySet()) {
				pstmt.setInt(1, reportID);
				pstmt.setDate(2, Date.valueOf(entry.getKey()));
				pstmt.setInt(3, entry.getValue());
				pstmt.addBatch(); // Batch insertion for performance
			}
			pstmt.executeBatch(); // Execute batch insertion
		}
	}

	/**
	 * Determines the quarter based on the provided month.
	 *
	 * @param month The month for which the quarter is determined.
	 * @return The quarter as a string (e.g., "Q1", "Q2", etc.).
	 */
	private String getQuarter(int month) {
		if (month <= 3)
			return "Q1";
		else if (month <= 6)
			return "Q2";
		else if (month <= 9)
			return "Q3";
		else
			return "Q4";
	}

	/**
	 * Retrieves the IDs of all restaurants.
	 *
	 * @return An array of restaurant IDs.
	 * @throws SQLException If a database access error occurs.
	 */
	private int[] getRestaurantIDs() throws SQLException {
		String query = "SELECT DISTINCT RestaurantID FROM `order`";
		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			List<Integer> restaurantIDs = new ArrayList<>();
			while (rs.next()) {
				restaurantIDs.add(rs.getInt("RestaurantID"));
			}
			return restaurantIDs.stream().mapToInt(i -> i).toArray();
		}
	}

	/**
	 * Calculates the average number of daily orders for a specific restaurant in a
	 * given year and month.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the orders are calculated.
	 * @param month        The month for which the orders are calculated.
	 * @return The average number of daily orders.
	 * @throws SQLException If a database access error occurs.
	 */
	private int calculateDailyOrders(int restaurantID, int year, int month) throws SQLException {
		String query = "SELECT COUNT(*) / COUNT(DISTINCT DateOfOrder) AS dailyOrders " + "FROM `order` "
				+ "WHERE RestaurantID = ? AND YEAR(DateOfOrder) = ? AND MONTH(DateOfOrder) = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Calculates the total revenue for a specific restaurant in a given year and
	 * month.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the revenue is calculated.
	 * @param month        The month for which the revenue is calculated.
	 * @return The total revenue.
	 * @throws SQLException If a database access error occurs.
	 */
	private float calculateTotalRevenue(int restaurantID, int year, int month) throws SQLException {
		String query = "SELECT SUM(Cost) AS totalRevenue " + "FROM `order` "
				+ "WHERE RestaurantID = ? AND YEAR(DateOfOrder) = ? AND MONTH(DateOfOrder) = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getFloat("totalRevenue");
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Calculates the total number of orders for a specific restaurant in a given
	 * year and month.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the orders are calculated.
	 * @param month        The month for which the orders are calculated.
	 * @return The total number of orders.
	 * @throws SQLException If a database access error occurs.
	 */
	private int calculateTotalOrders(int restaurantID, int year, int month) throws SQLException {
		String query = "SELECT COUNT(*) AS totalOrders " + "FROM `order` "
				+ "WHERE RestaurantID = ? AND YEAR(DateOfOrder) = ? AND MONTH(DateOfOrder) = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("totalOrders");
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Calculates the number of main meal orders for a specific restaurant in a
	 * given year and month.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the orders are calculated.
	 * @param month        The month for which the orders are calculated.
	 * @return The number of main meal orders.
	 * @throws SQLException If a database access error occurs.
	 */
	private int calculateMainMealOrders(int restaurantID, int year, int month) throws SQLException {
		String query = "SELECT COUNT(*) AS mainMealOrders FROM orderitem oi JOIN item i ON oi.ItemID = i.ItemID WHERE i.CategoryName = 'MainMeal' AND oi.OrderID IN (SELECT OrderID FROM `order` WHERE RestaurantID = ? AND YEAR(DateOfOrder) = ? AND MONTH(DateOfOrder) = ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("mainMealOrders");
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Calculates the number of salad orders for a specific restaurant in a given
	 * year and month.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the orders are calculated.
	 * @param month        The month for which the orders are calculated.
	 * @return The number of salad orders.
	 * @throws SQLException If a database access error occurs.
	 */
	private int calculateSaladOrders(int restaurantID, int year, int month) throws SQLException {
		String query = "SELECT COUNT(*) AS saladOrders FROM orderitem oi JOIN item i ON oi.ItemID = i.ItemID WHERE i.CategoryName = 'Salad' AND oi.OrderID IN (SELECT OrderID FROM `order` WHERE RestaurantID = ? AND YEAR(DateOfOrder) = ? AND MONTH(DateOfOrder) = ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("saladOrders");
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Calculates the number of sweets orders for a specific restaurant in a given
	 * year and month.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the orders are calculated.
	 * @param month        The month for which the orders are calculated.
	 * @return The number of sweets orders.
	 * @throws SQLException If a database access error occurs.
	 */
	private int calculateSweetsOrders(int restaurantID, int year, int month) throws SQLException {
		String query = "SELECT COUNT(*) AS sweetsOrders FROM orderitem oi JOIN item i ON oi.ItemID = i.ItemID WHERE i.CategoryName = 'Sweets' AND oi.OrderID IN (SELECT OrderID FROM `order` WHERE RestaurantID = ? AND YEAR(DateOfOrder) = ? AND MONTH(DateOfOrder) = ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("sweetsOrders");
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Calculates the number of drinks orders for a specific restaurant in a given
	 * year and month.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the orders are calculated.
	 * @param month        The month for which the orders are calculated.
	 * @return The number of drinks orders.
	 * @throws SQLException If a database access error occurs.
	 */
	private int calculateDrinksOrders(int restaurantID, int year, int month) throws SQLException {
		String query = "SELECT COUNT(*) AS drinksOrders FROM orderitem oi JOIN item i ON oi.ItemID = i.ItemID WHERE i.CategoryName = 'Drinks' AND oi.OrderID IN (SELECT OrderID FROM `order` WHERE RestaurantID = ? AND YEAR(DateOfOrder) = ? AND MONTH(DateOfOrder) = ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("drinksOrders");
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Calculates the number of on-time orders for a specific restaurant in a given
	 * year and month. An order is considered on-time if: - It was made with less
	 * than 2 hours of head time and was delivered within 1 hour. - It was made with
	 * at least 2 hours of head time and was delivered within 20 minutes.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the orders are calculated.
	 * @param month        The month for which the orders are calculated.
	 * @return The number of on-time orders.
	 * @throws SQLException If a database access error occurs.
	 */
	private int calculateOnTimeOrders(int restaurantID, int year, int month) throws SQLException {
		String query = "SELECT COUNT(*) AS onTimeOrders FROM `order` "
				+ "WHERE RestaurantID = ? AND YEAR(DateOfOrder) = ? AND MONTH(DateOfOrder) = ? "
				+ "AND Status = 'Received' " + "AND ("
				+ "(TIMESTAMPDIFF(MINUTE, CONCAT(DateOfOrder, ' ', CAST(TimeOfOrder AS TIME)), CONCAT(RequestedDateOfDelivery, ' ', CAST(RequestedTimeOfDelivery AS TIME))) < 120 "
				+ "AND CAST(Duration AS TIME) <= '00:01:00') " + "OR "
				+ "(TIMESTAMPDIFF(MINUTE, CONCAT(DateOfOrder, ' ', CAST(TimeOfOrder AS TIME)), CONCAT(RequestedDateOfDelivery, ' ', CAST(RequestedTimeOfDelivery AS TIME))) >= 120 "
				+ "AND CAST(Duration AS TIME) <= '00:00:20'))";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("onTimeOrders");
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Calculates the number of late orders for a specific restaurant in a given
	 * year and month. An order is considered late if: - It was made with less than
	 * 2 hours of head time and was delivered after 1 hour. - It was made with at
	 * least 2 hours of head time and was delivered after 20 minutes.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the orders are calculated.
	 * @param month        The month for which the orders are calculated.
	 * @return The number of late orders.
	 * @throws SQLException If a database access error occurs.
	 */
	private int calculateLateOrders(int restaurantID, int year, int month) throws SQLException {
		String query = "SELECT COUNT(*) AS lateOrders FROM `order` "
				+ "WHERE RestaurantID = ? AND YEAR(DateOfOrder) = ? AND MONTH(DateOfOrder) = ? "
				+ "AND Status = 'Received' " + "AND ("
				+ "(TIMESTAMPDIFF(MINUTE, CONCAT(DateOfOrder, ' ', CAST(TimeOfOrder AS TIME)), CONCAT(RequestedDateOfDelivery, ' ', CAST(RequestedTimeOfDelivery AS TIME))) < 120 "
				+ "AND CAST(Duration AS TIME) > '00:01:00') " + "OR "
				+ "(TIMESTAMPDIFF(MINUTE, CONCAT(DateOfOrder, ' ', CAST(TimeOfOrder AS TIME)), CONCAT(RequestedDateOfDelivery, ' ', CAST(RequestedTimeOfDelivery AS TIME))) >= 120 "
				+ "AND CAST(Duration AS TIME) > '00:00:20'))";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("lateOrders");
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Calculates the average delay time for orders in a specific restaurant in a
	 * given year and month. This includes early deliveries, on-time deliveries, and
	 * late deliveries.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the delay time is calculated.
	 * @param month        The month for which the delay time is calculated.
	 * @return The average delay time as a string in the format "HH:mm:ss".
	 * @throws SQLException If a database access error occurs.
	 */
	private String calculateAverageDelayTime(int restaurantID, int year, int month) throws SQLException {
		String query = "SELECT AVG(TIME_TO_SEC(CAST(Duration AS TIME))) AS avgDelayTime FROM `order` WHERE RestaurantID = ? AND YEAR(DateOfOrder) = ? AND MONTH(DateOfOrder) = ? AND Duration IS NOT NULL";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int avgDelayTimeSec = rs.getInt("avgDelayTime");
					int hours = avgDelayTimeSec / 3600;
					int minutes = (avgDelayTimeSec % 3600) / 60;
					int seconds = avgDelayTimeSec % 60;
					return String.format("%02d:%02d:%02d", hours, minutes, seconds);
				} else {
					return "00:00:00";
				}
			}
		}
	}

	/**
	 * Calculates the revenue for a specific category of items in a specific
	 * restaurant in a given year and month.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the revenue is calculated.
	 * @param month        The month for which the revenue is calculated.
	 * @param category     The category of items (e.g., Salad, Sweets, Drinks,
	 *                     MainMeal).
	 * @return The revenue for the specified category.
	 * @throws SQLException If a database access error occurs.
	 */
	private float calculateCategoryRevenue(int restaurantID, int year, int month, String category) throws SQLException {
		String query = "SELECT SUM(oi.Quantity * i.Cost) AS categoryRevenue " + "FROM orderitem oi "
				+ "JOIN item i ON oi.ItemID = i.ItemID " + "JOIN `order` o ON oi.OrderID = o.OrderID "
				+ "WHERE o.RestaurantID = ? AND YEAR(o.DateOfOrder) = ? AND MONTH(o.DateOfOrder) = ? AND i.CategoryName = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			pstmt.setString(4, category);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getFloat("categoryRevenue");
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * Saves a report to the database.
	 *
	 * @param restaurantID The ID of the restaurant.
	 * @param year         The year for which the report is generated.
	 * @param month        The month for which the report is generated.
	 * @param reportType   The type of report (e.g., "QuarterReport", "OrderReport",
	 *                     etc.).
	 * @return The generated ReportID.
	 * @throws SQLException If a database access error occurs.
	 */
	private int saveReport(int restaurantID, int year, int month, String reportType) throws SQLException {
		String query = "INSERT INTO report (RestaurantID, Year, Month, ReportType) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, restaurantID);
			pstmt.setInt(2, year);
			pstmt.setInt(3, month);
			pstmt.setString(4, reportType);
			pstmt.executeUpdate();

			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1); // Return the generated ReportID
				} else {
					throw new SQLException("Failed to insert report and retrieve ReportID.");
				}
			}
		}
	}

	/**
	 * Saves an order report to the database.
	 *
	 * @param reportID       The ID of the report.
	 * @param totalOrders    The total number of orders.
	 * @param mainMealOrders The number of main meal orders.
	 * @param saladOrders    The number of salad orders.
	 * @param sweetsOrders   The number of sweets orders.
	 * @param drinksOrders   The number of drinks orders.
	 * @throws SQLException If a database access error occurs.
	 */
	private void saveOrderReport(int reportID, int totalOrders, int mainMealOrders, int saladOrders, int sweetsOrders,
			int drinksOrders) throws SQLException {
		String query = "INSERT INTO orderreport (ReportID, TotalOrders, MainMealOrders, SaladOrders, SweetsOrders, DrinksOrders) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, reportID);
			pstmt.setInt(2, totalOrders);
			pstmt.setInt(3, mainMealOrders);
			pstmt.setInt(4, saladOrders);
			pstmt.setInt(5, sweetsOrders);
			pstmt.setInt(6, drinksOrders);
			pstmt.executeUpdate();
		}
	}

	/**
	 * Saves a quarter report to the database.
	 *
	 * @param reportID     The ID of the report.
	 * @param quarter      The quarter (e.g., "Q1", "Q2", etc.).
	 * @param dailyOrders  The number of daily orders.
	 * @param totalRevenue The total revenue.
	 * @throws SQLException If a database access error occurs.
	 */
	private void saveQuarterReport(int reportID, String quarter, int dailyOrders, float totalRevenue)
			throws SQLException {
		String query = "INSERT INTO quarterreport (ReportID, Quarter, DailyOrders, TotalRevenue) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, reportID);
			pstmt.setString(2, quarter);
			pstmt.setInt(3, dailyOrders);
			pstmt.setFloat(4, totalRevenue);
			pstmt.executeUpdate();
		}
	}

	/**
	 * Saves a performance report to the database.
	 *
	 * @param reportID         The ID of the report.
	 * @param onTimeOrders     The number of on-time orders.
	 * @param lateOrders       The number of late orders.
	 * @param averageDelayTime The average delay time.
	 * @throws SQLException If a database access error occurs.
	 */
	private void savePerformanceReport(int reportID, int onTimeOrders, int lateOrders, String averageDelayTime)
			throws SQLException {
		String query = "INSERT INTO performancereport (ReportID, OnTimeOrders, LateOrders, AverageDelayTime) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, reportID);
			pstmt.setInt(2, onTimeOrders);
			pstmt.setInt(3, lateOrders);
			pstmt.setString(4, averageDelayTime);
			pstmt.executeUpdate();
		}
	}

	/**
	 * Saves an income report to the database.
	 *
	 * @param reportID        The ID of the report.
	 * @param totalRevenue    The total revenue.
	 * @param saladRevenue    The revenue from salads.
	 * @param sweetsRevenue   The revenue from sweets.
	 * @param drinksRevenue   The revenue from drinks.
	 * @param mainMealRevenue The revenue from main meals.
	 * @throws SQLException If a database access error occurs.
	 */
	private void saveIncomeReport(int reportID, float totalRevenue, float saladRevenue, float sweetsRevenue,
			float drinksRevenue, float mainMealRevenue) throws SQLException {
		String query = "INSERT INTO incomereport (ReportID, TotalRevenue, SaladRevenue, SweetsRevenue, DrinksRevenue, MainMealRevenue) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, reportID);
			pstmt.setFloat(2, totalRevenue);
			pstmt.setFloat(3, saladRevenue);
			pstmt.setFloat(4, sweetsRevenue);
			pstmt.setFloat(5, drinksRevenue);
			pstmt.setFloat(6, mainMealRevenue);
			pstmt.executeUpdate();
		}
	}

	/**
	 * Adds a new user to the database.
	 *
	 * @param msgObjectList A list containing the user details.
	 * @return A message indicating the success or failure of the addition.
	 */
	public Object addUserToDB(ArrayList<Object> msgObjectList) {
		User userToAdd = (User) msgObjectList.get(0);
		String username = userToAdd.getUsername();
		ArrayList<Object> resultList = new ArrayList<>();
		try {
			// Check if the username already exists in the database
			String checkQuery = "SELECT COUNT(*) FROM user WHERE Username = ?";
			PreparedStatement ps = conn.prepareStatement(checkQuery);
			ps.setString(1, username);
			ResultSet rs = null;
			rs = ps.executeQuery();

			if (rs.next() && rs.getInt(1) > 0) {
				// Username exists, return a message
				resultList.add("Username already exists.");
				return new Message(MessageType.AddUserToDB, resultList);
			}

			// If username doesn't exist, add the user to the database
			String insertQuery = "INSERT INTO user (UserID, FirstName, LastName, Username, Password, UserType, BranchID, Phone, Email, CreditCard, isLogged, amountOfCopuns, AccountType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(insertQuery);
			ps.setInt(1, userToAdd.getUserID());
			ps.setString(2, userToAdd.getFirstName());
			ps.setString(3, userToAdd.getLastName());
			ps.setString(4, userToAdd.getUsername());
			ps.setString(5, userToAdd.getPassword());
			ps.setString(6, UserType.getStringType(userToAdd.getUserType()));
			ps.setInt(7, userToAdd.getBranchID());
			ps.setString(8, userToAdd.getPhone());
			ps.setString(9, userToAdd.getEmail());
			ps.setString(10, userToAdd.getCreditCard());
			ps.setInt(11, userToAdd.getIsLogged());
			ps.setInt(12, userToAdd.getAmountOfCopunts());
			ps.setString(13, userToAdd.getAccountType());

			ps.executeUpdate();

			resultList.add("User added successfully.");
			return new Message(MessageType.AddUserToDB, resultList);

		} catch (SQLException e) {
			e.printStackTrace();
			resultList.add("Database error: " + e.getMessage());
			return new Message(MessageType.AddUserToDB, resultList);
		}
	}

	/**
	 * Retrieves the available years and months for branch manager reports based on
	 * RestaurantID's associated with the given BranchID.
	 *
	 * @return A message containing the years and months.
	 */
	public Object getReportsYearMonth(ArrayList<Object> msgObjectList) {
		int branchID = (int) msgObjectList.get(0);
		ArrayList<Object> resultList = new ArrayList<>();
		Map<String, ArrayList<String>> mapMonthYear = new HashMap<>();

		try {
			// Step 1: Retrieve RestaurantID's associated with the given BranchID
			PreparedStatement psRestaurant = conn
					.prepareStatement("SELECT RestaurantID FROM restaurant WHERE BranchID = ?");
			psRestaurant.setInt(1, branchID);
			ResultSet rsRestaurant = psRestaurant.executeQuery();

			List<Integer> restaurantIDs = new ArrayList<>();
			while (rsRestaurant.next()) {
				restaurantIDs.add(rsRestaurant.getInt("RestaurantID"));
			}

			// Step 2: Retrieve Years and Months from the report table based on the
			// RestaurantID's
			if (!restaurantIDs.isEmpty()) {
				StringBuilder queryBuilder = new StringBuilder(
						"SELECT DISTINCT Year, Month FROM report WHERE RestaurantID IN (");
				for (int i = 0; i < restaurantIDs.size(); i++) {
					queryBuilder.append("?");
					if (i < restaurantIDs.size() - 1) {
						queryBuilder.append(", ");
					}
				}
				queryBuilder.append(")");

				PreparedStatement psYearMonth = conn.prepareStatement(queryBuilder.toString());

				for (int i = 0; i < restaurantIDs.size(); i++) {
					psYearMonth.setInt(i + 1, restaurantIDs.get(i));
				}

				ResultSet rsYearMonth = psYearMonth.executeQuery();

				while (rsYearMonth.next()) {
					String year = String.valueOf(rsYearMonth.getInt("Year"));
					String month = rsYearMonth.getString("Month");

					if (!mapMonthYear.containsKey(year)) {
						mapMonthYear.put(year, new ArrayList<>());
					}
					mapMonthYear.get(year).add(month);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		resultList.add(mapMonthYear);
		resultList.add(branchID);
		return new Message(MessageType.GetBranchManagerReportsYearMonth, resultList);
	}

	/**
	 * Retrieves the branch reports for a specific branch, year, and list of months.
	 *
	 * @param msgObjectList A list containing the branch ID, year, and a list of
	 *                      months.
	 * @return A message containing the branch reports.
	 */
	public Object getBranchReports(ArrayList<Object> msgObjectList) {
		int branchID = (int) msgObjectList.get(0);
		String year = (String) msgObjectList.get(1);
		@SuppressWarnings("unchecked")
		ArrayList<String> months = (ArrayList<String>) msgObjectList.get(2);
		IncomeReport ir;
		OrderReport or;
		PerformanceReport pr;
		ArrayList<Object> reportsList = new ArrayList<>();

		try {
			// Step 1: Get all RestaurantID associated with the BranchID
			PreparedStatement psRestaurant = DBControl.getInstance().conn
					.prepareStatement("SELECT RestaurantID FROM restaurant WHERE BranchID = ?");
			psRestaurant.setInt(1, branchID);
			ResultSet rsRestaurant = psRestaurant.executeQuery();

			// Store the RestaurantIDs
			ArrayList<Integer> restaurantIDs = new ArrayList<>();
			while (rsRestaurant.next()) {
				restaurantIDs.add(rsRestaurant.getInt("RestaurantID"));
			}

			// Step 2: For each RestaurantID, get the ReportIDs from the report table for
			// each month
			for (int restaurantID : restaurantIDs) {
				for (String month : months) {
					PreparedStatement psReport = DBControl.getInstance().conn.prepareStatement(
							"SELECT ReportID, ReportType FROM report WHERE RestaurantID = ? AND Year = ? AND Month = ?");
					psReport.setInt(1, restaurantID);
					psReport.setInt(2, Integer.parseInt(year));
					psReport.setString(3, month);
					ResultSet rsReport = psReport.executeQuery();

					// Step 3: For each ReportID, get the data from the relevant report tables
					while (rsReport.next()) {
						int reportID = rsReport.getInt("ReportID");
						String reportType = rsReport.getString("ReportType");

						switch (ReportType.valueOf(reportType)) {
						case IncomeReport:
							// Fetch the data from incomereport table
							PreparedStatement psIncome = DBControl.getInstance().conn.prepareStatement(
									"SELECT TotalRevenue, SaladRevenue, SweetsRevenue, DrinksRevenue, MainMealRevenue FROM incomereport WHERE ReportID = ?");
							psIncome.setInt(1, reportID);
							ResultSet rsIncome = psIncome.executeQuery();
							if (rsIncome.next()) {
								ir = new IncomeReport(reportID, restaurantID, Year.of(Integer.parseInt(year)), month,
										ReportType.IncomeReport, rsIncome.getFloat("TotalRevenue"),
										rsIncome.getFloat("SaladRevenue"), rsIncome.getFloat("SweetsRevenue"),
										rsIncome.getFloat("DrinksRevenue"), rsIncome.getFloat("MainMealRevenue"));
								reportsList.add(ir);
							}
							break;

						case OrderReport:
							// Fetch the data from orderreport table
							PreparedStatement psOrder = DBControl.getInstance().conn.prepareStatement(
									"SELECT TotalOrders, SaladOrders, SweetsOrders, DrinksOrders, MainMealOrders FROM orderreport WHERE ReportID = ?");
							psOrder.setInt(1, reportID);
							ResultSet rsOrder = psOrder.executeQuery();
							if (rsOrder.next()) {
								or = new OrderReport(reportID, restaurantID, Year.of(Integer.parseInt(year)), month,
										ReportType.OrderReport, rsOrder.getInt("TotalOrders"),
										rsOrder.getInt("SaladOrders"), rsOrder.getInt("SweetsOrders"),
										rsOrder.getInt("DrinksOrders"), rsOrder.getInt("MainMealOrders"));
								reportsList.add(or);
							}
							break;

						case PerformanceReport:
							// Fetch the data from performancereport table
							PreparedStatement psPerformance = DBControl.getInstance().conn.prepareStatement(
									"SELECT OnTimeOrders, LateOrders, AverageDelayTime FROM performancereport WHERE ReportID = ?");
							psPerformance.setInt(1, reportID);
							ResultSet rsPerformance = psPerformance.executeQuery();
							if (rsPerformance.next()) {
								pr = new PerformanceReport(reportID, restaurantID, Year.of(Integer.parseInt(year)),
										month, ReportType.PerformanceReport, rsPerformance.getInt("OnTimeOrders"),
										rsPerformance.getInt("LateOrders"),
										rsPerformance.getString("AverageDelayTime"));
								reportsList.add(pr);
							}
							break;

						default:
							break;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Message msg = new Message(MessageType.getBranchReports, reportsList);
		return msg;
	}

	/**
	 * Retrieves the available years and quarters for CEO reports based on
	 * RestaurantID's associated with the given BranchID.
	 *
	 * @return A message containing the years and quarters.
	 */
	public Object getCEOReprotsYearQuarter(ArrayList<Object> msgObjectList) {
		int branchID = (int) msgObjectList.get(0);
		ArrayList<Object> resultList = new ArrayList<>();
		Map<String, ArrayList<String>> mapYearQuarter = new HashMap<>();

		try {
			// Step 1: Retrieve RestaurantID's associated with the given BranchID
			PreparedStatement psRestaurant = conn
					.prepareStatement("SELECT RestaurantID FROM restaurant WHERE BranchID = ?");
			psRestaurant.setInt(1, branchID);
			ResultSet rsRestaurant = psRestaurant.executeQuery();

			List<Integer> restaurantIDs = new ArrayList<>();
			while (rsRestaurant.next()) {
				restaurantIDs.add(rsRestaurant.getInt("RestaurantID"));
			}

			// Step 2: Retrieve Years and Quarters from the quarterreport table based on the
			// RestaurantID's
			if (!restaurantIDs.isEmpty()) {
				StringBuilder queryBuilder = new StringBuilder(
						"SELECT DISTINCT r.Year, qr.Quarter FROM quarterreport qr "
								+ "JOIN report r ON qr.ReportID = r.ReportID WHERE r.RestaurantID IN (");
				for (int i = 0; i < restaurantIDs.size(); i++) {
					queryBuilder.append("?");
					if (i < restaurantIDs.size() - 1) {
						queryBuilder.append(", ");
					}
				}
				queryBuilder.append(")");

				PreparedStatement psYearQuarter = conn.prepareStatement(queryBuilder.toString());

				for (int i = 0; i < restaurantIDs.size(); i++) {
					psYearQuarter.setInt(i + 1, restaurantIDs.get(i));
				}

				ResultSet rsYearQuarter = psYearQuarter.executeQuery();

				while (rsYearQuarter.next()) {
					String year = String.valueOf(rsYearQuarter.getInt("Year"));
					String quarter = rsYearQuarter.getString("Quarter");

					if (!mapYearQuarter.containsKey(year)) {
						mapYearQuarter.put(year, new ArrayList<>());
					}
					mapYearQuarter.get(year).add(quarter);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		resultList.add(mapYearQuarter);
		resultList.add(branchID);
		return new Message(MessageType.GetCEOReportsYearQuarter, resultList);
	}

	/**
	 * Retrieves the quarter reports for a specific branch, year, and list of
	 * quarters.
	 *
	 * @param msgObjectList A list containing the branch ID, year, and a list of
	 *                      quarters.
	 * @return A message containing the quarter reports.
	 */
	public Object getQuarterReports(ArrayList<Object> msgObjectList) {
		int branchID = (int) msgObjectList.get(0);
		String year = (String) msgObjectList.get(1);
		@SuppressWarnings("unchecked")
		ArrayList<String> quarters = (ArrayList<String>) msgObjectList.get(2);

		ArrayList<Object> quarterReports = new ArrayList<>();

		try {
			// Step 1: Get all RestaurantID associated with the BranchID
			PreparedStatement psRestaurant = DBControl.getInstance().conn
					.prepareStatement("SELECT RestaurantID FROM restaurant WHERE BranchID = ?");
			psRestaurant.setInt(1, branchID);
			ResultSet rsRestaurant = psRestaurant.executeQuery();

			ArrayList<Integer> restaurantIDs = new ArrayList<>();
			while (rsRestaurant.next()) {
				restaurantIDs.add(rsRestaurant.getInt("RestaurantID"));
			}

			// Step 2: For each RestaurantID, get the ReportIDs from the report table for
			// each quarter
			for (int restaurantID : restaurantIDs) {
				for (String quarter : quarters) {
					PreparedStatement psReport = DBControl.getInstance().conn.prepareStatement(
							"SELECT ReportID, ReportType FROM report WHERE RestaurantID = ? AND Year = ? AND ReportType = 'QuarterReport'");
					psReport.setInt(1, restaurantID);
					psReport.setInt(2, Integer.parseInt(year));

					ResultSet rsReport = psReport.executeQuery();

					// Step 3: For each ReportID, get the data from the relevant quarterreport table
					while (rsReport.next()) {
						int reportID = rsReport.getInt("ReportID");

						PreparedStatement psQuarterReport = DBControl.getInstance().conn
								.prepareStatement("SELECT * FROM quarterreport WHERE ReportID = ? AND Quarter = ?");
						psQuarterReport.setInt(1, reportID);
						psQuarterReport.setString(2, quarter);

						ResultSet rsQuarterReport = psQuarterReport.executeQuery();
						while (rsQuarterReport.next()) {
							int dailyOrders = rsQuarterReport.getInt("DailyOrders");
							float totalRevenue = rsQuarterReport.getFloat("TotalRevenue");
							Quarter quarterEnum = Quarter.valueOf(rsQuarterReport.getString("Quarter"));

							// Step 4: Fetch daily order data from quarterreportdailyorders table
							PreparedStatement psDailyOrders = DBControl.getInstance().conn.prepareStatement(
									"SELECT Date, NumberOfOrders FROM quarterreportdailyorders WHERE ReportID = ?");
							psDailyOrders.setInt(1, reportID);
							ResultSet rsDailyOrders = psDailyOrders.executeQuery();

							Map<String, Integer> dayToOrder = new HashMap<>();
							while (rsDailyOrders.next()) {
								String date = rsDailyOrders.getDate("Date").toLocalDate().plusDays(1).toString();
								int numberOfOrders = rsDailyOrders.getInt("NumberOfOrders");
								dayToOrder.put(date, numberOfOrders);
							}

							// Step 5: Create a QuarterReport object and add it to the list
							QuarterReport quarterReport = new QuarterReport(reportID, restaurantID,
									Year.of(Integer.parseInt(year)), quarter, ReportType.QuarterReport, quarterEnum,
									dailyOrders, totalRevenue, dayToOrder);
							quarterReports.add(quarterReport);
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new Message(MessageType.getQuarterReports1, quarterReports);
	}

}
