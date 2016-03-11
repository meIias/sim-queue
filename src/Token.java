/**
 * Token singleton class
 *
 * Authors: Muhammad Elias, Charlie Ng
 */
public class Token {

    private int _owner;

    private static Token _token;

    private Token() {}

    public static Token getInstance() {

        if(_token == null) {

            _token = new Token();
            _token._owner = -1;
        }
        return _token;
    }

    public void setOwner(int newOwner) {

        _owner = newOwner;
    }

    public int getOwner() {

        return _owner;
    }
}
