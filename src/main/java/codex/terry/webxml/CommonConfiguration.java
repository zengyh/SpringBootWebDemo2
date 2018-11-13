package codex.terry.webxml;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * 文件名称: MvcConfiguration.java
 * 编写人: yh.zeng
 * 编写时间: 2018/11/13 21:48
 * 文件描述: todo
 */
@Configuration
public class CommonConfiguration extends WebMvcConfigurationSupport
{

    @Bean
    public ITemplateResolver thymeleafTemplateEngine() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setCharacterEncoding("utf-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    /**
     * 配置模板引擎
     * @return
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(thymeleafTemplateEngine());
        return templateEngine;
    }

    /**
     * Thymeleaf视图解析器
     * @return
     */
    @Bean
    public ThymeleafViewResolver viewResolverThymeLeaf() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setViewNames(new String[]{"*.html"});
        viewResolver.setCache(false);
        viewResolver.setOrder(1);
        return viewResolver;
    }

    /**
     * JSP视图解析器
     * @return
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setViewNames(new String[]{"*.jsp"});
        resolver.setOrder(2);
        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    /**
     * ResourceHandler: 访问路径中包含/static/则匹配为访问静态资源，不配置该属性的话，默认值为/**
     * ResourceLocations :静态资源存放路径，不配置该属性的话，默认值为 classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/WEB-INF/static/");
    }

}
