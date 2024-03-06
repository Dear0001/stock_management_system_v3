package org.group3.pp.views;

import org.group3.pp.dao.ProductDao;
import org.group3.pp.model.ProductModels;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ProductView {
    private final List<String[]> unsavedDataList = new ArrayList<>();
    public void start() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            ProductDao productDao = new ProductDao();
            int id = 0;
            productDao.queryDatabase(id);
            System.out.printf("%-15s %-15s %-15s %-15s %-15s\n", "F)irst", "P)revious", "N)ext", "L)ast", "G)oto");

            System.out.println("\n*)Display");
            System.out.printf("\n%-15s %-15s %-15s %-15s %-15s %-15s\n", "W)rite", "R)ead", "D)elete", "U)pdate", "S)earch", "Sa)ve");
            System.out.printf("%-15s %-15s %-15s %-15s\n", "Us)aved", "Re)Store", "H)elp", "E)xit");

            System.out.println("\n----------------------------------------------------------");

            System.out.print("\nEnter your choice: ");
            choice = scanner.nextLine().toLowerCase();

            switch (choice) {
                case "w": {
                    writeData();
                    break;
                }
                case "r": {
                    readData();
                    break;
                }

                case "u": {
                    updateData();
                    break;
                }

                case "d": {
                   deleteData();
                    break;
                }

                case "s": {
                    searchDataByName();
                    break;
                }
                case "sa": {
                    saveData();
                    break;
                }
                case "us": {
                    unsavedData();
                    break;
                }

                case "f": {
                    // Handle first page operation
                    break;
                }
                case "n": {
                    // Handle next page operation
                    break;
                }

                case "p": {
                    // Handle previous page operation
                    break;
                }

                case "l": {
                    // Handle last page operation
                    break;
                }

                case "g": {
                    // Handle go to specific page operation
                    break;
                }

                case "e":
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (!choice.equals("e"));
    }

    private void writeData() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the product qty: ");
        int qty = scanner.nextInt();
        System.out.print("Enter the product price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character

        LocalDate date = LocalDate.now();

        ProductModels productModels = new ProductModels(name, qty, price, date);
        String[] data = {productModels.getName(), String.valueOf(productModels.getQty()), String.valueOf(productModels.getPrice()), productModels.getImportedDate().toString()};
        unsavedDataList.add(data);

        System.out.println("Data write successfully.");
    }

    private void readData() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ID to read product: ");
        int id = scanner.nextInt();
        ProductDao productDao = new ProductDao();
        productDao.searchByID(id);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }


    private void unsavedData() {
        displayUnsavedData();
    }

    private void displayUnsavedData() {
        System.out.println("Unsaved Data:");
        for (String[] data : unsavedDataList) {
            if (data.length >= 4) {
                // Check if the data entry is for an update or insert based on its length
                if (data.length == 5) {
                    // Assuming ID is at index 0 in unsavedUpdate data
                    System.out.println("Update - ID: " + data[0] + ", Name: " + data[1] + ", Qty: " + data[2] + ", Price: " + data[3] + ", Date: " + data[4]);
                } else if (data.length == 4) {
                    System.out.println("Insert - Name: " + data[0] + ", Qty: " + data[1] + ", Price: " + data[2] + ", Date: " + data[3]);
                } else {
                    System.out.println("Invalid data format: " + Arrays.toString(data));
                }
            } else {
                System.out.println("Invalid data format: " + Arrays.toString(data));
            }
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }




    private void saveData() {
        ProductDao productDao = new ProductDao();
        for (String[] data : unsavedDataList) {
            try {
                double price = Double.parseDouble(data[1]);
                int quantity = Integer.parseInt(data[2]);
                productDao.insertData(data[0], (int) price, quantity, data[3]);
            } catch (NumberFormatException e) {
                // Handle the error - log it, show a message to the user, etc.
                System.err.println("Error parsing data for product: " + e.getMessage());
                // You might want to skip saving this specific data or handle it differently based on your requirements
            }
        }
        unsavedDataList.clear();
    }


    private void updateData() {
        ProductDao productDao = new ProductDao();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ID to update product: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter the new product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the new product qty: ");
        String qty = scanner.nextLine();
        System.out.print("Enter the new product price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character

        // Get the current date as the imported date
        LocalDate date = LocalDate.now();

        String[] data = {String.valueOf(id), name, qty, String.valueOf(price), date.toString()};
        unsavedDataList.add(data);
    }




    private void searchDataByName(){
        ProductDao productDao = new ProductDao();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the product name to search: ");
        String name = scanner.nextLine();
        productDao.searchByName(name);
        System.out.println("Enter Press to continue....");
        scanner.nextLine();
    }
    private void deleteData(){
        ProductDao productDao = new ProductDao();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the product id to delete: ");
        int id = scanner.nextInt();
        productDao.deleteData(id);
    }
}
