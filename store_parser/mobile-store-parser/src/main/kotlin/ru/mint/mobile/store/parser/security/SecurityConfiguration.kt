package ru.mint.mobile.store.parser.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.mint.mobile.store.parser.service.UserService

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var userService: UserService

    @Bean
    fun bcryptPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(4)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/*.*", "/auth/logout", "/assets/config/*.json", "/WEB-INF/resources/static/index.html").permitAll()
                .antMatchers("/app/create",
                        "/prefs/updater/get",
                        "/prefs/updater/set",
                        "/prefs/topparser/get",
                        "/prefs/topparser/set",
                        "/prefs/pinger/get",
                        "/prefs/pinger/set",
                        "/maillist/edit",
                        "/maillist/get",
                        "/keywords/edit",
                        "/keywords/get",
                        "/user/create",
                        "/user/getall",
                        "/user/get",
                        "/user/update",
                        "/user/setpassword",
                        "/user/delete",
                        "/tasks/pinger/restart",
                        "/tasks/topparser/restart",
                        "/tasks/updater/restart"
                        ).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/index.html")
                .loginProcessingUrl("/auth/login")
                .successForwardUrl("/auth/login?result=ok")
                .failureForwardUrl("/auth/login?result=error")
                .and()
                .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/index.html")
    }


    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService).passwordEncoder(bcryptPasswordEncoder())

    }
}