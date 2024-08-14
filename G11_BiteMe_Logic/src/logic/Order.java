package logic;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The Order class represents a customer's order in the restaurant system.
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int nextOrderID = 2;
	private int OrderID;
	private int UserID;
	private LocalDate DateOfOrder;
	private String TimeOfOrder;
	private int ResturantID;
	private String PickUpType;
	private String Location;
	private String Status;
	private String RequestedTimeOfDelivery;
	private LocalDate RequestedDateOfDelivery;
	private String ETA;
	private String duration;
	private float Cost;

	/**
	 * Constructs a new Order object with the specified details.
	 *
	 * @param orderID                 The unique ID of the order.
	 * @param userID                  The ID of the user who placed the order.
	 * @param dateOfOrder             The date the order was placed.
	 * @param timeOfOrder             The time the order was placed.
	 * @param resturantID             The ID of the restaurant where the order was
	 *                                placed.
	 * @param pickUpType              The type of pickup (e.g., delivery, takeaway).
	 * @param location                The location for delivery or pickup.
	 * @param status                  The status of the order.
	 * @param requestedTimeOfDelivery The requested time of delivery.
	 * @param requestedDateOfDelivery The requested date of delivery.
	 * @param ETA                     The estimated time of arrival for the order.
	 * @param duration                The duration of the order process.
	 * @param cost                    The cost of the order.
	 */
	public Order(int OrderID, int UserID, LocalDate DateOfOrder, String TimeOfOrder, int ResturantID, String PickUpType,
			String Location, String Status, String RequestedTimeOfDelivery, LocalDate RequestedDateOfDelivery,
			String ETA, String duration, float Cost) {
		this.OrderID = OrderID;
		this.UserID = UserID;
		this.DateOfOrder = DateOfOrder;
		this.TimeOfOrder = TimeOfOrder;
		this.ResturantID = ResturantID;
		this.PickUpType = PickUpType;
		this.Location = Location;
		this.Status = Status;
		this.RequestedTimeOfDelivery = RequestedTimeOfDelivery;
		this.RequestedDateOfDelivery = RequestedDateOfDelivery;
		this.ETA = ETA;
		this.duration = duration;
		this.Cost = Cost;
	}
	   /**
     * Gets the unique ID of the order.
     *
     * @return The order ID.
     */
	public int getOrderID() {
		return OrderID;
	}
    /**
     * Sets the unique ID of the order.
     *
     * @param orderID The order ID to set.
     */
	public void setOrderID(int orderID) {
		OrderID = orderID;
	}
    /**
     * Gets the ID of the user who placed the order.
     *
     * @return The user ID.
     */
	public int getUserID() {
		return UserID;
	}
    /**
     * Sets the ID of the user who placed the order.
     *
     * @param userID The user ID to set.
     */
	public void setUserID(int userID) {
		UserID = userID;
	}
    /**
     * Gets the date the order was placed.
     *
     * @return The date of the order.
     */
	public LocalDate getDateOfOrder() {
		return DateOfOrder;
	}
    /**
     * Sets the date the order was placed.
     *
     * @param dateOfOrder The date to set.
     */
	public void setDateOfOrder(LocalDate dateOfOrder) {
		DateOfOrder = dateOfOrder;
	}
    /**
     * Gets the time the order was placed.
     *
     * @return The time of the order.
     */
	public String getTimeOfOrder() {
		return TimeOfOrder;
	}
	   /**
     * Sets the time the order was placed.
     *
     * @param timeOfOrder The time to set.
     */
	public void setTimeOfOrder(String timeOfOrder) {
		TimeOfOrder = timeOfOrder;
	}
    /**
     * Gets the ID of the restaurant where the order was placed.
     *
     * @return The restaurant ID.
     */
	public int getResturantID() {
		return ResturantID;
	}
    /**
     * Sets the ID of the restaurant where the order was placed.
     *
     * @param resturantID The restaurant ID to set.
     */
	public void setResturantID(int resturantID) {
		ResturantID = resturantID;
	}
    /**
     * Gets the type of pickup for the order.
     *
     * @return The pickup type.
     */
	public String getPickUpType() {
		return PickUpType;
	}
    /**
     * Sets the type of pickup for the order.
     *
     * @param pickUpType The pickup type to set.
     */
	public void setPickUpType(String pickUpType) {
		PickUpType = pickUpType;
	}
    /**
     * Gets the location for delivery or pickup.
     *
     * @return The location.
     */
	public String getLocation() {
		return Location;
	}

    /**
     * Sets the location for delivery or pickup.
     *
     * @param location The location to set.
     */
	public void setLocation(String location) {
		Location = location;
	}
	   /**
     * Gets the status of the order.
     *
     * @return The order status.
     */
	public String getStatus() {
		return Status;
	}

    /**
     * Sets the status of the order.
     *
     * @param status The status to set.
     */
	public void setStatus(String status) {
		Status = status;
	}
    /**
     * Gets the requested time of delivery.
     *
     * @return The requested time of delivery.
     */
	public String getRequestedTimeOfDelivery() {
		return RequestedTimeOfDelivery;
	}
    /**
     * Sets the requested time of delivery.
     *
     * @param requestedTimeOfDelivery The requested time to set.
     */
	public void setRequestedTimeOfDelivery(String requestedTimeOfDelivery) {
		RequestedTimeOfDelivery = requestedTimeOfDelivery;
	}
    /**
     * Gets the requested date of delivery.
     *
     * @return The requested date of delivery.
     */
	public LocalDate getRequestedDateOfDelivery() {
		return RequestedDateOfDelivery;
	}
    /**
     * Sets the requested date of delivery.
     *
     * @param requestedDateOfDelivery The requested date to set.
     */
	public void setRequestedDateOfDelivery(LocalDate requestedDateOfDelivery) {
		RequestedDateOfDelivery = requestedDateOfDelivery;
	}
    /**
     * Gets the estimated time of arrival (ETA) for the order.
     *
     * @return The ETA.
     */
	public String getETA() {
		return ETA;
	}
    /**
     * Sets the estimated time of arrival (ETA) for the order.
     *
     * @param ETA The ETA to set.
     */
	public void setETA(String eTA) {
		ETA = eTA;
	}
    /**
     * Gets the duration of the order process.
     *
     * @return The duration.
     */
	public String getDuration() {
		return duration;
	}

    /**
     * Sets the duration of the order process.
     *
     * @param duration The duration to set.
     */
	public void setDuration(String duration) {
		this.duration = duration;
	}

    /**
     * Gets the cost of the order.
     *
     * @return The cost.
     */
	public float getCost() {
		return Cost;
	}
    /**
     * Sets the cost of the order.
     *
     * @param cost The cost to set.
     */
	public void setCost(float cost) {
		Cost = cost;
	}
    /**
     * Gets the next order ID in sequence.
     *
     * @return The next order ID.
     */
	public static synchronized int getNextOrderID() {
		return nextOrderID++;
	}
    /**
     * Gets the date and time when the order was placed.
     *
     * @return The order date and time.
     */
	public LocalDateTime getOrderDateTime() {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime time = LocalTime.parse(TimeOfOrder, timeFormatter);
		return LocalDateTime.of(DateOfOrder, time);
	}
	   /**
     * Gets the date and time when the delivery was requested.
     *
     * @return The requested delivery date and time.
     */
	public LocalDateTime getRequestedDeliveryDateTime() {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime time = LocalTime.parse(RequestedTimeOfDelivery, timeFormatter);
		return LocalDateTime.of(RequestedDateOfDelivery, time);
	}
    /**
     * Checks if the order is within two hours of the requested delivery time.
     *
     * @return True if the order is within two hours, false otherwise.
     */
	public boolean isOrderWithinTwoHours() {
		LocalDateTime orderDateTime = getOrderDateTime();
		LocalDateTime requestedDeliveryDateTime = getRequestedDeliveryDateTime();
		Duration duration = Duration.between(orderDateTime, requestedDeliveryDateTime);
		System.out.println(duration.toHours()*60+duration.toMinutes());
		return duration.toMinutes()  <= 120;
	}

    /**
     * Calculates the duration from now until the requested delivery time.
     */
	public void calculateDurationFromNow() {
		LocalDateTime requestedDateTime = getRequestedDeliveryDateTime();
		LocalDateTime currentDateTime = LocalDateTime.now();
		// Calculate the duration between current time and requested time
		Duration duration = Duration.between(requestedDateTime, currentDateTime);
		long diffMinutes = duration.toMinutes() % 60;
		long diffHours = duration.toHours() % 24;
		long diffDays = duration.toDays();

		// Format the duration as a string
		String durationString = String.format("%d:%02d:%02d", diffDays, diffHours, diffMinutes);

		// Set the Duration field
		this.duration = durationString;
	}
    /**
     * Determines if the customer is eligible for a coupon based on the order timing.
     *
     * @return True if the customer is eligible for a coupon, false otherwise.
     */
	public boolean customerEligibleForCoupon() {
		LocalDateTime requestedDateTime = getRequestedDeliveryDateTime();
		LocalDateTime currentDateTime = LocalDateTime.now();

		// Calculate the duration between current time and requested time
		Duration duration = Duration.between(requestedDateTime, currentDateTime);
		long diffMinutes = duration.toMinutes();
		long diffHours = duration.toHours();
		// Check if order is within 2 hours
		if (isOrderWithinTwoHours()) {
			// Order is within 2 hours, check if duration is longer than an hour
			return diffHours > 1;
		} else {
			// Order is more than 2 hours away, check if duration is more than 20 minutes
			return diffMinutes > 20;
		}
	}
}
