/**
 * This class <code>EOF</code> is employed only to terminate
 * the receiver server. The <code>Receiver</code> implements
 * the <code>Runnable</code> interface and continue to receive
 * any message from the acceptor and proposer. Sending a EOF
 * message to end the server to emulate the M2 and M3 with no
 * response cases.
 *
 * @see Receiver
 */
public class EOF extends Message {
    @Override
    public Type getType() {
        return Type.EOF;
    }

    @Override
    public Councillor getCouncillor() {
        return null;
    }

    @Override
    public String toString() {
        return Setting.EOF;
    }
}
