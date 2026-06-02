package com.mdsl.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
		basePackages = "com.mdsl.repository",
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "transactionManager"
)
public class ApiWebConfig {

	@Primary
	@Bean(name="apiProps")
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(name="datasource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource datasource(@Qualifier("apiProps") DataSourceProperties properties){
		return properties.initializeDataSourceBuilder().build();
	}

	@Primary
	@Bean(name="entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
			(EntityManagerFactoryBuilder builder,
			 @Qualifier("datasource") DataSource dataSource){
		return builder.dataSource(dataSource)
				.packages("com.mdsl.model",
						"com.mdsl.controller",
						"com.mdsl.framework",
						"com.mdsl.repository",
						"com.mdsl.utils",
						"com.mdsl.service",
						"com.mdsl.exceptionHandling"
				)
				.persistenceUnit("Api").build();
	}

	@Primary
	@Bean(name = "transactionManager")
	@ConfigurationProperties("spring.jpa")
	public PlatformTransactionManager transactionManager(
			@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	//JdbcTemplate for connection
	@Primary
	@Bean(name = "appJdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("datasource") DataSource ds) {
		return new JdbcTemplate(ds);
	}
}