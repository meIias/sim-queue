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

    /**
     * insert new event into GEL in its appropriate location
     *
     * @param event new event
     */
    public void insert(Event event) {

        if(_head == null) {

            _head = event;
            _tail = _head;
        }
        else {

            // insert based on ordering by time

            Event current = _head;
            while(current.getTime() <= event.getTime()) {

                if(current.getNext() == null || current.getNext().getTime() > event.getTime()) {
                    break;
                }

                current = current.getNext();
            }

            if(current.getPrevious() == null) {

                // insert head area

                if(current.getTime() > event.getTime()) {

                    // insert before
                    current.setPrevious(event);
                    event.setPrevious(null);
                    event.setNext(current);
                    _head = event;
                }
                else {

                    // insert after
                    event.setNext(_head.getNext());

                    if(_head.getNext() != null) {

                        _head.getNext().setPrevious(event);
                    }

                    _head.setNext(event);
                    event.setPrevious(_head);
                }
            }
            else if(current.getTime() <= event.getTime()) {

                // insert somewhere in the list other than head

                //insert after

                if(current == _tail) {

                    event.setNext(null);
                    event.setPrevious(current);
                    current.setNext(event);
                    _tail = event;
                }
                else {

                    event.setNext(current.getNext());

                    if(current.getNext() != null) {

                        current.getNext().setPrevious(event);
                    }

                    event.setPrevious(current);
                    current.setNext(event);
                }
            }
            else {

                //insert before

                event.setNext(current);
                event.setPrevious(current.getPrevious());
                current.setPrevious(event);
            }
        }

        _length++;
    }

    /**
     * remove front event from GEL
     */
    public Event removeFront() {

        if (_length == 0 || _head == null) {
            return null;
        }

        Event oldHeadEvent = _head;

        _head = _head.getNext();

        if(_head != null) {

            _head.setPrevious(null);
        }

        _length--;

        return oldHeadEvent;
    }

    public int getLength() {
        return _length;
    }
}
