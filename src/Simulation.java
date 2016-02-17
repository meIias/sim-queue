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

    public void runSimulation(double interArrivalRate, double transmissionRate, double maxBuffer) {

        initialize(interArrivalRate, transmissionRate, maxBuffer);

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

                processDepartureEvent(event);
            }
        }

        outputStatistics();
    }

    private void processDepartureEvent(Event event) {
        //todo charlie
    }

    /**
     * process arrival event
     *
     * @param event event at front of GEL
     */
    private void processArrivalEvent(Event event) {

        _time = event.getTime();

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
        }
        else if(_length > 0) {

            // server is busy, queue

            if(_length < _maxBuffer) {

                // add packet to queue
                _packetQueue.push(newPacket);
            }
            else {

                // drop packet
                _numPacketsDropped++;
            }

            _length++;

            // update stats
            _serverUtilization += _time;
            // mean queue length needs to update, idk how rn
        }
    }

    /**
     * initialize all variables, insert first event into GEL
     *
     * @param interArrivalRate  λ
     * @param transmissionRate  μ
     * @param maxBuffer         max num packets allowed in queue
     */
    private void initialize(double interArrivalRate, double transmissionRate, double maxBuffer) {

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
     * random variable following negative exponential distribution
     *
     * @param rate  λ or μ
     * @return      random variable
     */
    private double negativeExponentiallyDistributedTime(double rate) {

        double u;
        Random gen = new Random();
        u = gen.nextDouble();

        return((-1 / rate) * log(1 - u));
    }
}
