package org.group3.pp.views;

import org.group3.pp.dao.ProductDao;
import org.group3.pp.model.ProductModels;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ProductView {
    private static int DEFAULT_ROWS_PER_PAGE = 3;
    private static ProductDao productDao;
    public ProductView() {
        this.productDao = new ProductDao();
    }

    private final List<String[]> unsavedDataList = new ArrayList<>();
    public void start() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            int id = 0;
            productDao.queryDatabase(id, DEFAULT_ROWS_PER_PAGE);
            System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s\n", "F)irst", "P)revious", "N)ext", "L)ast", "G)oto", "Se)t row");

            System.out.println("\n*)Display");
            System.out.printf("\n%-15s %-15s %-15s %-15s %-15s %-15s\n", "W)rite", "R)ead", "D)elete", "U)pdate", "S)earch", "Sa)ve");
            System.out.printf("%-15s %-15s %-15s %-15s\n", "Un)aved", "Re)Store", "H)elp", "E)xit");

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
                    updateProductData();
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
                case "un": {
                    unsavedData();
                    break;
                }
                case "se": {
                     setRow();
                    break;
                }
                case "f": {
                    productDao.firstPage(DEFAULT_ROWS_PER_PAGE);
                    break;
                }
                case "n": {
                    int currentPage = getCurrentPage(); // Example method
                    productDao.nextPage(currentPage, DEFAULT_ROWS_PER_PAGE);
                    break;
                }
                case "p": {
                    // Get current page number from somewhere
                    int currentPage = getCurrentPage(); // Example method
                    productDao.previousPage(currentPage, DEFAULT_ROWS_PER_PAGE);
                    break;
                }
                case "l": {
                    productDao.lastPage(DEFAULT_ROWS_PER_PAGE);
                    break;
                }
                case "g": {
                    System.out.print("Enter the page number you want to go to: ");
                    int pageNumber = scanner.nextInt();
                    productDao.goToPage(pageNumber, DEFAULT_ROWS_PER_PAGE);
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
    private static void setRow() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of rows per page: ");
        int rowsPerPage = scanner.nextInt();
        if (rowsPerPage > 0) {
            DEFAULT_ROWS_PER_PAGE = rowsPerPage;
            System.out.println("Rows per page set to " + rowsPerPage);
            // Update the value in the database
            productDao.setRowsPerPage(rowsPerPage);
        } else {
            System.out.println("Invalid number of rows per page.");
        }
    }
