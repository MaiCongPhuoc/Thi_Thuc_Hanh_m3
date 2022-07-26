package DAO;

import model.Product;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {

    private String jdbcURL = "jdbc:mysql://localhost:3306/thith?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "123456789";
    private PreparedStatement stmt;

    private int noOfRecords;
    private static final String INSERT_PRODUCT_SQL = "INSERT INTO product (productName, price, quantity, color, description, idcategory) VALUES (?, ?, ?, ?, ?, ?);";

    private static final String SELECT_PRODUCT_BY_ID = "select id, productName, price, quantity, color, description, idcategory from product where id =?;";
    private static final String SELECT_ALL_PRODUCT = "select * from product;";

    private static final String SELECT_PRODUCT_BY_NAME = "select u.id, u.productName, u.price, u.quantity, u.color, u.description, u.idcategory" +
            " from product as u inner join category as c" +
            " where u.productName = ? and u.color = c.id;";
    private static final String DELETE_PRODUCT_SQL = "delete from product where id = ?;";
    private static final String UPDATE_PRODUCT_SQL = "update product set productName = ?, price = ?, quantity = ?, color = ?, description = ?, idcategory = ?  where id = ?;";
    private static final String SELECT_ALL_COLOR = "select * from thith.category;";

    private static final String SELECT_PRODUCTNAME = "SELECT productName FROM product;";

    public ProductDAO() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void insertProduct(Product product) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SQL)) {
            System.out.println(this.getClass() + " insertProduct: " + preparedStatement);
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setString(4, product.getColor());
            preparedStatement.setString(5, product.getDescription());
            preparedStatement.setInt(6, product.getIdcategory());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public Product selectProduct(int id) {
        Product product = null;
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_ID)) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                String name = rs.getString("productName");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String color = rs.getString("color");
                String description = rs.getString("description");
                int idcategory = rs.getInt("idcategory");
                product = new Product(id, name, price, quantity, color, description, idcategory);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return product;
    }

    @Override
    public List<Product> selectAllProduct() {
        List<Product> products = new ArrayList<>();
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PRODUCT)) {
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("id");
                String productName = rs.getString("productName");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String color = rs.getString("color");
                String description = rs.getString("description");
                int idcategory = rs.getInt("idcategory");
                products.add(new Product(id, productName, price, quantity, color, description, idcategory));
            }
//            rs.previous();
        } catch (SQLException e) {
            printSQLException(e);
        }
        return products;
    }

    @Override
    public boolean deleteProduct(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_SQL)) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean updateProduct(Product product) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_SQL)) {
            statement.setString(1, product.getProductName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setString(4, product.getColor());
            statement.setString(5, product.getDescription());
            statement.setInt(6, product.getIdcategory());
            statement.setInt(7, product.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public List<Product> selectProductPagging(String s) {
        List<Product> list = new ArrayList<>();
        Product product = null;
        Connection connection = getConnection();
        try {
            String query = "select * from product where productName like ?" ;
            stmt = connection.prepareStatement(query);
            stmt.setString(1, '%' + s + '%');
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setProductName(rs.getString("productName"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setColor(rs.getString("color"));
                product.setDescription(rs.getString("description"));
                product.setIdcategory(rs.getInt("idcategory"));
                list.add(product);
            }
            rs.close();

//            rs = stmt.executeQuery("SELECT FOUND_ROWS()");
//            if (rs.next())
//                this.noOfRecords = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public Product selectProductByName(String _name) {
        Product product = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCT_BY_NAME)) {
            preparedStatement.setString(1, _name);
            System.out.println(this.getClass() + " selectProductByName: " + preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String productName = rs.getString("productName");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String color = rs.getString("color");
                String description = rs.getString("description");
                int idcategory = rs.getInt("idcategory");
                product = new Product(id, productName, price, quantity, color, description, idcategory);
                return product;
            }
            return null;
        } catch (SQLException e) {
            printSQLException(e);
            return null;
        }
    }

    public List<User> selectAllUser() {
        // using try-with-resources to avoid closing resources (boiler plate code)
        List<User> listUser = new ArrayList<>();
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COLOR)) {

            System.out.println(this.getClass() + " selectAllUser: " + preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("id");
                String category = rs.getString("category");
                listUser.add(new User(id, category));
            }
//            rs.previous();
        } catch (SQLException e) {
            printSQLException(e);
        }
        return listUser;
    }

    public int getNoOfRecords() {
        return noOfRecords;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }


    public boolean findProductName(String name) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCTNAME)) {
            System.out.println(this.getClass() + "findProductName: " + preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                if (name.equals(rs.getString("productName"))) {
                    return true;
                }
            }
        }
        return false;
    }


}
