package com.example.twitterapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration  // this class is configuration class
@EnableWebSecurity // enable web security in the application
public class SecurityConfig {
//    this class would be handling the logic behind login and registeration pages

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Bean  // to manage by the spring container
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
        // return instance of BCryptPasswordEncoder for hashing password
    }

    @Bean
    public static SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable()) //// this will disable csrf protection this typically done when non browser client or testing

                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/register").permitAll() // we are allowing unrestricted access to the "/register" url so if a user with it's your application then he  should have an unrestricted access to the "/register" page that he shouldn't be ask for login before visiting the registeration page as it doesn't make any sense
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .anyRequest().authenticated() // we are requiring authentication for any other request not explicitly permitted
                )
                .formLogin(form -> form
                        .loginPage("/login") // specifies the login  page URL
                        .loginProcessingUrl("/login") // specifies the URL that will process the login form submission
                        .defaultSuccessUrl("/",true) //specifies the default URL that redirect to after successfull login
                        .permitAll() // allows unrestricted access to the login page and the login process URL
                )
                .logout(logout -> logout
                        .invalidateHttpSession(true) // we are invalidating HTTP session when the user logs out
                        .clearAuthentication(true) // we are clearing out the users authentication information
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // we are specifying the url that triggers the logout action
                        .logoutSuccessUrl("/login?logout") // we are redirecting to the login page with a  logout message after successful logout
                        .permitAll() // allowing unrestricted access to logout functionality
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // we are allowing the iframe within the same  origin that is useful for accessing the H2 console
                );
        return http.build(); // which would finalize the configuration and return the build securtiyfilterchain  object

    }
    @Autowired // it injects nessecary dependency in the method
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder()); // we are configuring spring security to use the CustomUserDetailsService or loading userDDetails and thee password encoder for the password hashing
    }

}
