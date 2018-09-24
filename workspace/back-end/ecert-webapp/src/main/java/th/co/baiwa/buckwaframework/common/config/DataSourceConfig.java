package th.co.baiwa.buckwaframework.common.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import th.co.baiwa.buckwaframework.common.persistence.jdbc.CommonJdbcTemplate;


@Configuration

@EnableTransactionManagement
public class DataSourceConfig {
	
	
	@Autowired
	private DataSource dataSource;
	
	@Bean(name = "commonJdbcTemplate")
	public CommonJdbcTemplate commonJdbcTemplate(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
		return new CommonJdbcTemplate(jdbcTemplate);
	}
	
	
}
