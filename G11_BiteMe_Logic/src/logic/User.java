package logic;

import java.io.Serializable;

/**
 * The User class represents a user in the system, with attributes such as name,
 * ID, contact details, and account information.
 */
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	private String firstName;
	private String lastName;
	private int UserID;
	private String Username;
	private String Password;
	private UserType userType;
	private int branchID;
	private String phone;
	private String email;
	private String creditCard;
	private int isLogged;
	private int amountOfCopunts;
	private String accountType;

	/**
	 * Constructs a User object with the specified details.
	 *
	 * @param userID          The unique ID of the user.
	 * @param firstName       The first name of the user.
	 * @param lastName        The last name of the user.
	 * @param username        The username of the user.
	 * @param password        The password of the user.
	 * @param userType        The type of the user.
	 * @param branchID        The ID of the branch associated with the user.
	 * @param phone           The phone number of the user.
	 * @param email           The email address of the user.
	 * @param creditCard      The credit card number of the user.
	 * @param isLogged        The login status of the user.
	 * @param amountOfCopunts The amount of copunts the user has.
	 * @param accountType     The account type of the user.
	 */
	public User(int userID, String firstName, String lastName, String username, String password, UserType userType,
			int branchID, String phone, String email, String creditCard, int isLogged, int amountOfCopunts,
			String accountType) {
		this.firstName = firstName;
		this.lastName = lastName;
		UserID = userID;
		Username = username;
		Password = password;
		this.userType = userType;
		this.branchID = branchID;
		this.phone = phone;
		this.email = email;
		this.creditCard = creditCard;
		this.isLogged = isLogged;
		this.amountOfCopunts = amountOfCopunts;
		this.accountType = accountType;
	}

	// Getters and Setters with JavaDoc
	/**
	 * Gets the account type of the user.
	 *
	 * @return The account type of the user.
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * Sets the account type of the user.
	 *
	 * @param accountType The new account type of the user.
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * Gets the amount of copunts the user has.
	 *
	 * @return The amount of copunts.
	 */
	public int getAmountOfCopunts() {
		return amountOfCopunts;
	}

	/**
	 * Sets the amount of copunts the user has.
	 *
	 * @param amountOfCopunts The new amount of copunts.
	 */
	public void setAmountOfCopunts(int amountOfCopunts) {
		this.amountOfCopunts = amountOfCopunts;
	}

	/**
	 * Gets the login status of the user.
	 *
	 * @return The login status.
	 */
	public int getIsLogged() {
		return isLogged;
	}

	/**
	 * Sets the login status of the user.
	 *
	 * @param isLogged The new login status.
	 */
	public void setIsLogged(int isLogged) {
		this.isLogged = isLogged;
	}

	/**
	 * Gets the first name of the user.
	 *
	 * @return The first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the user.
	 *
	 * @param firstName The new first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name of the user.
	 *
	 * @return The last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the user.
	 *
	 * @param lastName The new last name.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the user ID.
	 *
	 * @return The user ID.
	 */
	public int getUserID() {
		return UserID;
	}

	/**
	 * Sets the user ID.
	 *
	 * @param userID The new user ID.
	 */
	public void setUserID(int userID) {
		UserID = userID;
	}

	/**
	 * Gets the username of the user.
	 *
	 * @return The username.
	 */
	public String getUsername() {
		return Username;
	}

	/**
	 * Sets the username of the user.
	 *
	 * @param username The new username.
	 */
	public void setUsername(String username) {
		Username = username;
	}

	/**
	 * Gets the password of the user.
	 *
	 * @return The password.
	 */
	public String getPassword() {
		return Password;
	}

	/**
	 * Sets the password of the user.
	 *
	 * @param password The new password.
	 */
	public void setPassword(String password) {
		Password = password;
	}

	/**
	 * Gets the user type.
	 *
	 * @return The user type.
	 */
	public UserType getUserType() {
		return userType;
	}

	/**
	 * Sets the user type.
	 *
	 * @param userType The new user type.
	 */
	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	/**
	 * Gets the branch ID.
	 *
	 * @return The branch ID.
	 */
	public int getBranchID() {
		return branchID;
	}

	/**
	 * Sets the branch ID.
	 *
	 * @param branchID The new branch ID.
	 */
	public void setBranchID(int branchID) {
		this.branchID = branchID;
	}

	/**
	 * Gets the phone number of the user.
	 *
	 * @return The phone number.
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the phone number of the user.
	 *
	 * @param phone The new phone number.
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the email address of the user.
	 *
	 * @return The email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email address of the user.
	 *
	 * @param email The new email address.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the credit card number of the user.
	 *
	 * @return The credit card number.
	 */
	public String getCreditCard() {
		return creditCard;
	}

	/**
	 * Sets the credit card number of the user.
	 *
	 * @param creditCard The new credit card number.
	 */
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

}
