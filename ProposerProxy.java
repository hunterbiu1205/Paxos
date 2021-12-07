public class ProposerProxy extends Councillor {
    private final Councillor proposer;
    protected final Receiver receiver;

    public ProposerProxy(Councillor proposer) {
        this.proposer = proposer;
        receiver = new Receiver(this);
        Thread thread = new Thread(receiver);
        //thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void receive(Message message) {
        proposer.receive(message);
    }

    @Override
    public String getName() {
        return proposer.getName();
    }

    @Override
    public String getValue() {
        return proposer.getValue();
    }

    public void propose() {
        ((Proposer) proposer).propose();
    }
}
