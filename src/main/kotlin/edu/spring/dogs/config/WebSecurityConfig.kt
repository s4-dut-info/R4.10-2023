package edu.spring.dogs.config

import edu.spring.dogs.services.DbUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true
)
class WebSecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { authorizeHttpRequests ->
            authorizeHttpRequests.requestMatchers("/css/**", "/assets/**", "/login/**","/h2-console/**").permitAll() // (1)
            authorizeHttpRequests.requestMatchers("/admin/**").hasRole("ROLE_ADMIN") // (2)
            authorizeHttpRequests.requestMatchers("/master/**").hasAuthority("MANAGER") // (3)
            authorizeHttpRequests.anyRequest().authenticated() // (5)
        }
        http.formLogin().loginPage("/login").successForwardUrl("/")
        .and()
        .headers().frameOptions().sameOrigin() // (2)
        .and()
        .csrf().disable() // (3)
        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService? {
        return DbUserService()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder? {
        return BCryptPasswordEncoder()
    }

}