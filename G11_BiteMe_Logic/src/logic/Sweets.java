package logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Sweets class represents a sweet item in the menu, with its associated
 * ingredients.
 */
public class Sweets extends Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> ingredients;

	/**
	 * Constructs a Sweets object with the specified details.
	 *
	 * @param itemID       The unique ID of the sweet item.
	 * @param itemName     The name of the sweet.
	 * @param categoryName The category of the sweet.
	 * @param description  The description of the sweet.
	 * @param cost         The cost of the sweet.
	 * @param ingredients  The list of ingredients in the sweet.
	 */
	public Sweets(int itemID, String itemName, Category categoryName, String description, float cost,
			ArrayList<String> ingredients) {
		super(itemID, itemName, categoryName, description, cost);
		this.ingredients = ingredients;
	}

	/**
	 * Gets the list of ingredients in the sweet.
	 *
	 * @return A list of ingredients.
	 */
	public ArrayList<String> getSweetsDescription() {
		return ingredients;
	}

	/**
	 * Sets the list of ingredients in the sweet.
	 *
	 * @param ingredients A list of new ingredients.
	 */
	public void setSweetsDescription(ArrayList<String> ingredients) {
		this.ingredients = ingredients;
	}
}