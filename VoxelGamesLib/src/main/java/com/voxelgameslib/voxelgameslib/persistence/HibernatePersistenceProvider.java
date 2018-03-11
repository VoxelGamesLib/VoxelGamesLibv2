package com.voxelgameslib.voxelgameslib.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.type.descriptor.java.JavaTypeDescriptorRegistry;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Converter;
import javax.persistence.Entity;

import com.voxelgameslib.voxelgameslib.persistence.converter.VGLConverter;
import com.voxelgameslib.voxelgameslib.startup.StartupHandler;
import com.voxelgameslib.voxelgameslib.config.ConfigHandler;
import com.voxelgameslib.voxelgameslib.config.GlobalConfig;
import com.voxelgameslib.voxelgameslib.persistence.model.UserData;
import com.voxelgameslib.voxelgameslib.stats.Trackable;
import com.voxelgameslib.voxelgameslib.timings.Timings;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

/**
 * A implementation of the persistence provider based on hibernate
 */
@Singleton
public class HibernatePersistenceProvider implements PersistenceProvider {

    private static final Logger log = Logger.getLogger(HibernatePersistenceProvider.class.getName());
    @Inject
    private GlobalConfig config;
    @Inject
    private ConfigHandler configHandler;
    @Inject
    private StartupHandler startupHandler;

    private SessionFactory sessionFactory;

    @Override
    public void enable() {
        boolean shouldCreateTable = config.persistence.initialTableCreation;
        if (shouldCreateTable) {
            config.persistence.initialTableCreation = false;
            configHandler.saveGlobalConfig();
        }

        startupHandler.registerService("Hibernate");

        Thread thread = new Thread(() -> {
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                // credentials and stuff
                .applySetting("hibernate.connection.username", config.persistence.user)
                .applySetting("hibernate.connection.password", config.persistence.pass)
                .applySetting("hibernate.connection.driver_class", config.persistence.driver)
                .applySetting("hibernate.connection.url", config.persistence.url + "?useSSL=false")
                .applySetting("hibernate.dialect", config.persistence.dialect)
                // misc settings
                .applySetting("hibernate.hbm2ddl.auto", shouldCreateTable ? "create" : "update")
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

            MetadataSources sources = new MetadataSources(registry);

            Timings.time("Init converters",
                () -> new FastClasspathScanner().addClassLoader(getClass().getClassLoader())
                    .matchClassesImplementing(VGLConverter.class, (annotatedClass) -> {
                        try {
                            annotatedClass.newInstance().init();
                        } catch (InstantiationException | IllegalAccessException e) {
                            log.warning("Error while initializing converter " + annotatedClass.getSimpleName());
                            e.printStackTrace();
                        }
                    }).scan());

            Timings.time("RegisterDBEntities",
                () -> new FastClasspathScanner().addClassLoader(getClass().getClassLoader())
                    .matchClassesWithAnnotation(Entity.class, (annotatedClass) -> {
                        if (!annotatedClass.getName().contains("ebean")) sources.addAnnotatedClass(annotatedClass);
                    }).scan());

            try {
                Metadata metadata = sources.buildMetadata();
                sessionFactory = metadata.buildSessionFactory();
                log.info("Build HibernationSessionFactory with " + sources.getAnnotatedClasses().size() + " entities.");
            } catch (Exception e) {
                StandardServiceRegistryBuilder.destroy(registry);
                e.printStackTrace();
            }

            startupHandler.unregisterService("Hibernate");
        });
        thread.setName("Hibernate Startup");
        thread.start();
    }

    @Override
    public void saveUser(@Nonnull UserData user) {
        session(session -> {
            session.saveOrUpdate(user);
            return null;
        });
    }

    @Override
    @Nonnull
    public Optional<UserData> loadUser(@Nonnull UUID id) {
        return Optional.ofNullable(session(session -> session.get(UserData.class, id)));
    }

    @Nullable
    public <T> T session(@Nonnull SessionExecutor<T> executor) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        T t = executor.execute(session);

        session.getTransaction().commit();
        session.close();

        return t;
    }

    @FunctionalInterface
    public interface SessionExecutor<T> {

        @Nullable
        T execute(@Nonnull Session session);
    }

    @Override
    public void disable() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}