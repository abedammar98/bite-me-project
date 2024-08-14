package logic;

/**
 * The Menu class represents a menu with a unique identifier.
 */
public class Menu {
	private int MenuID;

	/**
	 * Constructs a new Menu object with the specified menu ID.
	 *
	 * @param menuID The unique identifier for the menu.
	 */
	public int getMenuID() {
		return MenuID;
	}

	/**
	 * Gets the unique identifier for the menu.
	 *
	 * @return The menu ID.
	 */
	public void setMenuID(int menuID) {
		MenuID = menuID;
	}

	/**
	 * Sets the unique identifier for the menu.
	 *
	 * @param menuID The new menu ID.
	 */
	public Menu(int menuID) {
		super();
		MenuID = menuID;
	}
}
