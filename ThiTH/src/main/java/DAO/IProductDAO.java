package DAO;

import model.Product;

import java.sql.SQLException;
import java.util.List;

public interface IProductDAO {
    void insertProduct(Product product) throws SQLException;

    Product selectProduct(int id);

    List<Product> selectAllProduct();

    boolean deleteProduct(int id) throws SQLException;

    boolean updateProduct(Product product) throws SQLException;
//    phân trang

    List<Product> selectProductPagging( String s);

    Product selectProductByName(String _name);
}
