import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * This class <code>Sender</code>, as its name
 * implies, is only responsible for sending messages.
 *
 * @see Councillor
 */
public class Sender {
    /**
     * Construct a server socket, listens and binds on
     * the given host and port.
     *
     * @param host a string representing either a hostname
     *             in internet domain notation like 'daring.cwi.nl'
     *             or an IPv4 address like '100.50.200.5'
     * @param serverPort an integer port for server port
     * @param message message needs to be sent
     */
    public static void send(String host, int serverPort, Message message) {
        int retry = 0;
        boolean succeed = false;
        while (retry < 5 && !succeed) {
            try {
                Socket socket = new Socket(host, serverPort);
                OutputStream os = socket.getOutputStream();
                os.write(message.toString().getBytes());
                os.flush();
                os.close();
                socket.close();
                succeed = true;
            } catch (IOException e) {
                retry++;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }
}
