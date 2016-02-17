/**
 * Packet class
 *
 * Authors: Muhammad Elias, Charlie Ng
 */
public class Packet {

    private double _serviceTime;

    public Packet(double serviceTime) {

        this._serviceTime = serviceTime;
    }

    public double getServiceTime() {
        return _serviceTime;
    }
}
