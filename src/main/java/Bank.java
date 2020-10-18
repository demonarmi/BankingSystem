import java.util.Scanner;

public class Bank extends Main {
    final Scanner scanner = new Scanner(System.in);
    String menuAction;
    String typedPin;
    String typedCardNumber;
    double balance = 0;



    void setTypedPin(String pin) {
        this.typedPin = pin;
    }

    void setTypedCardNumber(String card) {
        this.typedCardNumber = card;
    }

    private void setMenuAction(String menuAction) {
        this.menuAction = menuAction;
    }

    void mainMenuCommands(Bank bank) {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
        bank.setMenuAction(scanner.nextLine());
        System.out.println();
    }

    static boolean checkLuhn(String cardNo) {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {

            int d = cardNo.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    void createAccount(Account acc, String url) {
        acc.generateCardNumber(acc);
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(acc.cardNumber);
        acc.generatePinCode(acc);
        System.out.println(acc.PIN);
        System.out.println();
        QueryExecutor.executeQuery("INSERT INTO card (number,pin,balance) VALUES(" + acc.cardNumber + "," + acc.PIN + "," + acc.balance + ")", url);
    }

    void validatePIN(Account acc) {
        System.out.println("Enter your PIN:");
        String typedPIN = scanner.nextLine();
        acc.setTypedPin(typedPIN);
    }

    void validateCard(Account acc) {
        System.out.println("Enter your card number:");
        String typedCardNumber = scanner.nextLine();
        acc.setTypedCardNumber(typedCardNumber);
    }

    void checkBalance() {
        System.out.println("Balance: " + balance);
        System.out.println();
    }

    void logOut() {
        System.out.println("You have successfully logged out!");
        System.out.println();
    }

    void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }
}
