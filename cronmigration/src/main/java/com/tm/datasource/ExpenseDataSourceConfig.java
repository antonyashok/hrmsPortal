

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
@EnableJpaRepositories(entityManagerFactoryRef = "expenseEntityManagerFactory", 
		transactionManagerRef = "expenseTransactionManager", 
		basePackages = {ExpenseDataSourceConfig.ATLIS_REPOSITORY_SCAN })
@EnableTransactionManagement
public class ExpenseDataSourceConfig {

	protected static final String ATLIS_ENTITY_SCAN = "com.tm.expense.domain";

	protected static final String ATLIS_REPOSITORY_SCAN = "com.tm.expense.jpa.repository";

	@Bean(name = "expenseDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.ems-db")
    public HikariDataSource expenseDataSource() {
        return (HikariDataSource) DataSourceBuilder.create().type(HikariDataSource.class).build();
    }    

	@Value("${spring.jpa.show-sql}")
	private String expenseShowSQL;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String expenseDDLAuto;

	@Value("${spring.jpa.hibernate.naming.strategy}")
	private String expenseNamingStrategy;

	@Value("${spring.jpa.hibernate.dialect}")
	private String expenseDialect;

	@Bean(name = "expenseEntityManagerFactory")
	LocalContainerEntityManagerFactoryBean expenseEntityManagerFactory() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setGenerateDdl(false);
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setPackagesToScan(ATLIS_ENTITY_SCAN);
		factoryBean.setDataSource(expenseDataSource());
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		Properties properties = new Properties();
		properties.put("hibernate.show-sql", expenseShowSQL);
		properties.put("hibernate.ddl-auto", expenseDDLAuto);
		properties.put("hibernate.naming-strategy", expenseNamingStrategy);
		properties.put("hibernate.dialect", expenseDialect);
		factoryBean.setJpaProperties(properties);
		return factoryBean;
	}

	@Bean(name = "expenseTransactionManager")
	PlatformTransactionManager expenseTransactionManager() {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(
				expenseEntityManagerFactory().getObject());
		jpaTransactionManager.setGlobalRollbackOnParticipationFailure(false);
		return jpaTransactionManager;
	}
}

