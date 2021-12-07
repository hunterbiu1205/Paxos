import java.util.Arrays;

/**
 * This class <code>Setting</code> is a util
 * class which provides basic settings for the
 * whole program.
 */
public class Setting {
    /**
     * No of acceptors.
     */
    public static final int NO_ACCEPTORS = 6;

    /**
     * No of proposers.
     */
    public static final int NO_PROPOSER = 3;

    /**
     * Maximum time delay.
     */
    public static final int MAX_DELAY = 10;

    /**
     * A string representation of local host and
     * an alternative option is "0.0.0.0" if multiple
     * ip addresses exist.
     */
    public static final String LOCAL_HOST = "127.0.0.1";

    /**
     * A string representation of New Line.
     */
    public static final String NEW_LINE = "\r\n";

    /**
     * A string representation of separator.
     */
    public static final String SEP = ";";

    /**
     * A string representation of equal sign.
     */
    public static final String EQ = "=";

    /**
     * A string representation of "PROPOSAL NUMBER".
     */
    public static final String PROPOSAL_NUMBER = "PROPOSAL NUMBER";

    /**
     * A string representation of "PROPOSAL VALUE".
     */
    public static final String PROPOSAL_VALUE = "PROPOSAL VALUE";

    /**
     * A string representation of "STATUS".
     */
    public static final String STATUS = "STATUS";

    /**
     * A string representation of "FROM".
     */
    public static final String FROM = "FROM";

    /**
     * A string representation of "EOF".
     */
    public static final String EOF = "EOF";

    /**
     * A string representation of "TYPE".
     */
    public static final String TYPE = "TYPE";

    /**
     * A string representation of "ACCEPTED".
     */
    public static final String ACCEPTED = "ACCEPTED";

    /**
     * A string representation of "TRUE".
     */
    public static final String TRUE = "TRUE";

    /**
     * This array represents all the ports that will
     * be listened on. The first three represent the
     * proposer's ports and the remaining belong to
     * the acceptors.
     */
    public static final int[] PORTS = {
            8001, 8002, 8003, 8004, 8005,
            8006, 8007, 8008, 8009
    };

    /**
     * This array represents all the names for both
     * the proposers and the acceptors. And each name
     * is corresponding to the port number in the
     * PORTS array.
     */
    public static final String[] NAMES = {
            "M1", "M2", "M3", "M4", "M5",
            "M6", "M7", "M8", "M9"
    };

    /**
     * Get the port number according to the name.
     *
     * @param name proposer or acceptor's name
     * @return port number if the name exist in the array
     *         otherwise return -1 if it doesn't exist
     */
    public static int getPortByName(String name) {
        for (int i = 0; i < NAMES.length; ++i) {
            if (NAMES[i].equals(name)) {
                return PORTS[i];
            }
        }

        return -1;
    }

    /**
     * Parse the received message from the acceptor
     * or proposer.
     *
     * @param bytes received data either from the acceptor
     *             or proposer
     * @param nbytes number of bytes valid
     * @return an instance of <code>Message</code>
     */
    public static Message parseMessage(byte[] bytes, int nbytes) {
        byte[] v = Arrays.copyOf(bytes, nbytes);
        String data = new String(v);

        if (EOF.equalsIgnoreCase(data)) {
            return new EOF();
        } else if (data.startsWith(TYPE)) {
            String[] tokens = data.split(NEW_LINE);
            String type = tokens[0].split(EQ)[1];
            String name = tokens[1].split(EQ)[1];
            Status status = Status.parseStatus(tokens[2].split(EQ)[1]);

            if (Message.Type.REQUEST.name().equalsIgnoreCase(type)) {
                int propNo = Integer.parseInt(tokens[3].split(EQ)[1]);
                String propVal = tokens[4].split(EQ)[1];
                return new Request(propVal, status, new Proposer(propNo, name));
            } else {
                boolean accepted = Boolean.parseBoolean(tokens[3].split(EQ)[1]);
                int acceptedPropNo = Integer.parseInt(tokens[4].split(EQ)[1]);
                String acceptedValue = tokens[5].split(EQ)[1];
                return new Response(status, new Acceptor(accepted, acceptedPropNo, acceptedValue, name));
            }
        }

        throw new RuntimeException("Invalid data: " + data);
    }

    /**
     * This unique id will increase by
     * 1 once the <code>Proposal</code>
     * instance is constructed. And this
     * value is also thread safe.
     */
    private static int UNIQUE_ID = 0;

    /**
     * Get proposal id.
     *
     * @return unique proposal id
     */
    public static synchronized int getUniqueId() {
        UNIQUE_ID++;

        return UNIQUE_ID;
    }
}
