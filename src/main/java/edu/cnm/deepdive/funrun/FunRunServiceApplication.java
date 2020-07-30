package edu.cnm.deepdive.funrun;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * This class provides a framework for managing a JavaFX.
 */

@SpringBootApplication
@EnableWebSecurity
@EnableResourceServer
@EnableHypermediaSupport(type = HypermediaType.HAL)
public class FunRunServiceApplication extends ResourceServerConfigurerAdapter {

  @Value("${oauth.clientId}")
  private String clientId;

  /**
   * The class invokes this method in order to run.
   */
  public static void main(String[] args) {
    SpringApplication.run(FunRunServiceApplication.class, args);
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.resourceId(clientId);
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.authorizeRequests()
        .antMatchers(HttpMethod.POST, "/events").hasRole("USER")
        .antMatchers(HttpMethod.PUT, "/events/**").hasRole("USER")
        .antMatchers(HttpMethod.DELETE, "/events/**").hasRole("USER")
        .antMatchers(HttpMethod.POST, "/histories").hasRole("USER")
        .antMatchers(HttpMethod.PUT, "/histories/**").hasRole("USER")
        .antMatchers(HttpMethod.DELETE, "/histories/**").hasRole("USER")
        .antMatchers(HttpMethod.POST, "/comments").hasRole("USER")
        .antMatchers(HttpMethod.PUT, "/comments/**").hasRole("USER")
        .antMatchers(HttpMethod.DELETE, "/comments/**").hasRole("USER")
        .antMatchers(HttpMethod.POST, "/users").hasRole("USER")
        .antMatchers(HttpMethod.PUT, "/users/**").hasRole("USER")
        .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("USER")
        .antMatchers(HttpMethod.GET, "/users/me").permitAll()
        .antMatchers(HttpMethod.GET, "/users/**").hasRole("USER")
        .anyRequest().permitAll();

  }
}
