package hu.etterem.servlet;

import com.vaadin.spring.server.SpringVaadinServlet;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Created by Murdoc on 4/14/2017.
 */
@Component("vaadinServlet")
public class CustomVaadinServlet extends SpringVaadinServlet {

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
    }
}
