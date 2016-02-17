import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.log;

/**
 * Simulation class
 * discrete-event simulation model
 *
 * Authors: Muhammad Elias, Charlie Ng
 */
public class Simulation {

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

    public void runSimulation(double interArrivalRate, double transmissionRate) {

        initialize(interArrivalRate, transmissionRate);

        /**
         * 1. get the first event from the GEL;
         * 2. If the event is an arrival then process-arrival-event;
         * 3. Otherwise it must be a departure event and hence
         * process-service-completion;
         */
        for(int i = 0; i < 100000; i++) {

            Event event = _globalEventList.getFirstEvent();

            if(event.getType().equals("arrival")) {

                processArrivalEvent(event);
            }
            else {

                processDepartureEvent();
            }
        }

        outputStatistics();
    }

    private void processDepartureEvent() {
        //todo charlie
    }

    private void processArrivalEvent(Event event) {

        _time = event.getTime();

        // find time of next arrival
        double nextArrivalTime = _time + negativeExponentiallyDistributedTime(_interArrivalRate);

        // create a new packet with its service time
        double newPacketServiceTime = negativeExponentiallyDistributedTime(_transmissionRate);
        Packet newPacket = new Packet(newPacketServiceTime);
        _packetQueue.push(newPacket);

        // create and insert a new arrival time
        _globalEventList.insert(new Event(nextArrivalTime, "arrival"));
    }

    /**
     * Initialize
     *
     * Initialize all the data structures.
     * Initialize all the counters for maintaining the statistics.
     * Let length denote the number of packets in the queue (including, if any, being transmitted by the server).
     * We will initialize length to be 0.
     * Variable time to denote the current time. We initialize time to 0.
     *
     * Set the service rate and the arrival rate of the packets.
     *
     * Create the first arrival event and then insert it into the GEL.
     * The event time of the first arrival event is obtained by adding a
     * randomly generated inter-arrival time to the current time, which is 0.
     */
    private void initialize(double interArrivalRate, double transmissionRate) {

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
     * Utilization: What fraction of the time is the server busy?
     * To determine this, keep a running count of the time the server is busy
     * When the simulation terminates, the time for which the server is busy
     * divided by the total time will give the mean server utilization.
     *
     * Mean queue length: What is the mean number of packets in the queue as
     * seen by a new arriving packet?
     * As mentioned before, to do this we maintain the sum of the area under the curve and when the simulation terminates,
     * the area divided by the total time will give the mean queue length.
     *
     * Number of packets dropped: What is the total number of packets dropped with different λ values?
     * To determine this, keep a running count of the number of packets dropped.
     * Notice that you have to determine whether the packet needs to be dropped when it arrives at the buffer.
     */
    private void outputStatistics() {
        //todo
    }

    /**
     * The code for generating the random variables
     */
    private double negativeExponentiallyDistributedTime(double rate) {

        double u;
        Random gen = new Random();
        u = gen.nextDouble();

        return((-1 / rate) * log(1 - u));
    }
}
