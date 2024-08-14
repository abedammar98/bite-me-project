package logic;

import java.io.Serializable;

/**
 * Abstract class representing an item in the restaurant menu.
 */
public abstract class Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ItemID;
	private String ItemName;
	private Category CategoryName;
	private String Description;
	private float Cost;

	/**
	 * Constructs an Item with the specified details.
	 *
	 * @param itemID       The unique ID of the item.
	 * @param itemName     The name of the item.
	 * @param categoryName The category to which the item belongs.
	 * @param description  A brief description of the item.
	 * @param cost         The cost of the item.
	 */
	public Item(int itemID, String itemName, Category categoryName, String description, float cost) {
		ItemID = itemID;
		ItemName = itemName;
		CategoryName = categoryName;
		Description = description;
		Cost = cost;
	}

	/**
	 * Gets the unique ID of the item.
	 *
	 * @return The item ID.
	 */
	public int getItemID() {
		return ItemID;
	}

	/**
	 * Sets the unique ID of the item.
	 *
	 * @param itemID The item ID to set.
	 */
	public void setItemID(int itemID) {
		ItemID = itemID;
	}

	/**
	 * Gets the name of the item.
	 *
	 * @return The item name.
	 */
	public String getItemName() {
		return ItemName;
	}

	/**
	 * Sets the name of the item.
	 *
	 * @param itemName The item name to set.
	 */
	public void setItemName(String itemName) {
		ItemName = itemName;
	}

	/**
	 * Gets the category to which the item belongs.
	 *
	 * @return The category name.
	 */
	public Category getCategoryName() {
		return CategoryName;
	}

	/**
	 * Sets the category to which the item belongs.
	 *
	 * @param categoryName The category name to set.
	 */
	public void setCategoryName(Category categoryName) {
		CategoryName = categoryName;
	}

	/**
	 * Gets the description of the item.
	 *
	 * @return The item description.
	 */
	public String getDescription() {
		return Description;
	}

	/**
	 * Sets the description of the item.
	 *
	 * @param description The item description to set.
	 */
	public void setDescription(String description) {
		Description = description;
	}

	/**
	 * Gets the cost of the item.
	 *
	 * @return The item cost.
	 */
	public float getCost() {
		return Cost;
	}

	/**
	 * Sets the cost of the item.
	 *
	 * @param cost The item cost to set.
	 */
	public void setCost(float cost) {
		Cost = cost;
	}
}
