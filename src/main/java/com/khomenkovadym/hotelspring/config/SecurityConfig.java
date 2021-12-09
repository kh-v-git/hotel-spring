package com.khomenkovadym.hotelspring.config;

import com.khomenkovadym.hotelspring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String USER = "USER";
    private static final String MANAGER = "MANAGER";
    private static final String ADMIN = "ADMIN";

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/registration").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/room/category/**").permitAll()
                .antMatchers("/room/view/**").permitAll()

                .antMatchers("/room/user/**").hasRole(USER)
                .antMatchers("/request/user/**").hasRole(USER)
                .antMatchers("/order/user/**").hasRole(USER)
                .antMatchers("/user/user/**").hasRole(USER)

                .antMatchers("/room/manager/**").hasRole(MANAGER)
                .antMatchers("/request/manager/**").hasRole(MANAGER)
                .antMatchers("/order/manager/**").hasRole(MANAGER)
                .antMatchers("/user/manager/**").hasRole(MANAGER)

                .antMatchers("/room/admin/**").hasRole(ADMIN)
                .antMatchers("/request/admin/**").hasRole(ADMIN)
                .antMatchers("/order/admin/**").hasRole(ADMIN)
                .antMatchers("/user/admin/**").hasRole(ADMIN)
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/afterLogin")
                .failureUrl("/login?loginError=true")
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public CustomAuthenticationProvider authProvider() {
        return new CustomAuthenticationProvider();
    }

    public class CustomAuthenticationProvider implements AuthenticationProvider {
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            return null;
        }

        @Override
        public boolean supports(Class<?> aClass) {
            return false;
        }
    }
}
