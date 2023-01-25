package Help;

/**
 * A class describing a tuple of two generic Elements.
 * Once generated a tuple can not be changed and must instead be replaced with a new Tuple
 *
 * @param <T> the type of the first tuple element
 * @param <U> the type of the second tuple element
 */
public class Tuple<T, U> {
    /**
     * The first element of the Tuple of type T
     */
    public final T a;

    /**
     * The second element of the Tuple of type U
     */
    public final U b;

    /**
     * The Constructor of a Tuple
     *
     * @param a the first Element of the Tuple
     * @param b the second Element of the Tuple
     */
    public Tuple(T a, U b) {
        this.a = a;
        this.b = b;
    }

    /**
     * A method to save the tuple as a String
     *
     * @return first-element.toString:second-element.toString()
     */
    public String toString() {
        return a.toString() + ":" + b.toString();
    }
}