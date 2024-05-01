package com.pluralsight;

import java.io.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>,<time>,<vendor>,<type>,<amount>
        // For example: 2023-04-29,13:45:00,Amazon,PAYMENT,29.99
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
        String line;

        try {

            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine())!=null) {
                String[] parts = line.split("\\|");
                if(parts.length ==5) {
                    LocalDate date = LocalDate.parse(parts[0].trim());
                    LocalTime time = LocalTime.parse(parts[1].trim());
                    String type = parts[2].trim();
                    String vendor = parts[3].trim();
                    double price = Double.parseDouble(parts[4]);
                    transactions.add(new Transaction(date,time,type,vendor,price));
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loaded inventory: " + e.getMessage());
        }

    }

    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Deposit` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
        System.out.println("Enter the date in format " + DATE_FORMAT);
        String userDate = scanner.nextLine();
        LocalDate date = LocalDate.parse(userDate, DATE_FORMATTER);
        System.out.println("Enter the time in format " + TIME_FORMAT);
        String userTime = scanner.nextLine();
        System.out.println("Enter the description for the Deposit");
        String type = scanner.nextLine();
        LocalTime time = LocalTime.parse(userTime, TIME_FORMATTER);
        System.out.println("Enter name of vendor: ");
        String userVendor = scanner.nextLine().trim();
        double userDeposit;
        do {
            System.out.println("Enter amount of deposit: ");
            while(!scanner.hasNextDouble()){
                System.out.println("Please enter a valid deposit amount");
                scanner.next();
            }
            userDeposit = scanner.nextDouble();
            if (userDeposit < 0.0) {
                System.out.println("Enter a positive deposit amount");
            }
        }while(userDeposit<=0.0);


        Transaction newTransaction= new Transaction(date,time,type,userVendor,userDeposit);
        transactions.add(newTransaction);


        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String line = String.format("%s|%s|%s|%s|%.2f", newTransaction.getDate(), newTransaction.getTime(),newTransaction.getType(), newTransaction.getVendor(),newTransaction.getPrice());
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("Not able to write to specified file" + e.getMessage());
        }
    }

    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Payment` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
        System.out.println("Enter the date " + DATE_FORMAT);
        String userDate = scanner.nextLine();
        LocalDate date = LocalDate.parse(userDate,DATE_FORMATTER);
        System.out.println("Enter the time " + TIME_FORMAT);
        String userTime = scanner.nextLine();
        LocalTime time = LocalTime.parse(userTime,TIME_FORMATTER);
        System.out.println("Enter the description for the Payment");
        String type = scanner.nextLine();
        System.out.println("Enter name of vendor: ");
        String userVendor = scanner.nextLine().trim();
        double userPayment;
       // do {
            System.out.println("Enter amount of payment: ");
            userPayment = scanner.nextDouble();
                if (userPayment > 0) {
                    userPayment = userPayment * -1;
                }





        Transaction newPayment= new Transaction(date,time,type,userVendor,userPayment);
        transactions.add(newPayment);



        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String line = String.format("%s|%s|%s|%s|%.2f", date.format(DATE_FORMATTER), time.format(TIME_FORMATTER),type, userVendor,userPayment);
            bufferedWriter.write(line);

        } catch (IOException e) {
            System.out.println("Not able to write to specified file" + e.getMessage());
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, type, and amount.
        for(Transaction ledger :transactions) {
            System.out.printf("%-25s %-20s %-25s %-15s %-15.2f\n",
                    ledger.getDate(),
                    ledger.getTime(),
                    ledger.getVendor(),
                    ledger.getType(),
                    ledger.getPrice());
        }
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
        System.out.printf("%-25s %-20s %-20s %-25s %5s\n", "Date", "Time","Type", "Vendor", "Amount");
        boolean depositsFound = false;
        for (Transaction deposit : transactions){
            if (deposit.getPrice()>0){
                System.out.printf("%-25s %-20s %-20s %-15s %15.2f\n",
                        deposit.getDate(), deposit.getTime(),deposit.getType(),deposit.getVendor(),deposit.getPrice());
                depositsFound = true;
            }
        }
        if(!depositsFound){
            System.out.println("No deposits have been found");
        }

    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
        System.out.printf("%-25s %-20s %-20s %-20s %5s\n", "Date", "Time","Type", "Vendor", "Amount");
        boolean paymentsFound = false;
        for (Transaction payment : transactions){
            while (payment.getPrice() <0){
                System.out.printf("%-25s %-20s %-20s %-15s %15.2f\n",
                        payment.getDate(), payment.getTime(),payment.getType(),payment.getVendor(),payment.getPrice());
                paymentsFound = true;
            }
        }
        if(!paymentsFound){
            System.out.println("No payments have been found");
        }

    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate today = LocalDate.now();
                    LocalDate startOfMonth = today.withDayOfMonth(1);
                    filterTransactionsByDate(startOfMonth, today);
                    break;
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate today2 = LocalDate.now();
                    LocalDate startLastMonth = today2.minusMonths(1).withDayOfMonth(1);
                    LocalDate endLastMonth = today2.minusMonths(1).withDayOfMonth(today2.minusMonths(1).lengthOfMonth());
                    filterTransactionsByDate(startLastMonth, endLastMonth);
                    break;

                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate today3 = LocalDate.now();
                    LocalDate startOfYear = today3.withDayOfYear(1);
                    filterTransactionsByDate(startOfYear,today3);
                    break;
                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate today4 = LocalDate.now();
                    LocalDate startLastYear = today4.minusYears(1).withDayOfYear(1);
                    LocalDate endLastYear = today4.minusYears(1).withDayOfYear(today4.minusYears(1).lengthOfYear());
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, vendor, and amount for each transaction.
                    System.out.println("Please enter the vendor name");
                    String vendorName = scanner.nextLine();
                    filterTransactionsByVendor(vendorName);
                    break;
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
            //scanner.close();
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
        System.out.printf("%-25s %-20s %-25s %15s\n", "Date", "Time", "Vendor", "Amount");
        boolean withinRange = false;
        for(Transaction transaction : transactions){
            LocalDate transDate = transaction.getDate();
            if(transDate.isAfter(startDate.minusDays(1)) && transDate.isBefore(endDate.plusDays(1))){
                System.out.printf("%-25s %-20s %-25s %15.2f\n", transaction.getDate(), transaction.getTime(), transaction.getVendor(),transaction.getPrice());
                withinRange = true;
            }
            if(!withinRange){
                System.out.println("No transactions fall within the date range");
            }
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
        System.out.printf("%-25s %-20s %-25s %15s\n", "Date", "Time", "Vendor", "Amount");
        boolean withinRange = false;
        for(Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)){
                System.out.printf("%-25s %-20s %-25s %15.2f\n",transaction.getDate(), transaction.getTime(), transaction.getVendor(), transaction.getPrice() );
            }
            if(!withinRange){
                System.out.println("No transactions match the specified vendor name");
            }

        }
    }
}
