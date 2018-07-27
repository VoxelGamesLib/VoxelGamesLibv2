package com.voxelgameslib.voxelgameslib.persistence;

import com.google.inject.name.Named;

import com.bugsnag.Severity;

import net.kyori.text.Component;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;

import com.voxelgameslib.voxelgameslib.config.ConfigHandler;
import com.voxelgameslib.voxelgameslib.config.GlobalConfig;
import com.voxelgameslib.voxelgameslib.error.ErrorHandler;
import com.voxelgameslib.voxelgameslib.persistence.converter.VGLConverter;
import com.voxelgameslib.voxelgameslib.persistence.model.GameData;
import com.voxelgameslib.voxelgameslib.persistence.model.UserData;
import com.voxelgameslib.voxelgameslib.startup.StartupHandler;
import com.voxelgameslib.voxelgameslib.stats.Trackable;
import com.voxelgameslib.voxelgameslib.timings.Timing;
import com.voxelgameslib.voxelgameslib.utils.Pair;

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
    @Inject
    @Named("IncludeAddons")
    private FastClasspathScanner scanner;
    @Inject
    private ErrorHandler errorHandler;

    private SessionFactory sessionFactory;
    private CriteriaBuilder cBuilder;

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

            try (final Timing timing = new Timing("Init converters")) {
                scanner.matchClassesImplementing(VGLConverter.class, (annotatedClass) -> {
                    try {
                        annotatedClass.newInstance().init();
                    } catch (InstantiationException | IllegalAccessException e) {
                        log.warning("Error while initializing converter " + annotatedClass.getSimpleName());
                        e.printStackTrace();
                    }
                }).scan();
            }

            try (final Timing timing = new Timing("RegisterDBEntities")) {
                scanner.matchClassesWithAnnotation(Entity.class, (annotatedClass) -> {
                    if (!annotatedClass.getName().contains("ebean")) sources.addAnnotatedClass(annotatedClass);
                }).scan();
            }

            try {
                Metadata metadata = sources.buildMetadata();
                sessionFactory = metadata.buildSessionFactory();
                log.info("Build HibernationSessionFactory with " + sources.getAnnotatedClasses().size() + " entities.");
            } catch (Exception e) {
                StandardServiceRegistryBuilder.destroy(registry);
                e.printStackTrace();
            }

            cBuilder = sessionFactory.getCriteriaBuilder();

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

    @Override
    public List<Pair<Component, Double>> getTopWithName(Trackable type, int amount) {
        return session(session -> {
            Query query = session.createQuery("select user.displayName, stat.val from StatInstance stat, UserData user\n" +
                "where user.uuid = stat.uuid\n" +
                "and stat.statType = :type\n" +
                "order by stat.val desc");
            query.setParameter("type", type);
            query.setMaxResults(amount);

            List<Pair<Component, Double>> result = new ArrayList<>();
            //noinspection unchecked
            for (Object[] row : (List<Object[]>) query.getResultList()) {
                result.add(new Pair<>((Component) row[0], (Double) row[1]));
            }
            return result;
        });
    }

    @Override
    public List<Pair<UUID, Double>> getTopWithUUID(Trackable type, int amount) {
        return session(session -> {
            Query query = session.createQuery("select stat.uuid, stat.val from StatInstance stat\n" +
                "where stat.statType = :type\n" +
                "order by stat.val desc");
            query.setParameter("type", type);
            query.setMaxResults(amount);

            List<Pair<UUID, Double>> result = new ArrayList<>();
            //noinspection unchecked
            for (Object[] row : (List<Object[]>) query.getResultList()) {
                result.add(new Pair<>((UUID) row[0], (Double) row[1]));
            }
            return result;
        });
    }

    @Override
    public void saveGame(GameData gameData) {
        session(session -> {
            session.saveOrUpdate(gameData);
            return null;
        });
    }

    @Nullable
    @SuppressWarnings("Duplicates")
    private <T> T session(@Nonnull SessionExecutor<T> executor) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            T t = executor.execute(session);

            session.getTransaction().commit();
            session.close();

            return t;
        } catch (JDBCConnectionException ex) {
            log.finer("DB connection error, retrying... (" + ex.getMessage() + ")");
            // retry
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                T t = executor.execute(session);

                session.getTransaction().commit();
                session.close();

                return t;
            } catch (Exception e) {
                errorHandler.handle(ex, Severity.ERROR, true);
                return null;
            }
        } catch (Exception ex) {
            errorHandler.handle(ex, Severity.ERROR, true);
            return null;
        }
    }

    @FunctionalInterface
    interface SessionExecutor<T> {

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