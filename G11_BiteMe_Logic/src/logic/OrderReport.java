package logic;

import java.time.Year;

/**
 * The OrderReport class represents a report summarizing the orders in a
 * restaurant for a given period.
 */
public class OrderReport extends Report {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int TotalOrders;
	private int SaladOrders;
	private int SweetsOrders;
	private int DrinksOrders;
	private int MainMealOrders;

	/**
	 * Constructs an OrderReport object with the specified details.
	 *
	 * @param reportID       The unique ID of the report.
	 * @param restaurantID   The ID of the restaurant associated with the report.
	 * @param year           The year of the report.
	 * @param month          The month of the report.
	 * @param reportType     The type of report.
	 * @param totalOrders    The total number of orders.
	 * @param saladOrders    The number of salad orders.
	 * @param sweetsOrders   The number of sweets orders.
	 * @param drinksOrders   The number of drinks orders.
	 * @param mainMealOrders The number of main meal orders.
	 */
	public OrderReport(int reportID, int restaurantID, Year year, String month, ReportType reportType, int totalOrders,
			int saladOrders, int sweetsOrders, int drinksOrders, int mainMealOrders) {
		super(reportID, restaurantID, year, month, reportType);
		TotalOrders = totalOrders;
		SaladOrders = saladOrders;
		SweetsOrders = sweetsOrders;
		DrinksOrders = drinksOrders;
		MainMealOrders = mainMealOrders;
	}

	/**
	 * Gets the total number of orders.
	 *
	 * @return The total number of orders.
	 */
	public int getTotalOrders() {
		return TotalOrders;
	}

	/**
	 * Sets the total number of orders.
	 *
	 * @param totalOrders The total number of orders to set.
	 */
	public void setTotalOrders(int totalOrders) {
		TotalOrders = totalOrders;
	}

	/**
	 * Gets the number of salad orders.
	 *
	 * @return The number of salad orders.
	 */
	public int getSaladOrders() {
		return SaladOrders;
	}

	/**
	 * Sets the number of salad orders.
	 *
	 * @param saladOrders The number of salad orders to set.
	 */
	public void setSaladOrders(int saladOrders) {
		SaladOrders = saladOrders;
	}

	/**
	 * Gets the number of sweets orders.
	 *
	 * @return The number of sweets orders.
	 */
	public int getSweetsOrders() {
		return SweetsOrders;
	}

	/**
	 * Sets the number of sweets orders.
	 *
	 * @param sweetsOrders The number of sweets orders to set.
	 */
	public void setSweetsOrders(int sweetsOrders) {
		SweetsOrders = sweetsOrders;
	}

	/**
	 * Gets the number of drinks orders.
	 *
	 * @return The number of drinks orders.
	 */
	public int getDrinksOrders() {
		return DrinksOrders;
	}

	/**
	 * Sets the number of drinks orders.
	 *
	 * @param drinksOrders The number of drinks orders to set.
	 */
	public void setDrinksOrders(int drinksOrders) {
		DrinksOrders = drinksOrders;
	}

	/**
	 * Gets the number of main meal orders.
	 *
	 * @return The number of main meal orders.
	 */
	public int getMainMealOrders() {
		return MainMealOrders;
	}

	/**
	 * Sets the number of main meal orders.
	 *
	 * @param mainMealOrders The number of main meal orders to set.
	 */
	public void setMainMealOrders(int mainMealOrders) {
		MainMealOrders = mainMealOrders;
	}

}
