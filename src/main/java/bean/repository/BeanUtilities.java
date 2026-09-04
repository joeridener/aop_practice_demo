package bean.repository;

import encryption.CryptoUtils;
import logging.MyLogging;
import net.beans.www.ContactFormBean;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Aspect
@Repository
public class BeanUtilities {

    private Connection connection;
    private ContactFormBean bean;
    private MyLogging logging;

    @Autowired
    public BeanUtilities(ContactFormBean contactFormBean, MyLogging myLogging)
    {
        this.bean=contactFormBean;
        this.logging = myLogging;
    }

    private void openConnection()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/aop_practice", "root","Joe_R4279#");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
