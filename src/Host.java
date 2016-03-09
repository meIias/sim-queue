import java.util.LinkedList;

/**
 * Created by moeelias on 3/8/16.
 */
public class Host {

    public int address;

    public boolean hasToken;

    public LinkedList<Packet> packetQueue;

    public Host(int addr) {

        this.address = addr;
        this.hasToken = false;
        this.packetQueue = new LinkedList<Packet>();
    }
}
