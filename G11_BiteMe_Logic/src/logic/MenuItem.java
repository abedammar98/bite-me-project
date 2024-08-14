package logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The MenuItem class represents the items in a menu. It contains a list of
 * items and is associated with a specific menu ID.
 */
public class MenuItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int MenuID;
	private ArrayList<Item> menuItems = new ArrayList<>();

	/**
	 * Constructs a new MenuItem object with the specified menu ID and list of
	 * items.
	 *
	 * @param menuID    The unique identifier for the menu.
	 * @param menuItems The list of items in the menu.
	 */
	public MenuItem(int menuID, ArrayList<Item> menuItems) {
		MenuID = menuID;
		this.menuItems = menuItems;
	}

	/**
	 * Gets the unique identifier for the menu.
	 *
	 * @return The menu ID.
	 */
	public int getMenuID() {
		return MenuID;
	}

	/**
	 * Sets the unique identifier for the menu.
	 *
	 * @param menuID The new menu ID.
	 */
	public void setMenuID(int menuID) {
		MenuID = menuID;
	}

	/**
	 * Gets the list of items in the menu.
	 *
	 * @return The list of menu items.
	 */
	public ArrayList<Item> getMenuItems() {
		return menuItems;
	}

	/**
	 * Sets the list of items in the menu.
	 *
	 * @param menuItems The new list of menu items.
	 */
	public void setMenuItems(ArrayList<Item> menuItems) {
		this.menuItems = menuItems;
	}

	/**
	 * Adds an item to the menu.
	 *
	 * @param item The item to add to the menu.
	 */
	public void addItem(Item item) {
		menuItems.add(item);
	}
}
