package logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * The OrderItem class represents the items within an order.
 */
public class OrderItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private Order order;
	private List<OrderItemDetail> orderItemDetails = new ArrayList<>();
    /**
     * Constructs an OrderItem object for a given order.
     *
     * @param order The order associated with this OrderItem.
     */
	public OrderItem(Order order) {
		this.order = order;
	}
    /**
     * Sets the details of the items in the order.
     *
     * @param orderItemDetails The list of order item details.
     */
	public void setOrderItemDetails(List<OrderItemDetail> orderItemDetails) {
		this.orderItemDetails = orderItemDetails;
	}

    /**
     * Gets the order associated with this OrderItem.
     *
     * @return The order.
     */
	public Order getOrder() {
		return order;
	}
    /**
     * Sets the order associated with this OrderItem.
     *
     * @param order The order to set.
     */
	public void setOrder(Order order) {
		this.order = order;
	}
    /**
     * Gets the list of item details in the order.
     *
     * @return The list of order item details.
     */
	public List<OrderItemDetail> getOrderItemDetails() {
		return orderItemDetails;
	}
    /**
     * Adds an item to the order.
     *
     * @param item     The item to add.
     * @param notes    Any notes for the item.
     * @param quantity The quantity of the item.
     * @param cost     The cost of the item.
     */
	public void addItem(Item item, String notes, int quantity, float cost) {
		orderItemDetails.add(new OrderItemDetail(item, notes, quantity, cost));
	}
}
