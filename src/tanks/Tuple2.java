package tanks;

public class Tuple2<A, B> {
    private final A a;
    private final B b;

    public Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }
}
