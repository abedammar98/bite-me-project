package logic;

import java.io.Serializable;
/**
 * MainMeal represents a main meal item in the restaurant menu.
 * This class extends the abstract Item class.
 */
public class MainMeal extends Item implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cookMethod;
	private Doneness doneness;
    /**
     * Constructs a MainMeal item with the specified details.
     *
     * @param itemID       The unique ID of the main meal item.
     * @param itemName     The name of the main meal item.
     * @param categoryName The category to which the main meal item belongs.
     * @param description  A brief description of the main meal item.
     * @param cost         The cost of the main meal item.
     * @param cookMethod   The cooking method for the main meal.
     * @param doneness     The doneness level of the main meal.
     */
	public MainMeal(int itemID, String itemName, Category categoryName, String description, float cost,
			String cookMethod, Doneness doneness) {
		super(itemID, itemName, categoryName, description, cost);
		this.cookMethod = cookMethod;
		this.doneness = doneness;
	}
    /**
     * Gets the cooking method for the main meal.
     *
     * @return The cooking method.
     */
	public String getCookMethod() {
		return cookMethod;
	}
    /**
     * Sets the cooking method for the main meal.
     *
     * @param cookMethod The cooking method to set.
     */
	public void setCookMethod(String cookMethod) {
		this.cookMethod = cookMethod;
	}
    /**
     * Gets the doneness level of the main meal.
     *
     * @return The doneness level.
     */
	public Doneness getDoneness() {
		return doneness;
	}
    /**
     * Sets the doneness level of the main meal.
     *
     * @param doneness The doneness level to set.
     */
	public void setDoneness(Doneness doneness) {
		this.doneness = doneness;
	}
}