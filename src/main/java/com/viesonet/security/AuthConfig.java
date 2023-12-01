package com.viesonet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.viesonet.entity.Accounts;

import static org.springframework.security.config.Customizer.withDefaults;
import com.viesonet.service.AccountsService;
import com.viesonet.service.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity

public class AuthConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint JwtAuthenticationEntryPoint;

    @Autowired
    AccountsService accountsService;

    @Bean
    public JwtAuthenticationTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests()
                .requestMatchers("/api/forgetPassword/sendCode", "/api/createToken", "/api/register/sendCode",
                        "/api/register", "/api/forgetPassword", "/chat/**", "/private-notification/**")
                .permitAll()
                .requestMatchers("/admin/**").hasRole("1")
                .requestMatchers("/staff/**").hasAnyRole("2", "1")
                .requestMatchers("/**").hasAnyRole("1", "2", "3", "4")
                .anyRequest().authenticated()
                .and().rememberMe().rememberMeParameter("remember")
                .and().exceptionHandling() // Xử lý ngoại lệ khi người dùng chưa đăng nhập
                .authenticationEntryPoint(JwtAuthenticationEntryPoint)
                .and()
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    public Accounts getLoggedInAccount(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                String userId = ((UserDetailsImpl) principal).getId();
                return accountsService.getAccountById(userId);
            }
        }
        return null;
    }
}
