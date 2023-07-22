package com.example.webflux.springbootwebflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

//@Configuration
//@EnableWebFluxSecurity
public class WebFluxSecurityConfigMem {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                //.pathMatchers("/loginPage").permitAll()  //无需进行权限过滤的请求路径
                .anyExchange().authenticated()
                .and()
                .httpBasic().and()
                .formLogin()
        //.loginPage("/loginPage")  //自定义的登陆页面
        ;
        return http.build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        UserDetails build = User.withUsername("admin").password(passwordEncoder().encode("123")).authorities("admin").build();
        return new MapReactiveUserDetailsService(build);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}