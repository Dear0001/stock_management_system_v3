package org.group3.pp.dao;

import org.group3.pp.model.ProductModels;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.*;
import java.time.LocalDate;

public class ProductDao {
    private static final String URL = "jdbc:postgresql://localhost:5432/stock_management_system";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "0703";

    public ProductModels queryDatabase(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establishing connection to the database
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sqlQuery = "SELECT * FROM products";
            statement = connection.prepareStatement(sqlQuery);
            resultSet = statement.executeQuery();

            // Processing the result set
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

                // Accessing columns by name or index
                id = resultSet.getInt("id");
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

            // Add page and total information
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
        return null;
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

//    public void updateData(int id, String name, int qty, double price, String importedDate) {
//        Connection connection = null;
//        PreparedStatement statement = null;
//
//        try {
//            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//            String sqlQuery = "UPDATE products SET name = ?, qty = ?, price = ?, importedDate = ? WHERE id = ?";
//
//            statement = connection.prepareStatement(sqlQuery);
//            statement.setString(1, name);
//            statement.setInt(2, qty);
//            statement.setDouble(3, price);
//            statement.setString(4, importedDate);
//            statement.setInt(5, id);
//
//            int rowsUpdated = statement.executeUpdate();
//            if (rowsUpdated > 0) {
//                System.out.println("Data updated successfully.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }


    public void insertData(String name, int qty, double price, LocalDate importedDate) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sqlQuery = "INSERT INTO products (name, qty, price, importedDate) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sqlQuery);
            statement.setString(1, name);
            statement.setInt(2, qty);
            statement.setDouble(3, price);
            statement.setObject(4, importedDate);

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
}
