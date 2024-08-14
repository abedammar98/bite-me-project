package logic;
/**
 * The ClientInfo class represents the information of a client, including their name, address, and status.
 */
public class ClientInfo {
	private String name;
	private String address;
	private String Status;
	   /**
     * Constructs a new ClientInfo object with the specified name, address, and status.
     *
     * @param name    The name of the client.
     * @param address The address of the client.
     * @param status  The status of the client.
     */
	public ClientInfo(String name, String address, String status) {
		this.name = name;
		this.address = address;
		Status = status;
	}
    /**
     * Gets the name of the client.
     *
     * @return The client's name.
     */
	public String getName() {
		return name;
	}
    /**
     * Sets the name of the client.
     *
     * @param name The new name of the client.
     */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * Gets the address of the client.
     *
     * @return The client's address.
     */
	public String getAddress() {
		return address;
	}

    /**
     * Sets the address of the client.
     *
     * @param address The new address of the client.
     */
	public void setAddress(String address) {
		this.address = address;
	}

    /**
     * Gets the status of the client.
     *
     * @return The client's status.
     */
	public String getStatus() {
		return Status;
	}
    /**
     * Sets the status of the client.
     *
     * @param status The new status of the client.
     */
	public void setStatus(String status) {
		Status = status;
	}
}
