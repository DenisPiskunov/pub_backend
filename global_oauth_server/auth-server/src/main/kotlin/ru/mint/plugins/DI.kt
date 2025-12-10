package ru.mint.plugins

import io.ktor.application.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import ru.mint.rest.security.*
import ru.mint.service.*
import ru.mint.service.JWTFactory
import ru.mint.service.mail.MailService
import ru.mint.service.mail.MailServiceImpl

fun Application.configureDI() {

    di {
        importAll(serviceModule(environment), securityModule(environment))
    }
}

private fun serviceModule(environment: ApplicationEnvironment): DI.Module {
    val appConfig = environment.config

    return DI.Module("service") {
        bind<PasswordEncoder>() with singleton { BCryptPasswordEncoder() }
        bind<RandomStringGenerator>() with singleton { RandomStringGeneratorImpl() }

        bind<RefreshTokenEncoder>() with singleton { RefreshTokenEncoderImpl() }
        val refreshTokenTtlInSec = appConfig.config("application.refreshToken").property("ttlInSec").getString().toInt()
        bind<RefreshTokenFactory>() with singleton { RefreshTokenFactoryImpl(refreshTokenTtlInSec) }
        bind<RefreshTokenIssuer>() with singleton { RefreshTokenIssuerImpl(instance(), instance()) }

        bind<AccountStatusChecker>() with singleton { AccountStatusCheckerImpl() }

        bind<AccessTokenFactory>() with singleton { JWTFactory(newJWTOptions(environment)) }
        bind<AccessTokenIssuer>() with singleton { AccessTokenIssuerImpl(instance()) }
        bind<AccountTokensIssuer>() with singleton { AccountTokensIssuerImpl(instance(), instance(), instance(), instance()) }

        val mailConfig = MailingSMTPConfigurationFactory().newConfig(appConfig)
        bind<MailService>() with singleton { MailServiceImpl(mailConfig) }

        bind<AccountResetPasswordSecretCodeChecker>() with singleton { AccountResetPasswordSecretCodeCheckerImpl() }

        val resetPswdCodeLength = appConfig.config("application.resetPasswordCode").property("length").getString().toInt()
        val resetPswdCodeTtl = appConfig.config("application.resetPasswordCode").property("ttl").getString().toLong()
        val resetPswdURLTemplate = appConfig.config("application.resetPasswordCode").property("URLTemplate").getString()
        bind<AccountPasswordManager>() with singleton {
            AccountPasswordManagerImpl(
                instance(),
                instance(),
                resetPswdCodeLength,
                resetPswdCodeTtl,
                resetPswdURLTemplate,
                instance(),
                instance(),
                instance()
            )
        }
    }
}

private fun securityModule(environment: ApplicationEnvironment) = DI.Module("security") {
    val isDevEnv = environment.config.propertyOrNull("ktor.environment")?.getString() == "dev"
    val isHttpOnly = !isDevEnv
    val refreshTokenCookieName = environment.config.config("application.refreshToken").property("cookieName").getString()
    bind<RefreshTokenCookieFactory>() with singleton { RefreshTokenCookieFactoryImpl(isHttpOnly, refreshTokenCookieName) }
    bind<AuthenticationService>() with singleton { AuthenticationServiceImpl(instance(), instance(), instance()) }
    bind<SignOutService>() with singleton { SignOutServiceImpl(instance()) }
}