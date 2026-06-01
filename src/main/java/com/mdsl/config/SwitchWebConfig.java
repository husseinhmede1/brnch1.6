package com.mdsl.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.mdsl.swtch.repository", 
    entityManagerFactoryRef = "switchEntityManagerFactory", 
    transactionManagerRef = "switchTransactionManager"
)
public class SwitchWebConfig {

    @Bean(name="switchProps")
    @ConfigurationProperties("switchdb.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean(name="switchDatasource")
    @ConfigurationProperties(prefix = "switchdb.datasource")
    public DataSource datasource(@Qualifier("switchProps") DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name="switchEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean
            (EntityManagerFactoryBuilder builder,
             @Qualifier("switchDatasource") DataSource dataSource){
        return builder.dataSource(dataSource)
        		.packages("com.mdsl.swtch")
                .persistenceUnit("Switch").build();
    }


    @Bean(name = "switchTransactionManager")
    @ConfigurationProperties("spring.jpa")
    public PlatformTransactionManager transactionManager(
            @Qualifier("switchEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    
    //Swicth JdbcTemplate for connection
	@Bean(name = "switchjdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("switchDatasource") DataSource ds) {
		return new JdbcTemplate(ds);
	}    
}