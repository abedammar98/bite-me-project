package logic;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * The Salad class represents a salad item in the menu, with its associated ingredients and size.
 */
public class Salad extends Item implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> ingredients;
    private String size;
    /**
     * Constructs a Salad object with the specified details.
     *
     * @param itemID       The unique ID of the salad item.
     * @param itemName     The name of the salad.
     * @param categoryName The category of the salad.
     * @param description  The description of the salad.
     * @param cost         The cost of the salad.
     * @param ingredients  The ingredients of the salad.
     * @param size         The size of the salad.
     */
    public Salad(int itemID, String itemName, Category categoryName, String description, float cost, ArrayList<String> ingredients, String size) {
        super(itemID, itemName, categoryName, description, cost);
        this.ingredients = ingredients;
        this.size = size;
    }

    /**
     * Gets the ingredients of the salad.
     *
     * @return A list of ingredients.
     */
    public ArrayList<String> getIngredients() {
        return ingredients;
    }
    /**
     * Sets the ingredients of the salad.
     *
     * @param ingredients A list of ingredients.
     */
    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
    /**
     * Gets the size of the salad.
     *
     * @return The size of the salad.
     */
    public String getSize() {
        return size;
    }
    /**
     * Sets the size of the salad.
     *
     * @param size The size of the salad.
     */
    public void setSize(String size) {
        this.size = size;
    }
}