//    private void showFirstPage() {
//        productDao.queryDatabase(1, DEFAULT_ROWS_PER_PAGE);
//    }
//
//    private void showNextPage() {
//        int nextPage = getCurrentPage() + 1;
//        int totalPages = productDao.getTotalPages(DEFAULT_ROWS_PER_PAGE);
//        if (nextPage <= totalPages) {
//            productDao.queryDatabase(nextPage, DEFAULT_ROWS_PER_PAGE);
//        } else {
//            System.out.println("Already at the last page.");
//        }
//    }
//
//    private void showPreviousPage() {
//        int previousPage = getCurrentPage() - 1;
//        if (previousPage >= 1) {
//            productDao.queryDatabase(previousPage, DEFAULT_ROWS_PER_PAGE);
//        } else {
//            System.out.println("Already at the first page.");
//        }
//    }
//
//    private void showLastPage() {
//        int totalPages = productDao.getTotalPages(DEFAULT_ROWS_PER_PAGE);
//        productDao.queryDatabase(totalPages, DEFAULT_ROWS_PER_PAGE);
//    }
//    private void showSpecificPage(int pageNumber) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter the page number you want to go to: ");
//        pageNumber = scanner.nextInt();
//        int totalPages = productDao.getTotalPages(DEFAULT_ROWS_PER_PAGE);
//        if (pageNumber >= 1 && pageNumber <= totalPages) {
//            productDao.queryDatabase(pageNumber, DEFAULT_ROWS_PER_PAGE);
//        } else {
//            System.out.println("Invalid page number.");
//        }
//    }
    private int getCurrentPage() {
        // Implement logic to get the current page
        return 1; // For demonstration purposes, return 1
    }



    ////////////////////////////////////////////////////

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
        Table table = new Table(4, BorderStyle.UNICODE_DOUBLE_BOX_WIDE, ShownBorders.ALL);
        table.addCell("Unsaved Product List", new CellStyle(CellStyle.HorizontalAlign.CENTER), 5);
        table.addCell("Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Qty", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Price", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("Imported Date", new CellStyle(CellStyle.HorizontalAlign.CENTER));

        table.setColumnWidth(0, 20, 30);
        table.setColumnWidth(1, 10, 30);
        table.setColumnWidth(2, 10, 30);
        table.setColumnWidth(3, 20, 30);

        Scanner scanner = new Scanner(System.in);
        System.out.println("\"I\" for Unsaved Insertion and \"U\" for Unsaved Update and \"B\" for back to main menu : ");
        String userInput = scanner.nextLine().toUpperCase();

        boolean hasData = false;

        switch (userInput) {
            case "I":
                for (String[] data : unsavedDataList) {
                    if (data.length == 4) {
                        hasData = true;
                        table.addCell(data[0], new CellStyle(CellStyle.HorizontalAlign.CENTER));
                        table.addCell(data[1], new CellStyle(CellStyle.HorizontalAlign.CENTER));
                        table.addCell(data[2], new CellStyle(CellStyle.HorizontalAlign.CENTER));
                        table.addCell(data[3], new CellStyle(CellStyle.HorizontalAlign.CENTER));
                    }
                }
                break;
            case "U":
                for (String[] data : unsavedDataList) {
                    if (data.length == 5) {
                        hasData = true;
                        table.addCell(data[1], new CellStyle(CellStyle.HorizontalAlign.CENTER));
                        table.addCell(data[2], new CellStyle(CellStyle.HorizontalAlign.CENTER));
                        table.addCell(data[3], new CellStyle(CellStyle.HorizontalAlign.CENTER));
                        table.addCell(data[4], new CellStyle(CellStyle.HorizontalAlign.CENTER));

                    }
                }
                break;
            case "B":
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }

        if (!hasData) {
            table.addCell("No Data", new CellStyle(CellStyle.HorizontalAlign.CENTER), 4);
        }

        System.out.println(table.render());

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    private void saveData() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to save Unsaved Inserted or Unsaved Updated? Please choose one of them!");
        System.out.println("\"UI\" for save Unsaved Insertion and \"UU\" for save Unsaved Update and \"B\" for back to main menu : ");
        String userInput = scanner.nextLine().toUpperCase();

        switch (userInput) {
            case "UI":
                saveUnsavedInsertedData();
                break;
            case "UU":
                saveUnsavedUpdatedData();
                break;
            case "B":
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }
    private void saveUnsavedInsertedData() {
        ProductDao productDao = new ProductDao();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (String[] data : unsavedDataList) {
            try {
                String name = data[1];
                int price = (int) Double.parseDouble(data[2]);
                int quantity = Integer.parseInt(data[3]);
                Date importedDate = dateFormat.parse(data[4]);
                productDao.insertData(name, price, quantity, String.valueOf(importedDate));
            } catch (NumberFormatException | ParseException e) {
                System.err.println("Error parsing data for product: " + e.getMessage());
            }
        }
        unsavedDataList.clear();
    }
    private void saveUnsavedUpdatedData() {
        ProductDao productDao = new ProductDao();

        for (String[] data : unsavedDataList) {
            try {
                int id = Integer.parseInt(data[0]);
                String newName = data[1];
                int newQty = Integer.parseInt(data[2]);
                double newPrice = Double.parseDouble(data[3]);
                productDao.updateProductData(id, newName, newQty, newPrice);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing data for product: " + e.getMessage());
            }
        }
        unsavedDataList.clear();
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

public void updateProductData() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter the ID of the product to update: ");
    int id = scanner.nextInt();
    scanner.nextLine(); // Consume newline character

    System.out.print("Enter the new name: ");
    String newName = scanner.nextLine();

    System.out.print("Enter the new quantity: ");
    int newQuantity = scanner.nextInt();

    System.out.print("Enter the new price: ");
    double newPrice = scanner.nextDouble();

    String[] data = {String.valueOf(id),newName, String.valueOf(newQuantity), String.valueOf(newPrice)};
    unsavedDataList.add(data);

    }
}
