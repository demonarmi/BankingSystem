import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();
        bank.mainMenuCommands(bank);
        do {
            switch (bank.menuAction) {
                case "1":
                    bank.createAccount(bank);
                    bank.mainMenuCommands(bank);
                    break;
                case "2":
                    bank.validateCard(bank);
                    bank.validatePIN(bank);
                    int nextAction = 5;
                    if (bank.typedPin.equals(bank.PIN) && bank.typedCardNumber.equals(bank.cardNumber)) {
                        System.out.println("You have successfully logged in!");
                        System.out.println();
                        do {
                            System.out.println("1. Balance");
                            System.out.println("2. Log out");
                            System.out.println("0. Exit");
                            nextAction = scanner.nextInt();
                            if (nextAction == 1) {
                                System.out.println();
                                bank.checkBalance(bank);
                            } else if (nextAction == 2) {
                                System.out.println();
                                bank.logOut(bank);
                                bank.mainMenuCommands(bank);
                                break;
                            } else if (nextAction == 0) {
                                bank.exit(bank);
                            } else {
                                System.out.println("ERROR");
                            }
                        } while (nextAction != 0);
                    } else {
                        if (!bank.typedPin.equals(bank.PIN) || !bank.typedCardNumber.equals(bank.cardNumber))
                            System.out.println("Wrong card number or PIN!");
                        System.out.println();
                        bank.mainMenuCommands(bank);
                        break;
                    }

                    if (nextAction == 0) {
                        bank.exit(bank);
                    }
                    break;

                case "0":
                    bank.exit(bank);
                    break;
                default:
                    bank.mainMenuCommands(bank);
            }
        } while (!bank.menuAction.equals("0"));
        bank.exit(bank);
    }
}
