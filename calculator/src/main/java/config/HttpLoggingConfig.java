package config;

import ch.qos.logback.access.servlet.TeeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpLoggingConfig {
    @Bean
    public FilterRegistrationBean<TeeFilter> requestLoggingFilter() {
        return new FilterRegistrationBean<>(new TeeFilter());
    }
}
