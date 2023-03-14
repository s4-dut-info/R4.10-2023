package edu.spring.dogs.config

import edu.spring.dogs.services.DbUserService
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler





@Configuration
@EnableWebSecurity
class WebSecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable().authorizeHttpRequests()
            .requestMatchers("/init/**","/login/**").permitAll()
            .requestMatchers(PathRequest.toH2Console()).permitAll()
            .requestMatchers("/master/**").hasRole("manager")
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login")
            .successForwardUrl("/")
            .and()
            .headers().frameOptions().sameOrigin()
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

    @Bean
    fun roleHierarchy(): RoleHierarchy? {
        val roleHierarchy = RoleHierarchyImpl()
        val hierarchy = "ROLE_ADMIN > ROLE_MANAGER_DOG \n" +
                "ROLE_ADMIN > ROLE_MANAGER_MASTER \n" +
                "ROLE_MANAGER_DOG > ROLE_USER \n" +
                "ROLE_MANAGER_MASTER > ROLE_USER"
        roleHierarchy.setHierarchy(hierarchy)
        return roleHierarchy
    }

    @Bean
    fun webSecurityExpressionHandler(): DefaultWebSecurityExpressionHandler? {
        val expressionHandler = DefaultWebSecurityExpressionHandler()
        expressionHandler.setRoleHierarchy(roleHierarchy())
        return expressionHandler
    }

}