package hellojpa;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@EnableSpringDataWebSupport
public class Main08 {

    public static void main(String[] args) throws Exception{
        //Spring 프로젝트로 따로 만들었어야 했던 것 같다.
        //SpringApplication.run(Main08.class, args);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:tcp://localhost/~/test");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public JpaTransactionManager transactionManager() { return new JpaTransactionManager(); }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("hello.entity");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put(AvailableSettings.SHOW_SQL, true);
        jpaProperties.put(AvailableSettings.FORMAT_SQL, true);
        jpaProperties.put(AvailableSettings.USE_SQL_COMMENTS, true);
        jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, "create");
        jpaProperties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.H2Dialect");
        jpaProperties.put(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, "true");

        factoryBean.setJpaProperties(jpaProperties);
        return factoryBean;
    }
}
