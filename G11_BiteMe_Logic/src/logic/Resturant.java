package logic;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
/**
 * The Resturant class represents a restaurant with its associated information.
 */
public class Resturant implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int ResturantID;
	private String ResturantName;
	private Branch branch;
	private int MenuID;
    /**
     * Constructs a Resturant object with the specified details.
     *
     * @param resturantID   The unique ID of the restaurant.
     * @param resturantName The name of the restaurant.
     * @param branch        The branch where the restaurant is located.
     * @param menuID        The ID of the menu associated with the restaurant.
     */
	public Resturant(int resturantID, String resturantName, int branch, int menuID) {
		ResturantID = resturantID;
		ResturantName = resturantName;
		this.branch = Branch.fromValue(branch);
		MenuID = menuID;
	}
    /**
     * Gets the ID of the restaurant.
     *
     * @return The restaurant ID.
     */
	public int getResturantID() {
		return ResturantID;
	}
    /**
     * Sets the ID of the restaurant.
     *
     * @param resturantID The restaurant ID.
     */
	public void setResturantID(int resturantID) {
		ResturantID = resturantID;
	}
    /**
     * Gets the name of the restaurant.
     *
     * @return The restaurant name.
     */
	public String getResturantName() {
		return ResturantName;
	}
    /**
     * Sets the name of the restaurant.
     *
     * @param resturantName The restaurant name.
     */
	public void setResturantName(String resturantName) {
		ResturantName = resturantName;
	}
    /**
     * Gets the branch where the restaurant is located.
     *
     * @return The branch of the restaurant.
     */
	public Branch getBranchID() {
		return branch;
	}
    /**
     * Sets the branch where the restaurant is located.
     *
     * @param branch The branch of the restaurant.
     */
	public void setBranchID(Branch branch) {
		this.branch = branch;
	}
    /**
     * Gets the menu ID associated with the restaurant.
     *
     * @return The menu ID.
     */
	public int getMenuID() {
		return MenuID;
	}
	   /**
     * Sets the menu ID associated with the restaurant.
     *
     * @param menuID The menu ID.
     */
	public void setMenuID(int menuID) {
		MenuID = menuID;
	}
    /**
     * Gets the branch as a SimpleStringProperty for JavaFX binding.
     *
     * @return The branch as a SimpleStringProperty.
     */
	public SimpleStringProperty branchProperty() {
		return new SimpleStringProperty(branch.name());
	}
}
