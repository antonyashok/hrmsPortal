package com.tm.datasource;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "timeTsEntityManagerFactory",
        transactionManagerRef = "timeTsTransactionManager",
        basePackages = {TimeTsDataSourceConfig.ATLIS_REPOSITORY_SCAN})
@EnableTransactionManagement

public class TimeTsDataSourceConfig {

    protected static final String ATLIS_ENTITY_SCAN = "com.tm.timesheet.configuration.domain";

    protected static final String ATLIS_REPOSITORY_SCAN = "com.tm.timesheet.configuration.repository";

    @Bean(name = "timeTsDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.timetrack-db")
    public HikariDataSource timeTsDataSource() {
        return (HikariDataSource) DataSourceBuilder.create().type(HikariDataSource.class).build();
    }    

    @Value("${spring.jpa.show-sql}")
    private String tsHibernateShowSQL;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String tsHibernateDDLAuto;

    @Value("${spring.jpa.hibernate.naming.strategy}")
    private String tsHibernateNamingStrategy;

    @Value("${spring.jpa.hibernate.dialect}")
    private String tsHibernateDialect;

    @Bean(name = "timeTsEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean atlisTsEntityManagerFactory() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(false);
        LocalContainerEntityManagerFactoryBean factoryBean =
                new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan(ATLIS_ENTITY_SCAN);
        factoryBean.setDataSource(timeTsDataSource());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        Properties properties = new Properties();
        properties.put("hibernate.show-sql", tsHibernateShowSQL);
        properties.put("hibernate.ddl-auto", tsHibernateDDLAuto);
        properties.put("hibernate.naming-strategy", tsHibernateNamingStrategy);
        properties.put("hibernate.dialect", tsHibernateDialect);
        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

    @Bean(name = "timeTsTransactionManager")
    PlatformTransactionManager timesheetTransactionManager() {
        JpaTransactionManager jpaTransactionManager =
                new JpaTransactionManager(atlisTsEntityManagerFactory().getObject());
        jpaTransactionManager.setGlobalRollbackOnParticipationFailure(false);
        return jpaTransactionManager;
    }
}
