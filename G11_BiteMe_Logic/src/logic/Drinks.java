package logic;

import java.io.Serializable;

/**
 * Drinks represents a drink item in the restaurant menu. This class extends the
 * abstract Item class.
 */
public class Drinks extends Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Size size;

	/**
	 * Constructs a Drinks item with the specified details.
	 *
	 * @param itemID       The unique ID of the drink item.
	 * @param itemName     The name of the drink item.
	 * @param categoryName The category to which the drink item belongs.
	 * @param description  A brief description of the drink item.
	 * @param cost         The cost of the drink item.
	 * @param size         The size of the drink.
	 */
	public Drinks(int itemID, String itemName, Category categoryName, String description, float cost, Size size) {
		super(itemID, itemName, categoryName, description, cost);
		this.size = size;
	}
	/**
     * Gets the size of the drink.
     *
     * @return The size of the drink.
     */
	public Size getSize() {
		return size;
	}
	 /**
     * Sets the size of the drink.
     *
     * @param size The new size of the drink.
     */
	public void setSize(Size size) {
		this.size = size;
	}
}