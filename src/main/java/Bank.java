import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    void createAccount(Account acc, String url) throws SQLException {
        Statement statement = JDBCConnector.connect(url).createStatement();
        acc.generateCardNumber();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(acc.cardNumber);
        acc.generatePinCode();
        System.out.println(acc.PIN);
        System.out.println();
        statement.executeUpdate("INSERT INTO card (number,pin,balance) VALUES(" + acc.cardNumber + "," + acc.PIN + "," + acc.balance + ")");
        statement.close();
    }

    boolean validatePIN(Account acc, String url) throws SQLException {
        try {
            Statement statement = JDBCConnector.connect(url).createStatement();
            System.out.println("Enter your PIN:");
            String typedPIN = scanner.nextLine();
            ResultSet result = statement.executeQuery("SELECT pin FROM card WHERE pin = " + typedPIN + ";");
            acc.setTypedPin(result.getString("pin"));
            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(acc.typedPin.equals(acc.PIN)){
            return true;
        } else {
            return false;
        }
    }

    boolean validateCard(Account acc, String url) throws SQLException {
        try {
            Statement statement = JDBCConnector.connect(url).createStatement();
            System.out.println("Enter your card number:");
            String typedCard = scanner.nextLine();
            ResultSet result = statement.executeQuery("SELECT number FROM card WHERE number = " + typedCard);
            acc.setTypedCardNumber(result.getString("number"));
            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (acc.typedCardNumber.equals(acc.cardNumber)){
            return true;
        }else{
        return false;
        }
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
