package logic;

/**
 * ConcreteItem represents a specific item available in the restaurant menu.
 * This class extends the abstract Item class.
 */
public class ConcreteItem extends Item {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a ConcreteItem with the specified details.
     *
     * @param itemID       The unique ID of the item.
     * @param itemName     The name of the item.
     * @param categoryName The category to which the item belongs.
     * @param description  A brief description of the item.
     * @param cost         The cost of the item.
     */
    public ConcreteItem(int itemID, String itemName, Category categoryName, String description, float cost) {
        super(itemID, itemName, categoryName, description, cost);
    }

    @Override
    public String toString() {
        return "ConcreteItem{" +
                "itemID=" + getItemID() +
                ", itemName='" + getItemName() + '\'' +
                ", category=" + getCategoryName() +
                ", description='" + getDescription() + '\'' +
                ", cost=" + getCost() +
                '}';
    }
}

