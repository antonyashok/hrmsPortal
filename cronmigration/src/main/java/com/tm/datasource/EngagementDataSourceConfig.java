/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.datasource.engagementDataSourceConfig
 * Author        :  SMi -user
 * Date Created  : May 02, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.datasource;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "engagementEntityManagerFactory",
        transactionManagerRef = "engagementTransactionManager",
        basePackages = {EngagementDataSourceConfig.ATLIS_REPOSITORY_SCAN})
@EnableTransactionManagement
public class EngagementDataSourceConfig {

    protected static final String ATLIS_ENTITY_SCAN = "com.tm.engagement.domain";

    protected static final String ATLIS_REPOSITORY_SCAN = "com.tm.engagement.repository";

    @Bean(name = "engagementDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.engagement-db")
    public HikariDataSource engagementDataSource() {
        return (HikariDataSource) DataSourceBuilder.create().type(HikariDataSource.class).build();
    }    

    @Value("${spring.jpa.show-sql}")
    private String engagementHibernateShowSQL;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String engagementHibernateDDLAuto;

    @Value("${spring.jpa.hibernate.naming.strategy}")
    private String engagementHibernateNamingStrategy;

    @Value("${spring.jpa.hibernate.dialect}")
    private String engagementHibernateDialect;

    @Bean(name = "engagementEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean atlisTsEntityManagerFactory() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setGenerateDdl(false);
        LocalContainerEntityManagerFactoryBean factoryBean =
                new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan(ATLIS_ENTITY_SCAN);
        factoryBean.setDataSource(engagementDataSource());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        Properties properties = new Properties();
        properties.put("hibernate.show-sql", engagementHibernateShowSQL);
        properties.put("hibernate.ddl-auto", engagementHibernateDDLAuto);
        properties.put("hibernate.naming-strategy", engagementHibernateNamingStrategy);
        properties.put("hibernate.dialect", engagementHibernateDialect);
        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

    @Bean(name = "engagementTransactionManager")
    PlatformTransactionManager engagementTransactionManager() {
        JpaTransactionManager jpaTransactionManager =
                new JpaTransactionManager(atlisTsEntityManagerFactory().getObject());
        jpaTransactionManager.setGlobalRollbackOnParticipationFailure(false);
        return jpaTransactionManager;
    }

}
