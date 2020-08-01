package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static boolean isInDecryptMode = false;
    private static int key = 0;
    private static String data = "";
    private static String inFileName = "";
    private static String outFileName = "";
    private static Encrypter encrypter = null;

    private static boolean parseArguments(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            try {
                switch (args[i]) {
                    case "-alg":
                        encrypter = EncrypterFactory.makeEncrypter(args[i + 1]);
                    case "-mode":
                        if (args[i + 1].equals("dec")) {
                            isInDecryptMode = true;
                        }
                        break;
                    case "-key":
                        key = Integer.parseInt(args[i + 1]);
                        break;
                    case "-data":
                        data = args[i + 1];
                        break;
                    case "-in":
                        inFileName = args[i + 1];
                        break;
                    case "-out":
                        outFileName = args[i + 1];
                        break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: unexpected end of the argument list");
                return false;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        if (encrypter == null) {
            encrypter = EncrypterFactory.makeDefaultEncrypter();
        }
        return true;
    }

    public static void main(String[] args) {
        if (!parseArguments(args)) {
            return;
        }
        if (data.isEmpty()) {
            if (inFileName.isEmpty()) {
                System.out.println("Error: no data or input file specified");
                return;
            }
            try {
                data = new String(Files.readAllBytes(Paths.get(inFileName)));
            } catch (Exception e) {
                System.out.println("Error: error reading input file " + inFileName);
                return;
            }
        }
        String output = encrypter.process(data, key, isInDecryptMode);
        if (outFileName.isEmpty()) {
            System.out.println(output);
        } else {
            try (FileWriter outFile = new FileWriter(new File(outFileName))) {
                outFile.write(output);
            } catch (Exception e) {
                System.out.println("Error: error writing output file " + outFileName);
            }
        }
    }
}
