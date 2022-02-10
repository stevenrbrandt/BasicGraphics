package tanks;

import java.util.Random;
import java.util.TreeMap;

public class WeightedSet<T> {
    private final TreeMap<Integer, T> elements = new TreeMap<>();
    private final Random random = new Random();
    private int totalWeight = 0;

    public void add(T element, int weight) {
        totalWeight += weight;
        elements.put(totalWeight, element);
    }

    public T getRandom() {
        return elements.higherEntry(random.nextInt(totalWeight)).getValue();
    }
}
