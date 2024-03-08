package org.group3.pp.dao;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.*;

public class ProductDao {
    private static final String URL = "jdbc:postgresql://localhost:5432/stock_management_system";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "0703";

    public int getTotalPages(int rowsPerPage) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int totalRecords = 0;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String countQuery = "SELECT COUNT(*) FROM products";
            statement = connection.prepareStatement(countQuery);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalRecords = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Calculate total pages based on total records and rows per page
        return (int) Math.ceil((double) totalRecords / rowsPerPage);
    }
    public int getTotalRecords() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int totalRecords = 0;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String countQuery = "SELECT COUNT(*) FROM products";
            statement = connection.prepareStatement(countQuery);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalRecords = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return totalRecords;
    }

//    public int getRowsPerPage() {
//        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery("SELECT value FROM configuration WHERE key = 'rowsPerPage'")) {
//            if (resultSet.next()) {
//                return resultSet.getInt("value");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 3; // Default value
//    }
    public void setRowsPerPage(int rowsPerPage) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("UPDATE configuration SET value = ? WHERE key = 'rowsPerPage'")) {
            statement.setInt(1, rowsPerPage);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void firstPage(int rowsPerPage) {
        queryDatabase(1, rowsPerPage);
    }
    public void lastPage(int rowsPerPage) {
        queryDatabase(getTotalPages(rowsPerPage), rowsPerPage);
    }
    public void nextPage(int currentPage, int rowsPerPage) {
        queryDatabase(currentPage + 1, rowsPerPage);
    }
    public void previousPage(int currentPage, int rowsPerPage) {
        queryDatabase(currentPage - 1, rowsPerPage);
    }
    public void goToPage(int pageNumber, int rowsPerPage) {
        queryDatabase(pageNumber, rowsPerPage);
    }
    public void queryDatabase(int pageNumber, int rowsPerPage) {
        if (pageNumber <= 0) {
            pageNumber = 1;
        }

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sqlQuery = "SELECT * FROM products ORDER BY id LIMIT ? OFFSET ?";
            int offset = Math.max(0, (pageNumber - 1) * rowsPerPage);
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, rowsPerPage);
            statement.setInt(2, offset);
            resultSet = statement.executeQuery();

            // Processing the result set
            Table table = new Table(5, BorderStyle.UNICODE_DOUBLE_BOX_WIDE, ShownBorders.ALL);
            table.setColumnWidth(0, 10, 40);
            table.setColumnWidth(1, 20, 40);
            table.setColumnWidth(2, 15, 40);
            table.setColumnWidth(3, 15, 40);
            table.setColumnWidth(4, 20, 40);

            table.addCell("ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("Stock Quantity", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("Unit Price", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("Imported Date", new CellStyle(CellStyle.HorizontalAlign.CENTER));

            while (resultSet.next()) {

                // Accessing columns by name or index
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double unitPrice = resultSet.getDouble("price");
                int stockQuantity = resultSet.getInt("qty"); // Corrected column name
                String importedDate = resultSet.getString("imported_date");

                table.addCell(String.valueOf(id), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(name, new CellStyle(CellStyle.HorizontalAlign.LEFT));
                table.addCell(String.valueOf(stockQuantity), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell("$" + unitPrice, new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(importedDate, new CellStyle(CellStyle.HorizontalAlign.CENTER));
            }

            // Add pagination information
            table.addCell("Page: " + pageNumber + " of " + getTotalPages(rowsPerPage), new CellStyle(CellStyle.HorizontalAlign.CENTER), 2);
            table.addCell("Total record: " + getTotalRecords(), new CellStyle(CellStyle.HorizontalAlign.CENTER), 3);

            System.out.println(table.render());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void insertData(String name, int qty, double price, String importedData) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sqlQuery = "INSERT INTO products (name,qty, price, importedData) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, name);
            statement.setString(2, String.valueOf(qty));
            statement.setDouble(3, price);
            statement.setString(4, importedData);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteData(int id) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sqlQuery = "DELETE FROM products WHERE id = ?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Data deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void searchByName(String name) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sqlQuery = "SELECT * FROM products WHERE name LIKE ?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, "%" + name + "%"); // Using LIKE operator with wildcard '%' to search for partial matches
            resultSet = statement.executeQuery();
            Table table = new Table(5, BorderStyle.UNICODE_DOUBLE_BOX_WIDE, ShownBorders.ALL);
            table.setColumnWidth(0, 10, 40);
            table.setColumnWidth(1, 20, 40);
            table.setColumnWidth(2, 15, 40);
            table.setColumnWidth(3, 15, 40);
            table.setColumnWidth(4, 20, 40);
            table.addCell("Product List", new CellStyle(CellStyle.HorizontalAlign.CENTER), 5);
            table.addCell("ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("Stock Quantity", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("Unit Price", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("Imported Date", new CellStyle(CellStyle.HorizontalAlign.CENTER));

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String productName = resultSet.getString("name");
                int qty = Integer.parseInt(resultSet.getString("qty"));
                double price = resultSet.getDouble("price");
                String importedDate = resultSet.getString("imported_date");

                table.addCell(String.valueOf(id), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(productName, new CellStyle(CellStyle.HorizontalAlign.LEFT));
                table.addCell(String.valueOf(qty), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell("$" + price, new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(importedDate, new CellStyle(CellStyle.HorizontalAlign.CENTER));

            }
            table.addCell("Page: ", new CellStyle(CellStyle.HorizontalAlign.CENTER), 2);
            table.addCell("Total: ", new CellStyle(CellStyle.HorizontalAlign.CENTER), 3);

            System.out.println(table.render());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void searchByID(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sqlQuery = "SELECT * FROM products WHERE id = ?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            Table table = new Table(5, BorderStyle.UNICODE_DOUBLE_BOX_WIDE, ShownBorders.ALL);
            table.setColumnWidth(0, 10, 40);
            table.setColumnWidth(1, 20, 40);
            table.setColumnWidth(2, 15, 40);
            table.setColumnWidth(3, 15, 40);
            table.setColumnWidth(4, 20, 40);
            table.addCell("Product List", new CellStyle(CellStyle.HorizontalAlign.CENTER), 5);
            table.addCell("ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("Name", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("Stock Quantity", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("Unit Price", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("Imported Date", new CellStyle(CellStyle.HorizontalAlign.CENTER));

            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String productName = resultSet.getString("name");
                int qty = Integer.parseInt(resultSet.getString("qty"));
                double price = resultSet.getDouble("price");
                String importedDate = resultSet.getString("imported_date");

                table.addCell(String.valueOf(productId), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(productName, new CellStyle(CellStyle.HorizontalAlign.LEFT));
                table.addCell(String.valueOf(qty), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell("$" + price, new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(importedDate, new CellStyle(CellStyle.HorizontalAlign.CENTER));

            }
            table.addCell("Page: ", new CellStyle(CellStyle.HorizontalAlign.CENTER), 2);
            table.addCell("Total: ", new CellStyle(CellStyle.HorizontalAlign.CENTER), 3);

            System.out.println(table.render());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateProductData(int id, String newName, int newQuantity, double newPrice) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sqlQuery = "UPDATE products SET name = ?, qty = ?, price = ? WHERE id = ?";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, newName);
            statement.setInt(2, newQuantity);
            statement.setDouble(3, newPrice);
            statement.setInt(4, id);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data updated successfully.");
            } else {
                System.out.println("No rows updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



}
