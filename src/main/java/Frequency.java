import java.util.HashMap;
import java.util.Map;

public class Frequency<T> {
    public Map<T, Integer> frequencies = new HashMap<>();

    public int count = 0;

    public int get(T key) {
        return frequencies.getOrDefault(key, 0);
    }

    public double probability(T key) {
        return frequencies.getOrDefault(key, 0) / (double) count;
    }

    public void add(T key) {
        if (frequencies.containsKey(key)) {
            frequencies.put(key, frequencies.get(key) + 1);
        } else {
            frequencies.put(key, 1);
        }
        count++;
    }
}
