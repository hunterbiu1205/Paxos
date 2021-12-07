/**
 * This class <code>Status</code> represents all
 * the status including both the acceptor and the
 * proposer.
 */
public enum Status {
    PREPARE("Prepare"),
    PREPARE_OK("Prepare OK"),
    NACK("NACK"),
    ACCEPT_REQUEST("Accept Request"),
    ACCEPT_OK("Accept OK"),
    ACCEPT_REJECT("Accept Reject"),
    DECIDE("Decide"),
    DECIDE_OK("Decide OK");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    /**
     * Get status description.
     *
     * @return a string representation of status description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Parse the status by name.
     *
     * @param status status name
     */
    public static Status parseStatus(String status) {
        for (Status stat : Status.values()) {
            if (stat.getDescription().equalsIgnoreCase(status)) {
                return stat;
            }
        }

        return null;
    }
}
