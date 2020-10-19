package jdlf.compass.timetableIntegration.security.config;

import jdlf.compass.timetableIntegration.properties.CorsProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(CorsProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Log4j2
public class WebSecurityBasicConfig extends WebSecurityConfigurerAdapter {

  private final CorsProperties corsProperties;

  @Autowired
  public WebSecurityBasicConfig(CorsProperties corsProperties) {
    this.corsProperties = corsProperties;
  }

  @Value("${basic.username}")
  String username;

  @Value("${basic.password}")
  String password;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
            .httpBasic()
            .and()
            .cors()
            .and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/health").permitAll()
            .antMatchers("/**").authenticated()
            .anyRequest().authenticated();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser(username)
        .password(passwordEncoder().encode(password))
        .roles("timetabler");
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
    configuration.setAllowedMethods(corsProperties.getAllowedMethods());
    configuration.setAllowCredentials(corsProperties.getAllowCredentials());
    configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    log.info("░░░░░░░░░░░░░░░░CORS CONFIGURATION░░░░░░░░░░░░░░░░");
    log.info(String.format("Configured CORS settings: %s", corsProperties.toString()));
    log.info("░░░░░░░░░░░░░░░░░░END CORS CONFIG░░░░░░░░░░░░░░░░░");

    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
