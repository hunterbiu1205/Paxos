import java.util.concurrent.TimeUnit;

/**
 * This class <code>Acceptor</code> plays the
 * acceptor's role.
 */
public class Acceptor extends Councillor {
    private boolean accepted;
    private boolean decideOK;
    private int acceptedPropNo;
    private int timeDelay;
    private String acceptedValue;
    private final String name;

    /**
     * Construct the acceptor instance with the given parameters.
     *
     * @param accepted accepted proposal previously
     * @param acceptedPropNo accepted proposal number previously
     * @param acceptedValue accepted proposal value previously
     * @param name acceptor's name
     */
    public Acceptor(boolean accepted, int acceptedPropNo, String acceptedValue, String name) {
        timeDelay = -1;
        this.accepted = accepted;
        this.acceptedPropNo = acceptedPropNo;
        this.acceptedValue = acceptedValue;
        this.name = name;
        decideOK = false;
    }

    /**
     * Construct the acceptor instance with the given parameters.
     *
     * @param name acceptor's name
     */
    public Acceptor(String name) {
        this(false, -1, "", name);
    }

    /**
     * Set time delay for acceptor and
     * random time delay between [0, timeDelay)
     * will be set when response to the proposer.
     *
     * @param timeDelay time delay in seconds
     */
    public void setTimeDelay(int timeDelay) {
        if (timeDelay > 0) {
            this.timeDelay = timeDelay;
        }
    }

    public void receive(Message message) {
        Response response;
        Request request = (Request) message;
        Status status = request.getStatus();
        Proposer proposer = (Proposer) request.getCouncillor();
        StringBuilder sb = new StringBuilder();
        int port = Setting.getPortByName(proposer.getName());
        sb.append(String.format("%s reply to %s: ",
                name,  proposer.getName()));
        if (status == Status.PREPARE) {
            response = receivePrepare(request, sb);
        } else if (status == Status.ACCEPT_REQUEST) {
            response = receiveAcceptRequest(request, sb);
        } else if (status == Status.DECIDE) {
            response = receiveDecide(request, sb);
        } else {
            throw new RuntimeException("Unknown message format: " + message);
        }

        if (timeDelay > 0) {
            int rand = (int) (Math.random() * timeDelay);
            try {
                TimeUnit.SECONDS.sleep(rand);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(sb);
        Sender.send(Setting.LOCAL_HOST, port, response);
    }

    // when receiving PREPARE
    private Response receivePrepare(Request request, StringBuilder sb) {
        // if it's not accepted before.
        // 1. update the proposal ID
        // 2. reply with PREPARE_OK
        Proposer proposer = (Proposer) request.getCouncillor();
        String propValue = proposer.getValue();
        int propID = proposer.getProposalID();

        // If n > any previous proposal number received from any proposer by the acceptor
        // must return a promise to ignore all future proposals having number < n
        if (!isAccepted() || propID > acceptedPropNo) {
            accepted = true;
            acceptedPropNo = propID;
            acceptedValue = propValue;
            sb.append(String.format(
                    "{Proposal Number = %d, Proposal Value = %s, Status = %s, Accepted Before = False}\n",
                    acceptedPropNo, acceptedValue, Status.PREPARE_OK.getDescription()));
            return new Response(Status.PREPARE_OK, this);
        } else {
            sb.append(String.format(
                    "{Proposal Number = %d, Proposal Value = %s, Status = %s, Accepted Before = True}\n",
                    acceptedPropNo, acceptedValue, Status.PREPARE_OK.getDescription()));
            return new Response(Status.NACK, this);
        }
    }

    // when receiving ACCEPT_REQUEST
    private Response receiveAcceptRequest(Request request, StringBuilder sb) {
        Proposer proposer = (Proposer) request.getCouncillor();
        int propID = proposer.getProposalID();
        if (propID >= acceptedPropNo) {
            sb.append(String.format(
                    "{Proposal Number = %d, Proposal Value = %s, Status = %s}\n",
                    acceptedPropNo, acceptedValue, Status.ACCEPT_OK.getDescription()));
            return new Response(Status.ACCEPT_OK, this);
        } else {
            sb.append(String.format(
                    "{Proposal Number = %d, Proposal Value = %s, Status = %s}\n",
                    acceptedPropNo, acceptedValue, Status.ACCEPT_REJECT.getDescription()));
            return new Response(Status.ACCEPT_REJECT, this);
        }
    }

    // when receiving DECIDE
    private Response receiveDecide(Request request, StringBuilder sb) {
        Proposer proposer = (Proposer) request.getCouncillor();
        if (!decideOK) {
            decideOK = true;
            acceptedValue = proposer.getValue();
        }
        sb.append(String.format(
                "{Proposal Number = %d, Proposal Value = %s, Status = %s}\n",
                acceptedPropNo, acceptedValue, Status.DECIDE_OK.getDescription()));
        return new Response(Status.DECIDE_OK, this);
    }

    /**
     * Get acceptor's name.
     *
     * @return acceptor's name
     */
    public String getName() {
        return name;
    }

    public String getValue() {
        return acceptedValue;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public int getAcceptedPropNo() {
        return acceptedPropNo;
    }

    public String getAcceptedValue() {
        return acceptedValue;
    }
}
