package net.dainplay.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import net.dainplay.TestTask;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseService {
    private static SessionFactory sessionFactory;

    public static void initialize() {
        try {
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

            Configuration configuration = new Configuration();

            configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/minecraft");
            configuration.setProperty("hibernate.connection.username", "minecraft_user");
            configuration.setProperty("hibernate.connection.password", "password");
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

            configuration.setProperty("hibernate.c3p0.min_size", "5");
            configuration.setProperty("hibernate.c3p0.max_size", "20");
            configuration.setProperty("hibernate.c3p0.timeout", "300");
            configuration.setProperty("hibernate.c3p0.max_statements", "50");
            configuration.setProperty("hibernate.c3p0.idle_test_period", "3000");

            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("hibernate.show_sql", "false");

            configuration.addAnnotatedClass(MessageEntity.class);

            sessionFactory = configuration.buildSessionFactory(
                    new StandardServiceRegistryBuilder()
                            .applySettings(configuration.getProperties())
                            .build()
            );

            TestTask.LOGGER.info("Database connection initialized successfully");

        } catch (Exception e) {
            TestTask.LOGGER.error("Failed to initialize database connection: " + e.getMessage());
            throw new RuntimeException("Failed to connect to database: " + e.getMessage(), e);
        }
    }

    public static void saveMessage(UUID playerUuid, String text) {
        if (text.length() > 256) {
            text = text.substring(0, 256);
        }

        try (var session = sessionFactory.openSession()) {
            session.beginTransaction();
            MessageEntity entity = new MessageEntity(playerUuid, text);
            session.persist(entity);
            session.getTransaction().commit();
            TestTask.LOGGER.info("Message saved to database: {}", text);
        } catch (Exception e) {
            TestTask.LOGGER.error("Failed to save message to database: " + e.getMessage());
            throw new RuntimeException("Failed to save message", e);
        }
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}