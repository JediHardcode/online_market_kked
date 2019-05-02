package com.gmail.derynem.springbootmodule.security.config;

import com.gmail.derynem.repository.model.enums.RoleNameEnum;
import com.gmail.derynem.springbootmodule.security.handler.AppAccessDeniedHandler;
import com.gmail.derynem.springbootmodule.security.handler.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder encoder;

    public WebSecurityConfig(UserDetailsService userDetailsService, PasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler deniedHandler() {
        return new AppAccessDeniedHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .antMatchers("/403", "/login", "/home")
//                .permitAll()
//                .antMatchers("/users/**")
//                .hasAuthority(RoleNameEnum.ADMINISTRATOR.name())
//                .antMatchers("/items/**")
//                .hasAuthority(RoleNameEnum.CUSTOMER.name())
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .successHandler(successHandler())
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll()
//                .and()
//                .exceptionHandling()
//                .accessDeniedHandler(deniedHandler())
//                .and()
//                .csrf()
//                .disable();
    }
}