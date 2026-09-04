package logging;

import bean.repository.BeanUtilities;
import bean.repository.EncryptedStorage;
import ch6.aop.exc1.Main;
import encryption.CryptoUtils;
import enumerations.Contact_Method;
import impls.PublishFormMessages;
import interfaces.PersistFormBean;
import net.beans.www.ContactFormBean;
import net.config.www.MyProjectConfigurations;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import validation.ValidateContactFormUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

@Component
@Aspect
/**
 * As I was coding this file I became slightly off track and incorporated other methods for the sake of practicing AOP expression language.
 * Originally intended for logging using the Aspect annotation, and I simply added cryptography classes. Once the project is run it encrypts
 * and decrypts the data for simulated security purposes. As I write applications that border on publicly available information as well as
 * those that might visit my sites.
 */
public class MyLogging {


    /**
     * This program is primarily for practice purposes. The Lazy annotation is unecessary. It was part of an attempt to get the project to run properly
     * when marking multiple files with Aspect annotations. When the program finally ran correctly it appeared to be due to a source code file marked
     * with the Aspect annotation.
     */
    @Autowired
    @Lazy
    private BeanUtilities beanUtilities;
    /**
     * I am not familiar with lgger. it was the second time I used it. I am leaning toward the industry standard from my former System.print statements.
     */
    private static final Logger logger = LoggerFactory.getLogger(MyLogging.class);
    @Autowired
    private AnnotationConfigApplicationContext context= Main.context;
    @Autowired
    private ContactFormBean bean;
    private SecretKey key=CryptoUtils.generateKey();
    private IvParameterSpec iv=CryptoUtils.generateIv();
    @Autowired
    @Lazy
    /**
     * The lazy annotation forces loading in the context when it was called as an attempt at debugging an issue that was fixed elsewhere in the
     * project.
     */
    private EncryptedStorage encryptedStorage;


