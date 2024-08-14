package logic;

import java.time.Year;
import java.util.Map;

/**
 * The QuarterReport class represents a report summarizing the daily orders and
 * revenue of a restaurant for a given quarter.
 */
public class QuarterReport extends Report {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Quarter quarter;
	private int DailyOrders;
	private float TotalRevenue;
	private Map<String, Integer> dayToOrder;

	/**
	 * Constructs a QuarterReport with the specified details.
	 *
	 * @param reportID     The unique ID of the report.
	 * @param restaurantID The ID of the restaurant associated with the report.
	 * @param year         The year of the report.
	 * @param month        The month of the report.
	 * @param reportType   The type of the report.
	 * @param quarter      The quarter of the year.
	 * @param dailyOrders  The number of daily orders in the quarter.
	 * @param totalRevenue The total revenue generated in the quarter.
	 * @param dayToOrder   A map of day-to-order counts.
	 */
	public QuarterReport(int reportID, int restaurantID, Year year, String month, ReportType reportType,
			Quarter quarter, int dailyOrders, float totalRevenue, Map<String, Integer> dayToOrder) {
		super(reportID, restaurantID, year, month, reportType);
		this.quarter = quarter;
		DailyOrders = dailyOrders;
		TotalRevenue = totalRevenue;
		this.dayToOrder = dayToOrder;
	}

	/**
	 * Gets the map of day-to-order counts.
	 *
	 * @return The map of day-to-order counts.
	 */
	public Map<String, Integer> getDayToOrder() {
		return dayToOrder;
	}

	/**
	 * Sets the map of day-to-order counts.
	 *
	 * @param dayToOrder The new map of day-to-order counts.
	 */
	public void setDayToOrder(Map<String, Integer> dayToOrder) {
		this.dayToOrder = dayToOrder;
	}

	/**
	 * Gets the quarter of the year for the report.
	 *
	 * @return The quarter.
	 */
	public Quarter getQuarter() {
		return quarter;
	}

	/**
	 * Sets the quarter of the year for the report.
	 *
	 * @param quarter The new quarter.
	 */
	public void setQuarter(Quarter quarter) {
		this.quarter = quarter;
	}

	/**
	 * Gets the number of daily orders in the quarter.
	 *
	 * @return The number of daily orders.
	 */
	public int getDailyOrders() {
		return DailyOrders;
	}

	/**
	 * Sets the number of daily orders in the quarter.
	 *
	 * @param dailyOrders The new number of daily orders.
	 */
	public void setDailyOrders(int dailyOrders) {
		DailyOrders = dailyOrders;
	}

	/**
	 * Gets the total revenue generated in the quarter.
	 *
	 * @return The total revenue.
	 */
	public float getTotalRevenue() {
		return TotalRevenue;
	}

	/**
	 * Sets the total revenue generated in the quarter.
	 *
	 * @param totalRevenue The new total revenue.
	 */
	public void setTotalRevenue(float totalRevenue) {
		TotalRevenue = totalRevenue;
	}

}
