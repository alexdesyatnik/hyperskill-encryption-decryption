package encryptdecrypt;

/* Base class for encrypt/decrypt algorithms */

abstract class Encrypter {
    abstract String encrypt(String data, int key);
    abstract String decrypt(String data, int key);

    String process(String data, int key, boolean isInDecryptMode) {
        return isInDecryptMode ? decrypt(data, key) : encrypt(data, key);
    }
}

class ShiftEncrypter extends Encrypter {

    @Override
    String encrypt(String data, int key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            int code = data.charAt(i);
            int range = 'Z' - 'A' + 1;
            if (code >= 'a' && code <= 'z') {
                code = (((code - 'a') + key) % range + range) % range + 'a';
            } else if (code >= 'A' && code <= 'Z') {
                code = (((code - 'A') + key) % range + range) % range + 'A';
            }
            result.append((char) code);
        }
        return result.toString();
    }

    @Override
    String decrypt(String data, int key) {
        return encrypt(data, -key);
    }
}

class UnicodeEncrypter extends Encrypter {

    @Override
    String encrypt(String data, int key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            result.append((char) (data.charAt(i) + key));
        }
        return result.toString();
    }

    @Override
    String decrypt(String data, int key) {
        return encrypt(data, -key);
    }
}

class EncrypterFactory {
    static Encrypter makeEncrypter(String algorithm) {
        switch (algorithm.toLowerCase()) {
            case "shift":
                return new ShiftEncrypter();
            case "unicode":
                return new UnicodeEncrypter();
            default:
                throw new IllegalArgumentException("Error: unknown encryption algorithm " + algorithm);
        }
    }

    public static Encrypter makeDefaultEncrypter() {
        return new ShiftEncrypter();
    }
}
