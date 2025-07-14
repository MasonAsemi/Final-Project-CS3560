package com.library.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import com.library.model.Student;
import com.library.model.Book;
import com.library.model.Loan;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            System.out.println("Initializing Hibernate...");
            Configuration configuration = new Configuration().configure();

            configuration.addAnnotatedClass(Student.class);
            configuration.addAnnotatedClass(Book.class);
            configuration.addAnnotatedClass(Loan.class);

            System.out.println("Configuration loaded with entity classes");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            System.out.println("SessionFactory created successfully!");

        } catch (Exception e) {
            System.err.println("Error initializing Hibernate: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}