import java.util.LinkedList;
import java.util.Random;

/**
 * Simulation class
 * token ring protocol simulation
 *
 * Authors: Muhammad Elias, Charlie Ng
 */
public class Simulation {

    /**
     * number of hosts in ring
     */
    private int _numHosts;

    /**
     * max number of packets
     */
    private double  _maxBuffer;

    /**
     * to generate inter-arrival time. λ
     */
    private double _interArrivalRate;

    /**
     * current time
     */
    private double _time;

    /**
     * instance of gel to keep track of events
     */
    private GlobalEventList _globalEventList;

    /**
     * store hosts
     */
    private LinkedList<Host> _hosts;

    /**
     * all packets currently sent around the ring
     */
    private LinkedList<Packet> _frame;

    /**
     * total length of transmitted packets
     */
    private double _throughput;

    /**
     * average packet delay
     */
    private double _packetDelay;

    /**
     * main code
     */
    public void runSimulation(int numHosts, double interArrivalRate, double maxBuffer) {

        initialize(numHosts, interArrivalRate, maxBuffer);

        // steps
        for(int i = 0; i < 100000; i++) {

            // token holder releases transmits all packets, put into a frame
            processDepartureEvent();

            // who has received the frame
            int currentHost = Token.getInstance().getOwner() + 1;

            // how many hosts received the frame
            int hostsTraversed = 0;

            // while we haven't returned to the token holder
            while(hostsTraversed != _numHosts) {

                // loop around if we reached the end
                if(currentHost >= _numHosts) {

                    currentHost = 0;
                }

                // add frame to the host
                processArrivalEvent(currentHost);

                // increment number of hosts visited
                hostsTraversed++;

                // move to the next host
                currentHost++;
            }

            releaseToken();
        }

        outputStatistics();
    }

    /**
     * source host transmits all packets, put them in frame
     */
    private void processDepartureEvent() {

        _frame.clear();

        // add all packets to frame for release
        Host tokenHolder = _hosts.get(Token.getInstance().getOwner());
        while(tokenHolder.packetQueue.size() > 0) {

            Packet p = tokenHolder.packetQueue.remove(0);

            _time += negativeExponentiallyDistributedTime(1);

            _globalEventList.insert(new Event(
                    _time,
                    "departure"
            ));

            _frame.add(p);
        }
        _hosts.set(Token.getInstance().getOwner(), tokenHolder);
    }

    /**
     * process arrival event
     */
    private void processArrivalEvent(int index) {

        // add all packets to next host
        Host h = _hosts.get(index);

        for(Packet p : _frame) {

            h.receivedPackets.add(p);

            _throughput += p.length;

            _time += negativeExponentiallyDistributedTime(_interArrivalRate);

            _globalEventList.insert(new Event(
                    _time,
                    "arrival")
            );

            _packetDelay += 10;
        }
        _hosts.set(index, h);
    }

    /**
     * initialize all variables
     */
    private void initialize(int numHosts, double interArrivalRate, double maxBuffer) {

        _time = 0;
        _throughput = 0;
        _packetDelay = 0;
        _numHosts = numHosts;
        _maxBuffer = maxBuffer;
        _interArrivalRate = interArrivalRate;

        _hosts = new LinkedList<Host>();
        _frame = new LinkedList<Packet>();
        _globalEventList = new GlobalEventList();

        // create hosts with addresses 1 - numHosts
        for(int i = 0; i < _numHosts; i++) {

            _hosts.add(new Host(i));
        }

        // create a bunch of packets randomly and send to hosts
        for(int i = 0; i < 1000; i++) {

            Packet p = new Packet(_numHosts, generateTokenHolder());
            _hosts.get(p.hostAddress).packetQueue.add(p);

            _time += negativeExponentiallyDistributedTime(1);

            _globalEventList.insert(new Event(
                    _time,
                    "arrival"
            ));

            _packetDelay += 10;
        }

        // give token to a host initially
        Token.getInstance().setOwner(generateTokenHolder());
        _hosts.get(Token.getInstance().getOwner()).hasToken = true;
    }

    /**
     * give token to next host
     */
    private void releaseToken() {

        _hosts.get(Token.getInstance().getOwner()).hasToken = false;

        int nextTokenHost = Token.getInstance().getOwner() + 1;
        if(nextTokenHost > _numHosts - 1) {

            nextTokenHost = 0;
        }

        Token.getInstance().setOwner(nextTokenHost);
        _hosts.get(nextTokenHost).hasToken = true;
    }

    /**
     * Collecting Statistics
     */
    private void outputStatistics() {

        System.out.println(
                "λ: " + _interArrivalRate +
                "       " + "Time: " + _time +
                "       " + "Throughput: " + _throughput/_time +
                "       " + "Packet Delay: " + _packetDelay/_time + "\n");
    }

    /**
     * random variable following negative exponential distribution
     */
    private double negativeExponentiallyDistributedTime(double rate) {

        double u;
        Random gen = new Random();
        u = gen.nextDouble();

        return(((-1) / rate) * Math.log(1 - u));
    }

    /**
     * generate address to give host token
     */
    private int generateTokenHolder() {

        return (new Random().nextInt(_numHosts));
    }
}
