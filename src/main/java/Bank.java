
import javax.print.attribute.ResolutionSyntax;
import javax.swing.plaf.nimbus.State;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

public class Bank {
    final Scanner scanner = new Scanner(System.in);
    String menuAction;
    String activeAccountPin;
    String activeAccountCardNumber;

    public void setActiveAccountPin(String activeAccountPin) {
        this.activeAccountPin = activeAccountPin;
    }

    public void setActiveAccountCardNumber(String activeAccountCardNumber) {
        this.activeAccountCardNumber = activeAccountCardNumber;
    }

    private void setMenuAction(String menuAction) {
        this.menuAction = menuAction;
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

    void mainMenuCommands(Bank bank) {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
        bank.setMenuAction(scanner.nextLine());
        System.out.println();
    }

    void logOut() {
        System.out.println();
    }

    void exit() {
        System.out.println("Bye!");
        System.exit(0);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        statement.close();
        return account;
    }

    Account findAccount(String accountNumber, String url) throws SQLException {
        Account account = new Account(null, null);
        ResultSet rs = null;
        Statement statement = null;
        try {
            statement = JDBCConnector.connect(url).createStatement();
            rs = statement.executeQuery("SELECT number, pin FROM card WHERE number = " + accountNumber);
            if (rs.next() == false) {
                account.PIN = "0";
                account.cardNumber = "0";
                return account;
            }
            account.PIN = rs.getString("pin");
            account.cardNumber = rs.getString("number");
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs.close();
            statement.close();
        }
        return account;
    }

    boolean login(String url) throws SQLException {
        System.out.println("Enter your card number: ");
        String typedCard = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String typedPIN = scanner.nextLine();
        Account targetAccount = findAccount(typedCard, url);
        if (targetAccount == null) {
            return false;
        }
        setActiveAccountCardNumber(targetAccount.cardNumber);
        setActiveAccountPin(targetAccount.PIN);
        if (targetAccount == null) {
            return false;
        }
        if (!targetAccount.cardNumber.equals(typedCard) || !targetAccount.PIN.equals(typedPIN)) {
            return false;
        } else if (targetAccount.cardNumber.equals(typedCard) && targetAccount.PIN.equals(typedPIN)) {
            return true;
        } else {
            return false;
        }
    }

    void addIncome(double income, String url) throws SQLException {
        ResultSet rs = null;
        Statement statement = null;
        try {
            statement = JDBCConnector.connect(url).createStatement();
            rs = statement.executeQuery("SELECT number, balance FROM card where number = " + this.activeAccountCardNumber);
            double accountBalance = rs.getDouble("balance");
            rs.close();
            accountBalance += income;
            statement.executeUpdate("UPDATE card SET balance = " + accountBalance + " where number =  " + this.activeAccountCardNumber);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs.close();
            statement.close();
        }
    }

    void checkBalance(String url) throws SQLException {
        Statement statement = null;
        ResultSet rs = null;
        double accountBalance = 0;
        try {
            statement = JDBCConnector.connect(url).createStatement();
            rs = statement.executeQuery("SELECT balance FROM card where number = " + this.activeAccountCardNumber);
            accountBalance = rs.getDouble("balance");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs.close();
            statement.close();
        }

        System.out.println("Balance: " + accountBalance);
        System.out.println();
    }

    void closeAccount(String url) throws SQLException {
        Statement statement = null;
        try {
            statement = JDBCConnector.connect(url).createStatement();
            statement.executeUpdate("DELETE FROM card WHERE number = " + this.activeAccountCardNumber);
            setActiveAccountPin(null);
            setActiveAccountCardNumber(null);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            statement.close();
        }
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

    void doTransfer(String url) throws SQLException {
        ResultSet rs = null;
        String cardNumberToTransfer;
        double moneyToTransfer = 0;
        double accountBalance = 0;
        try (Statement statement = JDBCConnector.connect(url).createStatement()) {
            System.out.println("Enter card number: ");
            cardNumberToTransfer = scanner.nextLine();
            if (checkLuhn(cardNumberToTransfer) == false) {
                System.out.println("Probably you made mistake in the card number. Please try again!");
                System.out.println();
                return;
            }
            rs = statement.executeQuery("SELECT number from card where number = " + cardNumberToTransfer);
            if (rs.next() == false) {
                System.out.println("Such a card does not exist.");
                System.out.println();
                rs.close();
                return;
            } else if (rs.getString("number").equals(this.activeAccountCardNumber)) {
                System.out.println("You can't transfer money to the same account!");
                System.out.println();
                rs.close();
                return;
            } else {
                System.out.println("Enter how much money you want to transfer: ");
                moneyToTransfer = scanner.nextDouble();
                rs.close();
                rs = statement.executeQuery("SELECT balance from card where number = " + this.activeAccountCardNumber);
                accountBalance = rs.getDouble("balance");
                rs.close();

                if (accountBalance >= moneyToTransfer) {
                    statement.executeUpdate("UPDATE card SET balance = balance + " + moneyToTransfer + " where number = " + cardNumberToTransfer);
                    statement.executeUpdate("UPDATE card SET balance = balance - " + moneyToTransfer + " where number = " + this.activeAccountCardNumber);
                    System.out.println("Succes!");
                } else {
                    System.out.println("Not enough money!");
                }
                System.out.println();
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        rs.close();
        return;
    }
}
