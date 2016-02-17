import java.util.LinkedList;

/**
 * Simulation class
 * discrete-event simulation model
 *
 * Authors: Muhammad Elias, Charlie Ng
 */
public class Simulation {

    /**
     * current time
     */
    private int _time;

    /**
     * number of packets in queue
     */
    private int _length;

    /**
     * What fraction of the time is the server busy
     */
    private float _serverUtilization;

    /**
     * mean number of packets in the queue as seen by a new arriving packet
     */
    private float _meanQueueLength;

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

    public void runSimulation() {

        initialize();

        /**
         * 1. get the first event from the GEL;
         * 2. If the event is an arrival then process-arrival-event;
         * 3. Otherwise it must be a departure event and hence
         * process-service-completion;
         */
        for(int i = 0; i < 100000; i++) {

            Event event = _globalEventList.getFirstEvent();

            if(event.getType().equals("arrival")) {

                processArrivalEvent();
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

    private void processArrivalEvent() {
        //todo moe
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
    private void initialize() {

        _time = 0;
        _length = 0;
        _meanQueueLength = 0;
        _serverUtilization = 0;
        _numPacketsDropped = 0;
        _packetQueue = new LinkedList<Packet>();
        _globalEventList = new GlobalEventList();

        // more todo
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
     * Packet class
     */
    private class Packet {

        int _sequenceNumber;

        public Packet(int num) {

            this._sequenceNumber = num;
        }
    }
}