package me.minidigger.voxelgameslib.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.Log_$logger;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.internal.CoreMessageLogger_$logger;
import org.hibernate.internal.EntityManagerMessageLogger_$logger;
import org.hibernate.internal.log.ConnectionAccessLogger_$logger;
import org.hibernate.internal.log.ConnectionPoolingLogger_$logger;
import org.hibernate.type.descriptor.java.JavaTypeDescriptorRegistry;
import org.reflections.Reflections;

import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.persistence.Entity;

import me.minidigger.voxelgameslib.config.GlobalConfig;
import me.minidigger.voxelgameslib.timings.Timings;
import me.minidigger.voxelgameslib.user.GamePlayer;
import me.minidigger.voxelgameslib.user.User;

import lombok.extern.java.Log;

/**
 * A implementation of the persistence provider based on hibernate
 */
@Log
public class HibernatePersistenceProvider implements PersistenceProvider {

    @Inject
    private GlobalConfig config;

    private SessionFactory sessionFactory;

    @Override
    public void start() {
        // don't judge me, bukkit doesn't want to resolve those so we need to manually load them
        Class[] iDontEvenKnow = new Class[]{CoreMessageLogger_$logger.class, Log_$logger.class,
                ConnectionPoolingLogger_$logger.class, EntityManagerMessageLogger_$logger.class, ConnectionAccessLogger_$logger.class};

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                // credentials and stuff
                .applySetting("hibernate.connection.username", config.persistence.user)
                .applySetting("hibernate.connection.password", config.persistence.pass)
                .applySetting("hibernate.connection.driver_class", config.persistence.driver)
                .applySetting("hibernate.connection.url", config.persistence.url + "?useSSL=false")
                .applySetting("hibernate.dialect", config.persistence.dialect)
                // misc settings
                .applySetting("hibernate.hbm2ddl.auto", "update")
                .applySetting("hibernate.show_sql", config.persistence.showSQL + "")
                //TODO apparently this is an anti-pattern [0], but it fixes an issue so ¯\_(ツ)_/¯
                // [0]: https://vladmihalcea.com/2016/09/05/the-hibernate-enable_lazy_load_no_trans-anti-pattern/
                .applySetting("hibernate.enable_lazy_load_no_trans", true)
                .applySetting("hibernate.connection.autocommit", true)
                // connection pool
                .applySetting("hibernate.connection.pool_size", config.persistence.pool_size + "")
                //TODO figure out how to use hikari with hibernate
                //.applySetting("hibernate.connection.provider_class","com.zaxxer.hikari.hibernate.HikariConnectionProvider")
                .build();

        JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(ComponentTypeDescriptor.INSTANCE);

        MetadataSources sources = new MetadataSources(registry);

        Timings.time("RegisterDBEntities", () ->
                new Reflections("me.minidigger.voxelgameslib").getTypesAnnotatedWith(Entity.class).forEach(sources::addAnnotatedClass));

        try {
            Metadata metadata = sources.buildMetadata();
            sessionFactory = metadata.buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(User user) {
        session(session -> {
            session.saveOrUpdate(user);
            return null;
        });
    }

    @Override
    public Optional<User> loadUser(UUID id) {
        return Optional.ofNullable(session(session -> session.get(GamePlayer.class, id)));
    }

    private <T> T session(SessionExecutor<T> executor) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        T t = executor.execute(session);

        session.getTransaction().commit();
        session.close();

        return t;
    }

    @FunctionalInterface
    interface SessionExecutor<T> {
        T execute(Session session);
    }

    @Override
    public void stop() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}