public class AcceptorProxy extends Councillor {
    private final Councillor acceptor;
    protected Receiver receiver;

    public AcceptorProxy(Councillor acceptor) {
        this.acceptor = acceptor;
        receiver = new Receiver(this);
        Thread thread = new Thread(receiver);
        // thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void receive(Message message) {
        acceptor.receive(message);
    }

    @Override
    public String getName() {
        return acceptor.getName();
    }

    @Override
    public String getValue() {
        return acceptor.getValue();
    }
}
