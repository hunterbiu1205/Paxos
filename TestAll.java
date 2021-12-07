/**
 * This class <code>TestAll</code> is employed
 * to test the functionality of the PAXOS implementation
 * based on the command line arguments. The command line
 * arguments should comply to following requirements.
 *
 *  java TestAll [-p number] [-t number] [-o number]
 *
 * In default case when no arguments are provided, only 1
 * proposer will be emulated with 6 acceptors. Hence the
 * acceptors will accept what the proposers propose.
 *
 * When -p option is set which is short for the number of
 * proposers and the maximum number of proposers is 3. Case
 * with the given number of proposers with 6 acceptors will
 * be simulated.
 *
 * When the -t option is set, it means the response time from
 * the acceptor. A random time delay between [0, number) exclusively
 * will be set for each acceptor. And the maximum time delay is 10.
 * When this option is not set, the default behavior is immediate
 * response from the acceptor.
 *
 * When the -o option is set, it means the number of off lines. The
 * number should be between [0, 2]. Default case is no off lines.
 */
public class TestAll {
    private static void testOneProposer() {
        System.out.println(">>> Test 1 Proposer and 6 Acceptors <<<");
        System.out.println(">>> Acceptor Response Delay: 0 Secs <<<");
        for (int i = 0; i < Setting.NO_ACCEPTORS; ++i) {
            Councillor acceptor = new Acceptor(Setting.NAMES[i + Setting.NO_PROPOSER]);
            new AcceptorProxy(acceptor);
        }
        Councillor proposer = new Proposer(Setting.NAMES[0]);
        ProposerProxy proxy = new ProposerProxy(proposer);
        proxy.propose();
    }

    private static void testOneProposerWithDelay(int delay) {
        System.out.println(">>> Test 1 Proposer and 6 Acceptors <<<");
        System.out.println(">>> Acceptor Response Delay: " + delay + " Secs <<<");
        for (int i = 0; i < Setting.NO_ACCEPTORS; ++i) {
            Acceptor acceptor = new Acceptor(Setting.NAMES[i + Setting.NO_PROPOSER]);
            acceptor.setTimeDelay(delay);
            new AcceptorProxy(acceptor);
        }
        Councillor proposer = new Proposer(Setting.NAMES[0]);
        ProposerProxy proxy = new ProposerProxy(proposer);
        proxy.propose();
    }

    private static void testMultipleProposers(int p, int delay, int offline) {
        System.out.println(">>> Test " + p + " Proposers and 6 Acceptors <<<");
        System.out.println(">>> Acceptor Response Delay: " + delay + " Secs <<<");
        System.out.print(">>> Offline Member: ");
        Proposer[] proposers = new Proposer[p];
        ProposerProxy[] proxy = new ProposerProxy[p];
        for (int i = 0; i < p; ++i) {
            proposers[i] = new Proposer(Setting.NAMES[i]);
            proxy[i] = new ProposerProxy(proposers[i]);
        }
        if (offline > 0) {
            for (int i = 0; i < offline; ++i) {
                System.out.print(Setting.NAMES[i + 1]);
                proposers[i + 1].setOffline(true);
            }
            System.out.println();
        } else {
            System.out.println(" Empty");
        }

        for (int i = 0; i < Setting.NO_ACCEPTORS; ++i) {
            Acceptor acceptor = new Acceptor(Setting.NAMES[i + Setting.NO_PROPOSER]);
            acceptor.setTimeDelay(delay);
            new AcceptorProxy(acceptor);
        }

        for (int i = 0; i < p; ++i) {
            proxy[i].propose();
        }
    }

    public static void main(String[] args) {
        int p = 1;
        int t = 0;
        int o = 0;
        if (args.length == 0) {
            testOneProposer();
        } else if (args.length == 2 || args.length == 4 || args.length == 6) {
            for (int i = 0; i < args.length; i += 2) {
                if (args[i].equalsIgnoreCase("-p")) {
                    p = Integer.parseInt(args[i + 1]);
                    if (p < 1 || p > Setting.NO_PROPOSER) {
                        System.out.println("Invalid number of proposers: " + p);
                        break;
                    }
                } else if (args[i].equalsIgnoreCase("-t")) {
                    t = Integer.parseInt(args[i + 1]);
                    if (t < 1 || t > Setting.MAX_DELAY) {
                        System.out.println("Invalid time delay: " + t);
                        break;
                    }
                } else if (args[i].equalsIgnoreCase("-o")) {
                    o = Integer.parseInt(args[i + 1]);
                    if (o < 0 || o >= Setting.NO_PROPOSER) {
                        System.out.println("Invalid number of offlines: " + o);
                        break;
                    }
                }
            }
            if (o >= p) {
                System.out.println("Invalid number of offlines: offlines must be less than proposers");
            } else {
                if (p == 1 && t > 0) {
                    testOneProposerWithDelay(t);
                } else {
                    testMultipleProposers(p, t, o);
                }
            }
        } else {
            System.out.println("Usage java TestAll [-p number] [-t number] [-o number]");
        }
    }
}
