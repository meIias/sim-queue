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

    public Host(int addr) {

        this.address = addr;
        this.packetQueue = new LinkedList<Packet>();
        this.hasToken = (Token.getInstance().getOwner() == addr);
    }
}
