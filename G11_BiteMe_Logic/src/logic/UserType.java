package logic;
/**
 * The UserType enum represents different types of users in the system.
 */
public enum UserType {
	Customer, Manager, CEO, Supplier;
    /**
     * Converts a string to its corresponding UserType.
     *
     * @param type The string representing the user type.
     * @return The corresponding UserType, or null if no match is found.
     */
	public static UserType getUserType(String type) {
		if (type == null)
			return null;
		switch (type) {
		case "Customer":
			return UserType.Customer;
		case "Manager":
			return UserType.Manager;
		case "CEO":
			return UserType.CEO;
		case "Supplier":
			return UserType.Supplier;
		default:
			return null;
		}
	}
    /**
     * Converts a UserType to its corresponding string representation.
     *
     * @param type The UserType to convert.
     * @return The string representation of the UserType.
     */
	public static String getStringType(UserType type) {
		if (type.equals(Customer))
			return "Customer";
		if (type.equals(Manager))
			return "Manager";
		if(type.equals(Supplier))
			return "Supplier";
		return "CEO";
	}
}
