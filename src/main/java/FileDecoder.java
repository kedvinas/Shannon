import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileDecoder {
    private final byte[] bytes;

    private int offset = 0;

    public FileDecoder(String file, String outputFile) throws IOException {
        long value = 0L;

        bytes = Files.readAllBytes(Paths.get(file));

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            int inputPosition = 0;
            byte[] outputArray = new byte[8];
            int outputPosition = 0;
            long totalLength = 0L;

            long initialFileLength = read();
            long wordSize = read();
            Map<Code, Long> shannonCode = readShannonCode();

            for (int i = offset; i < bytes.length; i++) {
                for (int bit = 7; bit >= 0; bit--) {
                    inputPosition++;
                    value <<= 1;
                    value |= ((bytes[i] >> bit) & 1);

                    Code code = new Code(inputPosition, value);
                    if (shannonCode.containsKey(code)) {
                        long decoded = shannonCode.get(code);
                        for (int w = (int) wordSize - 1; w >= 0; w--) {
                            outputArray[outputPosition / 8] <<= 1;
                            outputArray[outputPosition / 8] |= (byte) (((decoded >> w)) & 0x1);
                            outputPosition++;
                            totalLength++;

                            if (outputPosition == 64) {
                                outputPosition = 0;
                                fos.write(outputArray);
                            }

                            if (totalLength == initialFileLength * 8) {
                                byte[] temp = Arrays.copyOfRange(outputArray, 0, outputPosition / 8);
                                fos.write(temp);
                                return;
                            }
                        }

                        value = 0;
                        inputPosition = 0;
                    }
                }
            }
        }
    }

    private Map<Code, Long> readShannonCode() {
        HashMap<Code, Long> codeToT = new HashMap<>();

        long codeCount = read();

        for (int i = 0; i < codeCount; i++) {
            long value = read();

            Code code = readCode();

            codeToT.put(code, value);
        }

        return codeToT;
    }

    private Code readCode() {
        int length = (int) read();
        long value = read();

        return new Code(length, value);
    }

    private long read() {
        byte length = bytes[offset++];
        long value = ByteUtils.byteArrayToLong(Arrays.copyOfRange(bytes, offset, offset + length));
        offset += length;
        return value;
    }
}
