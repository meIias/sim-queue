/**
 * GlobalEventList class
 * This will maintain all the events sorted in increasing order of time.
 *
 * Authors: Muhammad Elias, Charlie Ng
 */
public class GlobalEventList {

    private int _length;

    private Event _head;
    private Event _tail;

    public GlobalEventList() {

        this._length = 0;

        this._head = null;
        this._tail = null;
    }

    public void insert(int time, String type) {

        if(_head == null) {

            _head = new Event(time, type);
            _tail = _head;
        }
        else {

            // insert based on ordering by time

            Event current = _head;
            while(current.getTime() < time) {

                current = current.getNext();

                if(current == null) {
                    return;
                }
            }

            Event newEvent = new Event(time, type);

            if(current == _tail) {

                newEvent.setNext(null);
                _tail = newEvent;
            }
            else {

                newEvent.setNext(current.getNext());
                current.getNext().setPrevious(newEvent);
            }

            newEvent.setPrevious(current);
            current.setNext(newEvent);
        }

        _length++;
    }

    /*
     * get first event from gel
     */
    public Event getFirstEvent() {

        return _head;
    }

    /*
     * remove the first event
     */
    public void remove() {

        if (_length == 0) {
            return;
        }

        _head = _head.getNext();

        _length--;
    }

    public int getLength() {
        return _length;
    }
}
