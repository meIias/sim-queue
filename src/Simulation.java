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
     * to generate packet transmission/service time. μ
     */
    private double _transmissionRate;

    /**
     * current time
     */
    private double _time;

    /**
     * number of packets in queue
     */
    private int _length;

    /**
     * What fraction of the time is the server busy
     */
    private double _serverUtilization;

    /**
     * mean number of packets in the queue as seen by a new arriving packet
     */
    private double _meanQueueLength;

    /**
     * Number of packets dropped with different λ values
     */
    private int _numPacketsDropped;

    /**
     * instance of gel to keep track of events
     */
    private GlobalEventList _globalEventList;

    /**
     * fifo queue for packets
     */
    private LinkedList<Packet> _packetQueue;

    /**
     * store hosts
     */
    private LinkedList<Host> _hosts;

    public void runSimulation(int numHosts, double interArrivalRate, double transmissionRate, double maxBuffer) {

        initialize(numHosts, interArrivalRate, transmissionRate, maxBuffer);

        /**
         * 1. get the first event from the GEL;
         * 2. If the event is an arrival then process-arrival-event;
         * 3. Otherwise it must be a departure event and hence
         * process-service-completion;
         */
        for(int i = 0; i < 100000; i++) {

            Event event = _globalEventList.removeFront();

            // no more events
            if(event == null) {

                break;
            }

            if(event.getType().equals("arrival")) {

                processArrivalEvent(event);
            }
            else {

                processDepartureEvent(event);
            }
        }

        outputStatistics();
    }

    private void processDepartureEvent(Event event) {

        //set current time to event time
        double oldTime = _time;
        _time = event.getTime();

        // update stats
        _meanQueueLength += (_length * (_time - oldTime));

        // decrement the length since this is a packet departure
        _length--;

        // if queue
        if(_length > 0) {

            // dequeue the first packet
            Packet curPacket = _packetQueue.remove(0);

            // new departure time is packet service time plus transmission time
            double departureEventTime = _time + curPacket.getServiceTime();

            // insert new departure event
            _globalEventList.insert(new Event(departureEventTime, "departure"));
        }
        else if(_length == 0) {

            // todo RELEASE TOKEN
        }
        else {

            System.out.println("**** ERROR: queue length is negative ****");
        }
    }

    /**
     * process arrival event
     *
     * @param event event at front of GEL
     */
    private void processArrivalEvent(Event event) {

        double oldTime = _time;
        _time = event.getTime();

        if(oldTime != 0) {

            _meanQueueLength += (_length * (_time - oldTime));
        }

        // find time of next arrival
        double nextArrivalTime = _time + negativeExponentiallyDistributedTime(_interArrivalRate);

        // create a new packet with its service time
        double newPacketServiceTime = negativeExponentiallyDistributedTime(_transmissionRate);
        Packet newPacket = new Packet(newPacketServiceTime);

        // create and insert a new arrival time
        _globalEventList.insert(new Event(nextArrivalTime, "arrival"));

        // process the arrive event
        if(_length == 0) {

            // server is free, immediately schedule for transmission

            // new departure time is packet service time plus current time
            double departureEventTime = newPacket.getServiceTime() + _time;

            // insert new departure event
            _globalEventList.insert(new Event(departureEventTime, "departure"));

            _length++;

            // update stats
            _serverUtilization += newPacketServiceTime;
        }
        else if(_length > 0) {

            // server is busy, queue

            if(_length < _maxBuffer) {

                // add packet to queue
                _packetQueue.push(newPacket);
                _length++;

                // update stats
                _serverUtilization += newPacketServiceTime;
            }
            else {

                // drop packet
                _numPacketsDropped++;
            }
        }
    }

    /**
     * initialize all variables, insert first event into GEL
     *
     * @param interArrivalRate  λ
     * @param transmissionRate  μ
     * @param maxBuffer         max num packets allowed in queue
     */
    private void initialize(int numHosts, double interArrivalRate, double transmissionRate, double maxBuffer) {

        _numHosts = numHosts;

        // give token to a host initially
        Token.getInstance().setOwner(generateTokenHolder());

        // create hosts with addresses 1 - numHosts
        for(int i = 0; i < _numHosts; i++) {

            _hosts.add(new Host(i));
        }

        // max num packets
        _maxBuffer = maxBuffer;

        // init data structures
        _time = 0;
        _length = 0;
        _meanQueueLength = 0;
        _serverUtilization = 0;
        _numPacketsDropped = 0;
        _packetQueue = new LinkedList<Packet>();
        _globalEventList = new GlobalEventList();

        // init packet arrival and service times
        _interArrivalRate = interArrivalRate;
        _transmissionRate = transmissionRate;

        // init gel w/ first event
        double firstEventTime = _time + negativeExponentiallyDistributedTime(_interArrivalRate);
        _globalEventList.insert(new Event(firstEventTime, "arrival"));
    }

    /**
     * Collecting Statistics
     *
     * Mean queue length, server utilization, num packets dropped
     */
    private void outputStatistics() {

        System.out.println("λ: " + _interArrivalRate + "   μ: " + _transmissionRate + "   max_buffer: " + _maxBuffer + "   " + "time: " + _time);

        System.out.println("Server utilization: " + (_serverUtilization / _time));

        System.out.println("Mean queue length: " + (_meanQueueLength / _time));

        System.out.println("Number of packets dropped: " + _numPacketsDropped + "\n");
    }

    /**
     * random variable following negative exponential distribution
     *
     * @param rate  λ or μ
     * @return      random variable
     */
    private double negativeExponentiallyDistributedTime(double rate) {

        double u;
        Random gen = new Random();
        u = gen.nextDouble();

        return(((-1) / rate) * Math.log(1 - u));
    }

    /**
     * generate address to give host token
     * @return new token holder
     */
    private int generateTokenHolder() {

        return (new Random().nextInt(_numHosts));
    }
}
