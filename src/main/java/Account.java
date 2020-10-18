import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Account extends Bank {
    String cardNumber;
    String PIN;

    void logOut() {
        System.out.println("You have successfully logged out!");
        System.out.println();
    }

    void generateCardNumber(Account acc) {
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
        this.cardNumber = finalCardNumber;
    }

    void generatePinCode(Account acc) {
        System.out.println("Your card PIN:");
        Random rand = new Random();
        int generatedPIN = rand.nextInt(10000);
        this.PIN = String.valueOf(generatedPIN).format("%04d", generatedPIN);
    }

    void addIncome(Account acc, double income, String url) {
        acc.balance += income;
        QueryExecutor.executeQuery("UPDATE card SET balance = " + acc.balance + " where number =  " + acc.typedCardNumber, url);
    }

    void transferMoney(Account acc, String url) throws SQLException {
        System.out.println("Enter card number: ");
        String transferAccount = scanner.nextLine();
        Statement statement = JDBCConnector.connect(url).createStatement();
        checkLuhn(transferAccount);
        ResultSet result = statement.executeQuery("SELECT number FROM card WHERE number = " + transferAccount + ";");
        String transferAccountDB = result.getString("number");

        if(checkLuhn(transferAccount) == true){
            if (this.cardNumber.equals(transferAccount)) {
                System.out.println("You can't transfer money to the same account!");
                System.out.println();
                return;
            }else if (!transferAccount.equals(transferAccountDB)) {
                System.out.println("This card does not exist!");
                System.out.println();
                return;
            }else if(transferAccount.equals(transferAccountDB)){
                System.out.println("Enter how much money you want to transfer: ");
                double moneyToTransfer = scanner.nextDouble();
                if (acc.balance >= moneyToTransfer) {
                    acc.balance = acc.balance - moneyToTransfer;
                    statement.executeUpdate("UPDATE card SET balance = " + acc.balance + " WHERE number = " + acc.cardNumber + ";");
                    statement.executeUpdate("UPDATE card SET balance = balance + " + moneyToTransfer + " WHERE number = " + transferAccount + ";");
                    System.out.println("Succes!");
                    System.out.println();
                    return;
                } else {
                    System.out.println("Not enough money!");
                    return;
                }
            }
        } else if (checkLuhn(transferAccount) == false) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return;
        }
    }


    void closeAccount(Account acc, String url) throws SQLException {
        Statement statement = JDBCConnector.connect(url).createStatement();
        statement.executeUpdate("DELETE FROM card WHERE number = " + acc.cardNumber);
    }
}

