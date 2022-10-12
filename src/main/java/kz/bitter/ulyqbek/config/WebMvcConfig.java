package kz.bitter.ulyqbek.config;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import ro.isdc.wro.http.ConfigurableWroFilter;

@Configuration
// @ComponentScan("com.concretepage")
public class WebMvcConfig implements WebMvcConfigurer, ApplicationContextAware {

  private ApplicationContext applicationContext;

  // @Bean
  // public InternalResourceViewResolver viewResolver() {
  //   InternalResourceViewResolver resolver = new InternalResourceViewResolver();
  //   resolver.setPrefix("/resources/static/");
  //   resolver.setSuffix(".html");
  //   return resolver;
  // }
  //
  // @Bean
  // public Properties wroProperties() {
  //   PropertiesFactoryBean pfb = new PropertiesFactoryBean();
  //   pfb.setLocation(new ClassPathResource("wro.properties"));
  //   Properties prop = null;
  //   try {
  //     prop = pfb.getObject();
  //   } catch (IOException e) {
  //     e.printStackTrace();
  //   }
  //   return prop;
  // }
  //
  // @Bean(name = "wroFilter")
  // public ConfigurableWroFilter configurableWroFilter() {
  //   ConfigurableWroFilter cwfilter = new ConfigurableWroFilter();
  //   cwfilter.setProperties(wroProperties());
  //   return cwfilter;
  // }
  //
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Bean
  public LayoutDialect layoutDialect() {
    return new LayoutDialect();
  }

  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
    source.setDefaultEncoding("UTF-8");
    source.setBasename("classpath:messages");
    return source;
  }

  @Bean
  public CookieLocaleResolver localeResolver() {
    CookieLocaleResolver resolver = new CookieLocaleResolver();
    resolver.setDefaultLocale(new Locale("ru"));
    resolver.setCookieMaxAge(3600 * 24 * 30);
    resolver.setCookieName("lang_value");
    return resolver;
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lng");
    return interceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }
}
