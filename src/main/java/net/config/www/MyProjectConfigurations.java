package net.config.www;

import bean.repository.EncryptedStorage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"logging", "net.beans.www","validation","bean.repository"})
@Configuration
/**
 * This project demonstrates Spring AOP. I have pre-existing knowledge of
 * JDK, Java Servlets, JSP, JDBC. I am currently studying Spring Boot, Annotations,
 * and this project incorporates logging along with aspect orientated programming.
 * I am using component scan for the sake of inversion of control programming which
 * leaves an empty configuration class. My base packages are listed above and contain
 * component classes and repository classes (I am using JDBC and have not became familiar
 * with the Spring automated frameworks. Much of this project demonstrates object orientated
 * programming skills. As some de-coupling of an interface was practiced initially when I
 * first created this project.
 */
public class MyProjectConfigurations {

}
