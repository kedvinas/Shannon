import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class FileWriter {
    private final FileReader fileReader;
    private final byte[] outputArray = new byte[8];
    private int currentPosition = 0;

    public FileWriter(ShannonCode<Long> shannonCode, String filename, String initialFilename, int wordSize) throws IOException {

        fileReader = new FileReader(wordSize, initialFilename);

        try (FileOutputStream fos = new FileOutputStream(filename)) {
            writeOriginalFileSize(fos);
            writeWordSize(wordSize, fos);
            writeShannonCodeSize(shannonCode, fos);

            writeShannonCode(shannonCode, fos);

            if (currentPosition == 64) {
                currentPosition = 0;
                fos.write(outputArray);
            }

            while (fileReader.hasNext()) {
                long value = fileReader.read();
                Code code = shannonCode.get(value);

                for (int i = code.getLength() - 1; i >= 0; i--) {
                    outputArray[currentPosition / 8] <<= 1;
                    outputArray[currentPosition / 8] |= (code.getCode() >> i) & 1;
                    currentPosition++;

                    if (currentPosition == 64) {
                        currentPosition = 0;
                        fos.write(outputArray);
                    }
                }
            }

            outputArray[currentPosition / 8] = (byte) (outputArray[currentPosition / 8] << (8 - currentPosition % 8));
            byte[] temp = Arrays.copyOfRange(outputArray, 0, (currentPosition + 8) / 8);
            fos.write(temp);
        }
    }

    private void writeOriginalFileSize(OutputStream os) throws IOException {
        byte[] fileSize = ByteUtils.longToByteArray(fileReader.size());

        write((byte) fileSize.length, os);

        for (byte b : fileSize) {
            write(b, os);
        }
    }

    private void writeWordSize(int wordSize, OutputStream os) throws IOException {
        byte[] wordSizeLength = ByteUtils.longToByteArray(wordSize);

        write((byte) wordSizeLength.length, os);

        for (byte b : wordSizeLength) {
            write(b, os);
        }
    }

    private void writeShannonCodeSize(ShannonCode<Long> shannonCode, OutputStream os) throws IOException {
        byte[] codeCount = ByteUtils.longToByteArray(shannonCode.size());

        write((byte) codeCount.length, os);

        for (byte b : codeCount) {
            write(b, os);
        }
    }

    private void writeShannonCode(ShannonCode<Long> shannonCode, OutputStream os) throws IOException {
        for (long key : shannonCode.keys()) {
            writeCodeKey(key, os);
            writeCode(shannonCode.get(key), os);
        }
    }

    private void writeCodeKey(long key, OutputStream os) throws IOException {
        byte[] keyLength = ByteUtils.longToByteArray(key);

        write((byte) keyLength.length, os);

        for (byte b : keyLength) {
            write(b, os);
        }
    }

    private void writeCode(Code code, OutputStream os) throws IOException {
        byte[] codeLength = ByteUtils.longToByteArray(code.getLength());

        write((byte) codeLength.length, os);

        for (byte b : codeLength) {
            write(b, os);
        }

        byte[] codeBinary = ByteUtils.longToByteArray(code.getCode());

        write((byte) codeBinary.length, os);

        for (byte b : codeBinary) {
            write(b, os);
        }
    }

    private void write(byte b, OutputStream os) throws IOException {
        if (currentPosition == 64) {
            currentPosition = 0;
            os.write(outputArray);
        }

        outputArray[currentPosition / 8] = b;
        currentPosition += 8;
    }
}
