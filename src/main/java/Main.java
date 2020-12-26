import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java -jar Shannon.jar [OPTION]... INPUT_FILE OUTPUT_FILE");
            System.out.println("Compress or uncompress INPUT_FILE to OUTPUT_FILE (using Shannon coding)");
            System.out.println("Options:");
            System.out.println("-d    decompress");
            System.out.println("-w    word size between 2-64 (default 8)");
            return;
        }

        String outputFile = args[args.length - 1];
        String inputFile = args[args.length - 2];

        long startTime = System.currentTimeMillis();

        if (args.length == 2 || args.length == 4) {
            int wordSize = 8;
            if (args[0].equals("-w")) {
                wordSize = Integer.parseInt(args[1]);
            }

            Frequency<Long> frequencies = new Frequency<>();

            FileReader fileReader = new FileReader(wordSize, inputFile);

            while (fileReader.hasNext()) {
                frequencies.add(fileReader.read());
            }

            ShannonCode<Long> code = new ShannonCode<>(frequencies);
            new FileWriter(code, outputFile, inputFile, wordSize);

            System.out.println("Initial file size:    " + fileReader.size());
            System.out.println("Compressed file size: " + new File(outputFile).length());
        } else if (args[0].equals("-d")) {
            new FileDecoder(inputFile, outputFile);
        }

        System.out.println("Time passed:          " + (System.currentTimeMillis() - startTime) / 1000.0 + " s");
    }
}
