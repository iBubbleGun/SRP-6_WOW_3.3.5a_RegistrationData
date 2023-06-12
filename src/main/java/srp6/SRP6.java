package srp6;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Tymur Kosiak (https://github.com/iBubbleGun)
 */
public class SRP6 {

    private static final int CONSTANT_WOW_G = 7;
    private static final String CONSTANT_WOW_N = "894B645E89E1535BBDAD5B8B290650530801B18EBFBF5E8FAB3C82872A3E9BB7";

    public static String calculateSRP6Verifier(String ACCOUNT_NAME, String ACCOUNT_PASSWORD, String SALT) {
        BigInteger g = BigInteger.valueOf(CONSTANT_WOW_G);
        BigInteger N = new BigInteger(CONSTANT_WOW_N, 16);

        byte[] h1 = getSHA1Hash((ACCOUNT_NAME + ':' + ACCOUNT_PASSWORD).toUpperCase().getBytes(StandardCharsets.UTF_8));
        BigInteger h2 = new BigInteger(1, reverseByteArray(getSHA1Hash(concatenateByteArrays(hexStringToByteArray(SALT), h1))));
        BigInteger pow = g.modPow(h2, N);
        BigInteger b = new BigInteger(1, reverseByteArray(hexStringToByteArray(leftPadWithZeros(reverseString(pow.toString(16)), 64))));

        return reverseString(leftPadWithZeros(b.toString(16), 64)).toUpperCase();
    }

    public static String[] getRegistrationData(String ACCOUNT_NAME, String ACCOUNT_PASSWORD) {
        final String SALT = getNewSalt(32);
        final String VERIFIER = calculateSRP6Verifier(ACCOUNT_NAME, ACCOUNT_PASSWORD, SALT);

        String[] registrationData = new String[2];
        registrationData[0] = SALT.toUpperCase();
        registrationData[1] = VERIFIER.toUpperCase();

        return registrationData;
    }

    public static String verifySRP6(String ACCOUNT_NAME, String ACCOUNT_PASSWORD, String SALT) {
        return calculateSRP6Verifier(ACCOUNT_NAME, ACCOUNT_PASSWORD, SALT);
    }

    private static byte[] getSHA1Hash(byte[] input) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            return sha1.digest(input);
        } catch (NoSuchAlgorithmException e) {
            //e.printStackTrace();
            return null;
        }
    }

    private static byte[] reverseByteArray(byte[] input) {
        byte[] reversed = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            reversed[i] = input[input.length - 1 - i];
        }
        return reversed;
    }

    private static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    private static byte[] concatenateByteArrays(byte[] array1, byte[] array2) {
        byte[] concatenated = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, concatenated, 0, array1.length);
        System.arraycopy(array2, 0, concatenated, array1.length, array2.length);
        return concatenated;
    }

    private static String leftPadWithZeros(String input, int length) {
        while (input.length() < length) {
            input = "0" + input;
        }
        return input;
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return byteArray;
    }

    private static String getNewSalt(int len) {
        return HexStringGenerator.getNewHexString(len);
    }
}
