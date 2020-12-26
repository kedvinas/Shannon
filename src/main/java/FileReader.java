import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {
    private final byte[] bytes;
    private int currentPosition = 0;
    private final int wordSize;

    public FileReader(int wordSize, String file) throws IOException {
        this.wordSize = wordSize;

        bytes = Files.readAllBytes(Paths.get(file));
    }

    public int size() {
        return bytes.length;
    }

    public long read() {
        long value = 0L;

        int bitsRead = 0;

        while (bitsRead < wordSize && currentPosition < bytes.length * 8) {
            value <<= 1;
            value |= (bytes[currentPosition / 8] >> ((8 - ((currentPosition + 1) % 8)) % 8)) & 1;
            bitsRead++;
            currentPosition++;
        }

        value <<= (wordSize - bitsRead);

        return value;
    }

    public boolean hasNext() {
        return currentPosition < bytes.length * 8;
    }
}
