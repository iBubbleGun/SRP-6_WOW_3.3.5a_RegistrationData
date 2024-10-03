package srp;

/**
 *
 * @author Tymur Kosiak (https://github.com/iBubbleGun)
 */
public class Examples {

    public static void main(String[] args) {

        SRP6 srp6 = new SRP6();

        final String USER_LOGIN = "MyLogin";        // Your account Login
        final String USER_PASSWORD = "MyPassword";  // Your account Password

        String[] registartionData = srp6.getRegistrationData(USER_LOGIN, USER_PASSWORD);
        final String SALT = registartionData[0];
        final String VERIFIER = registartionData[1];

        // Check the validity of the authentication data
        boolean isVerified = false;
        if (VERIFIER.equals(srp6.verifySRP6(USER_LOGIN, USER_PASSWORD, SALT))) {
            isVerified = true;
        }

        System.out.println("Login:          " + USER_LOGIN);
        System.out.println("Password:       " + USER_PASSWORD);
        System.out.println("SALT:           " + SALT);
        System.out.println("VERIFIER:       " + VERIFIER);
        System.out.println("Login verified: " + ((isVerified) ? "Yes" : "No"));
        System.out.println("-".repeat(80));
    }
}
