import java.util.Random;
import java.util.Scanner;

public class Bank {
    final Scanner scanner = new Scanner(System.in);
    String menuAction;
    String PIN;
    String typedPin;
    String typedCardNumber;
    String cardNumber;

    private void setUserPin(String pin) {
        this.typedPin = pin;
    }

    private void setUserCard(String card) {
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
        String cardNumberToCheck = "" + IIN + random_long; //was string and at start "" + inn + ...
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
        Random random = new Random();
        System.out.println("Your card PIN:");
        int generatedPIN = random.nextInt(9999);
        PIN = String.format("%04d", generatedPIN);
    }

    void createAccount(Bank bank) {
        bank.generateCardNumber(bank);
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        bank.generatePinCode(bank);

        System.out.println(PIN);
        System.out.println();
    }

    void validatePIN(Bank bank) {
        System.out.println("Enter your PIN:");
        String typedPIN = scanner.nextLine();
        bank.setUserPin(typedPIN);
        System.out.println();
    }

    void validateCard(Bank bank) {
        System.out.println("Enter your card number:");
        String typedCardNumber = scanner.nextLine();
        bank.setUserCard(typedCardNumber);
    }

    void checkBalance(Bank bank) {
        System.out.println("Balance: 0");
        System.out.println();
    }

    void logOut(Bank bank) {
        System.out.println("You have successfully logged out!");
        System.out.println();
    }

    void exit(Bank bank) {
        System.out.println("Bye!");
        System.exit(0);
    }
}