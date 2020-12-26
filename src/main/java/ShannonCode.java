import java.util.*;
import java.util.stream.Collectors;

public class ShannonCode<T> {
    private final Map<T, Code> tToCode = new HashMap<>();

    public ShannonCode(Frequency<T> frequency) {
        double additiveProbability = 0.0;

        List<T> sortedFrequencies = frequency.frequencies.entrySet().stream()
                .sorted(Comparator.comparing(e -> -e.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        for (T key : sortedFrequencies) {
            int length = log2(1 / frequency.probability(key)) + 1;

            double prob = additiveProbability;
            long code = 0L;

            for (int i = 0; i < length; i++) {
                code = code << 1;

                prob *= 2;

                if (prob >= 1) {
                    code = code | 1;
                    prob -= 1;
                }
            }

            additiveProbability += frequency.probability(key);
            tToCode.put(key, new Code(length, code));
        }
    }

    public Set<T> keys() {
        return tToCode.keySet();
    }

    public Code get(T key) {
        return tToCode.get(key);
    }

    public int size() {
        return tToCode.size();
    }

    private int log2(double n) {
        return (int) (Math.log(n) / Math.log(2));
    }
}
