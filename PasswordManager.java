import java.util.InputMismatchException;
import java.util.Scanner;

// PasswordManager represents a simple console-based authentication system.
// It allows users to log in and checks password strength.
// Also provides an option to generate or manually set a new password.

class PasswordManager {

    // Command line text colors
    final static String RED = "\u001b[31;1m";
    final static String BLUE = "\u001b[36;1m";
    final static String GREEN = "\u001b[32;1m";
    final static String RESET = "\u001b[0m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Demo Credentials, You can test the system using the following credentials:
        // Username: Aland
        // Password: aland!@#123

        // Database of users and their corresponding hashed passwords
        String[] username = { "Aland", "Ashkan", "Akar", "Mohammad" };
        int[] password = { 807953594, -1664862333, -979663289, 124163642 };

        System.out.println(BLUE + "\n=== Welcome to Password Manager ===" + RESET);

        // Main application loop
        while (true) {
            System.out.println("\n" + BLUE + "- Enter 1 to log in" + RESET);
            System.out.println(BLUE + "- Enter 0 to exit" + RESET);
            System.out.print("> ");

            try {
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1: usernameVerification(scanner, username, password); break;
                    case 0: System.out.println(GREEN + "- Exiting system. Have a great day! :D" + RESET); System.exit(0); break;
                    default: System.out.println(RED + "*) Invalid option. Please try again." + RESET);
                }
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Clear the previous input
                System.out.println(RED + "*) Invalid input. Please enter a number." + RESET);
            }
        }
    }

    // Handles the first step of login: verifying the username.
    public static void usernameVerification(Scanner scanner, String[] username, int[] password) {
        boolean condition = true;
        while (condition) {
            System.out.println(BLUE + "\n[ Username Verification ]" + RESET);
            System.out.println("- Enter 'qqq' to return to the main menu");
            System.out.print("- Username: ");
            String inputN = scanner.nextLine();

            // Exit condition
            if (inputN.equals("qqq")) {
                break;
            }

            boolean userNameExistence = false;
            int index = 0;

            // Search the array for the entered username
            for (int i = 0; i < username.length; i++) {
                if (inputN.equals(username[i])) {
                    userNameExistence = true;
                    index = i; // Save the index to match with the password array later
                    System.out.println(GREEN + "- Username found." + RESET);

                    // Move to password verification
                    passwordVerification(scanner, username, password, index);
                    condition = false;
                    break;
                }
            }
            if (!userNameExistence) {
                System.out.println(RED + "*) Invalid username. Please try again." + RESET);
            }
        }
    }

    // Handles the second step of login: verifying the password.
    public static void passwordVerification(Scanner scanner, String[] username, int[] password, int index) {
        while (true) {
            System.out.println(BLUE + "\n[ Password Verification ]" + RESET);
            System.out.println("- Enter 'qqq' to step back");
            System.out.print("- Password: ");
            String inputP = scanner.nextLine();

            if (inputP.equals("qqq")) {
                break;
            }

            boolean passwordExistence = false;

            // Compare the hash of the input to the stored hash
            if (inputP.hashCode() == password[index]) {
                passwordExistence = true;
                System.out.println(GREEN + "- Login successful!" + RESET);

                // Offer the user the ability to change their password upon successful login
                System.out.print("\n- Would you like to update your password? (Y/N): ");
                String inputYN = scanner.nextLine();
                if (inputYN.equalsIgnoreCase("Y")) {
                    changingPassword(scanner, password, index);
                    break;
                } else {
                    break;
                }
            }
            if (!passwordExistence) {
                System.out.println(RED + "*) Incorrect password. Please try again." + RESET);
            }
        }
    }

    // Allows the user to manually type a new password or generate a random one.
    public static void changingPassword(Scanner scanner, int[] password, int index) {
        boolean condition = true;
        while (condition) {
            System.out.println(BLUE + "\n[ Password Update ]" + RESET);
            System.out.println("- Enter 'm' to manually type a new password");
            System.out.println("- Enter 'r' to generate a secure random password");
            System.out.print("> ");
            String option = scanner.nextLine();

            if (option.equalsIgnoreCase("m")) {
                // Manual password entry logic
                while (true) {
                    System.out.print("\n- Enter new password (min 8 characters): ");
                    String passnew = scanner.nextLine();

                    // Minimum length check
                    if (passnew.length() < 8) {
                        System.out.println(RED + "*) Password too short! Must be at least 8 characters." + RESET);
                        continue;
                    }

                    // Verify complexity
                    boolean passwordStrength = checkPasswordStrength(passnew);
                    if (passwordStrength) {
                        password[index] = passnew.hashCode(); // Store the new hash
                        System.out.println(password[index]);
                        System.out.println(GREEN + "- Success! Your password has been updated." + RESET);
                        condition = false;
                        break;
                    } else {
                        // Warn user if password is weak but allow them to proceed anyway
                        System.out.print(RED + "*) Warning: Password is weak. Set anyway? (Y/N): " + RESET);
                        String op = scanner.nextLine();
                        if (op.equalsIgnoreCase("Y")) {
                            password[index] = passnew.hashCode();
                            System.out.println(GREEN + "- Success! Your password has been updated." + RESET);
                            condition = false;
                            break;
                        } else if (op.equalsIgnoreCase("N")) {
                            continue;
                        }
                    }
                }
            } else if (option.equalsIgnoreCase("r")) {
                // Random password generation logic
                while (true) {
                    System.out.print("\n- Enter desired password length (min 8): ");

                    try {
                        int length = scanner.nextInt();

                        if (length < 8) {
                            System.out.println(RED + "*) Length must be at least 8." + RESET);
                            continue;
                        }

                        String passg = passwordGenerator(length);
                        scanner.nextLine();

                        System.out.println("- Generated Password: " + BLUE + passg + RESET);
                        System.out.print("- Keep this password? (Y/N): ");
                        String confirmOption = scanner.nextLine();

                        if (confirmOption.equalsIgnoreCase("Y")) {
                            password[index] = passg.hashCode();
                            condition = false;
                            System.out.println(GREEN + "- Success! Your password has been updated." + RESET);
                            break;
                        }
                    } catch (InputMismatchException e) {
                        scanner.nextLine(); // Clear the previuos input
                        System.out.println(RED + "*) Invalid input. Please enter a number." + RESET);
                    }
                }
            } else {
                System.out.println(RED + "*) Invalid input." + RESET);
            }
        }
    }

    // Checks password complexity.
    // Ensures the password contains a mix of letters, numbers, and symbols.
    public static boolean checkPasswordStrength(String passnew) {
        int symbolCounter = 0;
        int numberCounter = 0;
        boolean passwordStrength;

        // Loop through every character in the password
        for (int i = 0; i < passnew.length(); i++) {
            int j = 0;
            char checking;

            // Checking symbol ranges (!"#$%&'()*+,-./)
            for (j = 33; j < 48; j++) {
                checking = (char) j;
                if (passnew.charAt(i) == checking) {
                    symbolCounter++;
                }
            }
            // Checking number range (0-9)
            for (j = 48; j < 58; j++) {
                checking = (char) j;
                if (passnew.charAt(i) == checking) {
                    numberCounter++;
                }
            }
            // Checking more symbol ranges
            for (j = 58; j < 65; j++) {
                checking = (char) j;
                if (passnew.charAt(i) == checking) {
                    symbolCounter++;
                }
            }
            // Checking more symbol ranges (brackets, slashes)
            for (j = 91; j < 97; j++) {
                checking = (char) j;
                if (passnew.charAt(i) == checking) {
                    symbolCounter++;
                }
            }
            // Checking final symbol ranges (braces, tilde)
            for (j = 123; j < 127; j++) {
                checking = (char) j;
                if (passnew.charAt(i) == checking) {
                    symbolCounter++;
                }
            }
        }

        // Requires more than 2 numbers OR more than 2 symbols to be considered "strong"
        if (numberCounter <= 2 || symbolCounter <= 2) {
            passwordStrength = false;
        } else {
            passwordStrength = true;
        }

        return passwordStrength;
    }

    // Generates a random string containing a mix of letters and symbols.
    public static String passwordGenerator(int length) {
        String str = "";
        // Generate letters
        for (int i = 0; i < length - 3; i++) {
            char letter = (char) (65 + (int) (Math.random() * 57));
            str = str + letter;
        }
        // Append 3 random symbols at the end
        for (int i = 0; i < 3; i++) {
            char letter = (char) (33 + (int) (Math.random() * 31));
            str = str + letter;
        }
        return str;
    }
}