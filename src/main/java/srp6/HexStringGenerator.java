package srp6;

import java.security.SecureRandom;

/**
 *
 * @author Tymur Kosiak (https://github.com/iBubbleGun)
 */
public class HexStringGenerator {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String getNewHexString(int quantity) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[quantity];
        random.nextBytes(bytes);
        return bytesToHex(bytes);
    }
}
