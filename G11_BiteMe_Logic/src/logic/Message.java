package logic;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * The Message class represents a message sent between the client and server.
 * It contains a type indicating the purpose of the message and an object containing data relevant to the message.
 */
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MessageType type;
	ArrayList<Object> object;
    /**
     * Constructs a new Message object with the specified type and data.
     *
     * @param type   The type of the message.
     * @param object The data contained in the message.
     */
	public Message(MessageType type, ArrayList<Object> object) {
		this.type = type;
		this.object = object;
	}
    /**
     * Gets the type of the message.
     *
     * @return The message type.
     */
	public MessageType getType() {
		return type;
	}
    /**
     * Sets the type of the message.
     *
     * @param type The new message type.
     */
	public void setType(MessageType type) {
		this.type = type;
	}
    /**
     * Gets the data contained in the message.
     *
     * @return The data object.
     */
	public Object getObject() {
		return object;
	}

    /**
     * Sets the data contained in the message.
     *
     * @param object The new data object.
     */
	public void setObject(ArrayList<Object> object) {
		this.object = object;
	}

}
