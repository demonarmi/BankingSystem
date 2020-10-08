
import java.util.Random;
import java.util.Scanner;

public class Bank extends JDBCConnector{
    final Scanner scanner = new Scanner(System.in);
    String menuAction;
    String PIN;
    String typedPin;
    String typedCardNumber;
    String cardNumber;
    int balance = 0;


    private void setTypedPin(String pin) {
        this.typedPin = pin;
    }

    private void setTypedCardNumber(String card) {
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

    void generateCardNumber(Bank bank) {
        long IIN = 400000;
        long min = 100000000L;
        long max = 999999999L;
        long random_long = (long) (Math.random() * (max - min + 1) + min);
        String cardNumberToCheck = "" + IIN + random_long;
        int checksum = 0;


        int[] intArr = new int[cardNumberToCheck.length()];
        for (int i = 0; i < cardNumberToCheck.length(); i++) {
            intArr[i] = Integer.parseInt(cardNumberToCheck.substring(i, i+1));
        }
        for (int i = intArr.length - 1; i >= 0; i = i - 2){
            int j = intArr[i];
            j = j * 2;

            if (j > 9) {
                j = j % 10 + 1;
            }
            intArr[i] = j;
        }

        int sum = 0;
        for (int i = 0; i < intArr.length; i++) {
            sum += intArr[i];
        }

        if (sum % 10 != 0) {
            checksum = (10 - sum % 10) % 10;
        } else {
            checksum = 0;
        }
        String finalCardNumber = cardNumberToCheck + checksum;
        bank.cardNumber = finalCardNumber;
    }

    void generatePinCode(Bank bank) {
        System.out.println("Your card PIN:");
        int min = 0000;
        int max = 9999;
        int random_int = (int) (Math.random() * (max - min + 1) + min);
        int generatedPIN = random_int;
        PIN = String.format("%04d", generatedPIN);
    }

    void createAccount(Bank bank, String url) {
        bank.generateCardNumber(bank);
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        bank.generatePinCode(bank);
        System.out.println(PIN);
        System.out.println();
        String sql = "INSERT INTO cards (number,pin,balance) VALUES(" + bank.cardNumber + "," + bank.PIN + "," + bank.balance + ")";
        JDBCConnector.insert(sql, url);
    }

    void validatePIN(Bank bank) {
        System.out.println("Enter your PIN:");
        String typedPIN = scanner.nextLine();
        bank.setTypedPin(typedPIN);
        System.out.println();
    }

    void validateCard(Bank bank) {
        System.out.println("Enter your card number:");
        String typedCardNumber = scanner.nextLine();
        bank.setTypedCardNumber(typedCardNumber);
    }

    void checkBalance() {
        System.out.println("Balance: " + balance);
        System.out.println();
    }

    void logOut() {
        System.out.println("You have successfully logged out!");
        System.out.println();
    }

    void exit(Bank bank) {
        System.out.println("Bye!");
        System.exit(0);
    }
}
