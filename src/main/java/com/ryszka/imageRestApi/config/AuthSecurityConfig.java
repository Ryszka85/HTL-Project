package com.ryszka.imageRestApi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryszka.imageRestApi.dao.UserDAO;
import com.ryszka.imageRestApi.security.*;

import com.ryszka.imageRestApi.service.serviceV2.readService.UserAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
/*
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
*/

import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserAuthService UserAuth;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ObjectMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(AuthSecurityConfig.class);
    private final UserDAO userDAO;

    public AuthSecurityConfig(UserAuthService userAuth,
                              BCryptPasswordEncoder bCryptPasswordEncoder,
                              ObjectMapper mapper,
                              UserDAO userDAO) {
        UserAuth = userAuth;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mapper = mapper;
        this.userDAO = userDAO;
    }

    /*public AuthSecurityConfig(UserAuthService UserAuth,
                              BCryptPasswordEncoder bCryptPasswordEncoder,
                              ObjectMapper mapper) {
        this.UserAuth = UserAuth;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mapper = mapper;
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(UserAuth).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users/signUp").permitAll()
                .antMatchers(HttpMethod.POST, "/users/oauth/login").permitAll()
                /*.antMatchers(HttpMethod.POST, "/images/set/tag/**").permitAll()*/
                .antMatchers(HttpMethod.GET, "/testing").permitAll()
                .antMatchers(HttpMethod.GET, "/reset/password/**").permitAll()
                .antMatchers(HttpMethod.GET, "/images/user/**").permitAll()
                .antMatchers(HttpMethod.GET, "/images/**").permitAll()
                .antMatchers(HttpMethod.GET, "/images/search/by/tag/**").permitAll()
                .antMatchers(HttpMethod.GET, "/library/download/**").permitAll()
                .antMatchers(HttpMethod.POST, "/library/download/**").permitAll()
                .antMatchers(HttpMethod.POST, "/library/download/foo").permitAll()
                .antMatchers(HttpMethod.POST, "/library/download/resolution").permitAll()
                .antMatchers(HttpMethod.POST, "/library/search-by/**").permitAll()
                .antMatchers(HttpMethod.POST, "/library/search-by/tag").permitAll()
                .antMatchers(HttpMethod.GET, "/library/search-by/**").permitAll()
                .antMatchers(HttpMethod.GET, "/library/search-by/**").permitAll()
                .antMatchers(HttpMethod.POST, "/library/validate-image/").permitAll()
                .antMatchers(HttpMethod.GET, "/query/**").permitAll()
                .antMatchers(HttpMethod.GET, "/addresses/zip/cities/**").permitAll()
                .antMatchers(HttpMethod.GET, "/addresses/get/all/countries").permitAll()
                .antMatchers(HttpMethod.GET, "/addresses/regions/**").permitAll()
                .antMatchers(HttpMethod.GET, "/addresses/zipcodes/**").permitAll()
                .antMatchers(HttpMethod.GET, "/users/details/**").permitAll()
                .antMatchers(HttpMethod.GET, "/search/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/**").permitAll()
                .antMatchers(HttpMethod.GET, "/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.POST, "/users/insert/profile/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(getAuthenticationFilter())
                .addFilter(getGoogleAuthenticationFilter())
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new SuccessfulLogoutHandler(mapper))
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling();
    }


    /*private void logoutSuccessHandler(HttpServletRequest httpServletRequest,
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        mapper.writeValue(response.getWriter(), "Bye!");
    }*/

    public GoogleAuthFilter getGoogleAuthenticationFilter() throws Exception {
        GoogleAuthFilter authenticationFilter = new GoogleAuthFilter(authenticationManager(), userDAO, mapper, bCryptPasswordEncoder);
        authenticationFilter.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/users/oauth/login", "POST"));
        authenticationFilter.setAuthenticationSuccessHandler(new GoogleSuccessHandler(mapper));
        authenticationFilter.setAuthenticationFailureHandler(new GoogleFailureHandler());
        return authenticationFilter;
    }


    public AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), mapper);
        authenticationFilter.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/users/login", "POST"));
        authenticationFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(mapper));
        authenticationFilter.setAuthenticationFailureHandler(new LoginFailureHandler(mapper));
        return authenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Cache-Control", "Content-Type", "withCredentials", "isMobile"));
        configuration.setExposedHeaders(Collections.singletonList("UserId"));
        configuration.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
