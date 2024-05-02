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
                scanner.nextLine();
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
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Not able to write to specified file" + e.getMessage());
        }
    }

    private static void addPayment(Scanner scanner) {
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
            bufferedWriter.newLine();
            bufferedWriter.close();
            scanner.nextLine();
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
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        for(Transaction ledger :transactions) {
            System.out.printf("%-25s %-20s %-25s %-20s %-15.2f\n",
                    ledger.getDate(),
                    ledger.getTime(),
                    ledger.getVendor(),
                    ledger.getType(),
                    ledger.getPrice());
        }
    }

    private static void displayDeposits() {
        System.out.printf("%-25s %-20s %-20s %-25s %5s\n", "Date", "Time","Type", "Vendor", "Amount");
        boolean depositsFound = false;
        for (Transaction deposit : transactions){
            if (deposit.getPrice()>0){
                System.out.printf("%-25s %-20s %-25s %-20s %-15.2f\n",
                        deposit.getDate(), deposit.getTime(),deposit.getType(),deposit.getVendor(),deposit.getPrice());
                depositsFound = true;
            }
        }
        if(!depositsFound){
            System.out.println("No deposits have been found");
        }

    }

    private static void displayPayments() {

        System.out.printf("%-25s %-20s %-20s %-20s %5s\n", "Date", "Time","Type", "Vendor", "Amount");
        boolean paymentsFound = false;
        for (Transaction payment : transactions){
            if (payment.getPrice() <0){
                System.out.printf("%-25s %-20s %-25s %-20s %-15.2f\n",
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
                    LocalDate today = LocalDate.now();
                    LocalDate startOfMonth = today.withDayOfMonth(1);
                    filterTransactionsByDate(startOfMonth, today);
                    break;
                case "2":
                    LocalDate today2 = LocalDate.now();
                    LocalDate startLastMonth = today2.minusMonths(1).withDayOfMonth(1);
                    LocalDate endLastMonth = today2.minusMonths(1).withDayOfMonth(today2.minusMonths(1).lengthOfMonth());
                    filterTransactionsByDate(startLastMonth, endLastMonth);
                    break;

                case "3":
                    LocalDate today3 = LocalDate.now();
                    LocalDate startOfYear = today3.withDayOfYear(1);
                    filterTransactionsByDate(startOfYear,today3);
                    break;
                case "4":
                    LocalDate today4 = LocalDate.now();
                    LocalDate startLastYear = today4.minusYears(1).withDayOfYear(1);
                    LocalDate endLastYear = today4.minusYears(1).withDayOfYear(today4.minusYears(1).lengthOfYear());
                    filterTransactionsByDate(startLastYear,endLastYear);
                    break;
                case "5":
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

        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        System.out.printf("%-25s %-20s %-25s %15s\n", "Date", "Time", "Vendor", "Amount");
        boolean withinRange = false;
        for(Transaction transaction : transactions){

            LocalDate transDate = transaction.getDate();
            if(transDate.isAfter(startDate.minusDays(1)) && transDate.isBefore(endDate.plusDays(1))){
                System.out.printf("%-25s %-20s %-25s %15.2f\n", transaction.getDate(), transaction.getTime(), transaction.getVendor(),transaction.getPrice());
                withinRange = true;
            }

        }
        if (!withinRange){
            System.out.println("No transactions fall within the date range");
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
        System.out.printf("%-25s %-20s %-25s %15s\n", "Date", "Time", "Vendor", "Amount");
        boolean withinRange = false;
        for(Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)){
                System.out.printf("%-25s %-20s %-25s %15.2f\n",transaction.getDate(), transaction.getTime(), transaction.getVendor(), transaction.getPrice() );
                withinRange = true;
            }


        }
        if(!withinRange){
            System.out.println("No transactions match the specified vendor name");
        }
    }
}
