import java.util.Arrays;

public class ByteUtils {
    public static byte[] longToByteArray(long l) {
        byte[] result = new byte[8];

        for (int i = 0; i < 8; i++) {
            result[7 - i] = (byte) ((l >> (8 * i)) & 0xFF);

            if (l >> (8 * i) == 0L) {
                if (i == 0) {
                    return Arrays.copyOfRange(result, 7, 8);
                }
                return Arrays.copyOfRange(result, 8 - i, 8);
            }
        }

        return result;
    }

    public static long byteArrayToLong(byte[] array) {
        long result = 0L;

        for (byte b : array) {
            result = result << 8;
            result += (b & 0xFF);
        }

        return result;
    }
}
