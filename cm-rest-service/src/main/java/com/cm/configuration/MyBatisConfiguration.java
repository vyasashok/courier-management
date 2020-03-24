package com.cm.configuration;

import javax.inject.Named;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cm.persistence.dao.RegistrationDao;



@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
public class MyBatisConfiguration {
	
	public static final String CMDATASOURCENAME = "cmdatasource";
	public static final String CMSESSIONFACTORYBEAN = "cmSessionFactoryBean";
	
	
	@Value("${spring.mysql.datasource.driver-class-name}")
	private String mysqlDriver;
	
	@Bean(name = CMDATASOURCENAME)
	@Primary
	@ConfigurationProperties(prefix = "spring.mysql.datasource")
	public DataSource vedataSource() {
		return DataSourceBuilder.create().driverClassName(mysqlDriver).build();
	}
	
	@Bean(name = CMSESSIONFACTORYBEAN)
	@Primary
	public SqlSessionFactoryBean sqlSessionFactory(@Named(CMDATASOURCENAME) final DataSource oneDataSource)
			throws Exception {
		final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(oneDataSource);
		SqlSessionFactory sqlSessionFactory;
		sqlSessionFactory = sqlSessionFactoryBean.getObject();
		sqlSessionFactory.getConfiguration().addMapper(RegistrationDao.class);
		return sqlSessionFactoryBean;
	}
	
	@Bean
	public MapperFactoryBean<RegistrationDao> registrationMapper(
			@Named(CMSESSIONFACTORYBEAN) final SqlSessionFactoryBean sessionFactoryBean) throws Exception {
		MapperFactoryBean<RegistrationDao> registrationMFB = new MapperFactoryBean<RegistrationDao>();
		registrationMFB.setSqlSessionFactory(sessionFactoryBean.getObject());
		registrationMFB.setMapperInterface(RegistrationDao.class);
		return registrationMFB;
	}

}
