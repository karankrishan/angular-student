package com.pdizzle.app;

import javax.ws.rs.ext.Provider;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.pdizzle.utils.SecurityUtils;

@Configuration
@ComponentScan(basePackages = { "com.pdizzle"},
		includeFilters = @ComponentScan.Filter(
		type = FilterType.ANNOTATION,
		value = Provider.class))
@ImportResource("classpath:springmvc-resteasy.xml")
public class StudentServicesConfig {
	
	private final Logger LOG = LoggerFactory.getLogger(StudentServicesConfig.class);
	private @Value("${db.url}")
	String url;
	private @Value("${db.username}")
	String username;
	private @Value("${db.pwd}")
	String pwd;

	@Bean
	public static PropertyPlaceholderConfigurer properties() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		String environment= System.getProperty("env") != null ? System.getProperty("env") : "dev";
		Resource[] resources = new ClassPathResource[] { new ClassPathResource(environment + "-student-services.properties") };
		ppc.setLocations(resources);
		ppc.setIgnoreUnresolvablePlaceholders(true);
		return ppc;
	}

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		LOG.info("Datasource: " + url);
		DataSource ds = new DataSource();
		ds.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(SecurityUtils.unencrypt(pwd));
		ds.setMaxActive(20);
		ds.setMinIdle(5);
		ds.setMaxIdle(20);
		ds.setMaxWait(20000);
		ds.setRemoveAbandoned(true);
		ds.setRemoveAbandonedTimeout(60);
		ds.setInitSQL("SELECT 1");
		ds.setTestWhileIdle(true);
		ds.setValidationQuery("SELECT 1");
		ds.setTimeBetweenEvictionRunsMillis(60000);

		return ds;
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}
}
