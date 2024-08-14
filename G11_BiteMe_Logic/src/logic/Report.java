package logic;

import java.io.Serializable;
import java.time.Year;

/**
 * The Report class serves as an abstract base class for various types of
 * reports.
 */
public abstract class Report implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int reportID;
	private int restaurantID;
	private Year year;
	private String month;
	private ReportType reportType;

	/**
	 * Constructs a Report with the specified details.
	 *
	 * @param reportID     The unique ID of the report.
	 * @param restaurantID The ID of the restaurant associated with the report.
	 * @param year         The year of the report.
	 * @param month        The month of the report.
	 * @param reportType   The type of the report.
	 */
	public Report(int reportID, int restaurantID, Year year, String month, ReportType reportType) {
		super();
		this.reportID = reportID;
		this.restaurantID = restaurantID;
		this.year = year;
		this.month = month;
		this.reportType = reportType;
	}
    /**
     * Gets the unique ID of the report.
     *
     * @return The report ID.
     */
	public int getReportID() {
		return reportID;
	}
    /**
     * Sets the unique ID of the report.
     *
     * @param reportID The new report ID.
     */
	public void setReportID(int reportID) {
		this.reportID = reportID;
	}

    /**
     * Gets the ID of the restaurant associated with the report.
     *
     * @return The restaurant ID.
     */
	public int getRestaurantID() {
		return restaurantID;
	}

    /**
     * Sets the ID of the restaurant associated with the report.
     *
     * @param restaurantID The new restaurant ID.
     */
	public void setRestaurantID(int restaurantID) {
		this.restaurantID = restaurantID;
	}
    /**
     * Gets the year of the report.
     *
     * @return The year of the report.
     */
	public Year getYear() {
		return year;
	}
    /**
     * Sets the year of the report.
     *
     * @param year The new year of the report.
     */
	public void setYear(Year year) {
		this.year = year;
	}
    /**
     * Gets the month of the report.
     *
     * @return The month of the report.
     */
	public String getMonth() {
		return month;
	}
    /**
     * Sets the month of the report.
     *
     * @param month The new month of the report.
     */
	public void setMonth(String month) {
		this.month = month;
	}
    /**
     * Gets the type of the report.
     *
     * @return The type of the report.
     */
	public ReportType getReportType() {
		return reportType;
	}
    /**
     * Sets the type of the report.
     *
     * @param reportType The new type of the report.
     */
	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

}
