import java.util.LinkedList;

/**
 * Host class
 *
 * Authors: Muhammad Elias, Charlie Ng
 */
public class Host {

    public int address;

    public boolean hasToken;

    public LinkedList<Packet> packetQueue;

    public LinkedList<Packet> receivedPackets;

    public Host(int addr) {

        this.address = addr;
        this.packetQueue = new LinkedList<Packet>();
        this.receivedPackets = new LinkedList<Packet>();
        this.hasToken = (Token.getInstance().getOwner() == addr);
    }

    public static double getNumPackets(LinkedList<Host> hosts) {

        double size = 0;

        for(Host h : hosts) {

            size += h.packetQueue.size();
        }

        return size;
    }
}
