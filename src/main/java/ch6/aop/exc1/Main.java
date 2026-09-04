package ch6.aop.exc1;


import enumerations.Contact_Method;
import net.beans.www.ContactFormBean;
import net.config.www.MyProjectConfigurations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static AnnotationConfigApplicationContext context;
    private static ContactFormBean bean;
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        context = new AnnotationConfigApplicationContext(MyProjectConfigurations.class);

        bean = context.getBean(ContactFormBean.class);

        bean.setFirstName("Michael@");
        bean.setLastName("O'Reilley");
        bean.setTelephone("304-555-9821");
        bean.seteMail("michael.harrington@mail.com");
        bean.setMethod(Contact_Method.TELEPHONE);
        bean.setMessage("Please contact me tomorrow morning.");



    }
}