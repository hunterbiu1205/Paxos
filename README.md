# Paxos-
The class TestAll.java is employed to test the functionality of the PAXOS implementation based on the command line arguments.
The command line arguments should comply to following requirements.

Compile all java file first you can use: javac *.java

1. Running without any set: java TestAll
In default case when no arguments are provided, only 1 proposer will be emulated with 6 acceptors. Hence the acceptors will accept what the proposers propose.

2. Running with set: java TestAll [-p number] [-t number] [-o number] Space between option and number

When -p option is set which is short for the number of proposers and the maximum number of proposers is 3 which are M1 to M3. Case with the given number of proposers with 6 acceptors will be simulated.

When the -t option is set, it means the response time from the acceptor. A random time delay between [0, number] exclusively will be set for each acceptor. And the maximum time delay is 10. When this option is not set, the default behaviour is immediate response from the acceptor.

When the -o option is set, it means the number of off lines. The number should be between [0, 2]. Default case is no offline.
[-o 2] means, both M2 and M3 will propose and then go offline.
