package es.unizar.smartcampuz.infrastructure.config;

import es.unizar.smartcampuz.infrastructure.auth.jwt.JwtAuthFilter;
import es.unizar.smartcampuz.infrastructure.auth.jwt.JwtAuthenticationEntryPoint;
import es.unizar.smartcampuz.infrastructure.auth.jwt.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthEndPoint;

    @Override
    public void configure(AuthenticationManagerBuilder auth)  throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure (HttpSecurity http) throws Exception{
        http.csrf().disable();

        http.authorizeRequests()
            .antMatchers("/listFeedback")
            .hasAuthority("ROLE_ADMIN")
            .antMatchers("/**/*")
            .permitAll()
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthEndPoint);
    }
}
