
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlite:" + args[1];
        JDBCConnector.connect(url);
        JDBCConnector.createTable(url);

        final Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();
        Account acc = new Account();
        bank.mainMenuCommands(bank);
        do {
            switch (bank.menuAction) {
                case "1":
                    acc.createAccount(acc, url);
                    bank.mainMenuCommands(bank);
                    break;
                case "2":
                    bank.validateCard(acc);
                    bank.validatePIN(acc);
                    int nextAction = 9;
                    if (acc.typedCardNumber.equals(acc.cardNumber)  && acc.typedPin.equals(acc.PIN)) {
                        System.out.println("You have successfully logged in!");
                        System.out.println();
                        do {
                            System.out.println("1. Balance");
                            System.out.println("2. Add income");
                            System.out.println("3. Do transfer");
                            System.out.println("4. Close account");
                            System.out.println("5. Log out");
                            System.out.println("0. Exit");
                            nextAction = scanner.nextInt();
                            if (nextAction == 1) {
                                System.out.println();
                                acc.checkBalance();
                            } else if (nextAction == 2) {
                                System.out.println("Enter income: ");
                                acc.addIncome(acc, scanner.nextDouble(), url);
                                System.out.println("Income was added!");
                                System.out.println();
                            } else if (nextAction == 0) {
                                bank.exit();
                            } else if (nextAction == 3) {
                                acc.transferMoney(acc, url);
                            } else if (nextAction == 4) {
                                acc.closeAccount(acc, url);
                                System.out.println();
                                System.out.println("The account has been closed!");
                                System.out.println();
                            } else if (nextAction == 5) {
                                System.out.println();
                                bank.logOut();
                                bank.mainMenuCommands(bank);
                                break;
                            } else {
                                System.out.println("ERROR");
                            }
                        } while (nextAction != 0);
                    } else {
                        if (!acc.typedCardNumber.equals(acc.cardNumber)  || !acc.typedPin.equals(acc.PIN))
                            System.out.println("Wrong card number or PIN!");
                        System.out.println();
                        bank.mainMenuCommands(bank);
                        break;
                    }

                    if (nextAction == 0) {
                        bank.exit();
                    }
                    break;

                case "0":
                    bank.exit();
                    break;
                default:
                    bank.mainMenuCommands(bank);
            }
        } while (!bank.menuAction.equals("0"));
        bank.exit();
    }
}
