package controller;

import DAO.ProductDAO;
import model.Product;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

@WebServlet(urlPatterns = "/product")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 50, // 50MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;
    private String errors = "";

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
        if (this.getServletContext().getAttribute("listColor") == null) {
            this.getServletContext().setAttribute("listColor", productDAO.selectAllUser());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    showCreateForm(req, resp);
                    break;
                case "createupload":
//                    showFormUpload(req, resp);
                    break;
                case "edit":
                    showNewForm(req, resp);
                    break;
                case "delete":
                    deleteProduct(req, resp);
                    break;
                case "search":
//                    searchCountry(req, resp);
                    break;
                default:
//                    listUser(request, response);
                    listStudentPagging(req, resp);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        try {
            switch (action) {
                case "create":
                    insertProduct(req, resp);
                    break;
                case "edit":
                    updateProduct(req, resp);
                    break;
                case "createupload":
//                    insertUpload(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void insertProduct(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException  {
        Product product = new Product();
        boolean flag = true;
        Map<String, String> hasMap = new HashMap<String, String>();
        System.out.println(this.getClass() + " insertProduct whith validate.");

        try {
//            int id = Integer.parseInt(req.getParameter("id"));
//            product.setId(id);
            String producName = req.getParameter("productName");
            product.setProductName(producName);
            double price = Double.parseDouble(req.getParameter("price"));
            product.setPrice(price);
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            product.setQuantity(quantity);
            String description = req.getParameter("description");
            product.setDescription(description);
            int category = Integer.parseInt(req.getParameter("category"));
            product.setIdcategory(category);
            String color = req.getParameter("color");
            product.setColor(color);


            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            Validator validator = validatorFactory.getValidator();

            Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);

            System.out.println("User: " + product);
            if (!constraintViolations.isEmpty()) {
                errors = "<ul style='color: red;'>";
                for (ConstraintViolation<Product> constraintViolation : constraintViolations) {
                    errors += "<li style= font-size: 13px;>" + constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage()
                            + "</li>";
                }
                errors += "</ul>";
                req.setAttribute("user", product);
                req.setAttribute("errors", errors);

                System.out.println(this.getClass() + " !constraintViolations.isEmpty()");
//                req.getRequestDispatcher("/WEB-INF/admin/product/add-product.jsp").forward(req, resp);
            } else {
                if(quantity <= 0) {
                    flag = false;
                    req.setAttribute("errorQuantity", "quantity cannot be less than 0");
//                    req.getRequestDispatcher("/WEB-INF/admin/product/add-product.jsp").forward(req, resp);
                }
                if (price <= 0) {
                    flag = false;
                    req.setAttribute("errorPrice", "price cannot be less than 0");
//                    req.getRequestDispatcher("/WEB-INF/admin/product/add-product.jsp").forward(req, resp);
                }
                if (productDAO.findProductName(producName)) {
                    flag = false;
                    req.setAttribute("errorEmail1", "Email already exists!");
                    System.out.println(this.getClass() + " Email exits in database");
                }
                if (flag) {
                    productDAO.insertProduct(product);
                    List<Product> listProduct = productDAO.selectAllProduct();
                    req.setAttribute("listProduct", listProduct);
                    //req.setAttribute("successful", "$.extend($.gritter.options, { position: 'bottom-right' }), $.gritter.add({ title: 'Success', text: 'Add successful!', class_name: 'color success' }), !1 ");
                    req.getRequestDispatcher("/WEB-INF/user/list.jsp").forward(req, resp);
                } else {
                    // Error: Email exits in database
                    // Error: Country invalid
                    errors = "<ul style='color: red;'>";
                    hasMap.forEach(new BiConsumer<String, String>() {
                        @Override
                        public void accept(String keyError, String valueError) {
                            errors += "<li style= font-size: 13px;>" + valueError + "</li>";
                        }
                    });
                    errors += "</ul>";
                    req.setAttribute("product", product);
                    req.setAttribute("errors", errors);
                    System.out.println(this.getClass() + " error database and country");
                    //req.setAttribute("errorsCreate", "$.extend($.gritter.options, { position: 'bottom-right' }), $.gritter.add({ title: 'Error!', text: 'You modified information failed!', class_name: 'color danger' }), !1;");
                    req.getRequestDispatcher("/WEB-INF/user/create.jsp").forward(req, resp);
                }
            }
        } catch (NumberFormatException ex) {
            System.out.println(this.getClass() + " NumberFormatException: Product info from requst: " + product);
            errors = "<ul style='color: red;'>"
                    + "<li style= font-size: 13px;> Input format not right </li>"
                    + "</ul>";
            req.setAttribute("user", product);
            req.setAttribute("errors", errors);
           // req.setAttribute("errorsCreate", "$.extend($.gritter.options, { position: 'bottom-right' }), $.gritter.add({ title: 'Error!', text: 'You modified information failed!', class_name: 'color danger' }), !1;");
            req.getRequestDispatcher("/WEB-INF/user/create.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProduct(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {


        //*********************************

//        int id = Integer.parseInt(req.getParameter("id"));
//        String name = req.getParameter("productName");
//        double email = Double.parseDouble(req.getParameter("price"));
//        int quantity = Integer.parseInt(req.getParameter("quantity"));
//        String color = req.getParameter("color");
//        String description = req.getParameter("description");
//        int category = Integer.parseInt(req.getParameter("category"));
//
//        Product user = new Product(id, name, email, quantity, color, description, category);
//        productDAO.updateProduct(user);
//        List<Product> listProduct = productDAO.selectAllProduct();
//        req.setAttribute("listProduct", listProduct);
//        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/list.jsp");
//        dispatcher.forward(req, resp);

        // ******************************

        Product product = new Product();
        boolean flag = true;
        Map<String, String> hasMap = new HashMap<String, String>();
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            product.setId(id);
            String producName = req.getParameter("productName");
            product.setProductName(producName);
            double price = Double.parseDouble(req.getParameter("price"));
            product.setPrice(price);
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            product.setQuantity(quantity);
            String description = req.getParameter("description");
            product.setDescription(description);
            int category = Integer.parseInt(req.getParameter("category"));
            product.setIdcategory(category);
            String color = req.getParameter("color");
            product.setColor(color);

            ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
            Validator validator = validatorFactory.getValidator();

            Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);

            System.out.println("product: " + product);
            if (!constraintViolations.isEmpty()) {
                errors = "<ul style='color: red;'>";
                for (ConstraintViolation<Product> constraintViolation : constraintViolations) {
                    errors += "<li style= font-size: 13px;>" + constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage()
                            + "</li>";
                }
                errors += "</ul>";
                req.setAttribute("user", product);
                req.setAttribute("errors", errors);
                req.setAttribute("errorsCreate", "$.extend($.gritter.options, { position: 'bottom-right' }), $.gritter.add({ title: 'Error!', text: 'You modified information failed!', class_name: 'color danger' }), !1;");
                System.out.println(this.getClass() + " !constraintViolations.isEmpty()");
                req.getRequestDispatcher("/WEB-INF/user/edit.jsp").forward(req, resp);
            } else {
                if (quantity <= 0) {
                    flag = false;
                    req.setAttribute("errorQuantity", "quantity cannot be less than 0");
//                    req.getRequestDispatcher("/WEB-INF/admin/product/add-product.jsp").forward(req, resp);
                }
                if (price <= 0) {
                    flag = false;
                    req.setAttribute("errorPrice", "price cannot be less than 0");
//                    req.getRequestDispatcher("/WEB-INF/admin/product/add-product.jsp").forward(req, resp);
                }
                if (productDAO.findProductName(producName)) {
                    flag = false;
                    req.setAttribute("edit", "edit");
                    req.setAttribute("errorName", "Name already exists!");
                    System.out.println(this.getClass() + " Name exits in database");
                }
                if (flag) {
                    productDAO.updateProduct(product);
                    List<Product> listProduct = productDAO.selectAllProduct();
                    req.setAttribute("listProduct", listProduct);
                    // req.setAttribute("successful", "$.extend($.gritter.options, { position: 'bottom-right' }), $.gritter.add({ title: 'Success', text: 'Edit successful!', class_name: 'color success' }), !1 ");
                    req.getRequestDispatcher("/WEB-INF/user/list.jsp").forward(req, resp);
                } else {
                    // Error: Email exits in database
                    // Error: Country invalid
                    errors = "<ul style='color: red;'>";
                    hasMap.forEach(new BiConsumer<String, String>() {
                        @Override
                        public void accept(String keyError, String valueError) {
                            errors += "<li  style= font-size: 13px;>" + valueError + "</li>";
                        }
                    });
                    errors += "</ul>";
                    req.setAttribute("product", product);
                    req.setAttribute("errors", errors);
                    req.setAttribute("edit", "edit");
                    System.out.println(this.getClass() + " error database and country");
                    req.setAttribute("errorsCreate", "$.extend($.gritter.options, { position: 'bottom-right' }), $.gritter.add({ title: 'Error!', text: 'You modified information failed!', class_name: 'color danger' }), !1;");
                    req.getRequestDispatcher("/WEB-INF/user/edit.jsp").forward(req, resp);
                }
            }
        } catch (NumberFormatException ex) {
            System.out.println(this.getClass() + " NumberFormatException: Product info from requst: " + product);
            errors = "<ul style='color: red;'>"
                    + "<li style= font-size: 13px;> Input format not right </li>"
                    + "</ul>";
            req.setAttribute("user", product);
            req.setAttribute("errors", errors);
            req.setAttribute("errorsCreate", "$.extend($.gritter.options, { position: 'bottom-right' }), $.gritter.add({ title: 'Error!', text: 'You modified information failed!', class_name: 'color danger' }), !1;");
            req.getRequestDispatcher("/WEB-INF/user/edit.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteProduct(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(req.getParameter("id"));
        productDAO.deleteProduct(id);
        List<Product> listProduct = productDAO.selectAllProduct();
        req.setAttribute("listProduct", listProduct);
        listStudentPagging(req, resp);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/list.jsp");
        dispatcher.forward(req, resp);
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/user/create.jsp");
        requestDispatcher.forward(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Product Product = productDAO.selectProduct(id);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/user/edit.jsp");
        req.setAttribute("product", Product);
        dispatcher.forward(req, resp);

//        List<Product> listProduct = productDAO.selectAllProduct();
//        req.setAttribute("listProduct", listProduct);
//        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/user/create.jsp");
//        requestDispatcher.forward(req,resp);
    }

    private void listStudentPagging(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        int page = 1;
//        int recordsPerPage = 4;
        String s = "";
//        int idcountry = -1;
        if (req.getParameter("s") != null) {
            s = req.getParameter("s");
        }
//        if (req.getParameter("idcountry") != null) {
//            idcountry = Integer.parseInt(req.getParameter("idcountry"));
//        }
//        if (req.getParameter("page") != null) {
//            page = Integer.parseInt(req.getParameter("page"));
//        }
        // lấy số lượng dòng cho mỗi trang
        List<Product> listProduct = productDAO.selectProductPagging(s);
        int noOfRecords = productDAO.getNoOfRecords();
        // đánh số trang
//        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
        req.setAttribute("listProduct", listProduct);
//        req.setAttribute("noOfPages", noOfPages);
//        req.setAttribute("currentPage", page);
        req.setAttribute("s", s);
//        req.setAttribute("idcountry", idcountry);
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/user/list.jsp");
        view.forward(req, resp);
    }


}
