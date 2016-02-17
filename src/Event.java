/**
 * Created by moeelias on 2/16/16.
 */
public class Event {

    /**
     * This is the time when the event occurs. For an arrival event it is the time the
     * packet arrives at the transmitter and for a departure event it is the
     * time when the server is finished transmitting the packet.
     */
    private String _time;

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

    public Event(String time, String type) {

        this._time = time;
        this._type = type;

        this._next = null;
        this._previous = null;
    }

    public String get_time() {
        return _time;
    }

    public String get_type() {
        return _type;
    }

    public Event get_next() {
        return _next;
    }

    public Event get_previous() {
        return _previous;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public void set_next(Event _next) {
        this._next = _next;
    }

    public void set_previous(Event _previous) {
        this._previous = _previous;
    }
}
