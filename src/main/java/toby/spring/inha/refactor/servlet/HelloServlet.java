package toby.spring.inha.refactor.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @WebServlet
 * > name: Servlet name
 * > urlPatterns: URL mapping -> http://localhost:8080/hello
 */

/**
 * http://localhost:8080/hello?username=world
 * HelloServlet.service
 * request = org.apache.catalina.connector.RequestFacade@524e72
 * response = org.apache.catalina.connector.ResponseFacade@37d112b6
 * username = world
 */

@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("HelloServlet.service");
        System.out.println("request = " + req);
        System.out.println("response = " + resp);

        String username = req.getParameter("username");
        System.out.println("username = "+ username);

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write("hello " + username);
    }
}
