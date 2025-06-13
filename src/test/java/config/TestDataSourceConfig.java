package config;

import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import utils.TestPropertiesUtil;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@Profile("test")
public class TestDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        var testProperties = TestPropertiesUtil.getTestProperties();

        var jdbcDataSource = new JdbcDataSource();

        jdbcDataSource.setUrl(testProperties.getProperty("datasource.url"));

        return jdbcDataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        var sessionFactory = new LocalSessionFactoryBean();

        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("models.entities");
        sessionFactory.setHibernateProperties(TestPropertiesUtil.getHibernateProperties());

        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean
    @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session session(SessionFactory sessionFactory) {
        return sessionFactory.getCurrentSession();
    }
}