    /**
     * <h1>@Around("execution(* net.beans.www.*.set*(..))")
     *     public Object printSetter(ProceedingJoinPoint joinPoint)</h1>
     *     <p>loging statements to track the flow of execution while learning AOP></p>
     *     <p>Object result is created and assigned the value of the returning joinPoint.proceed() call.</p>
     * @param joinPoint
     * @return joinPoint.proceed();
     */
    @Around("execution(* net.beans.www.*.set*(..))")
    public Object printSetter(ProceedingJoinPoint joinPoint){
        Object result;
        try {
            logger.info( new java.util.Date().toString() );
            result = joinPoint.proceed();
            logger.info("Method Executed!");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * <h1>@Around("execution(* net.beans.www.ContactFormBean.setFirstName(..))")
     *     public Object validationFirstName(ProceedingJoinPoint joinPoint)</h1>
     * This AOP method is automatically ran to supplement data entered into a simulated contact form.
     * <p>It checks each string char comparing it to allowable ASCII characters.</p>
     * <p>ValidateContactFormUtil.validateFirstName(bean.getFirstName()) is a static method and marks illegal chars with errors.</p>
     * <p>Logger supplements console output with informative updates on conclusions of validation.</p>
     *
     * @param joinPoint
     * @return joinPoint.proceed() Object
     */
    @Around("execution(* net.beans.www.ContactFormBean.setFirstName(..))")
    public Object validationFirstName(ProceedingJoinPoint joinPoint){
        Object result=new Object();
        try {
            result=joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        logger.info("Begin form validation...");
        logger.info("Bean initialized");
        logger.info(bean.toString());
        if( ValidateContactFormUtil.validateFirstName(bean.getFirstName()) )
            logger.error("Errors contained in bean firstName INVALID STRING!!!");
        else
            logger.info("No errors found in input string for firstName");

        logger.info("Exiting AOP method");

        return result;
    }

    /**
     * <h1>@Around("execution(* net.beans.www.ContactFormBean.setLastName(..) )")
     *     public Object validateLastName(ProceedingJoinPoint joinPoint)</h1>
     * This AOP method is automatically ran to supplement data entered into a simulated contact form.
     * <p>It checks each string char comparing it to allowable ASCII characters.</p>
     * <p>ValidateContactFormUtil.validateFirstName(bean.getFirstName()) is a static method and marks illegal chars with errors.</p>
     * <p>Logger supplements console output with informative updates on conclusions of validation.</p>
     *
     * @param joinPoint
     * @return joinPoint.proceed() Object
     */
    @Around("execution(* net.beans.www.ContactFormBean.setLastName(..) )")
    public Object validateLastName(ProceedingJoinPoint joinPoint)
    {
        Object obj = new Object();

        logger.info("Validation of lastName string start");
        try {
            obj=joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if( ValidateContactFormUtil.validateLastName(bean.getLastName()) )
        {
            logger.error("String lastName contains errors. INVALID STRING!!!");
        }else logger.info("String lastName passed validation method. SUCCESS!");

        return obj;
    }


    /**
     * <h1>@Around("execution( * net.beans.www.ContactFormBean.set*(..))")
     *     public Object callPublisher(ProceedingJoinPoint joinPoint)</h1>
     *     <p>Line 165 checks that all fields of Class ContactFormBean have been initialized =! null.</p>
     *     <p>Once all instance variables have been set Class PublishFormMessages publishFormMessages = new PublishFormMessages(); which is a class marked with
     *     the Repository annotation. Text I am studying uses terminologyu publish; however, it is a hand-coded JDBC source-code file.</p>
     * @param joinPoint
     * @return result = joinPoint.proceed();
     */
    @Around("execution( * net.beans.www.ContactFormBean.set*(..))")
    public Object callPublisher(ProceedingJoinPoint joinPoint){
        Object result= new Object();

        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        if( bean.getFirstName() != null && bean.getLastName() != null && bean.geteMail() != null && bean.getTelephone() != null && bean.getMethod() != null && bean.getMessage() != null )
        {}else{return result;}



            PublishFormMessages publishFormMessages = new PublishFormMessages();
            //method title writeToDIsk was not properly named by me.
            //it is saved on MySQL.
            publishFormMessages.writeToDisk(this.bean);
            logger.info("");
            logger.info("FormBean persisted please check MySQL to ensure.");
            logger.info("");

            //beanId is an int primative data type. This value is auto-incremented and auto-created by MySQL. Therefore, the instance
            //of ContactFormBean must be instantiated and then written to the repository prior to the capability of accessing the id's value.
            //it is also encrypted automatically by AOP; thus, requires decryption.
            bean.id((int)encryptedStorage.getBeanId());

            System.out.println("BEAN ID ======== "+bean.getId());




        return result;
    }


    /**
     * <h1>@Around( "execution( * net.beans.www.ContactFormBean.set*(..))")
     *     public Object printInstanceVariables(ProceedingJoinPoint joinPoint)</h1>
     *
     *     <p>Method is called whenever bean.set * methods are called. So testing for null values is necessary that method will not fire prior to bean fields
     *     being set. Each value is decrypted and then print to console using Logger.</p>
     * @param joinPoint
     * @return
     */
    @Around( "execution( * net.beans.www.ContactFormBean.set*(..))")
    public Object printInstanceVariables(ProceedingJoinPoint joinPoint)
    {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        if( bean.getFirstName() != null && bean.getLastName() != null && bean.geteMail() != null && bean.getTelephone() != null && bean.getMethod() != null && bean.getMessage() != null )
        {}else{return result;}

        String firstName;
        String lastName;
        String telephone;
        String eMail;
        String message;

        try {
            firstName = CryptoUtils.decrypt(bean.getFirstName(), this.key, this.iv);
            lastName = CryptoUtils.decrypt(bean.getLastName(), key, iv);
            telephone = CryptoUtils.decrypt(bean.getTelephone(), key, iv);
            eMail = CryptoUtils.decrypt((bean.geteMail()), key, iv);
            message = CryptoUtils.decrypt(bean.getMessage(), key, iv);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String firstName0 = String.format("Contact First Name: %s", firstName);
        String lastName0 = String.format("Contact Last Name: %s", lastName);
        String telephone0 = String.format("Contact Telephone Number: %s", telephone);
        String eMail0 = String.format("Contact E-Mail: %s", eMail);
        String method = String.format("Contact Preferred Method of Contact: %s", bean.getMethod().getLabel());
        String message0 = String.format("Contact Message: %s", message);

        logger.info("Printing Instance Variables from Spring Bean Component");
        logger.info("");
        logger.info(firstName0);
        logger.info(lastName0);
        logger.info(telephone0);
        logger.info(eMail0);
        logger.info(method);
        logger.info(message0);
        logger.info("Object.result.toString() ");

        return  result;
    }

    @Around("execution(* net.beans.www.ContactFormBean.set*(..))")
    public Object encryption( ProceedingJoinPoint joinPoint)
    {

        Object result=null;

        try {

            Object[] objs = joinPoint.getArgs();
            Object[] objs0 = joinPoint.getArgs();
            String field = (objs[0] instanceof String)? (String)objs[0]:(String)((Contact_Method)objs[0]).getLabel();
            String str0 = String.format("AOP Method setting & encrypting field %s with %s", joinPoint.getSignature().getName(), field );
            String encryptedField = CryptoUtils.encrypt(field, key, iv);
            String str1 = String.format("Encrypted field: %s", encryptedField);



                    if(objs[0] instanceof String){
                        objs[0]=encryptedField;

                    }else{
                        objs[0]=objs0[0];
                    }


            result = joinPoint.proceed(objs);

            String decryptedField = CryptoUtils.decrypt(encryptedField, key, iv);
            String str2 = String.format("Decrypted field %s", decryptedField);

            logger.info("");
            logger.info(str0);
            logger.info(str1);
            logger.info(str2);
            logger.info("");

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public AnnotationConfigApplicationContext getContext() {
        return context;
    }

    public void setContext(AnnotationConfigApplicationContext context) {
        this.context = context;
    }

    public ContactFormBean getBean() {
        return bean;
    }

    public void setBean(ContactFormBean bean) {
        this.bean = bean;
    }

    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    public IvParameterSpec getIv() {
        return iv;
    }


    public void setIv(IvParameterSpec iv) {
        this.iv = iv;
    }

}
