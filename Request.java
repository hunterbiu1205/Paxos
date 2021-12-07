/**
 * This class <code>Request</code> represents the
 * request sent by the proposer. The request format
 * is shown below.
 * Type: Request
 * From: Proposer Name
 * Status: Prepare | Accept Request | Decide
 * Proposal Number: Number
 * Proposal Value: String (If Exists)
 */
public class Request extends Message {
    private final String value;
    private final Status status;
    private final Proposer proposer;

    /**
     * Constructs a request with the given parameters
     * explained below.
     *
     * @param value request value
     * @param status proposer's current phase status, see {@code Status}
     * @param proposer a reference to the proposer
     */
    public Request(String value, Status status, Proposer proposer) {
        this.value = value;
        this.status = status;
        this.proposer = proposer;
    }

    @Override
    public Councillor getCouncillor() {
        return proposer;
    }

    @Override
    public Type getType() {
        return Type.REQUEST;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return Setting.TYPE + Setting.EQ + getType().name() + Setting.NEW_LINE +
                Setting.FROM + Setting.EQ + proposer.getName() + Setting.NEW_LINE +
                Setting.STATUS + Setting.EQ + status.getDescription() + Setting.NEW_LINE +
                Setting.PROPOSAL_NUMBER + Setting.EQ + proposer.getProposalID() + Setting.NEW_LINE +
                Setting.PROPOSAL_VALUE + Setting.EQ + value + Setting.NEW_LINE;
    }
}
