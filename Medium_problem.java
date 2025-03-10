import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database_name"; // Change to your DB URL
    private static final String USERNAME = "your_username"; // Change to your DB username
    private static final String PASSWORD = "your_password"; // Change to your DB password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false); // Start transaction handling

            while (true) {
                System.out.println("\n---- Menu ----");
                System.out.println("1. Create Product");
                System.out.println("2. Read Product");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        createProduct(connection, scanner);
                        break;
                    case 2:
                        readProduct(connection, scanner);
                        break;
                    case 3:
                        updateProduct(connection, scanner);
                        break;
                    case 4:
                        deleteProduct(connection, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        connection.close();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createProduct(Connection connection, Scanner scanner) {
        System.out.print("Enter ProductID: ");
        int productID = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.print("Enter ProductName: ");
        String productName = scanner.nextLine();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        String insertSQL = "INSERT INTO Product (ProductID, ProductName, Price, Quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, productID);
            preparedStatement.setString(2, productName);
            preparedStatement.setDouble(3, price);
            preparedStatement.setInt(4, quantity);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product created successfully.");
                connection.commit(); // Commit transaction
            } else {
                System.out.println("Failed to create product.");
                connection.rollback(); // Rollback transaction
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private static void readProduct(Connection connection, Scanner scanner) {
        System.out.print("Enter ProductID to fetch details: ");
        int productID = scanner.nextInt();

        String selectSQL = "SELECT * FROM Product WHERE ProductID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, productID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("ProductID: " + resultSet.getInt("ProductID"));
                System.out.println("ProductName: " + resultSet.getString("ProductName"));
                System.out.println("Price: " + resultSet.getDouble("Price"));
                System.out.println("Quantity: " + resultSet.getInt("Quantity"));
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateProduct(Connection connection, Scanner scanner) {
        System.out.print("Enter ProductID to update: ");
        int productID = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        System.out.print("Enter new ProductName: ");
        String productName = scanner.nextLine();
        System.out.print("Enter new Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter new Quantity: ");
        int quantity = scanner.nextInt();

        String updateSQL = "UPDATE Product SET ProductName = ?, Price = ?, Quantity = ? WHERE ProductID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, productName);
            preparedStatement.setDouble(2, price);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setInt(4, productID);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product updated successfully.");
                connection.commit(); // Commit transaction
            } else {
                System.out.println("Failed to update product.");
                connection.rollback(); // Rollback transaction
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private static void deleteProduct(Connection connection, Scanner scanner) {
        System.out.print("Enter ProductID to delete: ");
        int productID = scanner.nextInt();

        String deleteSQL = "DELETE FROM Product WHERE ProductID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, productID);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product deleted successfully.");
                connection.commit(); // Commit transaction
            } else {
                System.out.println("Failed to delete product.");
                connection.rollback(); // Rollback transaction
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
