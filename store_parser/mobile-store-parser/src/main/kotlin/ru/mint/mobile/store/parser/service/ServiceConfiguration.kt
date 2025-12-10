package ru.mint.mobile.store.parser.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.thymeleaf.spring4.SpringTemplateEngine
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import ru.mint.luminati.support.LuminatiHttpProxyClient
import ru.mint.luminati.support.StringHttpResponseConverter
import java.nio.charset.StandardCharsets
import java.util.*
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session

@EnableScheduling
@ComponentScan
@Configuration
class ServiceConfiguration {

    @Bean
    fun jacksonMapper() = ObjectMapper()

    @Bean(destroyMethod = "shutdown")
    fun taskScheduler() =
            ThreadPoolTaskScheduler().apply {
                threadNamePrefix = "taskScheduler"
                poolSize = 6
                isRemoveOnCancelPolicy = true
            }

    @Bean
    fun mailSender(appSettingsService: AppSettingsService): JavaMailSender {
        val props = Properties()
        props.put("mail.smtp.auth", appSettingsService.getSmtpAuth())
        props.put("mail.smtp.starttls.enable", appSettingsService.getSmtpStartTLS())
        props.put("mail.smtp.host", appSettingsService.getSmtpHost())
        props.put("mail.smtp.port", appSettingsService.getSmtpPort())
        val jMailSender = JavaMailSenderImpl()
        jMailSender.javaMailProperties = props
        jMailSender.defaultEncoding = StandardCharsets.UTF_8.name()
        jMailSender.session = Session.getInstance(props,
                object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        val sender = appSettingsService.getSenderEmail()
                        val password = appSettingsService.getSenderPassword()
                        return PasswordAuthentication(sender, password)
                    }
                })
        return jMailSender
    }

    @Bean
    fun emailTemplateEngine(): SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        templateEngine.addTemplateResolver(htmlTemplateResolver())
        return templateEngine
    }

    @Bean
    fun httpProxyClient(appSettingsService: AppSettingsService): LuminatiHttpProxyClient<String> {
        val username = appSettingsService.getLuminatiUsername()
        val password = appSettingsService.getLuminatiPassword()
        val maxFailures = appSettingsService.getRequestMaxFailures()
        val port = appSettingsService.getLuminatiProxyPort()
        return LuminatiHttpProxyClient(username, password, maxFailures = maxFailures, port = port, responseConverter = StringHttpResponseConverter())
    }

    private fun htmlTemplateResolver(): ITemplateResolver {
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.prefix = "/mail/"
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML
        templateResolver.characterEncoding = StandardCharsets.UTF_8.name()
        templateResolver.isCacheable = false
        return templateResolver
    }

}