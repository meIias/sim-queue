import java.util.Random;

/**
 * Packet class
 *
 * Authors: Muhammad Elias, Charlie Ng
 */
public class Packet {

    public double length;
    public int destination;
    public int hostAddress;
    public double serviceTime;

    public Packet(double serviceTime) {

        this.serviceTime = serviceTime;
    }

    public Packet(int numHosts, int hostAddress, double serviceTime) {

        this.serviceTime = serviceTime;

        this.hostAddress = hostAddress;

        this.length = (new Random().nextInt(1518 - 64) + 64);

        int dest = (new Random().nextInt(numHosts));
        while(dest == this.hostAddress) {

            dest = (new Random().nextInt(numHosts));
        }
        this.destination = dest;
    }

    public double getServiceTime() { return serviceTime; }
}
