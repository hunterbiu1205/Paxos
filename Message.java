/**
 * This class <code>Message</code> is an abstract class,
 * which represents a generalized message, which could be
 * categorized as request or response instance.
 *
 * @see Request
 * @see Response
 */
public abstract class Message {
    /**
     * This is an enumeration of message types.
     */
    public enum Type {
        /**
         * Request sent by the proposer.
         */
        REQUEST,
        /**
         * Response sent by the acceptor.
         */
        RESPONSE,
        /**
         * End of file.
         */
        EOF
    }

    /**
     * Get an instance of <code>Councillor</code>.
     *
     * @return an instance of <code>Councillor</code>
     */
    public abstract Councillor getCouncillor();

    /**
     * Get message type.
     *
     * @return an instance of <code>Type</code>
     */
    public abstract Type getType();
}
