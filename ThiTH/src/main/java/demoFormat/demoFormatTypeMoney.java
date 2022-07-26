package demoFormat;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

@WebServlet(urlPatterns = "/money")
public class demoFormatTypeMoney extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        java.util.Currency usd = java.util.Currency.getInstance("USD");
//        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.GERMANY);
//        format.setCurrency(usd);
//        System.out.println(format.format(23));

        Locale lc = new Locale("ENGLISH","US");
        NumberFormat nf = NumberFormat.getCurrencyInstance(lc);

        String formatMoney =  nf.format(123456);
        System.out.println("money " + formatMoney);
        req.setAttribute("money", formatMoney);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/demoformat/index.jsp");
        requestDispatcher.forward(req,resp);
//        DecimalFormat formatter = new DecimalFormat("###,###,###");
//
//        System.out.println(formatter.format(1000000)+" $");
//        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/demoformat/index.jsp");
//        requestDispatcher.forward(req,resp);
    }
}
