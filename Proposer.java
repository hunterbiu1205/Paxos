import java.util.HashMap;
import java.util.Map;

/**
 * This class <code>Proposer</code> plays the
 * proposer's role. The fields only include
 * proposer's name and current proposal's ID.
 */
public class Proposer extends Councillor {
    private int propId;
    private int rcvCount;
    private boolean offline;
    private Status status;
    private String propValue;
    private final String name;
    private final Map<Status, Integer> statusCount;

    /**
     * Construct the proposer with the given parameters.
     *
     * @param name proposer's name
     */
    public Proposer(String name) {
        this(Setting.getUniqueId(), name);
    }

    /**
     * Construct the proposer with the given parameters.
     *
     * @param propId  proposal ID
     * @param name proposer's name
     */
    public Proposer(int propId, String name) {
        offline = false;
        status = Status.PREPARE;
        this.propId = propId;
        this.name = name;
        statusCount = new HashMap<>();
        propValue = getName() + " Becomes President !!! ";
    }

    /**
     * Set offline for this proposer through
     * sending a EOF packet to its server to
     * terminate the forever loop server thread.
     *
     * @param offline true for offline
     */
    public void setOffline(boolean offline) {
        this.offline = offline;
        int port = Setting.getPortByName(getName());
        Sender.send(Setting.LOCAL_HOST, port, new EOF());
    }

    /**
     * Get proposer's name.
     *
     * @return proposer's name
     */
    public String getName() {
        return name;
    }

    public String getValue() {
        return propValue;
    }

    /**
     * Get proposal ID.
     *
     * @return proposal ID
     */
    public int getProposalID() {
        return propId;
    }

    /**
     * Make a propose to the acceptors.
     */
    public void propose() {
        rcvCount = 0;
        statusCount.clear();
        Request request = new Request(propValue, status, this);
        for (int i = 0; i < Setting.NO_ACCEPTORS; ++i) {
            Sender.send(Setting.LOCAL_HOST, Setting.PORTS[i + Setting.NO_PROPOSER], request);
            System.out.printf(getName() +
                            " sending proposal to %s: {Proposal Number = %d, Proposal Value = %s, Status = %s}\n",
                            Setting.NAMES[i + Setting.NO_PROPOSER], propId, propValue, status.getDescription());
        }
    }

    public void receive(Message message) {
        // ignore the message if offline
        if (offline) {
            return;
        }
        rcvCount++;
        Response response = (Response) message;
        Status rcvStatus = response.getStatus();

        // check receive status
        if (rcvStatus == Status.PREPARE_OK) {
            receivePrepareOK();
        } else if (rcvStatus == Status.NACK) {
            receiveNACK();
        } else if (rcvStatus == Status.ACCEPT_OK) {
            receiveAcceptOK();
        } else if (rcvStatus == Status.ACCEPT_REJECT) {
            receiveAcceptReject();
        } else if (rcvStatus == Status.DECIDE_OK) {
            propValue = response.getCouncillor().getValue();
            System.out.println(">>> Present is " + propValue);
        }
    }

    // when received PREPARE_OK
    private void receivePrepareOK() {
        int count = statusCount.getOrDefault(Status.PREPARE_OK, 0) + 1;
        // if received PREPARE_OK from the majority
        // 1. change status to ACCEPT_REQUEST and send proposal to acceptors again
        // 2. clear the status count since new proposal is sent
        // 3. Proposal ID should not change, and also the proposal value for this situation
        if (count > Setting.NO_ACCEPTORS / 2 && rcvCount == Setting.NO_ACCEPTORS) {
            status = Status.ACCEPT_REQUEST;
            propose();
        } else {
            statusCount.put(Status.PREPARE_OK, count);
        }
    }

    // when received NACK
    private void receiveNACK() {
        int count = statusCount.getOrDefault(Status.NACK, 0) + 1;
        // if received NACK from the majority
        // 1. need to update the proposal ID and send again
        // 2. update the status
        if (count > Setting.NO_ACCEPTORS / 2 && rcvCount == Setting.NO_ACCEPTORS) {
            status = Status.PREPARE;
            propId = Setting.getUniqueId();
            propose();
        } else {
            statusCount.put(Status.NACK, count);
        }
    }

    // when received ACCEPT_OK
    private void receiveAcceptOK() {
        int count = statusCount.getOrDefault(Status.ACCEPT_OK, 0) + 1;
        // if received ACCEPT_OK from the majority
        // 1. need to send DECIDE request to the acceptors
        // 2. need to update the status
        if (count > Setting.NO_ACCEPTORS / 2 && rcvCount == Setting.NO_ACCEPTORS) {
            status = Status.DECIDE;
            propose();
        } else {
            statusCount.put(Status.ACCEPT_OK, count);
        }
    }

    // when received ACCEPT_REJECT
    private void receiveAcceptReject() {
        int count = statusCount.getOrDefault(Status.ACCEPT_REJECT, 0) + 1;
        // if received ACCEPT_REJECT from the majority
        // 1. need to update the proposal ID and send again
        // 2. update the status
        if (count > Setting.NO_ACCEPTORS / 2 && rcvCount  == Setting.NO_ACCEPTORS) {
            status = Status.PREPARE;
            propId = Setting.getUniqueId();
            propose();
        } else {
            statusCount.put(Status.ACCEPT_REJECT, count);
        }
    }
}
