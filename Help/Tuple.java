package Help;

public class Tuple<T, U> {
    public final T a;
    public final U b;

    public Tuple(T a, U b) {
        this.a = a;
        this.b = b;
    }

    public String toString() {
        return a.toString() + ":" + b.toString();
    }
}