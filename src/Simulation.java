import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Simulation class
 * discrete-event simulation model
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

    public void runSimulation(int numHosts, double interArrivalRate, double maxBuffer) {

        initialize(numHosts, interArrivalRate, maxBuffer);

        // steps
        for(int i = 0; i < 10; i++) {

            Event e = _globalEventList.removeFront();

            if(e == null) {

                e = new Event(
                        _time + negativeExponentiallyDistributedTime(_interArrivalRate),
                        ""
                );
            }

            // token holder releases transmits all packets, put into a frame
            processDepartureEvent(e);

            // who has received the frame
            int currentHost = Token.getInstance().getOwner() + 1;
            if(currentHost >= _numHosts) {

                currentHost = 0;
            }

            // how many hosts received the frame
            int hostsTraversed = 0;

            // while we haven't returned to the token holder
            while(hostsTraversed != _numHosts) {

                // loop around if we reached the end
                if(currentHost >= _numHosts) {

                    currentHost = 0;
                }

                // add frame to the host
                processArrivalEvent(e, currentHost);

                // increment number of hosts visited
                hostsTraversed++;

                // move to the next host
                currentHost++;
            }

            // we've returned to the token holder, give token to someone else
            _hosts.get(Token.getInstance().getOwner()).hasToken = false;
            int nextTokenHost = Token.getInstance().getOwner() + 1;
            if(nextTokenHost > _numHosts - 1) {

                nextTokenHost = 0;
            }
            Token.getInstance().setOwner(nextTokenHost);
            _hosts.get(nextTokenHost).hasToken = true;
        }

        outputStatistics();
    }

    /**
     * source host transmits all packets, put them in frame
     */
    private void processDepartureEvent(Event event) {

        _time = event.getTime();

        _frame.clear();

        // add all packets to frame for release
        Host tokenHolder = _hosts.get(Token.getInstance().getOwner());
        while(tokenHolder.packetQueue.size() > 0) {

            Packet p = tokenHolder.packetQueue.remove(0);

            _frame.add(p);
        }
        _hosts.set(Token.getInstance().getOwner(), tokenHolder);
    }

    /**
     * process arrival event
     */
    private void processArrivalEvent(Event event, int index) {

        _time = event.getTime();

        // add all packets to next host
        Host h = _hosts.get(index);
        for(Packet p : _frame) {

            h.receivedPackets.add(p);

            _throughput += p.length;

            _globalEventList.insert(new Event(
                    _time + negativeExponentiallyDistributedTime(_interArrivalRate),
                    "arrival")
            );
        }
        _hosts.set(index, h);
    }

    /**
     * initialize all variables, insert first event into GEL
     */
    private void initialize(int numHosts, double interArrivalRate, double maxBuffer) {

        _numHosts = numHosts;

        // create hosts with addresses 1 - numHosts
        _hosts = new LinkedList<Host>();
        for(int i = 0; i < _numHosts; i++) {

            _hosts.add(new Host(i));
        }

        // create a bunch of packets randomly and send to hosts
        for(int i = 0; i < 100; i++) {

            Packet p = new Packet(_numHosts, generateTokenHolder());
            _hosts.get(p.hostAddress).packetQueue.add(p);
        }

        // max num packets
        _maxBuffer = maxBuffer;

        // init data structures
        _time = 0;
        _throughput = 0;
        _frame = new LinkedList<Packet>();
        _globalEventList = new GlobalEventList();

        // init packet arrival and service times
        _interArrivalRate = interArrivalRate;

        // give token to a host initially
        Token.getInstance().setOwner(generateTokenHolder());
        _hosts.get(Token.getInstance().getOwner()).hasToken = true;

        // create start event
        _globalEventList.insert(new Event(0, ""));
    }

    /**
     * Collecting Statistics
     */
    private void outputStatistics() {

        System.out.println("throughput: " + _throughput);
        System.out.println("time: " + _time);
        System.out.println("throughput/time: " + _throughput/_time);

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
