package io.github.onsaenaro.config

import io.github.onsaenaro.endpoint.user.entity.UserRole
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }

            .formLogin { it.disable() }

            .httpBasic { it.disable() }

            .authorizeHttpRequests { it
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
            }

            // 아직 jwt 없음.
//            .addFilterAt(
//                LoginFilter(authenticationManager(authenticationConfiguration)),
//                UsernamePasswordAuthenticationFilter::class.java
//            )

            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun roleHierarchy(): RoleHierarchy =
        RoleHierarchyImpl.withDefaultRolePrefix()
            .role(UserRole.ADMIN.name).implies(UserRole.USER.name)
            .build()

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager
}