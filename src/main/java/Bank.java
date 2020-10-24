
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

public class Bank {
    final Scanner scanner = new Scanner(System.in);
    String menuAction;
    String typedPin;
    String typedCardNumber;


    Account findAccount(String accountNumber, String url) throws SQLException {
        String pin = "0000";
        Account account = new Account(accountNumber,pin);
        ResultSet rs = null;
        try {
            Statement statement = JDBCConnector.connect(url).createStatement();
            rs = statement.executeQuery("SELECT number, pin FROM card WHERE number = " + accountNumber);
            account.PIN = rs.getString("pin");
            account.cardNumber = rs.getString("number");
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    boolean login(String url) throws SQLException {
        System.out.println("Enter your card number: ");
        String typedCard = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String typedPIN = scanner.nextLine();
        Account targetAccount = findAccount(typedCard, url);
        if (!targetAccount.cardNumber.equals(typedCard) || !targetAccount.PIN.equals(typedPIN)) {
            return false;
        } else if (targetAccount.cardNumber.equals(typedCard) || targetAccount.PIN.equals(typedPIN)) {
            return true;
        }
        return false;
    }

    void addIncome(double income, String url) throws SQLException {
        Statement statement = JDBCConnector.connect(url).createStatement();

        //balance += income;
        //statement.executeUpdate("UPDATE card SET balance = " + balance + " where number =  " + cardNumber);
    }


    public String generateCardNumber() {
        long IIN = 400000;
        long min = 100000000L;
        long max = 999999999L;
        long random_long = (long) (Math.random() * (max - min + 1) + min);
        String cardNumberToCheck = "" + IIN + random_long;
        int checksum = 0;


        int[] intArr = new int[cardNumberToCheck.length()];
        for (int i = 0; i < cardNumberToCheck.length(); i++) {
            intArr[i] = Integer.parseInt(cardNumberToCheck.substring(i, i + 1));
        }
        for (int i = intArr.length - 1; i >= 0; i = i - 2) {
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
        return finalCardNumber;

    }

    public String generatePinCode() {
        Random rand = new Random();
        int generatedPIN = rand.nextInt(10000);
        String PIN = String.valueOf(generatedPIN).format("%04d", generatedPIN);
        return PIN;
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
            nSum += d / 10;
            nSum += d % 10;
            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    Account createAccount(String url) throws SQLException {
        Account account = new Account();
        Statement statement = null;
        try {
            statement = JDBCConnector.connect(url).createStatement();
            account.cardNumber = generateCardNumber();
            account.PIN = generatePinCode();
            System.out.println("Your card has been created");
            System.out.println("Your card number:");
            System.out.println(account.cardNumber);
            System.out.println("Your card PIN: ");
            System.out.println(account.PIN);
            System.out.println();
            statement.executeUpdate("INSERT INTO card (number,pin,balance) VALUES(" + account.cardNumber + "," + account.PIN + "," + account.balance + ")");
            //return account;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        statement.close();
        return account;
    }

    /*boolean validatePIN(Account acc, String url) throws SQLException {
        try {
            Statement statement = JDBCConnector.connect(url).createStatement();
            System.out.println("Enter your PIN:");
            String typedPIN = scanner.nextLine();
            ResultSet result = statement.executeQuery("SELECT pin FROM card WHERE pin = " + typedPIN + ";");
            acc.PIN = result.getString("pin");

            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(acc.PIN.equals(typedPIN)){
            return true;
        } else {
            return false;
        }
    }*/


    //tu cos jest zjebane i namieszane
    /*boolean validate(Account account, String url) throws SQLException {
        Statement statement = null;
        ResultSet resultCard = null;
        ResultSet resultPIN = null;
        try {
            statement = JDBCConnector.connect(url).createStatement();
            System.out.println("Enter your card number: ");
            String typedCard = account.cardNumber;
            resultCard = statement.executeQuery("SELECT number FROM card WHERE number = " + typedCard);
            System.out.println("Enter your PIN:");
            String typedPIN = account.PIN;
            Account targetAccount = findAccount(typedCard, typedPIN, url);
            resultPIN = statement.executeQuery("SELECT pin FROM card WHERE pin = " + typedPIN + ";" + "AND number = " + typedCard);
            if (resultCard.getString("number").equals(targetAccount.cardNumber) && resultPIN.getString("pin").equals(targetAccount.PIN)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            statement.close();
            resultCard.close();
            resultPIN.close();
        }
        return false;
    }*/


    /*boolean validateCard(Account acc, String url) throws SQLException {
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
    }*/

   /*void checkBalance() {
        System.out.println("Balance: " + balance);
        System.out.println();
    }*/

    void logOut() {
        System.out.println();
    }

    void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }
}
