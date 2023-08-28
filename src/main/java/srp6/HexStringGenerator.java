package srp6;

import java.security.SecureRandom;

/**
 *
 * @author Tymur Kosiak (https://github.com/iBubbleGun)
 */
public class HexStringGenerator {

    private final SecureRandom random;
    private final char[] HEX_ARRAY;

    public HexStringGenerator() {
        this.random = new SecureRandom();
        this.HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    }

    public String getNewHexString(int quantity) {
        byte[] bytes = new byte[quantity];
        this.random.nextBytes(bytes);
        return bytesToHex(bytes);
    }

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = this.HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = this.HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
