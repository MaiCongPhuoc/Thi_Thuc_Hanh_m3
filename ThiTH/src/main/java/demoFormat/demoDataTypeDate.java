package demoFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(urlPatterns = "/date")
public class demoDataTypeDate extends HttpServlet {
    private static final String PATTERN_FORMAT = "dd-MM-yyyy HH:mm";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_FORMAT);
        String formatDate = format.format(today);
        req.setAttribute("time", formatDate);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/demoformat/index.jsp");
        requestDispatcher.forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
