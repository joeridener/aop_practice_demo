package bean.repository;

import logging.MyLogging;
import net.beans.www.ContactFormBean;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Repository;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.sql.*;

/**
 * The sole purpose ofm this class is to save the SecretKey and IvParameterSpec data-types to MySQL. Each bean has it's own unique encryptpion key and each key uses
 * the beanID to indentify both the bean and the SecretKey that decrypts it's data. The class is marked with Aspect and did cause some confusion. This is among
 * the first projects using AOP that I have written and the AOP method would not automatically fire with it's given target. Likely due to it as the
 * second source code file marked with Aspect annotation and a source of unexplained problems.
 */
@Aspect
@Repository
public class EncryptedStorage {

    private static final Logger log = LoggerFactory.getLogger(EncryptedStorage.class);
    private ContactFormBean bean;
    private AnnotationConfigApplicationContext context;
    private Connection connection;
    private MyLogging myLogging;
    private SecretKey secretKey;
    private IvParameterSpec ivParameterSpec;


    public EncryptedStorage(){}

    @Autowired
    public EncryptedStorage(ContactFormBean bean, AnnotationConfigApplicationContext annotationConfigApplicationContext, MyLogging logging)
    {
        this.setBean(bean);
        this.setContext(annotationConfigApplicationContext);
        this.setMyLogging(logging);
        this.initializeEncryptedFields();
    }

    @Around("execution( * bean.repository.EncryptedStorage.getBeanId(..))")
    public Object storeSecretKey(ProceedingJoinPoint joinPoint)
    {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        if(bean.getId() == 0){
            return result;
        }

        this.openConnection();
        Connection c = this.getConnection();
        try {
            PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO secret_keys(id,secret_key, iv_index) VALUES (?,?,?)");

            preparedStatement.setInt(1, bean.getId());
            preparedStatement.setBytes(2, this.secretKey.getEncoded());
            preparedStatement.setBytes(3, this.ivParameterSpec.getIV());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            this.getConnection().close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private void persistEncryptionKeys()
    {
        this.openConnection();
        Connection c = this.getConnection();
        try {
            PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO secret_keys(id,secret_key, iv_index) VALUES (?,?,?)");

            preparedStatement.setInt(1, bean.getId());
            preparedStatement.setBytes(2, this.secretKey.getEncoded());
            preparedStatement.setBytes(3, this.ivParameterSpec.getIV());

            preparedStatement.executeUpdate();

            preparedStatement.close();
            this.getConnection().close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <h1>My UML</h1>
     * <p>Returns int </p>
     * @return beanID
     */
    public int getBeanId(){
        int id = 0;
        this.openConnection();
        id=this.queryId();
        bean.id(id);
        persistEncryptionKeys();
        return id;
    }


    /**
     * This is a UML.
     * @return
     */
    private Integer queryId()
    {
        int id=0;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM beans WHERE first_name=? AND last_name =?");


            preparedStatement.setString(1, bean.getFirstName() );
            preparedStatement.setString(2, bean.getLastName());

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            id = resultSet.getInt(1);

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Integer(id);
    }

    public void openConnection()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.setConnection(DriverManager.getConnection("jdbc:mysql://localhost:3306/aop_practice", "root","Joe_R4279#"));

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ContactFormBean getBean() {
        return bean;
    }

    public void setBean(ContactFormBean bean) {
        this.bean = bean;
    }

    public AnnotationConfigApplicationContext getContext() {
        return context;
    }

    public void setContext(AnnotationConfigApplicationContext context) {
        this.context = context;
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public MyLogging getMyLogging() {
        return myLogging;
    }

    public void setMyLogging(MyLogging myLogging) {
        this.myLogging = myLogging;
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public IvParameterSpec getIvParameterSpec() {
        return ivParameterSpec;
    }

    public void setIvParameterSpec(IvParameterSpec ivParameterSpec) {
        this.ivParameterSpec = ivParameterSpec;
    }
    private void initializeEncryptedFields()
    {
        this.setSecretKey(myLogging.getKey());
        this.setIvParameterSpec(myLogging.getIv());
    }

}
