package org.fuc.config;

import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public interface DataSourceConfig {
    DataSource dataSource();
}