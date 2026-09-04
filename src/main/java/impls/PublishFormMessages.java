package impls;

import interfaces.PersistFormBean;
import net.beans.www.ContactFormBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class written for decoupling purposes originally. By the end of the project I use it unnecessarily; thus, coupled.
 */
public class PublishFormMessages implements PersistFormBean {

    private Connection connection;


    @Override
    public void openConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.setConnection(DriverManager.getConnection("jdbc:mysql://localhost:3306/aop_practice", "root", "Joe_R4279#"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void writeToDisk(Object obj) {

        this.openConnection();

        if(obj instanceof ContactFormBean )
        {ContactFormBean bean = (ContactFormBean) obj;
            try {

                PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO beans ( first_name, last_name, email, telephone, message, method) VALUES(?,?,?,?,?,?)");
                preparedStatement.setString(1, bean.getFirstName());
                preparedStatement.setString(2, bean.getLastName());
                preparedStatement.setString(3, bean.geteMail());
                preparedStatement.setString(4,bean.getTelephone());
                preparedStatement.setString(5,bean.getMessage());
                preparedStatement.setString(6,bean.getMethod().getLabel());

                preparedStatement.executeUpdate();
                preparedStatement.close();


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            finally {
                this.closeConnection();
            }

        }


    }

    @Override
    public ContactFormBean[] readRecords() {
        return new ContactFormBean[0];
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void closeConnection(){

        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
