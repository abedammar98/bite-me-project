package logic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * The OrderItemDetail class represents the details of an item within an order.
 */
public class OrderItemDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	private Item item;
	private String notes;
	private int quantity;
	private float cost;

	/**
	 * Constructs an OrderItemDetail object with the specified item, notes,
	 * quantity, and cost.
	 *
	 * @param item     The item in the order.
	 * @param notes    Any notes associated with the item.
	 * @param quantity The quantity of the item.
	 * @param cost     The cost of the item.
	 */
	public OrderItemDetail(Item item, String notes, int quantity, float cost) {
		this.item = item;
		this.notes = notes;
		this.quantity = quantity;
		this.cost = cost;
	}

	/**
	 * Gets the item associated with this detail.
	 *
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Sets the item associated with this detail.
	 *
	 * @param item The item to set.
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * Gets the notes associated with the item.
	 *
	 * @return The notes.
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * Sets the notes associated with the item.
	 *
	 * @param notes The notes to set.
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * Gets the quantity of the item.
	 *
	 * @return The quantity.
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity of the item.
	 *
	 * @param quantity The quantity to set.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Gets the cost of the item.
	 *
	 * @return The cost.
	 */
	public float getCost() {
		return cost;
	}

	/**
	 * Sets the cost of the item.
	 *
	 * @param cost The cost to set.
	 */
	public void setCost(float cost) {
		this.cost = cost;
	}

	/**
	 * Calculates the total cost of all items in the given list.
	 *
	 * @param list The list of OrderItemDetail objects.
	 * @return The total cost, rounded to two decimal places.
	 */
	public static float totalCost(ArrayList<OrderItemDetail> list) {
		float total = 0;
		for (OrderItemDetail oid : list) {
			total += oid.getCost() * oid.getQuantity();
		}
		// Round to 2 decimal places
		BigDecimal bd = new BigDecimal(Float.toString(total));
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.floatValue();
	}
    /**
     * Gets the name of the item.
     *
     * @return The item name.
     */
	public String getItemName() {
		return item.getItemName();
	}
}