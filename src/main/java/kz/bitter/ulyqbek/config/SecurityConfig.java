package kz.bitter.ulyqbek.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import kz.bitter.ulyqbek.service.UserService;

@Configuration
@EnableGlobalMethodSecurity (prePostEnabled = true,proxyTargetClass = true,securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // @Autowired
    // private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      //TODO: Remove deprecated WebSecurityConfigurerAdapter (guide https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter)
        http.authorizeRequests()
                .antMatchers("/css/**","/js/**","/").permitAll();
        http.exceptionHandling().accessDeniedPage("/errorPages/403");
        http.csrf().disable();
        http.formLogin()
                .loginProcessingUrl("/authIn")
                .usernameParameter("user_login")
                .passwordParameter("user_password")
                .failureUrl("/?error=true")
                .loginPage("/")
                .defaultSuccessUrl("/profile")
                .permitAll();

        http.logout()
                .logoutUrl("/authOut")
                .logoutSuccessUrl("/")
                .permitAll();
        http.authorizeRequests().antMatchers("/**").permitAll();

//        http.authorizeRequests().antMatchers(HttpMethod.GET, "/user/**").hasAnyAuthority("ROLE_USER");
//        http.authorizeRequests().antMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority("ROLE_ADMIN");
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority("ROLE_ADMIN");

//        http.authorizeRequests().anyRequest().authenticated();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService);
//
//    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
