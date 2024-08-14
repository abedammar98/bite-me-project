package logic;
/**
 * Enumeration representing different branches with their associated values.
 */
public enum Branch {
    NORTH(1),
    SOUTH(2),
    CENTER(3);

    private final int value;
    /**
     * Constructor for the Branch enum.
     *
     * @param value The integer value associated with the branch.
     */
    Branch(int value) {
        this.value = value;
    }

    /**
     * Gets the integer value associated with the branch.
     *
     * @return The integer value of the branch.
     */
    public int getValue() {
        return value;
    }

    /**
     * Converts an integer value to its corresponding Branch enum.
     *
     * @param value The integer value representing a branch.
     * @return The corresponding Branch enum.
     * @throws IllegalArgumentException if the value does not correspond to any branch.
     */
    public static Branch fromValue(int value) {
        for (Branch branch : Branch.values()) {
            if (branch.getValue() == value) {
                return branch;
            }
        }
        throw new IllegalArgumentException("Invalid branch value: " + value);
    }
}

