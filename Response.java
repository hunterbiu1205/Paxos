/**
 * This class <code>Request</code> represents the
 * response sent by the acceptor. The response format
 * is shown below.
 * Type: Response
 * From: Acceptor's Name
 * Status: PREPARE_OK | NACK | ACCEPT_OK | ACCEPT_REJECT
 * Accepted: True | False
 * Proposal Number: Number
 * Accepted Value: String
 *
 * @see Status
 */
public class Response extends Message {
    private final Status status;
    private final Acceptor acceptor;

    /**
     * Constructs the response with the given acceptor.
     *
     * @param status acceptor's status
     * @param acceptor a reference to acceptor
     */
    public Response(Status status, Acceptor acceptor) {
        this.status = status;
        this.acceptor = acceptor;
    }

    @Override
    public Councillor getCouncillor() {
        return acceptor;
    }

    @Override
    public Type getType() {
        return Type.RESPONSE;
    }

    @Override
    public String toString() {
        String res = Setting.TYPE + Setting.EQ + getType().name() + Setting.NEW_LINE +
                Setting.FROM + Setting.EQ + acceptor.getName() + Setting.NEW_LINE +
                Setting.STATUS + Setting.EQ + status.getDescription() + Setting.NEW_LINE;
        Acceptor acc = (Acceptor) acceptor;
        res +=  Setting.ACCEPTED + Setting.EQ + acc.isAccepted() + Setting.NEW_LINE +
                Setting.PROPOSAL_NUMBER + Setting.EQ + acc.getAcceptedPropNo() + Setting.NEW_LINE +
                Setting.PROPOSAL_VALUE + Setting.EQ + acc.getAcceptedValue() + Setting.NEW_LINE;

        return res;
    }

    public Status getStatus() {
        return status;
    }
}
