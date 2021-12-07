import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class <code>Receiver</code>, as its name
 * implies, is only responsible for receiving messages.
 * The <code>Runnable</code> interface is implemented,
 * the method of `run` loops forever. Once a message
 * is received, it will notify the <code>Councillor</code>,
 * which resembles the Observer design pattern.
 *
 * @see Councillor
 */
public class Receiver implements Runnable {
    private final Councillor councillor;
    private ServerSocket serverSocket;

    /**
     * Constructs the receiver with the given
     * <code>Councillor</code> instance.
     *
     * @param councillor councillor who will be notified
     *                   when the message is received
     */
    public Receiver(final Councillor councillor) {
        this.councillor = councillor;
        int port = Setting.getPortByName(councillor.getName());
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(councillor.getName() + " Listens On: " + Setting.LOCAL_HOST + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte[] bytes = new byte[1024];
        try {
            while (true) {
                Socket connSocket = serverSocket.accept();
                InputStream is = connSocket.getInputStream();
                int nbytes = is.read(bytes);
                if (nbytes == -1) {
                    continue;
                }

                Message message = Setting.parseMessage(bytes, nbytes);
                if (message instanceof EOF) {
                    System.out.println(">>> " + councillor.getName() + " Gets Offline.");
                    break;
                } else {
                    councillor.receive(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("terminate");
    }
}
