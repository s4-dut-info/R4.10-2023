package edu.spring.dogs.config

import edu.spring.dogs.services.DbUserService
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler


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
            authorizeHttpRequests.requestMatchers(PathRequest.toH2Console()).permitAll()
            authorizeHttpRequests.requestMatchers("/login").permitAll()
            authorizeHttpRequests.requestMatchers("/init/**", "/masters/**", "/dogs/**", "/css/**", "/images/**").permitAll()
            authorizeHttpRequests.requestMatchers("/master/**").hasAuthority("USER")
            authorizeHttpRequests.anyRequest().authenticated()
        }
        http
            .csrf().disable()
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
    fun roleHierarchy(): RoleHierarchy {
        val roleHierarchy = RoleHierarchyImpl()
        val hierarchy = "ROLE_ADMIN > ROLE_MANAGER_DOG \n ROLE_ADMIN > ROLE_MANAGER_MASTER \n" +
                " ROLE_MANAGER_DOG > ROLE_USER \n" +
                " ROLE_MANAGER_MASTER > ROLE_USER"
        roleHierarchy.setHierarchy(hierarchy)
        return roleHierarchy
    }

    @Bean
    fun webSecurityExpressionHandler(): DefaultWebSecurityExpressionHandler {
        val expressionHandler = DefaultWebSecurityExpressionHandler()
        expressionHandler.setRoleHierarchy(roleHierarchy())
        return expressionHandler
    }

    @Bean
    fun expressionHandler(): DefaultMethodSecurityExpressionHandler {
        val expressionHandler = DefaultMethodSecurityExpressionHandler()
        expressionHandler.setRoleHierarchy(roleHierarchy())
        return expressionHandler
    }

    @Bean
    fun grantedAuthoritiesMapper(roleHierarchy: RoleHierarchy): GrantedAuthoritiesMapper {
        return RoleHierarchyAuthoritiesMapper(roleHierarchy)
    }

}