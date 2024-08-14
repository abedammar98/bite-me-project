package logic;

/**
 * The CertifiedWorker class extends the User class and represents a worker
 * certified to work at a specific restaurant.
 */
public class CertifiedWorker extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int resturantID;

	/**
	 * Constructs a new CertifiedWorker with the specified details.
	 *
	 * @param userID          The user's unique ID.
	 * @param firstName       The user's first name.
	 * @param lastName        The user's last name.
	 * @param username        The user's username.
	 * @param password        The user's password.
	 * @param userType        The user's type (e.g., Customer, Supplier, etc.).
	 * @param branchID        The branch ID associated with the user.
	 * @param phone           The user's phone number.
	 * @param email           The user's email address.
	 * @param creditCard      The user's credit card information.
	 * @param isLogged        Indicates whether the user is logged in.
	 * @param amountOfCopunts The number of coupons the user has.
	 * @param resturantID     The ID of the restaurant the worker is certified to
	 *                        work at.
	 * @param accountType     The type of account the user has.
	 */
	public CertifiedWorker(int userID, String firstName, String lastName, String username, String password,
			UserType userType, int branchID, String phone, String email, String creditCard, int isLogged,
			int amountOfCopunts, int resturantID, String accountType) {
		super(userID, firstName, lastName, username, password, userType, branchID, phone, email, creditCard, isLogged,
				amountOfCopunts, accountType);

		this.resturantID = resturantID;
	}
    /**
     * Gets the restaurant ID associated with this certified worker.
     *
     * @return The restaurant ID.
     */
	public int getResturantID() {
		return resturantID;
	}
    /**
     * Sets the restaurant ID associated with this certified worker.
     *
     * @param resturantID The new restaurant ID.
     */
	public void setResturantID(int resturantID) {
		this.resturantID = resturantID;
	}

}
