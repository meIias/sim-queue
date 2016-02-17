/**
 * Event class
 *
 * Authors: Muhammad Elias, Charlie Ng
 */
public class Event {

    /**
     * This is the time when the event occurs. For an arrival event it is the time the
     * packet arrives at the transmitter and for a departure event it is the
     * time when the server is finished transmitting the packet.
     */
    private int _time;

    /**
     * Type of the event which can one of two types
     * 1) an arrival event or 2) a departure event.
     */
    private String _type;

    /**
     * Pointers to the next and previous events.
     */
    private Event _next;
    private Event _previous;

    public Event(int time, String type) {

        this._time = time;
        this._type = type;

        this._next = null;
        this._previous = null;
    }

    public int getTime() {
        return _time;
    }

    public String getType() {
        return _type;
    }

    public Event getNext() {
        return _next;
    }

    public Event getPrevious() {
        return _previous;
    }

    public void setTime(int _time) {
        this._time = _time;
    }

    public void setType(String _type) {
        this._type = _type;
    }

    public void setNext(Event _next) {
        this._next = _next;
    }

    public void setPrevious(Event _previous) {
        this._previous = _previous;
    }
}
