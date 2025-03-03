package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.logging.Logger;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String DIALECT = "org.hibernate.dialect.MySQLDialect";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "qwerty12";
    private static final String URL = "jdbc:mysql://localhost:3306/pp_1_1_3";

    private static Logger log = Logger.getLogger(Util.class.getName());

    private static SessionFactory sessionFactory;

    private Util() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration()
                .setProperty("hibernate.dialect", DIALECT)
                .setProperty("hibernate.connection.driver_class", DRIVER)
                .setProperty("hibernate.connection.url", URL)
                .setProperty("hibernate.connection.username", USER_NAME)
                .setProperty("hibernate.connection.password", PASSWORD)
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .addAnnotatedClass(User.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
                log.info("SessionFactory created");
            } catch (Exception e) {
                log.severe("SessionFactory creation failed with message: " + e.getMessage());
            }
        }
        return sessionFactory;
    }

}
