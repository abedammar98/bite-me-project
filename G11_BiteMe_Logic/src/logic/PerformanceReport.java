package logic;

import java.time.Year;
/**
 * The PerformanceReport class represents a report summarizing the performance of a restaurant in terms of order delivery times.
 */
public class PerformanceReport extends Report {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int OnTimeOrders;
	private int LateOrders;
	private String AverageDelayTime;
    /**
     * Constructs a PerformanceReport with the specified details.
     *
     * @param reportID         The unique ID of the report.
     * @param restaurantID     The ID of the restaurant associated with the report.
     * @param year             The year of the report.
     * @param month            The month of the report.
     * @param reportType       The type of the report.
     * @param onTimeOrders     The number of orders delivered on time.
     * @param lateOrders       The number of orders delivered late.
     * @param averageDelayTime The average delay time for late orders.
     */
	public PerformanceReport(int reportID, int restaurantID, Year year, String month, ReportType reportType,
			int onTimeOrders, int lateOrders, String averageDelayTime) {
		super(reportID, restaurantID, year, month, reportType);
		OnTimeOrders = onTimeOrders;
		LateOrders = lateOrders;
		AverageDelayTime = averageDelayTime;
	}
    /**
     * Gets the number of orders delivered on time.
     *
     * @return The number of on-time orders.
     */
	public int getOnTimeOrders() {
		return OnTimeOrders;
	}
    /**
     * Sets the number of orders delivered on time.
     *
     * @param onTimeOrders The number of on-time orders.
     */
	public void setOnTimeOrders(int onTimeOrders) {
		OnTimeOrders = onTimeOrders;
	}
    /**
     * Gets the number of orders delivered late.
     *
     * @return The number of late orders.
     */
	public int getLateOrders() {
		return LateOrders;
	}
    /**
     * Sets the number of orders delivered late.
     *
     * @param lateOrders The number of late orders.
     */
	public void setLateOrders(int lateOrders) {
		LateOrders = lateOrders;
	}
    /**
     * Gets the average delay time for late orders.
     *
     * @return The average delay time as a string.
     */
	public String getAverageDelayTime() {
		return AverageDelayTime;
	}
    /**
     * Sets the average delay time for late orders.
     *
     * @param averageDelayTime The average delay time as a string.
     */
	public void setAverageDelayTime(String averageDelayTime) {
		AverageDelayTime = averageDelayTime;
	}

}
