package srp;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Tymur Kosiak (https://github.com/iBubbleGun)
 */
public final class SRP6 {

    private static final int CONSTANT_WOW_G = 7;
    private static final String CONSTANT_WOW_N = "894B645E89E1535BBDAD5B8B290650530801B18EBFBF5E8FAB3C82872A3E9BB7";

    private final HexStringGenerator hexStringGenerator;

    // Attention! The SRP6 class is immutable.
    public SRP6() {
        this.hexStringGenerator = new HexStringGenerator();
    }

    public String[] getRegistrationData(
            String accountName,
            String accountPassword) {
        final String salt = getNewSalt(32);
        final String verifier = calculateSRP6Verifier(
                accountName,
                accountPassword,
                salt
        );
        return new String[]{salt.toUpperCase(), verifier};
    }

    public String verifySRP6(
            String accountName,
            String accountPassword,
            String salt) {
        return calculateSRP6Verifier(accountName, accountPassword, salt);
    }

    private String calculateSRP6Verifier(
            String accountName,
            String accountPassword,
            String salt) {
        BigInteger g = BigInteger.valueOf(CONSTANT_WOW_G);
        BigInteger n = new BigInteger(CONSTANT_WOW_N, 16);

        byte[] h1 = getSHA1Hash(
                (accountName + ':' + accountPassword)
                        .toUpperCase()
                        .getBytes(StandardCharsets.UTF_8)
        );

        BigInteger h2 = new BigInteger(
                1,
                reverseByteArray(
                        getSHA1Hash(
                                concatenateByteArrays(
                                        hexStringToByteArray(salt),
                                        h1
                                )
                        )
                )
        );

        BigInteger pow = g.modPow(h2, n);

        BigInteger b = new BigInteger(
                1,
                reverseByteArray(
                        hexStringToByteArray(
                                leftPadWithZeros(
                                        reverseString(
                                                pow.toString(16)
                                        ),
                                        64
                                )
                        )
                )
        );

        return reverseString(
                leftPadWithZeros(
                        b.toString(16),
                        64
                )
        ).toUpperCase();
    }

    private byte[] getSHA1Hash(byte[] input) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            return sha1.digest(input);
        } catch (NoSuchAlgorithmException e) {
            //e.printStackTrace();
            return new byte[0];
        }
    }

    private byte[] reverseByteArray(byte[] input) {
        byte[] reversed = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            reversed[i] = input[input.length - 1 - i];
        }
        return reversed;
    }

    private String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    private byte[] concatenateByteArrays(byte[] arr1, byte[] arr2) {
        byte[] result = new byte[arr1.length + arr2.length];

        System.arraycopy(
                arr1, 0, result, 0, arr1.length
        );
        System.arraycopy(
                arr2, 0, result, arr1.length, arr2.length
        );

        return result;
    }

    private String leftPadWithZeros(String input, int length) {
        while (input.length() < length) {
            input = "0".concat(input);
        }
        return input;
    }

    private byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(
                    hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }

        return byteArray;
    }

    private String getNewSalt(int len) {
        return hexStringGenerator.getNewHexString(len);
    }
}
