import java.util.Observer;

/**
 * This abstract class <code>Councillor</code>
 * resembles a general councillor, which
 * will be implemented by <code>Acceptor</code>
 * and <code>Proposer</code>.
 *
 * @see Acceptor
 * @see Proposer
 */
public abstract class Councillor {
    /**
     * Need to take action when receiving message
     * either from acceptor or proposer.
     *
     * @param message message from acceptor or proposer
     */
    public abstract void receive(Message message);

    /**
     * Get councillor's name.
     *
     * @return councillor's name
     */
    public abstract String getName();

    /**
     * Get proposal value.
     *
     * @return a string representation of proposal value
     */
    public abstract String getValue();
}
