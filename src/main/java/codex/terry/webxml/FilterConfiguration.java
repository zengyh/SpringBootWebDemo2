package codex.terry.webxml;

import codex.terry.filter.PathFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 编写人: yh.zeng
 * 编写时间: 2018-8-28
 * 文件描述: 注册Filter Bean
 */
@Configuration
public class FilterConfiguration
{
    @Bean
    public FilterRegistrationBean pathFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new PathFilter());
        registration.addUrlPatterns("/*");
        //registration.addInitParameter("paramName", "paramValue");
        registration.setName("PathFilter");
        registration.setOrder(1);
        return registration;
    }
}
