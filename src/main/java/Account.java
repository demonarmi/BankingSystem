import java.util.Random;

public class Account extends Bank{
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
        this.cardNumber = finalCardNumber;
    }
    void generatePinCode(Account acc) {
        System.out.println("Your card PIN:");
        Random rand = new Random();
        int generatedPIN = rand.nextInt(10000);
        //this.PIN = String.format("%04d%" + generatedPIN);
        //int min = 0000;
        //int max = 9999;
        //int random_int = (int) (Math.random() * (max - min + 1) + min);
        //int generatedPIN = random_int;
        this.PIN = String.valueOf(generatedPIN).format("%04d", generatedPIN);
    }
    void addIncome(Account acc, double income, String url){
        acc.balance += income;
        executeStatement("UPDATE cards SET balance = " + income + " where number =  " + acc.typedCardNumber, url);
    }
    void transferMoney(Bank bank){
        System.out.println("Enter card number: ");
        String transferAccount = scanner.nextLine();
        checkLuhn(transferAccount);
        if(checkLuhn(transferAccount) == true){
            System.out.println("Enter how much money you want to transfer: ");
            double moneyToTransfer = scanner.nextDouble();
            if(bank.balance >= moneyToTransfer){
                bank.balance -= moneyToTransfer;
                System.out.println("Succes!");
                System.out.println();
            }else {
                System.out.println("Not enough money!");
            }
        }else if(checkLuhn(transferAccount) == false){
            System.out.println("Probably you made mistake in the card number. Please try again!");
        }
    }
}

